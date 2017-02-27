package com.mad.itunestoppaidapps;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;

/**
 * Created by Chinmay Rawool on 2/23/2017.
 */

public class AppDetails implements Serializable{
    String title, imageUrl, priceAmount, priceCurrency;
    boolean favorite=false;

    public static AppDetails createApp(JSONObject appObject) throws JSONException, ParseException {
        AppDetails app = new AppDetails();

        if(appObject.getString("title")!=null) {
            //Log.d("demo","Inside title");
            JSONObject appObject1 = appObject.getJSONObject("title");
            if(appObject1.getString("label")!=null) {
                //Log.d("demo","Inside label");
                app.setTitle(appObject1.getString("label"));
                Log.d("demo","Title set:"+appObject1.getString("label"));
            }else {
                app.setTitle("Title null");
                Log.d("demo","Title null");
            }
        }

        if(appObject.getString("im:price")!=null) {
            //Log.d("demo","Inside im:price");
            JSONObject appObject1 = appObject.getJSONObject("im:price");
            if(appObject1.getString("attributes")!=null){
                //Log.d("demo","Inside attributes");
                JSONObject appObject2 = appObject1.getJSONObject("attributes");

                String str = appObject2.getString("amount");
                app.setPriceAmount(str);
                Log.d("demo","Price amt set"+str);
                str = appObject2.getString("currency");
                app.setPriceCurrency(str);
                Log.d("demo","Price currency set"+str);
            }else{
                app.setPriceAmount("amt null");
                Log.d("demo","Price amount set:null");
                app.setPriceCurrency("currency null");
                Log.d("demo","Price currency set:null");
            }
        }

        if(appObject.getString("im:image")!=null) {
            //Log.d("demo","Inside im:image");
            JSONArray imageArray = appObject.getJSONArray("im:image");
            //Log.d("demo","imageArray set");
            for(int i=0; i<imageArray.length();i++) {
                JSONObject iObject = (JSONObject) imageArray.get(i);
                JSONObject attrObject = iObject.getJSONObject("attributes");
                if (attrObject.getString("height").equals("100")) {
                    //JSONObject appObject3 = appObject1.getJSONObject("label");
                    //Log.d("demo", "height check 100");
                    String str = iObject.getString("label");
                    app.setImageUrl(str);
                    Log.d("demo", "image url set:" + str);
                }
            }
        }
        //news.setDescription(newsObject.getString("description"));
        //news.setTitle(newsObject.getString("title"));
        //news.setUrlToImage(newsObject.getString("urlToImage"));
        //String str = newsObject.getString("publishedAt");

        return app;
    }





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(String priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Title:"+getTitle()+" "+"Price: "+getPriceAmount()+" "+getPriceCurrency()+" "+"Image url: "+getImageUrl();
    }

    public String buildStr() {
        Log.d("String Builder:",getTitle()+"###"+getPriceAmount()+"###"+getPriceCurrency()+"###"+getImageUrl()+";");
        return getTitle()+"###"+getPriceAmount()+"###"+getPriceCurrency()+"###"+getImageUrl()+"###"+String.valueOf(isFavorite())+";";
    }


}
