package com.optimalotaku.overguide;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Brandon on 1/24/17.
 */
public class APIchecker extends AsyncTask<Object, Object, String> {
    public JSONObject tester;
    @Override
    protected String doInBackground(Object... strings) {
        Object collectedTag = strings[0];
        Object collectedPlatform = strings[1];
        Object collectedRegion = strings[2];
        URL url = null;
        try {
            url = new URL( "https://api.lootbox.eu/" + collectedPlatform + "/" + collectedRegion + "/" + collectedTag + "/competitive/heroes" );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected void onPostExecute(String result) {
        if (result == null) {
            Log.i("INFO", "PLAYER NOT FOUND");
        }
        else try {
            JSONObject obj = new JSONObject(result);
            tester = obj;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
