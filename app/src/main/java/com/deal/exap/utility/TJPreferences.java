package com.deal.exap.utility;

import android.content.Context;
import android.content.SharedPreferences;





public class TJPreferences {

    public static final String PREF_NAME = "TRAVELJAR_PREFERENCES";
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_PHONE = "PHONE";
    public static final String KEY_PROFILE_IMG = "PROFILE_IMG";
    public static final String API_KEY = "API_KEY";
    public static final String KEY_CURRENT_JID = "ACTIVE_JOURNEY_ID";
    public static final String KEY_CURRENT_BUDDY_IDS = "ACTIVE_JOURNEY_BUDDY_IDS";
    public static final String KEY_STATUS = "STATUS";
    public static final String USER_PASSWORD = "PASSWORD";
    public static final String KEY_GCM_REG_ID = "GCM_REG_ID";
    public static final String KEY_APP_VERSION = "APP_VERSION";
    public static final String TIME_CAPSULE_SONG_ID = "SONG_ID";
    public static final String TIME_CAPSULE_DURATION = "DURATION";
    public static final String TIME_CAPSULE_MEMORY_COUNT = "MEMORY_COUNT";
    public static final String USER_OTP = "OTP";
    public static final String APP_LANG = "LANG";

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

    public static void setProfileImgPath(Context context, String path) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PROFILE_IMG, path);
        editor.apply();
    }

    public static String getProfileImgPath(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_PROFILE_IMG,
                null);
    }

    public static void setUserName(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NAME, userId);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_NAME,
                null);
    }

    public static void setEmail(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_EMAIL, userId);
        editor.apply();
    }

    public static String getEmail(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_EMAIL,
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

    public static void setApiKey(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(API_KEY, userId);
        editor.apply();
    }

    public static String getApiKey(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(API_KEY,
                null);
    }

    public static void setActiveJourneyId(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CURRENT_JID, userId);
        editor.apply();
    }

    public static String getActiveJourneyId(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                KEY_CURRENT_JID, null);
    }

    public static void setActiveBuddyIds(Context context, String buddyIds) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CURRENT_BUDDY_IDS, buddyIds);
        editor.apply();
    }

    public static String getActiveBuddyIds(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                KEY_CURRENT_BUDDY_IDS, null);
    }

    public static void setUserStatus(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_STATUS, userId);
        editor.apply();
    }

    public static String getUserStatus(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_STATUS,
                null);
    }

    public static void setUserPassword(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_PASSWORD, userId);
        editor.apply();
    }

    public static String getUserPassword(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                USER_PASSWORD, null);
    }

    public static String getTimeCapsuleSongId() {
        return TIME_CAPSULE_SONG_ID;
    }

    public static String getTimeCapsuleDuration() {
        return TIME_CAPSULE_DURATION;
    }

    public static void setGcmRegId(Context context, String regId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_GCM_REG_ID, regId);
        editor.apply();

    }

    public static String getGcmRegId(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                KEY_GCM_REG_ID, null);
    }

    public static void setAppVersion(Context context, int appVersion) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_APP_VERSION, appVersion);
        editor.apply();
    }

    public static Integer getAppVersion(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(
                KEY_APP_VERSION, Integer.MIN_VALUE);
    }

    public static void setTIME_CAPSULE_SONG_ID(Context context, int song_id) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TIME_CAPSULE_SONG_ID, song_id);
        editor.apply();
    }

    public static Integer getTIME_CAPSULE_SONG_ID(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(
                TIME_CAPSULE_SONG_ID, Integer.MIN_VALUE);
    }

    public static void setTIME_CAPSULE_DURATION(Context context, int durationId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // editor.putString(TIME_CAPSULE_DURATION, durationId);
        editor.putInt(TIME_CAPSULE_DURATION, durationId);
        editor.apply();
    }

    public static Integer getTIME_CAPSULE_DURATION(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(
                TIME_CAPSULE_DURATION, Integer.MIN_VALUE);
    }

    public static void setTIME_CAPSULE_MEMORY_COUNT(Context context, int count) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TIME_CAPSULE_MEMORY_COUNT, count);
        editor.apply();
    }

    public static Integer getTIME_CAPSULE_MEMORY_COUNT(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(
                TIME_CAPSULE_MEMORY_COUNT, 0);
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

    // use ENG for english by default language
    public static String getAPP_LANG(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                APP_LANG, "ENG");
    }


    public static void clearAllPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // User SharedPreferences.Editor.remove() in place of putString()
        editor.putString(KEY_USER_ID, null);
        editor.putString(KEY_NAME, null);
        editor.putString(KEY_EMAIL, null);
        editor.putString(KEY_PHONE, null);
        editor.putString(KEY_CURRENT_JID, null);
        editor.putString(KEY_STATUS, null);
        editor.putString(TIME_CAPSULE_SONG_ID, null);
        editor.putString(TIME_CAPSULE_DURATION, null);
        editor.putString(IS_LOGGED_IN, "false");
        editor.apply();

    }
}
