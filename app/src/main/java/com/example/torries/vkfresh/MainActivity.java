package com.example.torries.vkfresh;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    static SimpleAdapter newsAdapter;
    int state =10;
    static ArrayList<HashMap<String,Object>> dataArray = new ArrayList<>();
    static ListView mainListView;
    final String TEXT="text";
    final String IMAGE="img";
    final String PATH="path";
    static int screenHeightDp;
    static int screenWidthDp;
    static String DIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenHeightDp = this.getResources().getConfiguration().screenHeightDp;
        screenWidthDp = this.getResources().getConfiguration().screenWidthDp;
        mainListView = (ListView) findViewById(R.id.mainListView);
        final String TEXT="text";
        final String IMAGE="img";

             DIR= getFilesDir().toString();
        Log.v("WTF", "wtf");
        DownloadNews downloadNews = new DownloadNews();
        if (dataArray.isEmpty()){
        downloadNews.execute(0);}
        newsAdapter = new SimpleAdapter(this, MainActivity.dataArray, R.layout.main_element, new String[]{TEXT, IMAGE} , new int[]{R.id.itemTextView,R.id.ivImg});
        newsAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                    ImageView iv = (ImageView) view;
                    if (data != null){
                    Bitmap bm = (Bitmap) data;
                    Log.v("Data is not null","wtf");
                    iv.setImageBitmap(bm);}
                    else {
                        iv.setImageBitmap(null);
                    }
                    return true;
                }
                else if ((view instanceof ImageView) & (data == null)){
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap(null);
                }
                return false;

            }
        });
        mainListView.setAdapter(MainActivity.newsAdapter);


        mainListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(visibleItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount){
                    DownloadNews.updateView(state);
                    state+=10;
                }
            }
        });
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,Object> hashMap = (HashMap<String,Object>) newsAdapter.getItem(position);
                Intent detailsIntent = new Intent(getApplicationContext(),DetailActivity.class);
                detailsIntent.putExtra(Intent.EXTRA_TEXT, hashMap.get(TEXT).toString());
                detailsIntent.setData((android.net.Uri) hashMap.get(PATH));
                startActivity(detailsIntent);
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
