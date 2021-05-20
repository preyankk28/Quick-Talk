package io.quicktalk.agilean.utils;

import android.content.Context;
import android.content.SharedPreferences;

import io.quicktalk.agilean.Constants;


public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
