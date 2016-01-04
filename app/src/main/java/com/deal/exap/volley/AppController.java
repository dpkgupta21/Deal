package com.deal.exap.volley;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

// This class id included in android manifest in <application>
// So that it is android launches it every time app starts

public class AppController extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    //private static final String TWITTER_KEY = "0WzgEZ838raQlA7BPASXLgsub";
    private static final String TWITTER_KEY = "A0roEDkb3B399yjz52sjPJhQ8";
    private static final String TWITTER_SECRET = "Nse4ZXiYL3ntf1Huj00kFXFUzcofw7eEJnA3VeSD3LRLClGjEP";
//    private static final String TWITTER_SECRET = "szOdlqn9obH0MEMaGnz2dTMMQXIdcbSQvtDcT7YkOjyALQKuEF";


    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
  //  public static ArrayList<String> buddyList;
  //  public static List<Laps> lapsList;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        mInstance = this;
      //  lapsList = new ArrayList<>();

//        LeakCanary.install(this);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}