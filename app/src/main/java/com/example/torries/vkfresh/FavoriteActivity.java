package com.example.torries.vkfresh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.torries.vkfresh.R.id.favIvImg;

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
       /* DBGetData dbGetData = new DBGetData(this);
        dbGetData.execute();*/
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
                        if (iDrawerItem.getIdentifier()==1){
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .build();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("favoritetable", null, null, null, null, null, null);
        SimpleCursorAdapter scad = new SimpleCursorAdapter(this,R.layout.favorite_element,c,new String[]{TEXT,IMAGE,LABEL},new int[]{R.id.favTextView, favIvImg,R.id.favItemTextView});
        scad.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if ((view instanceof ImageView) & (columnIndex == 3)) {
                    ImageView iv = (ImageView) view;
                    File image = new File(cursor.getString(columnIndex));
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                    Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);


                    iv.setImageBitmap(bitmap);

                    Log.v("Image is not null", "wtf");
                    return true;
            }
                else if (view instanceof TextView){
                    ((TextView) view).setText(cursor.getString(columnIndex));
                }

            return true;

            }
        });

        /*favAdapter = new SimpleAdapter(this, favoriteArray, R.layout.favorite_element, new String[]{TEXT,IMAGE,LABEL} , new int[]{R.id.favTextView,R.id.favIvImg,R.id.favItemTextView});
        ListView favListView = (ListView) findViewById(R.id.favoriteListView);
        favAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {

                if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                    ImageView iv = (ImageView) view;
                    if (data != null){

                        Bitmap bm = (Bitmap) data;
                        Log.v("Data is not null","favorite");
                        iv.setImageBitmap(bm);}
                    return true;
                }
                else if ((view instanceof ImageView) & (data == null)){
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap(null);
                }
                return false;

            }
        });*/
        ListView favListView = (ListView) findViewById(R.id.favoriteListView);
        favListView.setAdapter(scad);
    }
}
