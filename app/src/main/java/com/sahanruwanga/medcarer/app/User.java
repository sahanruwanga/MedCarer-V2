package com.sahanruwanga.medcarer.app;

import android.database.sqlite.SQLiteDatabase;

import com.sahanruwanga.medcarer.helper.SQLiteHandler;

/**
 * Created by Sahan Ruwanga on 3/7/2018.
 */

public class User {
    private static String userId;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

}
