package com.example.eventmanagementsystem.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventmanagementsystem.MySingleton;
import com.example.eventmanagementsystem.model.Event;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class updateEventRemote extends Worker {

    String id;

    public updateEventRemote(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        // the task to perform , in this case update

        Event event = new Event();
        // We can pass values from the worker request variable

        String ev =getInputData().getString("event");
        Log.d("TAG", "doWork: "+ev);
        String[] res=ev.split("[,]",0);

            id=res[0];
            Log.d("TAG", "doWork: "+id);
            event.setEventName(res[1]);
            Log.d("TAG", "doWork: "+event.getEventName());
            event.setEventType(res[2]);
            event.setEventVenue(res[3]);
            event.setEventDate(res[4]);
            event.setEventStime(res[5]);
            event.setEventEtime(res[6]);
            event.setEventDescription(res[7]);
            Log.d("TAG", "doWork: "+event.getEventDescription());

            //the function to perform
        uploadToRemote(getApplicationContext(),event);
        return Result.success();

    }


    private void uploadToRemote(Context context, Event event) {
        //making a String request, the method will receive instruction from the php script from the given hosted website
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/updateEvent.php",

                new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.equalsIgnoreCase("OK")) {
                                    Log.d("updateEvent", "onResponse: " + response+ event.getEventName()+ event.getEventDescription());

                                }
                                else
                                {
                                    Log.d("updateEvent", "onResponse: " + response);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("addRegistration", "onResponse: " + error.getMessage());
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);
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
                //add request to queue
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }



}

