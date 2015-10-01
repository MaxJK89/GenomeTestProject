package quant.cann.genometestproject;

import android.app.Application;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import quant.cann.genometestproject.utils.BitmapCache;

/**
 * Created by Max Kleine on 9/30/2015.
 */
public class Global extends Application {
    private static Global singleton;
    public static LocationService lsLocationService;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private final String TAG = "GLOBAL";

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static synchronized Global getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        lsLocationService = new LocationService(this);
//        Log.d(TAG, lsLocationService.getClosestZip());

    }

    // if it can't get your lat, it will go to New York City
    public static double getLatitude() {
        if (lsLocationService != null) {
            return lsLocationService.getLatitude();
        }
        return 40.7127;
    }

    // if it can't get your lng, it will go to New York City
    public static double getLongitude() {
        if (lsLocationService != null) {
            return lsLocationService.getLongitude();
        }
        return 74.0059;
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
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new BitmapCache());
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
