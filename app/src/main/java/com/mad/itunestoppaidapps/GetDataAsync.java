package com.mad.itunestoppaidapps;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Chinmay Rawool on 2/23/2017.
 */

public class GetDataAsync extends AsyncTask<String,Void,ArrayList<AppDetails>> {

    IData activity;

    public GetDataAsync(IData activity){
        this.activity = activity;
    }

    @Override
    protected ArrayList<AppDetails> doInBackground(String... strings) {
        try{
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            if(statusCode == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while(line != null){
                    sb.append(line);
                    line=br.readLine();
                }
                Log.d("demo",sb.toString());
                return AppUtil.AppJSONParser.parseApp(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<AppDetails> apps) {
        super.onPostExecute(apps);
        activity.sendData(apps);
    }

    public static interface IData{
        public void sendData(ArrayList<AppDetails> apps);
    }
}
