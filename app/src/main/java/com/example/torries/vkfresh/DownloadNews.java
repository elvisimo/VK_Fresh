package com.example.torries.vkfresh;

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
import java.util.List;

/**
 * Created by torries on 19.04.15.
 */
public class DownloadNews extends AsyncTask<Integer,Void,List<Spannable>> {
    final String BASE_URL = "https://api.vk.com/method/";
    final String METHOD_NAME = "wall.get?";
    final String URL_TO_GET = BASE_URL + METHOD_NAME;
    final String OWNER_ID = "owner_id";
    final String COUNT = "count";
    final String OFFSET = "offset";
    final String API_VERSION = "api_version";
    URL url;
    HttpURLConnection urlConnection = null;
    BufferedReader bufferedReader;

    static List<Spannable> updateView(int totalCount){
        DownloadNews downloadNews = new DownloadNews();
        downloadNews.execute(totalCount);
     return null;
    }
    //https://api.vk.com/method/wall.get?owner_id=-225666&count=10&version=5.30
    private List<Spannable> getNewsFromJson(String vknewsJson) throws JSONException {

        JSONObject jsonObject = new JSONObject(vknewsJson);
        JSONArray jsonArray = jsonObject.getJSONArray("response");
        List<Spannable> spannableArray = new ArrayList<>();
        for (int i = 1; i < jsonArray.length(); i++){

            JSONObject object = (JSONObject) jsonArray.get(i);
            if (!object.get("text").equals("")){
                spannableArray.add((Spannable) Html.fromHtml((String) object.get("text")));}

            else continue;
        }
        return spannableArray;
    }

    @Override
    protected List<Spannable> doInBackground(Integer... params) {
        String vknewsJson = null;
        List<Spannable> vknews= new ArrayList<>();
        Uri uri = Uri.parse(URL_TO_GET).buildUpon()
             .appendQueryParameter(OWNER_ID, "-225666")
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
            vknews = getNewsFromJson(vknewsJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vknews;
    }

    @Override
    protected void onPostExecute(List<Spannable> strings) {
        super.onPostExecute(strings);
        Log.d("FINAL ARRAY",strings.toString());
        for (Spannable x :strings) {
            MainActivity.newsAdapter.add(x);
        }
    }
}