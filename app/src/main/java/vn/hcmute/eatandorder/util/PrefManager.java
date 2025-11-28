package vn.hcmute.eatandorder.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String PREF_NAME = "auth";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_LOGGED_IN = "loggedIn";

    private final SharedPreferences prefs;

    public PrefManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

}
