package com.mad.itunestoppaidapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.RecursiveTask;

public class MainActivity extends AppCompatActivity implements GetDataAsync.IData{
    ProgressDialog pg;
    ListView listView;
    ArrayList<AppDetails> arrayList=null;
    AppAdapter adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        String listString = preferences.getString("LIST_APPS", "");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pg = new ProgressDialog(this);
        pg.setMessage("Parsing Data");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Log.d("initial list:",listString);
        if(listString.equalsIgnoreCase("")) {
            pg.show();
            new GetDataAsync(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
        }else{
            arrayList = getArrayList(listString);
            listView = (ListView) findViewById(R.id.listView_1);
            adapter = new AppAdapter(this,R.layout.row_layout,arrayList);
            listView.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_refresh) {
            Toast.makeText(this, "Refresh List clicked", Toast.LENGTH_SHORT).show();
            pg.show();
            new GetDataAsync(MainActivity.this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
        }
        if(item.getItemId()==R.id.action_favorites) {
            Toast.makeText(this, "Favorites clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,FavoriteActivity.class);
            String str1 = buildString(arrayList);
            editor.putString("LIST_APPS",str1);
            editor.apply();

            //finish();
            startActivity(intent);
        }
        if(item.getItemId()==R.id.action_increasingly) {
            Toast.makeText(this, "Sort Increasingly clicked", Toast.LENGTH_SHORT).show();
            Collections.sort(arrayList, new Comparator<AppDetails>() {
                @Override
                public int compare(AppDetails app1, AppDetails app2)
                {

                    return  app1.getPriceAmount().compareTo(app2.getPriceAmount());
                }
            });
            listView = (ListView) findViewById(R.id.listView_1);
            adapter = new AppAdapter(this,R.layout.row_layout,arrayList);
            listView.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
        }
        if(item.getItemId()==R.id.action_decreasingly) {
            Toast.makeText(this, "Sort Decreasingly clicked", Toast.LENGTH_SHORT).show();
            Collections.sort(arrayList, new Comparator<AppDetails>() {
                @Override
                public int compare(AppDetails app2, AppDetails app1)
                {

                    return  app1.getPriceAmount().compareTo(app2.getPriceAmount());
                }
            });
            listView = (ListView) findViewById(R.id.listView_1);
            adapter = new AppAdapter(this,R.layout.row_layout,arrayList);
            listView.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendData(final ArrayList<AppDetails> apps) {
        arrayList = apps;
        String str1 = buildString(arrayList);
        editor.putString("LIST_APPS",str1);
        editor.apply();
        pg.dismiss();
        Log.d("Arraylist",apps.toString());
        listView = (ListView) findViewById(R.id.listView_1);
        adapter = new AppAdapter(this,R.layout.row_layout,arrayList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);



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

}
