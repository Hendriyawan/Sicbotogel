package com.hdev.sicbotogel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {

    private static AppPreferences ourInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private AppPreferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static AppPreferences getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppPreferences(context);
        }
        return ourInstance;
    }

    public static boolean getFisrtInstall() {
        return sharedPreferences.getBoolean("is_first_install", true);
    }

    public static void setFirstInstall(boolean firstInstall) {
        editor = sharedPreferences.edit();
        editor.putBoolean("is_first_install", firstInstall);
        editor.apply();
    }
}
