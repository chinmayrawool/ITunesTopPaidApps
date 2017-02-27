package com.mad.itunestoppaidapps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    ArrayList<AppDetails> list = null;
    ListView listView;
    ArrayList<AppDetails> arrayList=null;
    AppAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //list = (ArrayList<AppDetails>) getIntent().getExtras().getSerializable("ARRAYLIST");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        String listString = preferences.getString("LIST_APPS", "");
        list = getArrayList(listString);
        //Log.d("demo",list.toString());
        Toast.makeText(this, "Array list available.", Toast.LENGTH_SHORT).show();
        arrayList = new ArrayList<AppDetails>();
        for(int i=0;i<list.size();i++){
            if(list.get(i).isFavorite()){
                arrayList.add(list.get(i));
            }
        }
        listView = (ListView) findViewById(R.id.listView_2);
        adapter = new AppAdapter(this,R.layout.row_layout,arrayList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);


        findViewById(R.id.btn_backmain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("LIST_APPS",buildString(list));
                editor.apply();
                Intent intent =new Intent(FavoriteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public ArrayList<AppDetails> getArrayList(String str){
        ArrayList<AppDetails> list = new ArrayList<AppDetails>();
        String[] strings = str.split(";");
        for(int i=1;i<=Integer.parseInt(strings[0]);i++){
            String[] a = strings[i].split("###");
            AppDetails app = new AppDetails();
            app.setTitle(a[0]);
            app.setPriceAmount(a[1]);
            app.setPriceCurrency(a[2]);
            app.setImageUrl(a[3]);
            app.setFavorite(Boolean.parseBoolean(a[4]));
            list.add(app);
        }
        return list;
    }
    public String buildString(ArrayList<AppDetails> list){
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
