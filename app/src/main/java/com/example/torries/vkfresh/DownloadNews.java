package com.example.torries.vkfresh;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by torries on 19.04.15.
 */
public class DownloadNews extends AsyncTask<Integer,Void,ArrayList<HashMap<String,Object>>> {
    final String BASE_URL = "https://api.vk.com/method/";
    final String METHOD_NAME = "wall.get?";
    final String URL_TO_GET = BASE_URL + METHOD_NAME;
    final String OWNER_ID = "owner_id";
    final String COUNT = "count";
    final String OFFSET = "offset";
    final String API_VERSION = "api_version";
    final String PATH="path";
    final String TEXT="text";
    final String IMAGE="img";
    ArrayList<HashMap<String,Object>> vknews= new ArrayList<>();

    URL url;
    HttpURLConnection urlConnection = null;
    BufferedReader bufferedReader;




    //https://api.vk.com/method/wall.get?owner_id=-225666&count=10&version=5.30
    private ArrayList<HashMap<String,Object>> getNewsFromJson(String vknewsJson) throws JSONException {
        DownloadPicture downloadPicture = new DownloadPicture();
        JSONObject jsonObject = new JSONObject(vknewsJson);
        Log.v("WTF",vknewsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("response");

        Log.v("Before cycle",jsonArray.toString());
        for (int i = 1; i < jsonArray.length(); i++){
            HashMap<String,Object> myHash= new HashMap<>();
            JSONObject object = (JSONObject) jsonArray.get(i);
            if (!object.get("text").equals("")){
                myHash.put(TEXT,(Spannable) Html.fromHtml((String) object.get("text")));
                JSONObject objectAttach = object.getJSONObject("attachment");
                if (objectAttach.optJSONObject("photo")!=null){
                JSONObject objectImg = objectAttach.getJSONObject("photo");
                    Uri urri;
                if (objectImg.opt("src_xxbig")!=null){
                    urri = Uri.parse(objectImg.get("src_xxbig").toString());
                }else if (objectImg.opt("src_xxbig") != null) {

                        urri = Uri.parse(objectImg.get("src_xbig").toString());
                    }
                    else urri = Uri.parse(objectImg.get("src_big").toString());
                    Log.v("WTF", urri.toString());
                    myHash.put(PATH, urri);
                    Bitmap myBitmap = downloadPicture.doInBackground(urri);
                    myHash.put(IMAGE, myBitmap);
                } else {
                    myHash.put(IMAGE, null);
                    Log.v("Empty image", myHash.toString());
                }
                vknews.add(myHash);

                }
            }

        return null;
        }



    @Override
    protected ArrayList<HashMap<String,Object>> doInBackground(Integer... params) {
        String vknewsJson = null;
        MainActivity.isLoading = true;

        Uri uri = Uri.parse(URL_TO_GET).buildUpon()
             .appendQueryParameter(OWNER_ID, params[1].toString())
             .appendQueryParameter(COUNT, "10")
             .appendQueryParameter(OFFSET,params[0].toString())
             .appendQueryParameter(API_VERSION, "5.30").build();


        try {
            url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) return null;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + '\n');
            }
            if (buffer.length() == 0) return null;
            vknewsJson = buffer.toString();
        }catch (final IOException e){ Log.e("IOError while connect",e.toString());}
        finally {
            if (urlConnection!=null) urlConnection.disconnect();
            if (bufferedReader != null) try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        try {
            getNewsFromJson(vknewsJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String,Object>> strings) {
        super.onPostExecute(strings);
        MainActivity.dataArray.addAll(vknews);
        MainActivity.isLoading=false;
        MainActivity.newsAdapter.notifyDataSetChanged();

    }


}