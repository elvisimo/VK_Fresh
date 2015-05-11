package com.example.torries.vkfresh;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.HashMap;

/**
 * Created by torries on 12.05.15.
 */
public class DBGetData extends AsyncTask<Void,Void,Void> {
    Context mContext;


    public DBGetData(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        HashMap<String,Object> hash = new HashMap<>();
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке

            int labelColIndex = c.getColumnIndex("label");
            int textColIndex = c.getColumnIndex("text");
            int imageColIndex = c.getColumnIndex("image_name");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                /*Log.d("DBTEST",
                        "ID = " + c.getInt(idColIndex) +
                                ", label = " + c.getString(labelColIndex) +
                                ", text = " + c.getString(textColIndex) +
                                ", image_name = " + c.getString(imageColIndex));*/

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                File image = mContext.getFileStreamPath(c.getString(imageColIndex));
                hash.put("label",c.getString(labelColIndex));
                hash.put("image_name",image);
                hash.put("text",c.getString(textColIndex));
                FavoriteActivity.favoriteArray.add(hash);

            } while (c.moveToNext());
        } else
            Log.d("DBTEST", "0 rows");
        FavoriteActivity.favAdapter.notifyDataSetChanged();
        c.close();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}
