package com.mad.itunestoppaidapps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Chinmay Rawool on 2/23/2017.
 */

public class AppAdapter extends ArrayAdapter<AppDetails> {
    Context mContext;
    int mResource;
    List<AppDetails> mData;
    String ad = "add";
    AppDetails app;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String listString;

    int i=0;
    public AppAdapter(Context context, int resource, List<AppDetails> objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.mResource =resource;
        this.mData = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = preferences.edit();
        listString = preferences.getString("LIST_APPS", "");
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        app = mData.get(position);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_image);
        Picasso.with(mContext).load(app.getImageUrl()).into(imageView);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        tv_title.setText("Title: "+app.getTitle());
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        tv_price.setText("Price: "+app.getPriceAmount()+" "+app.getPriceCurrency());
        final ImageButton imageButton = (ImageButton)convertView.findViewById(R.id.iv_star);
        if(app.isFavorite()) {
            imageButton.setImageResource(R.drawable.black24x24);
        }else{
            imageButton.setImageResource(R.drawable.white24x24);
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final ImageView iv = (ImageView) v;
                if(mData.get(position).isFavorite()){
                    builder.setTitle("Add to Favourites").setMessage("Are you sure that you want to remove this App from favorites?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    mData.get(position).setFavorite(false);
                                    listString = buildString(mData);
                                    editor.putString("LIST_APPS", listString);
                                    editor.apply();
                                    changeStarColor(mData.get(position),iv);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }else {
                    Log.d("demo",v.toString());
                    builder.setTitle("Add to Favourites").setMessage("Are you sure that you want to add this App to favorites?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    mData.get(position).setFavorite(true);
                                    changeStarColor(mData.get(position),iv);
                                    listString = buildString(mData);
                                    editor.putString("LIST_APPS", listString);
                                    editor.apply();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        i++;
        return convertView;

    }
    public void changeStarColor(AppDetails iTune,ImageView iv){
        if(iTune.isFavorite()){
            Picasso.with(mContext).load(R.drawable.black24x24).into(iv);
        }else {
            Picasso.with(mContext).load(R.drawable.white24x24).into(iv);
        }
    }

    public String buildString(List<AppDetails> list){
        StringBuilder sb = new StringBuilder();
        String size = String.valueOf(list.size());
        sb.append(size+";");
        for(int i=0;i<list.size();i++){
            sb.append(list.get(i).buildStr());
        }
        Log.d("String:",sb.toString());
        return sb.toString();
    }
}
