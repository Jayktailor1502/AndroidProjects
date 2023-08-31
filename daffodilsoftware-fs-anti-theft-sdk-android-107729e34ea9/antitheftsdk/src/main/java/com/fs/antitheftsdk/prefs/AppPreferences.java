package com.fs.antitheftsdk.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.fs.antitheftsdk.sdkClient.FsAntiTheftClient;

public class AppPreferences {
    private static final String SETTINGS_NAME = "FsAntiTheft_shared_preferences";
    private static AppPreferences sInstance;
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private AppPreferences(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        if (mPref != null)
            mEditor = mPref.edit();
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new AppPreferences(context.getApplicationContext());
        }
    }

    public static AppPreferences getInstance() {
        if (sInstance == null) {
            init(FsAntiTheftClient.getContext());
        }
        return sInstance;
    }

    public boolean isUserAlreadyLoggedIn() {

        return mPref.getBoolean(AppPreferenceKey.IS_LOGGED_IN, false);
    }

    public void putString(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public void putInt(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public void putBoolean(String key, boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public void putFloat(String key, float val) {
        mEditor.putFloat(key, val);
        mEditor.commit();
    }

    /**
     * Convenience method for storing doubles.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to store.
     * @param val The new value for the preference.
     */
    public void putDouble(String key, double val) {
        mEditor.putString(key, String.valueOf(val));
        mEditor.commit();
    }

    public void putLong(String key, long val) {
        mEditor.putLong(key, val);
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public String getString(String key) {
        return mPref.getString(key, null);
    }

    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return mPref.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return mPref.getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        return mPref.getFloat(key, defaultValue);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    public double getDouble(String key, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public void remove(String... keys) {
        for (String key : keys) {
            mEditor.remove(key);
        }
        mEditor.commit();
    }

    public void removePref() {
        mPref.edit().clear().apply();
    }

}
