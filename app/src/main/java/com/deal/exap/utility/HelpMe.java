package com.deal.exap.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HelpMe {

    public HelpMe() {
        Calendar rightNow = Calendar.getInstance();
    }


    public static String getDurationTime(String startTime, String endTime) {
        //String startDateString = "06/27/2007";

        String diff = null;
        try {
            String REQUEST_DATE_FORMAT = "dd MMM yyyy";
            DateFormat df = new SimpleDateFormat(REQUEST_DATE_FORMAT);
            Date startDate = df.parse(startTime);
            Date endDate = df.parse(endTime);
            long timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
            diff = String.format("%dD %dH %dM",TimeUnit.MILLISECONDS.toDays(timeDiff),
                    TimeUnit.MILLISECONDS.toHours(timeDiff)-
                            TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(timeDiff)),
                    TimeUnit.MILLISECONDS.toMinutes(timeDiff) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diff;

    }

    // Check for valid mobile number of 10 digits
    public static void setLocale(String languageCode, Context mContext) {
        String code = null;
        if (languageCode.equalsIgnoreCase(Constant.LANG_ENGLISH_CODE)) {
            code = "en";
        } else {
            code = "ar";
        }
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
    }


    public static String getDistanceUnitSign(String distanceUnitSign, Context mContext) {

        if (distanceUnitSign.equalsIgnoreCase(Constant.DISTANCE_UNIT_KM_ENG)) {
            return isArabic(mContext) ? Constant.DISTANCE_UNIT_KM_ARA : Constant.DISTANCE_UNIT_KM_ENG;
        } else {
            return isArabic(mContext) ? Constant.DISTANCE_UNIT_MILES_ARA : Constant.DISTANCE_UNIT_MILES_ENG;
        }
    }


    public static String getRelatedPreferenceText(Context mContext, String titleEng, String titleAra) {

        return isArabic(mContext) ? ((titleAra == null || titleAra.equalsIgnoreCase("")) ? titleEng : titleAra) :
                titleEng;

    }

    public static String getCurrencySign(Context mContext) {

        return isArabic(mContext) ? DealPreferences.getCurrencyAra(mContext) :
                DealPreferences.getCurrencyEng(mContext);

    }

    public static boolean isArabic(Context mContext) {
        if (DealPreferences.getAPP_LANG(mContext).contains(Constant.LANG_ARABIC_CODE)) {
            return true;
        } else
            return false;
    }


    public static boolean isMiles(Context mContext) {
        if (DealPreferences.getDistanceUnit(mContext).equalsIgnoreCase(Constant.DISTANCE_UNIT_MILES_ENG)) {
            return true;
        } else
            return false;
    }
    public static boolean isCurrencyCheck(Context mContext, String currencyNameEng) {
        if (DealPreferences.getCurrencyEng(mContext).equalsIgnoreCase(currencyNameEng)) {
            return true;
        } else
            return false;
    }
    public static double convertKMToMiles(String kilometers) {

        DecimalFormat decimalFormat = new DecimalFormat("#");
        int km = Integer.parseInt(kilometers);
        double miles = 0.621 * km;

        decimalFormat.format(miles);
        return    Math.abs(miles);
    }

    public static void pullDb(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "data/data/" + context.getPackageName() + "/databases/memories.db";
                String backupDBPath = "memories.sqlite";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
