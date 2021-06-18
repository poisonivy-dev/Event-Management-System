package com.example.eventmanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventmanagementsystem.UI.EventRecyclerViewAdapter;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.Util.NetworkMonitor;
import com.example.eventmanagementsystem.data.EventDatabaseHandler;
import com.example.eventmanagementsystem.data.syncStorage;
import com.example.eventmanagementsystem.model.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ListEventActivity extends AppCompatActivity {
    private static final String TAG = "ListEventActivity";
    private RecyclerView recyclerView;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    private List<Event> eventList = new ArrayList<>();
    private EventDatabaseHandler eventDatabaseHandler;
    private FloatingActionButton fab;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        //setting the recycler view
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        eventDatabaseHandler = new EventDatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        readFromLocal();

        //broadcast receiver to check the status of synchronization, it will update the UI when the data is added to remote.

        broadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocal();
            }
        };



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEventDetailsActivity();
            }
        });

    }

    private void readFromLocal() {
        eventList.clear();

        //Get events from db
        if(eventDatabaseHandler.getEventsCount()>0) {
            eventList = eventDatabaseHandler.getAllEvents();

            for (Event event : eventList) {
                Log.d(TAG, "onCreate: " + event.getEventName() + " " + event.getEventDate() + " " + event.getEventDescription());

                //check if there is any event that is not synchronized..
                if(event.getSync_status()==Constants.SYNC_STATUS_FAILED) {

                    //if internet is available , then insert the event and update the UI
                    if (NetworkMonitor.checkNetworkConnection(this)) {
                            Log.d("addEvent", "checkNetwork: connected");

                            //Using volley for sending data over
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/insertEvent.php",

                                    //rsponse method when action is performed
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            if (response.equalsIgnoreCase("OK")) {
                                                event.setSync_status(Constants.SYNC_STATUS_OK);
                                                eventDatabaseHandler.updateEvent(event);

                                                Log.d("addEvent", "onResponse: " + response);
                                            } else {
                                                Log.d("addEvent", "onResponse: " + response);}

                                        }
                                        //error listener
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("addEvent", "onResponse: " + error.getMessage());
                                }
                            }) {

                                //sending the data to be inserted..
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("eTitle", event.getEventName());
                                    params.put("eType", event.getEventType());
                                    params.put("eVenue", event.getEventVenue());
                                    params.put("eDate", event.getEventDate());
                                    params.put("eStime", event.getEventStime());
                                    params.put("eEtime", event.getEventEtime());
                                    params.put("eDesc", event.getEventDescription());
                                    return params;
                                }
                            };
                                //then adding the request to request queue
                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                        } else {
                            Log.d("addEvent", "saveEvent: no network");

                        }
                    }
            }
            //also set the recycler view
            eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventList);
            recyclerView.setAdapter(eventRecyclerViewAdapter);
            eventRecyclerViewAdapter.notifyDataSetChanged();
        }

        }

    //returning from the add event activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK){
            Log.d(TAG, "onActivityResult: returned from addEventActivity");
            readFromLocal();

        }
    }

    //starting add event activity
    private void startEventDetailsActivity() {
        Intent intent = new Intent(ListEventActivity.this,EventDetailsActivity.class);
        startActivityForResult(intent,2);

    }

    //register broadcast
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




