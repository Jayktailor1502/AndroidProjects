package com.fs.antitheftsdk.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.fs.antitheftsdk.R;

import java.util.ArrayList;
import java.util.List;

import static com.fs.antitheftsdk.base.Constants.TAG;

/**
 * Handles requests for device location via GPS and/or Network.
 */
public class PositionManager implements LocationListener {
    private static final long MIN_TIME_FIX = 1000;
    private static final float MIN_DISTANCE = 10.0f;
    private final LocationManager mLocationManager;
    private final List<PositionListener> mListeners;
    private volatile boolean mGpsProviderAvailable;
    private volatile boolean mNetworkProviderAvailable;
    private volatile boolean mMonitoring;
    private Context mContext;

    /**
     * Create a new instance,
     *
     * @param context context.
     */
    public PositionManager(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mListeners = new ArrayList<>();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }
        updateProviderStatus(location.getProvider(), true);

        if (!mNetworkProviderAvailable && !mGpsProviderAvailable) {
            publishLocation(null);
        } else {
            publishLocation(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        switch (i) {
            case LocationProvider.AVAILABLE:
                updateProviderStatus(s, true);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                updateProviderStatus(s, false);
                break;
            case LocationProvider.OUT_OF_SERVICE:
                updateProviderStatus(s, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        updateProviderStatus(s, true);
    }

    @Override
    public void onProviderDisabled(String s) {
        updateProviderStatus(s, false);
    }

    /**
     * Update provider status.
     *
     * @param provider location provider for which status has changed.
     * @param value    enabled or disabled value.
     */
    private synchronized void updateProviderStatus(String provider, boolean value) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            mGpsProviderAvailable = value;
        } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            mNetworkProviderAvailable = value;
        }
    }

    /**
     * Publish location to all registered listeners.
     *
     * @param location Location to publish.
     */
    private synchronized void publishLocation(Location location) {
        List<PositionListener> unregisterList = new ArrayList<>();
        for (PositionListener listener : mListeners) {
            boolean listening = listener.onLocationUpdated(location);
            if (!listening) {
                unregisterList.add(listener);
            }
        }
        mListeners.removeAll(unregisterList);

        if (mListeners.isEmpty()) {
            stopMonitoring();
        }
    }

    /**
     * Stops location monitoring.
     */
    public synchronized void stopMonitoring() {
        if (!mMonitoring) {
            return;
        }

        if (Boolean.TRUE.equals(checkLocationPermission(mContext))) {
            Log.d(TAG, mContext.getString(R.string.permission_is_true));
        } else {
            mLocationManager.removeUpdates(this);
            mMonitoring = false;
        }
    }

    /**
     * Request a location update.
     *
     * @param listener listener for location updates, or null to just get last location as return value.
     * @return Last known location , or null if not any.
     */
    public synchronized Location requestPosition(PositionListener listener) {
        if (listener != null && !mListeners.contains(listener)) {
            mListeners.add(listener);
        }
        if (!mMonitoring && !mListeners.isEmpty()) {
            startMonitoring();
        }
        return getLastLocation();
    }

    /**
     * Starts location monitoring.
     */
    public synchronized void startMonitoring() {
        if (mMonitoring || mListeners.isEmpty()) {
            return;
        }
        LocationProvider pg = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        LocationProvider pn = mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (Boolean.TRUE.equals(checkLocationPermission(mContext))) {
            Log.d(TAG, mContext.getString(R.string.permission_is_true));
        } else {
            if (pg != null) {
                mLocationManager.requestLocationUpdates(pg.getName(), MIN_TIME_FIX, MIN_DISTANCE, this);
            } else {
                mGpsProviderAvailable = false;
            }
            if (pn != null) {
                mLocationManager.requestLocationUpdates(pn.getName(), MIN_TIME_FIX, MIN_DISTANCE, this);
            } else {
                mNetworkProviderAvailable = false;
            }
            mMonitoring = true;
        }
    }

    /**
     * Get last known location.
     *
     * @return Last known location, or null if no location remembered.
     */
    private Location getLastLocation() {
        Location lg = null;
        Location ln = null;
        if (Boolean.TRUE.equals(checkLocationPermission(mContext))) {
            return null;
        } else {

            if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                lg = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
                ln = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }


        if (lg == null && ln == null) {
            return null;
        }

        if (lg != null && ln != null) {
            return lg.getTime() < ln.getTime() ? lg : ln;
        }

        if (lg != null) {
            return lg;
        } else {
            return ln;
        }
    }

    /**
     * Cancel a location request.
     *
     * @param listener same location listener that was used to request position.
     */
    public synchronized void cancelRequest(PositionListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
        if (mListeners.isEmpty()) {
            stopMonitoring();
        }
    }

    /**
     * Callback interface for position updates.
     */
    public interface PositionListener {
        /**
         * Called when a new location fix is available.
         *
         * @param location Location, or null is location is not available (location providers may ne switched off etc).
         * @return If true is returned, future locations will be delivered. If false is returned ,
         * listener is unregistered and no future locations are delivered.
         */
        boolean onLocationUpdated(Location location);
    }

    public static Boolean checkLocationPermission(Context mContext){
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }
}

