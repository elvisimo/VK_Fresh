package com.example.torries.vkfresh;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class MainActivity extends ActionBarActivity {

    static ArrayAdapter newsAdapter;
    int state =10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mainListView = (ListView) findViewById(R.id.mainListView);
        newsAdapter = new ArrayAdapter(this, R.layout.main_element, R.id.itemTextView);
        mainListView.setAdapter(newsAdapter);

        mainListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0){
                    DownloadNews.updateView(state);
                    state+=10;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
              //  if (/*(totalItemCount - firstVisibleItem) < 5*/firstVisibleItem == 5  ){
                //    DownloadNews.updateView(totalItemCount);
               // }
            }
        });
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Spannable text = (Spannable) newsAdapter.getItem(position);
                Intent detailsIntent = new Intent(getApplicationContext(),DetailActivity.class);
                detailsIntent.putExtra(Intent.EXTRA_TEXT,text.toString());
                startActivity(detailsIntent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        DownloadNews downloadNews = new DownloadNews();


        downloadNews.execute(0);


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
