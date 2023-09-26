package com.scanner.scanner.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SHARED_PREF_NAME = "SCANNER_";
    String SESSION_USER_ID = SHARED_PREF_NAME + "USER_ID";
    String SESSION_USER_TYPE = SHARED_PREF_NAME + "USER_TYPE";
    String SESSION_LANGUAGE = SHARED_PREF_NAME + "LANGUAGE";
    String SESSION_EXAM_ID = SHARED_PREF_NAME + "EXAM_ID";


    public SessionManagement(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserID(String userID, String userType) {
        editor.putString(SESSION_USER_ID, userID).commit();
        editor.putString(SESSION_USER_TYPE, userType).commit();
    }


    public String getUserID() {
        return sharedPreferences.getString(SESSION_USER_ID, "-1");
    }

    public String getUserType() {
        return sharedPreferences.getString(SESSION_USER_TYPE, "-1");
    }

    public void removeUser() {
        editor.putString(SESSION_USER_ID, "-1").commit();
    }

}
