package com.example.visitus.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.visitus.R;

public class show_data extends Fragment {
    private TextView name,city,about;
    private ImageView image;
    private ImageButton location;
    private String longitude,latitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            getActivity().setTheme(R.style.Theme_Dark);
        }else {
            getActivity().setTheme(R.style.Theme_Light);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_show_data, container, false);
        intialization(v);
        location();
        return v;
    }
    private void intialization(View v)
    {
        name=v.findViewById(R.id.user_show_name);
        city=v.findViewById(R.id.user_show_city);
        about=v.findViewById(R.id.user_show_about);
        image=v.findViewById(R.id.user_show_image);
        location=v.findViewById(R.id.user_show_location);

        Bundle b=getArguments();
        name.setText(b.getString("name"));
        city.setText(b.getString("city"));
        about.setText(b.getString("about"));
        Glide.with(getActivity()).load(b.getString("image")).into(image);
        longitude=b.getString("long");
        latitude=b.getString("lati");

    }
    private void location()
    {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?saddr=" + "%f" + "," + "%f" + "&daddr=" + latitude+ "," + longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }
}