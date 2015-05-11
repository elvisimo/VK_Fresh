package com.example.torries.vkfresh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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
                Log.v("Download Picture","params[0] == null");
            }
            try {
                HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } catch (IOException io) {
                io.printStackTrace();

                return null;

            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;


        }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

// CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

// RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

// RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }
}

