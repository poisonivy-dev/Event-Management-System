package com.example.eventmanagementsystem.data;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventmanagementsystem.MySingleton;
import java.util.HashMap;
import java.util.Map;

    public class syncStorage extends Worker {

        public syncStorage(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @NonNull
        @Override
        public Result doWork() {
                    deleteFromRemote(getApplicationContext());
            return Result.success();
        }

        //the work to do
        private void deleteFromRemote(Context context){
            String id= getInputData().getString("id");

            //making http string request through volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mywebdevproj.000webhostapp.com/deleteEvent.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equalsIgnoreCase("OK")) {
                                Log.d("deleteEvent", "onResponse: " + response);
                            }
                            else
                            {
                                Log.d("deleteEvent", "onResponse: " + response);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("deleteEvent", "onResponse: " + error.getMessage());
                }
            }) {
                //id to send so that it can be deleted
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }

    }


