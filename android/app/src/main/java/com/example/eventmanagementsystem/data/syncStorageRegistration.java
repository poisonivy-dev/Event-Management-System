//package com.example.eventmanagementsystem.data;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.eventmanagementsystem.MySingleton;
//import com.example.eventmanagementsystem.Util.Constants;
//import com.example.eventmanagementsystem.model.Event;
//import com.example.eventmanagementsystem.model.Registration;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class syncStorageRegistration extends Worker{
//
//    public syncStorageRegistration(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        String command = getInputData().getString("command");
//        if(command == "Insert") {
//            uploadToRemote(getApplicationContext());
//            Log.d("TAG", "doWork: "+command);
//        }
//        else if(command == "Delete"){
//            deleteFromRemote(getApplicationContext());
//
//            Log.d("TAG", "doWork: "+command);
//        }
//        return Result.success();
//    }
//    private void deleteFromRemote(Context context){
//        String id= getInputData().getString("id");
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/deleteRegistration.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        if (response.equalsIgnoreCase("OK")) {
//                            Log.d("deleteRegistration", "onResponse: " + response);
//                        }
//                        else
//                        {
//                            Log.d("deleteRegistration", "onResponse: " + response);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("deleteRegistration", "onResponse: " + error.getMessage());
//            }
//        }) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", id);
//                return params;
//            }
//        };
//
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//
//    }
//
//    private void uploadToRemote(Context context) {
//        RegistrationDatabaseHandler registrationDatabaseHandler = new RegistrationDatabaseHandler(context);
//        List<Registration> registrationList = new ArrayList<>();
//        registrationList = registrationDatabaseHandler.getAllRegistrations();
//        for (Registration registration : registrationList) {
//            if(registration.getSync_status()!= Constants.SYNC_STATUS_OK){
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/insertRegistration.php",
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                                if (response.equalsIgnoreCase("OK")) {
//                                    registration.setSync_status(Constants.SYNC_STATUS_OK);
//                                    registrationDatabaseHandler.updateRegistration(registration);
//                                    context.sendBroadcast(new Intent(Constants.UI_UPDATE_BORADCAST));
//                                    Log.d("addRegistration", "onResponse: " + response);
//                                }
//                                else
//                                {
//                                    Log.d("addRegistration", "onResponse: " + response);
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("addRegistration", "onResponse: " + error.getMessage());
//                    }
//                }) {
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("rName", registration.getrName());
//                        params.put("rRoll", registration.getrRollNumber());
//                        params.put("rDept", registration.getrDepartment());
//                        params.put("rContact", registration.getrContact());
//                        params.put("rStatus", registration.getrStatus());
//                        params.put("rEventID", String.valueOf(registration.getEventID()));
//                        return params;
//                    }
//                };
//
//                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//
//            } else {
//                Log.d("addRegistration", "saveRegistration: error");
//
//
//            }
//
//        }
//
//    }
//
//}
//
//
//
