package com.example.torries.vkfresh;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by torries on 11.05.15.
 */
public class DialogSave extends DialogFragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Add to favorite?");
        View v = inflater.inflate(R.layout.dialog, null);
        v.findViewById(R.id.buttonOk).setOnClickListener(this);
        v.findViewById(R.id.buttonCancel).setOnClickListener(this);
        return v;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonOk){
            FileOutputStream out = null;
            ContentValues cv = new ContentValues();
            EditText editText = (EditText) getDialog().findViewById(R.id.editTextLabel);
            String label = editText.getText().toString();
            String text = DetailActivity.newsText;
            Uri image_uri = DetailActivity.bmUri;
            DBHelper dbHelper = new DBHelper(v.getContext());
            DownloadPicture downloadPicture = new DownloadPicture();

            Bitmap bm = null;
            String filename = "file"+ Calendar.DATE + Calendar.DAY_OF_WEEK +Calendar.SECOND + Calendar.MILLISECOND +".png";
            try {
                bm = downloadPicture.execute(image_uri).get();
                out = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            cv.put("label", label);
            cv.put("text", text);
            cv.put("image_name", filename);
            db.insert("mytable", null, cv);
            //проверка, что в бд данные сохраняются корректно
            /*Log.d("DBTEST", "--- Rows in mytable: ---");
            // делаем запрос всех данных из таблицы mytable, получаем Cursor
            Cursor c = db.query("mytable", null, null, null, null, null, null);

            // ставим позицию курсора на первую строку выборки
            // если в выборке нет строк, вернется false
            if (c.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int labelColIndex = c.getColumnIndex("label");
                int textColIndex = c.getColumnIndex("text");
                int imageColIndex = c.getColumnIndex("image_name");

                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d("DBTEST",
                            "ID = " + c.getInt(idColIndex) +
                                    ", label = " + c.getString(labelColIndex) +
                                    ", text = " + c.getString(textColIndex) +
                                    ", image_name = "+ c.getString(imageColIndex));
                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (c.moveToNext());
            } else
                Log.d("DBTEST", "0 rows");
            c.close();*/
            dbHelper.close();
            dismiss();
            Log.v("filename",filename);
        }
        if (v.getId() == R.id.buttonCancel){

            dismiss();
        }
    }
}
