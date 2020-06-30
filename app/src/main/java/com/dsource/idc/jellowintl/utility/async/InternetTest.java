package com.dsource.idc.jellowintl.utility.async;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

import com.dsource.idc.jellowintl.activities.BaseActivity;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.interfaces.CheckNetworkStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InternetTest extends AsyncTask<Context, Void, Boolean> {
    private CheckNetworkStatus listener;

    public void registerReceiver(CheckNetworkStatus listener){
        this.listener = listener;
    }

    public void unRegisterReceiver(){
        this.listener = null;
    }

    @Override
    protected Boolean doInBackground(Context... params) {
        if(!((BaseActivity)params[0]).isConnectedToNetwork((ConnectivityManager)
                params[0].getSystemService(Context.CONNECTIVITY_SERVICE)))
            return false;
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                urlc.disconnect();
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isConnected) {
        super.onPostExecute(isConnected);
        if(isConnected){
            listener.onReceiveNetworkState(GlobalConstants.NETWORK_CONNECTED);
        }else{
            listener.onReceiveNetworkState(GlobalConstants.NETWORK_DISCONNECTED);
        }
    }
}