package com.example.visitus.user.user_class;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class sharedprefrance {
    SharedPreferences preferences;
    Context context;
    public sharedprefrance(Context context){
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setlanguage(String language){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language",language);
        editor.apply();
    }

    public String loadlanguage(){
        return preferences.getString("language","");
    }
    public void setThemeMode(boolean state){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("state",state);
        editor.apply();
    }

    public boolean loadThemeMode(){
        return preferences.getBoolean("state",false);
    }
}
