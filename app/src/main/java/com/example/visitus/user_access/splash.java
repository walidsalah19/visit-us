package com.example.visitus.user_access;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.example.visitus.R;
import com.example.visitus.user.user_class.sharedprefrance;

import java.util.Locale;

public class splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               language();
               darkmode();
                startActivity(new Intent(splash.this, login.class));
            }
        },3000);
    }
    private void darkmode()
    {
        sharedprefrance s=new sharedprefrance(splash.this);
        boolean theme= s.loadThemeMode();
        if (theme)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    private void language()
    {
        sharedprefrance s=new sharedprefrance(splash.this);
        String currentLang= s.loadlanguage();
        if (TextUtils.isEmpty(currentLang))
        {

        }
        else  if (currentLang.equals("ar"))
        {
            change_local_language("ar");
        }
        else if (currentLang.equals("en"))
        {
            change_local_language("en");
        }
    }
    private void change_local_language(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}