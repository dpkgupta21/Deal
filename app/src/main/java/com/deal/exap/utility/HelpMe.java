package com.deal.exap.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Locale;

public class HelpMe {

    public HelpMe() {
        Calendar rightNow = Calendar.getInstance();
    }


    // Check for valid mobile number of 10 digits
    public static void setLocale(String languageCode, Context mContext) {
        String code=null;
        if(languageCode.equalsIgnoreCase(Constant.LANG_ENGLISH_CODE)){
            code="en";
        }else{
            code="ar";
        }
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
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
