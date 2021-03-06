package com.deal.exap.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.deal.exap.login.SignInFragment;
import com.deal.exap.login.SplashScreen;

public class SessionManager {

    private static final String TAG = "<SessionManager>";
    private Context _context;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;

        Log.d(TAG, "session manager onstructor called");
    }

    /**
     * Check login method will check user login status If false it will redirect
     * user to login page Else won't do anything
     */
    public void checkLogin(Context mContext) {
        // Check login status
        if (!this.isLoggedIn(mContext)) {
            Log.d(TAG, "USer is not logged in");
            Toast.makeText(_context, "Please log in to continue", Toast.LENGTH_LONG).show();
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, SignInFragment.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     */
    public static void logoutUser(Context mContext) {
        // Clearing all data from Shared Preferences
        DealPreferences.clearAllPreferences(mContext);

        // After logout redirect user to Loing Activity
        Intent i = new Intent(mContext, SplashScreen.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
        Utils.ShowLog(TAG, "logging out user");
    }

    /**
     * Quick check for login
     * *
     */
    // Get Login State
    public boolean isLoggedIn(Context mContext) {
        return DealPreferences.isLoggedIn(mContext);
    }
}
