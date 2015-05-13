package com.example.torries.vkfresh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
            options.inSampleSize = calculateInSampleSize(options,MainActivity.screenWidthDp,MainActivity.screenHeightDp);
            Bitmap myBitmap = BitmapFactory.decodeStream(input,null, options);
           // myBitmap = getResizedBitmap(myBitmap,MainActivity.screenHeightDp/2,MainActivity.screenWidthDp);
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

   /* public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

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

    }*/
   public static int calculateInSampleSize(
           BitmapFactory.Options options, int reqWidth, int reqHeight) {
       final int height = options.outHeight;
       final int width = options.outWidth;
       int inSampleSize = 1;

       if (height > reqHeight || width > reqWidth) {
           if (width > height) {
               inSampleSize = Math.round((float)height / (float)reqHeight);
           } else {
               inSampleSize = Math.round((float)width / (float)reqWidth);
           }
       }
       return inSampleSize;
   }
}

