package com.example.eventmanagementsystem.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.util.Log;

public class NetworkMonitor {
    public static boolean checkNetworkConnection(Context context) {

        boolean result=false;
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if(capabilities!=null){

                    Log.d("TAG", "checkNetworkConnection: iss null "+ (capabilities!=null));
                    if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        result= true;
                    }else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        result=true;
                    }
                }
            }
        }
        return result;
    }
}
