package com.example.visitus.user;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.visitus.MainActivity;
import com.example.visitus.R;
import com.example.visitus.data.event_bus;
import com.example.visitus.user.user_class.sharedprefrance;
import com.example.visitus.user_access.forget_password;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class setting extends Fragment {

    private TextView forget_text,edit_text, language;
    private SwitchCompat switchCompat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_setting, container, false);
        darkmode(v);
        forgit_password(v);
        edit_profile(v);
        language(v);
        return v;
    }
    private void darkmode(View v)
    {
        sharedprefrance s=new sharedprefrance(getActivity());
        switchCompat =(SwitchCompat) v.findViewById(R.id.check1);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            switchCompat.setChecked(true);
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    s.setThemeMode(true);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    s.setThemeMode(false);
                }
                EventBus.getDefault().postSticky(new event_bus("main"));
            }
        });

    }
    private void language(View v)
    {
        language=v.findViewById(R.id.change_language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             change_language();
            }
        });
    }
    private  void change_language()
    {
        SweetAlertDialog dialog=   new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        dialog .setTitleText(getString(R.string.change_language));
        dialog  .setCustomImage(R.drawable.ic_baseline_language_24);
        dialog .setConfirmText("Arabic");
        dialog  .getProgressHelper().setBarColor(Color.RED);
        dialog   .setConfirmClickListener(nDialog -> {

                    change_local_language( "ar");
                    nDialog.dismiss();

                }).setCancelText("english")
                .setCancelClickListener(sweetAlertDialog -> {

                        change_local_language("en");
                        sweetAlertDialog.dismiss();
                })
                .show();
    }
    private void change_local_language(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getActivity().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        sharedprefrance s=new sharedprefrance(getActivity());
        s.setlanguage(lang);
        EventBus.getDefault().postSticky(new event_bus("main"));
    }
    private void forgit_password(View v)
    {
        forget_text=v.findViewById(R.id.change_password);
        forget_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(getActivity(),forget_password.class);
                startActivity(notificationIntent);
            }
        });
    }
    private void edit_profile(View v)
    {
        edit_text=v.findViewById(R.id.edit_profile_setting);
        edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(getActivity(),user_profile.class);
                startActivity(notificationIntent);
            }
        });
    }
}