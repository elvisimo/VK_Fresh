package com.example.torries.vkfresh;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


/**
 * Created by torries on 20.04.15.
 */
public class DetailActivity extends ActionBarActivity {
    static String newsText;
    static Uri bmUri;
    private Bitmap bm;
    DialogFragment df;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        df = new DialogSave();
        setContentView(R.layout.detail_text);

        //if (intent.hasExtra(Intent.EXTRA_TEXT) && intent.hasExtra("BITMAP_IMAGE")){
        newsText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (intent.getData()!=null){
        bmUri = intent.getData();
        DownloadPicture downloadPicture = new DownloadPicture();

        Bitmap bm = null;
        try {
            bm = downloadPicture.execute(bmUri).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
            ImageView imageView = (ImageView)findViewById(R.id.detailIvView);
            imageView.setImageBitmap(bm);}
        TextView textView = (TextView)findViewById(R.id.detailTextView);
        textView.setText(newsText);
        com.example.torries.vkfresh.FloatingActionButton fabButton = new com.example.torries.vkfresh.FloatingActionButton.Builder(this)
                .withDrawable(getDrawable(R.drawable.ic_add2))
                .withButtonColor(Color.WHITE)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withButtonSize(90)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              df.show(getFragmentManager(), "get dialog");

            }
        });
    }
}
