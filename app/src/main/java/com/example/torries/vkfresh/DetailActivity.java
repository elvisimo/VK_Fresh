package com.example.torries.vkfresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;


/**
 * Created by torries on 20.04.15.
 */
public class DetailActivity extends ActionBarActivity {
    private String newsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        setContentView(R.layout.detail_text);
        if (intent.hasExtra(Intent.EXTRA_TEXT)){
            newsText = intent.getStringExtra(Intent.EXTRA_TEXT);
            TextView textView = (TextView)findViewById(R.id.detailTextView);
            textView.setText(newsText);
        }

    }
}
