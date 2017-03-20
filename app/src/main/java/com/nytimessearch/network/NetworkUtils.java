package com.nytimessearch.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Utility class for network
 */
public class NetworkUtils {

    /**
     * Function checks if the network is available
     * @param context
     * @return true if network is available else false
     */
    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    /**
     * Functino pings to check if connected to internet
     * @return true if yes else false
     */
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            //This is appropriate handled in the caller
        }
        return false;
    }
}
