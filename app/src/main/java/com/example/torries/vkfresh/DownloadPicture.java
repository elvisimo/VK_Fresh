package com.example.torries.vkfresh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by torries on 11.05.15.
 */
public class DownloadPicture extends AsyncTask<Uri,Void,Bitmap>{


    @Override
    protected Bitmap doInBackground(Uri... params){
            URL imgUrl = null;
            InputStream input = null;
            try {
                imgUrl = new URL(params[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }try{
            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();}catch (IOException io) {
            io.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

    }


}

