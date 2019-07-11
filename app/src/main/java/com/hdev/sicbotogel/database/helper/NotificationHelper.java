package com.hdev.sicbotogel.database.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.hdev.sicbotogel.database.model.Notifications;
import com.hdev.sicbotogel.interfaces.NotificationView;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.hdev.sicbotogel.database.contract.NotificationContract.NotificationColumns.BODY;
import static com.hdev.sicbotogel.database.contract.NotificationContract.NotificationColumns.DATE;
import static com.hdev.sicbotogel.database.contract.NotificationContract.NotificationColumns.STATUS_READ;
import static com.hdev.sicbotogel.database.contract.NotificationContract.NotificationColumns.TITLE;
import static com.hdev.sicbotogel.database.contract.NotificationContract.TABLE_NAME;

/*
9 Juli 2019 created by Hendriyawan
Android Project - WebView Sicbotogel with FCM
 */
public class NotificationHelper {
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public NotificationHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }


    //metode get all notifikasi
    @SuppressLint("StaticFieldLeak")
    public void getAllNotifications(final NotificationView view) {
        new AsyncTask<Void, Void, List<Notifications>>() {
            @Override
            protected List<Notifications> doInBackground(Void... voids) {
                return getNotifications();
            }

            @Override
            protected void onPostExecute(List<Notifications> notifications) {
                super.onPostExecute(notifications);
                if (notifications.size() > 0) {
                    view.onNotificationLoaded(notifications);
                } else {
                    view.onDataEmpty();
                }
            }
        }.execute();
    }

    /*
    Query get all notification
     */
    private List<Notifications> getNotifications() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + _ID + " DESC", null);
        cursor.moveToFirst();

        List<Notifications> notificationsList = new ArrayList<>();
        Notifications notifications;
        if (cursor.getCount() > 0) {
            do {
                notifications = new Notifications();
                notifications.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                notifications.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                notifications.setBody(cursor.getString(cursor.getColumnIndexOrThrow(BODY)));
                notifications.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                notifications.setStatus_read(cursor.getString(cursor.getColumnIndexOrThrow(STATUS_READ)));
                notificationsList.add(notifications);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return notificationsList;
    }

    @SuppressLint("StaticFieldLeak")
    public void insert(final Notifications notifications) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                ContentValues cv = new ContentValues();
                cv.put(TITLE, notifications.getTitle());
                cv.put(BODY, notifications.getBody());
                cv.put(DATE, notifications.getDate());
                cv.put(STATUS_READ, notifications.getStatus_read());
                return database.insert(TABLE_NAME, null, cv);
            }

            @Override
            protected void onPostExecute(Long aLong) {
                super.onPostExecute(aLong);
                Log.d("DEBUG_INSERT", String.valueOf(aLong));
            }
        }.execute();
    }

    //metode membaca berapa notifikasi yang belum terbaca
    @SuppressLint("StaticFieldLeak")
    public void getCountNotificationUnread(final TextView textView) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return getNotificationUnread();
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                textView.setText(String.valueOf(integer));
            }
        }.execute();
    }


    /*
    Query get single row
     */
    private int getNotificationUnread() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + "  WHERE " + STATUS_READ + " LIKE '%unread%'", null);
        cursor.moveToFirst();
        int result = cursor.getCount();
        cursor.close();
        return result;
    }

    //update
    //update data
    @SuppressLint("StaticFieldLeak")
    public void update(final Notifications notifications, final String id) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                ContentValues cv = new ContentValues();
                cv.put(TITLE, notifications.getTitle());
                cv.put(BODY, notifications.getBody());
                cv.put(DATE, notifications.getDate());
                cv.put(STATUS_READ, notifications.getStatus_read());
                return database.update(TABLE_NAME, cv, _ID + " =?", new String[]{id});
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d("UPDATE", String.valueOf(integer));
            }
        }.execute();
    }

    public void delete(int id) {
        database.delete(TABLE_NAME, _ID + "= '" + id + "'", null);
    }
}
