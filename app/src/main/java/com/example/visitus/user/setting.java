package com.example.visitus.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.visitus.R;
import com.example.visitus.user_access.forget_password;


public class setting extends Fragment {

    private TextView forget_text,edit_text, language;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_setting, container, false);
        forgit_password(v);
        edit_profile(v);
        return v;
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