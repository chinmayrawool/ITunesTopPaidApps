package com.mad.itunestoppaidapps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Chinmay Rawool on 2/23/2017.
 */

public class AppUtil {

    static public class AppJSONParser{
        static ArrayList<AppDetails> parseApp(String in) throws JSONException, ParseException {
            ArrayList<AppDetails> arrayList = new ArrayList<AppDetails>();

            JSONObject entryObject=new JSONObject(in);
            JSONObject feedObject = entryObject.getJSONObject("feed");
            Log.d("demo","feed object created");
            JSONArray entryArray=feedObject.getJSONArray("entry");
            Log.d("demo","entry array created");
            for(int i=0;i<entryArray.length();i++){
                JSONObject appObject = entryArray.getJSONObject(i);
                AppDetails app = AppDetails.createApp(appObject);
                arrayList.add(app);
            }


            return arrayList;
        }
    }
}
