package com.example.eventmanagementsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventmanagementsystem.UI.ParticipantRecyclerViewAdapter;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.Util.NetworkMonitor;
import com.example.eventmanagementsystem.data.ParticipantDatabaseHandler;
import com.example.eventmanagementsystem.model.Participant;
import com.example.eventmanagementsystem.model.Registration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantListActivity extends AppCompatActivity {
    private static final String P_TAG = "ListRActivity";
    private RecyclerView pRecyclerView;
    private ParticipantRecyclerViewAdapter participantRecyclerViewAdapter;
    private List<Participant> participantList=new ArrayList<>();
    private ParticipantDatabaseHandler participantDatabaseHandler;
    private FloatingActionButton pFab;
    BroadcastReceiver broadcastReceiver;
    private int eventID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_list);
        pRecyclerView = findViewById(R.id.pRecyclerView);
        pFab = findViewById(R.id.pFab);


        participantDatabaseHandler = new ParticipantDatabaseHandler(this);
        pRecyclerView.setHasFixedSize(true);
        pRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        readFromLocal();

        broadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocal();
            }
        };



        pFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startParticipantDetailsActivity(eventID);
            }
        });
    }

    private void readFromLocal() {
        participantList.clear();
        //Get events from db

        eventID = getIntent().getIntExtra("eventID", -1);
        //Get participants from db
        if (participantDatabaseHandler.getParticipantsCount() > 0) {
            participantList = participantDatabaseHandler.getAllParticipants(eventID);

            for (Participant participant : participantList) {
                Log.d(P_TAG, "onCreate: " + participant.getParticipantName() + " " + participant.getParticipantDepartment());
                if (participant.getSync_status() == Constants.SYNC_STATUS_FAILED) {
                    if (NetworkMonitor.checkNetworkConnection(this)) {
                        Log.d("addReg", "checkNetwork: connected");

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/insertParticipant.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if (response.equalsIgnoreCase("OK")) {
                                            participant.setSync_status(Constants.SYNC_STATUS_OK);
                                            participantDatabaseHandler.updateParticipant(participant);

                                            Log.d("addParticipant", "onResponse: " + response);
                                        } else {
                                            Log.d("addParticipant", "onResponse: " + response);
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("addParticipant", "onResponse: " + error.getMessage());
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("name", participant.getParticipantName());
                                params.put("roll", participant.getParticipantRollNumber());
                                params.put("dept", participant.getParticipantDepartment());
                                params.put("contact", participant.getParticipantContact());
                                params.put("role", participant.getParticipantRole());
                                params.put("eID", String.valueOf(participant.getEventID()));
                                return params;
                            }
                        };

                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                    } else {
                        Log.d("addParticipant", "saveEvent: no network");

                    }
                }
            }
            participantRecyclerViewAdapter = new ParticipantRecyclerViewAdapter(this, participantList);
            pRecyclerView.setAdapter(participantRecyclerViewAdapter);
            participantRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==8 && resultCode==RESULT_OK){
            Log.d(P_TAG, "onActivityResult: returned from addParticipantActivity");
            eventID=data.getIntExtra("eventID",-1);

            readFromLocal();
        }
    }
    private void startParticipantDetailsActivity(int eventID) {
        Intent intent = new Intent(ParticipantListActivity.this,ParticipantDetailsActivity.class);
        intent.putExtra("eventID",eventID);
        startActivityForResult(intent,8);

    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(broadcastReceiver, new IntentFilter(Constants.UI_UPDATE_BORADCAST));


    }
    //unregister broadcast
    @Override
    protected void onPause() {

        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}

