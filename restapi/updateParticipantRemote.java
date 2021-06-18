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
import com.example.eventmanagementsystem.model.Participant;
import com.example.eventmanagementsystem.model.Registration;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class updateParticipantRemote extends Worker {

    String id;

    public updateParticipantRemote(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        Participant participant  = new Participant();
        String ev =getInputData().getString("participant");
        Log.d("TAG", "doWork: participant update "+ev);
        String[] res=ev.split("[,]",0);

        id=res[0];
        Log.d("TAG", "doWork:participant update "+id);
        participant.setEventID(Integer.parseInt(res[1]));
        Log.d("TAG", "doWork: "+participant.getEventID());
        participant.setParticipantName(res[2]);
        participant.setParticipantRollNumber(res[3]);
        participant.setParticipantDepartment(res[4]);
        participant.setParticipantContact(res[5]);
        participant.setParticipantRole(res[6]);
        Log.d("TAG", "doWork:participant update "+participant.getEventID());

        uploadToRemote(getApplicationContext(),participant);
        return Result.success();

    }


    private void uploadToRemote(Context context, Participant participant) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/updateParticipant.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("OK")) {
                            Log.d("updateParticipant", "onResponse: " + response+ participant.getParticipantName()+ participant.getParticipantDepartment());

                        }
                        else
                        {
                            Log.d("updateParticipant", "onResponse: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateParticipant", "onResponse: " + error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
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

    }



}

