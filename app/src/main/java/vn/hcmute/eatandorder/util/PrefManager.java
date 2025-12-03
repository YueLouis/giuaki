package vn.hcmute.eatandorder.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import vn.hcmute.eatandorder.data.model.AuthResponse;

public class PrefManager {
    private static final String PREF_NAME = "EAT_AND_ORDER";
    private static final String KEY_USER = "user";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Gson gson = new Gson();

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUser(AuthResponse user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER, userJson);
        editor.apply();
    }

    public AuthResponse getUser() {
        String userJson = pref.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, AuthResponse.class);
        }
        return null;
    }

    public boolean isLoggedIn() {
        return pref.getString(KEY_USER, null) != null;
    }

    public void logout() {
        editor.remove(KEY_USER);
        editor.apply();
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}
