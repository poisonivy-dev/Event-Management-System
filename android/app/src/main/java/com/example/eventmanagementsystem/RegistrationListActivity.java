package com.example.eventmanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventmanagementsystem.UI.EventRecyclerViewAdapter;
import com.example.eventmanagementsystem.UI.RegistrationRecyclerViewAdapter;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.Util.NetworkMonitor;
import com.example.eventmanagementsystem.data.EventDatabaseHandler;
import com.example.eventmanagementsystem.data.RegistrationDatabaseHandler;
import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.model.Registration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationListActivity extends AppCompatActivity {
    private static final String REG_TAG = "ListRActivity";
    private RecyclerView regRecyclerView;
    private RegistrationRecyclerViewAdapter registrationRecyclerViewAdapter;
    private List<Registration> registrationList  = new ArrayList<>();;
    private RegistrationDatabaseHandler registrationDatabaseHandler;
    private FloatingActionButton regFab;
    BroadcastReceiver broadcastReceiver;
    private int eventID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting the recycler view
        setContentView(R.layout.activity_registration_list);
        regRecyclerView = findViewById(R.id.regRecyclerview);
        regFab = findViewById(R.id.regFab);


        registrationDatabaseHandler = new RegistrationDatabaseHandler(this);
        regRecyclerView.setHasFixedSize(true);
        regRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            readFromLocal();

        broadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocal();
            }
        };

        regFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegistrationDetailsActivity(eventID);
            }
        });
    }

    private void readFromLocal() {

        registrationList.clear();

        eventID = getIntent().getIntExtra("eventID", -1);
        //Get registrations from db

        if (registrationDatabaseHandler.getRegistrationsCount() > 0) {
            registrationList = registrationDatabaseHandler.getAllRegistrations(eventID);

            for (Registration registration : registrationList) {
                Log.d(REG_TAG, "onCreate: " + registration.getrName() + " " + registration.getrDepartment());

                //check for unsynchronized data and then synchronize when internet is available
                if (registration.getSync_status() == Constants.SYNC_STATUS_FAILED) {
                    if (NetworkMonitor.checkNetworkConnection(this)) {
                        Log.d("addReg", "checkNetwork: connected");

                        //response to the function
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/insertRegistration.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if (response.equalsIgnoreCase("OK")) {
                                            registration.setSync_status(Constants.SYNC_STATUS_OK);
                                            registrationDatabaseHandler.updateRegistration(registration);

                                            Log.d("addReg", "onResponse: " + response);
                                        } else {
                                            Log.d("addReg", "onResponse: " + response);
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("addReg", "onResponse: " + error.getMessage());
                            }
                        }) {

                            //that that will be passed
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                        params.put("rName", registration.getrName());
                        params.put("rRoll", registration.getrRollNumber());
                        params.put("rDept", registration.getrDepartment());
                        params.put("rContact", registration.getrContact());
                        params.put("rStatus", registration.getrStatus());
                        params.put("rEventID", String.valueOf(registration.getEventID()));
                        return params;
                            }
                        };

                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                    } else {
                        Log.d("addEvent", "saveEvent: no network");

                    }
                }
            }

            registrationRecyclerViewAdapter = new RegistrationRecyclerViewAdapter(this, registrationList);
            regRecyclerView.setAdapter(registrationRecyclerViewAdapter);
            registrationRecyclerViewAdapter.notifyDataSetChanged();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==5 && resultCode==RESULT_OK){
            Log.d(REG_TAG, "onActivityResult: returned from addRegistrationActivity");
            eventID=data.getIntExtra("eventID",-1);
            readFromLocal();
        }
    }
    private void startRegistrationDetailsActivity(int eventID) {
        Intent intent = new Intent(RegistrationListActivity.this,RegistrationDetailsActivity.class);
        intent.putExtra("eventID",eventID);
        startActivityForResult(intent,5);

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