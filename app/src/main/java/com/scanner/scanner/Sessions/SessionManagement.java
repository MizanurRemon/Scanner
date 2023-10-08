package com.scanner.scanner.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

import com.scanner.scanner.Model.AuthResponse;
import com.scanner.scanner.Utils.Constants;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SHARED_PREF_NAME = "SCANNER_";
    String SESSION_USER_ID = SHARED_PREF_NAME + "USER_ID";
    String SESSION_USER_TYPE = SHARED_PREF_NAME + "USER_TYPE";

    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserID(String userID) {
        editor.putString(SESSION_USER_ID, userID).commit();
    }

    public void saveToken(AuthResponse authResponse, String userType) {
        editor.putString(Constants.REFRESH_TOKEN, authResponse.refreshToken);
        editor.putString(Constants.ACCESS_TOKEN, authResponse.accessToken);
        editor.putString(Constants.USERTYPE, userType);

        editor.commit();
    }

    public String getAccessToken() {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN, "-1");
    }

    public String getUserType() {
        return sharedPreferences.getString(Constants.USERTYPE, "-1");
    }

    public void removeUser() {
        editor.putString(Constants.ACCESS_TOKEN, "-1").commit();
        editor.putString(Constants.REFRESH_TOKEN, "-1").commit();
        editor.putString(Constants.USERTYPE, "-1").commit();
    }

}
