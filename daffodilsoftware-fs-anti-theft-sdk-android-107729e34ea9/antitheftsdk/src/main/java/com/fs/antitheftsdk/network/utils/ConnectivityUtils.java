package com.fs.antitheftsdk.network.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class ConnectivityUtils {
    private ConnectivityUtils() {
    }

    /**
     * param pContext
     * return true if network available else false.
     */
    public static boolean isNetworkEnabled(Context pContext) {
        NetworkInfo activeNetwork = getActiveNetwork(pContext);
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static NetworkInfo getActiveNetwork(Context pContext) {
        ConnectivityManager conMngr = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMngr == null ? null : conMngr.getActiveNetworkInfo();

    }

    /**
     * param pContext
     * return
     */
    public static boolean isGpsEnabled(Context pContext) {
        LocationManager locationManager = (LocationManager) pContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    /**
     * Get the network info
     * <p>
     * param context
     * return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * <p>
     * param context
     * return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * <p>
     * param context
     * param type
     * return
     */
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * <p>
     * param context
     * param type
     * return
     */
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * <p>
     * param context
     * return
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * <p>
     * param type
     * param subType
     * return
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true;
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true;
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return true;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return true;
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return true;
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return false;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return true;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}