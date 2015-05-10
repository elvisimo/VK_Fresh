package com.example.torries.vkfresh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


/**
 * Created by torries on 20.04.15.
 */
public class DetailActivity extends ActionBarActivity {
    private String newsText;
    private Uri bmUri;
    private Bitmap bm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
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

        //}

    }
}
