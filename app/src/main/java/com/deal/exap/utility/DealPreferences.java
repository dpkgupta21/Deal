package com.deal.exap.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.deal.exap.R;

import java.io.IOException;


public class DealPreferences {

    public static final String PREF_NAME = "DEAL_PREFERENCES";

    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String KEY_PHONE = "PHONE";
    public static final String USER_OTP = "OTP";
    public static final String APP_LANG = "LANG";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String DISTANCE_UNIT = "DISTANCE_UNIT";
    public static final String PUSH_REGISTRATION_ID = "PUSH_REGISTRATION_ID";
    public static final String IS_SHOW_SURVEY_AFTER_LOGIN = "IS_SHOW_SURVEY_AFTER_LOGIN";
    public static final String CURRENCY_ENG = "CURRENCY_ENG";
    public static final String CURRENCY_ARA = "CURRENCY_ARA";
    public static final String IS_GPS_ENABLE = "IS_GPS_ENABLE";


    // for payment info
    public static final String ACCOUNTS = "ACCOUNTS";


    public static void setIsGpsEnable(Context context, boolean isUpdateLocation) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_GPS_ENABLE, isUpdateLocation);
        editor.apply();
    }

    public static boolean getIsGpsEnable(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).
                getBoolean(IS_GPS_ENABLE, false);
    }


    public static void setCurrencyEng(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CURRENCY_ENG, userId);
        editor.apply();
    }

    public static String getCurrencyEng(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(CURRENCY_ENG,
                context.getString(R.string.sar));
    }

    public static void setCurrencyAra(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CURRENCY_ARA, userId);
        editor.apply();
    }

    public static String getCurrencyAra(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(CURRENCY_ARA,
                context.getString(R.string.sar));
    }

    public static void setIsShowSurveyAfterLogin(Context context, Boolean isShowSurveyAfterLogin) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(IS_SHOW_SURVEY_AFTER_LOGIN, isShowSurveyAfterLogin);
        editor.apply();
    }

    public static Boolean isShowSurveyAfterLogin(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(
                IS_SHOW_SURVEY_AFTER_LOGIN, false);
    }


    public static void setPushRegistrationId(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PUSH_REGISTRATION_ID, userId);
        editor.apply();
    }

    public static String getPushRegistrationId(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(PUSH_REGISTRATION_ID,
                null);
    }


    public static void setLatitude(Context context, double latitude) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LATITUDE, String.valueOf(latitude));
        editor.apply();
    }

    public static double getLatitude(Context context) {
        String latitude = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
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
        String longitude = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
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

    // Use KM by default distance unit
    public static String getDistanceUnit(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(DISTANCE_UNIT,
                Constant.DISTANCE_UNIT_KM_ENG);
    }


    /**
     * This genric method use to put object into preference<br>
     * How to use<br>
     * Bean bean = new Bean();<br>
     * putObjectIntoPref(context,bean,key)
     *
     * @param context Context of an application
     * @param e       your genric object
     * @param key     String key which is associate with object
     */
    public static <E> void putObjectIntoPref(Context context, E e, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString(key, ObjectSerializer.serialize(e));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        editor.commit();

    }

    public static <E> void removeObjectIntoPref(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();

    }

    /**
     * This method is use to get your object from preference.<br>
     * How to use<br>
     * Bean bean = getObjectFromPref(context,key);
     *
     * @param context
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E getObjectFromPref(Context context, String key) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            return (E) ObjectSerializer.deserialize(context.getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE).getString(key, null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearAllPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PHONE, null);
        editor.putString(LATITUDE, "0.0");
        editor.putString(LONGITUDE, "0.0");
        editor.putBoolean(IS_LOGGED_IN, false);
        editor.putBoolean(IS_SHOW_SURVEY_AFTER_LOGIN, false);
        editor.apply();

    }
}
