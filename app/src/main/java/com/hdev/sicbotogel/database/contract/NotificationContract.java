package com.hdev.sicbotogel.database.contract;

import android.provider.BaseColumns;

public class NotificationContract {
    public static final String TABLE_NAME = "notification";

    public static class NotificationColumns implements BaseColumns {
        public static final String TITLE = "title";
        public static final String BODY = "body";
        public static final String DATE = "date";
        public static final String STATUS_READ = "status_read";
    }
}
