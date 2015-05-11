package com.example.torries.vkfresh;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.SimpleAdapter;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by torries on 12.05.15.
 */
public class FavoriteActivity extends ActionBarActivity {
    final String TEXT = "text";
    final String IMAGE = "image_name";
    final String LABEL= "label";
    static SimpleAdapter favAdapter;
    static ArrayList<HashMap<String,Object>> favoriteArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        DBGetData dbGetData = new DBGetData(this);
        dbGetData.execute();
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
                .build();
        favAdapter = new SimpleAdapter(this, favoriteArray, R.layout.favorite_element, new String[]{LABEL,IMAGE,TEXT} , new int[]{R.id.favTextView,R.id.favIvImg,R.id.favItemTextView});

    }
}
