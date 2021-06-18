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
import com.example.eventmanagementsystem.model.Registration;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class updateRegistrationRemote extends Worker {

    String id;


    public updateRegistrationRemote(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Registration registration  = new Registration();
        String ev =getInputData().getString("registration");
        Log.d("TAG", "doWork: registration update "+ev);
        String[] res=ev.split("[,]",0);

        id=res[0];
        Log.d("TAG", "doWork:registration update "+id);
        registration.setEventID(Integer.parseInt(res[1]));
        Log.d("TAG", "doWork: "+registration.getEventID());
        registration.setrName(res[2]);
        registration.setrRollNumber(res[3]);
        registration.setrDepartment(res[4]);
        registration.setrContact(res[5]);
        registration.setrStatus(res[6]);
        Log.d("TAG", "doWork:registration update "+registration.getEventID());

        uploadToRemote(getApplicationContext(),registration);
        return Result.success();

    }


    private void uploadToRemote(Context context, Registration registration) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/updateRegistration.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("OK")) {
                            Log.d("updateReg", "onResponse: " + response+ registration.getrName()+ registration.getrDepartment());

                        }
                        else
                        {
                            Log.d("updateReg", "onResponse: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("updateReg", "onResponse: " + error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
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

    }



}

