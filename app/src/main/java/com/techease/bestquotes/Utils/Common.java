package com.techease.bestquotes.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.techease.bestquotes.Model.Step;
import com.techease.bestquotes.Model.Wolf;
import com.techease.bestquotes.StepsActivity;

public class Common {

    public static Wolf currentWolf = null;
    public static Step currentStep = null;

    public static StepsActivity stepsActivity = null;


    public static boolean isConnectingToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork  = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork  != null && activeNetwork .isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }


        /*
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            return isConnected;
        }

        return false;
        */
    }


}
