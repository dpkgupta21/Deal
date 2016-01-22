package com.deal.exap.utility;

import android.content.Context;
import android.content.SharedPreferences;





public class DealPreferences {

    public static final String PREF_NAME = "DEAL_PREFERENCES";

    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_PHONE = "PHONE";
    public static final String USER_OTP = "OTP";
    public static final String APP_LANG = "LANG";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String DISTANCE_UNIT = "DISTANCE_UNIT";



    public static void setLatitude(Context context, double latitude) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LATITUDE, String.valueOf(latitude));
        editor.apply();
    }

    public static double getLatitude(Context context) {
        String latitude= context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                LATITUDE, "0.0");
        return Double.parseDouble(latitude);
    }

    public static void setLongitude(Context context, double latitude) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LONGITUDE, String.valueOf(latitude));
        editor.apply();
    }

    public static double getLongitude(Context context) {
        String longitude= context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                LONGITUDE, "0.0");
        return Double.parseDouble(longitude);
    }

    public static void setLoggedIn(Context context, Boolean loggedIn) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_LOGGED_IN, loggedIn);
        editor.apply();
    }

    public static Boolean isLoggedIn(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(
                IS_LOGGED_IN, false);
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_USER_ID,
                null);
    }

    public static void setPhone(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PHONE, userId);
        editor.apply();
    }

    public static String getPhone(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_PHONE,
                null);
    }




    public static void setUSER_OTP(Context context, String otp) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_OTP, otp);
        editor.apply();
    }

    public static String getUSER_OTP(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                USER_OTP, "");
    }

    public static void setAPP_LANG(Context context, String lang) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_LANG, lang);
        editor.apply();
    }

    // Use ENG  by default language
    public static String getAPP_LANG(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                APP_LANG, Constant.LANG_ENGLISH_CODE);
    }

    public static void setDistanceUnit(Context context, String distanceUnit) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DISTANCE_UNIT, distanceUnit);
        editor.apply();
    }

    // Use Miles by default distance unit
    public static String getDistanceUnit(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(DISTANCE_UNIT,
                Constant.DISTANCE_UNIT_MILES);
    }

    public static void clearAllPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // User SharedPreferences.Editor.remove() in place of putString()

        editor.putString(KEY_USER_ID, null);
        editor.putString(DISTANCE_UNIT, Constant.DISTANCE_UNIT_MILES);
        editor.putString(KEY_PHONE, null);
        editor.putString(APP_LANG, Constant.LANG_ENGLISH_CODE);
        editor.putString(LATITUDE, "0.0");
        editor.putString(LONGITUDE, "0.0");
        editor.putBoolean(IS_LOGGED_IN, false);

        editor.apply();

    }
}
