package com.example.eventmanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.Util.NetworkMonitor;
import com.example.eventmanagementsystem.data.EventDatabaseHandler;
import com.example.eventmanagementsystem.model.Event;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {
    private Button cancelButton;
    private Button saveButton;
    private EditText eventTitle;
    private EditText eventType;
    private EditText eventVenue;
    private EditText eventDate;
    private EditText eventStime;
    private EditText eventEtime;
    private EditText eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_event_activity);

        eventTitle = findViewById(R.id.eventTitle);
        eventType = findViewById(R.id.eventType);
        eventVenue = findViewById(R.id.eventVenue);
        eventDate = findViewById(R.id.eventDate);
        eventStime = findViewById(R.id.eventStime);
        eventEtime = findViewById(R.id.eventEtime);
        eventDescription = findViewById(R.id.eventDescription);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        //save Event to database and synchronize upon save
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eventTitle.getText().toString().isEmpty()
                        && !eventType.getText().toString().isEmpty()
                        && !eventVenue.getText().toString().isEmpty()
                        && !eventDate.getText().toString().isEmpty()
                        && !eventStime.getText().toString().isEmpty()
                        && !eventEtime.getText().toString().isEmpty()
                        && !eventDescription.getText().toString().isEmpty()) {
                    saveEvent(v);

                    setResult(RESULT_OK);
                    finish();

                } else {
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void saveEvent(View view) {
        //Todo: save each event item to db

        Event event = new Event();
        String eTitle = eventTitle.getText().toString().trim();
        String eType = eventType.getText().toString().trim();
        String eVenue = eventVenue.getText().toString().trim();
        String eDate = eventDate.getText().toString().trim();
        String eStime = eventStime.getText().toString().trim();
        String eEtime = eventEtime.getText().toString().trim();
        String eDesc = eventDescription.getText().toString().trim();

        event.setEventName(eTitle);
        event.setEventType(eType);
        event.setEventVenue(eVenue);
        event.setEventDate(eDate);
        event.setEventStime(eStime);
        event.setEventEtime(eEtime);
        event.setEventDescription(eDesc);
        event.setSync_status(Constants.SYNC_STATUS_FAILED);

        EventDatabaseHandler eventDatabaseHandler = new EventDatabaseHandler(this);
        eventDatabaseHandler.addEvent(event);
        Snackbar.make(view, "Event Saved", Snackbar.LENGTH_LONG)
                .show();

    }

}