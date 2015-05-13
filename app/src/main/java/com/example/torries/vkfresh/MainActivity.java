package com.example.torries.vkfresh;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends ActionBarActivity {

    static SimpleAdapter newsAdapter;
    int state =10;
    static ArrayList<HashMap<String,Object>> dataArray = new ArrayList<>();
    static ListView mainListView;
    static int groupId = -225666;
    final String PATH="path";
    static int screenHeightDp;
    static int screenWidthDp;
    static String DIR;
    private DownloadNews downloadNews;
    private DownloadNews updateNews;
    static boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),

                        new PrimaryDrawerItem().withName(R.string.drawer_item_favorite).withIcon(FontAwesome.Icon.faw_star).withIdentifier(2),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Log.v("IDreawerItem",iDrawerItem.toString());
                        if (iDrawerItem.getIdentifier()==2){
                            Intent intent = new Intent(getApplicationContext(),FavoriteActivity.class);
                            startActivity(intent);
                        }

                    }
                })
                .build();





        screenHeightDp = this.getResources().getConfiguration().screenHeightDp;
        screenWidthDp = this.getResources().getConfiguration().screenWidthDp;
        mainListView = (ListView) findViewById(R.id.mainListView);
        final String TEXT="text";
        final String IMAGE="img";

             DIR= getFilesDir().toString();
        Log.v("WTF", "wtf");
        downloadNews = new DownloadNews();
        if (dataArray.isEmpty()){
        downloadNews.execute(0,groupId);}
        newsAdapter = new SimpleAdapter(this, MainActivity.dataArray, R.layout.main_element, new String[]{TEXT, IMAGE} , new int[]{R.id.itemTextView,R.id.ivImg});
        newsAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                    ImageView iv = (ImageView) view;

                    Bitmap bm = (Bitmap) data;
                    Log.v("Data is not null","wtf");
                    iv.setImageBitmap(bm);

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
                if((!isLoading) && ((firstVisibleItem + visibleItemCount +3) == totalItemCount)){

                        updateNews = new DownloadNews();
                        updateNews.execute(state,groupId);
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
        mainListView.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
                Log.v("Image deleted","WOHOO");
                ImageView imageView = (ImageView) view.findViewById(R.id.ivImg);

                imageView.setImageBitmap(null);
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
