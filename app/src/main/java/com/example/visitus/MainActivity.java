package com.example.visitus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.visitus.data.event_bus;
import com.example.visitus.data.place_data;
import com.example.visitus.user.about_as;
import com.example.visitus.user.setting;
import com.example.visitus.chating.user_chat;
import com.example.visitus.user.user_class.sharedprefrance;
import com.example.visitus.user.user_home;
import com.example.visitus.user.user_profile;
import com.example.visitus.user_access.login;
import com.example.visitus.user_access.splash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private  FirebaseFirestore database;
    private CircleImageView image;
    private TextView user_name;
    private SearchView search;
    private  ArrayList<place_data> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedprefrance s=new sharedprefrance(MainActivity.this);
        boolean theme= s.loadThemeMode();
        if (theme)
        {
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.activity_main);
        initialization();
        toolpar_intialize();
        navigation_items();
        get_user_data();
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user==null)
        {
            startActivity(new Intent(this,login.class));
        }
    }
    private void initialization()
    {
        auth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();
        replace_fragment(new user_home());
    }
    private void toolpar_intialize() {
        toolbar = findViewById(R.id.user_appbar_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,  drawerLayout, toolbar, R.string.close, R.string.open);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }
    private void navigation_items() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v= navigationView.getHeaderView(0);
        image=v.findViewById(R.id.circle_image_navigation_head);
        user_name=v.findViewById(R.id.user_name_navigation_head);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigation_item_click(item);
                return false;
            }
        });
    }
    private void navigation_item_click(MenuItem item)
    {
        if(item.getItemId()==R.id.navigation_menu_home) {
            replace_fragment(new user_home());
        }
        if(item.getItemId()==R.id.navigation_menu_setting) {
            replace_fragment(new setting());
        }
        else if(item.getItemId()==R.id.navigation_menu_contact) {
            replace_fragment(new user_chat());
        }
        else if(item.getItemId()==R.id.navigation_menu_about) {
            replace_fragment(new about_as());
        }
        else if(item.getItemId()==R.id.navigation_menu_profile) {
            startActivity(new Intent(MainActivity.this, user_profile.class));
        }
        else if(item.getItemId()==R.id.navigation_logout) {
              auth.signOut();
            startActivity(new Intent(MainActivity.this, login.class));

        }
    }

    private void replace_fragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.user_frameLayout,fragment).addToBackStack(null).commitAllowingStateLoss();


    }

    private void get_user_data()
    {
        String user_id=auth.getCurrentUser().getUid();
        database.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists())
                {
                    if (task.isSuccessful())
                    {
                        if (task.getResult().contains("image")) {
                            Glide.with(MainActivity.this).load(task.getResult().get("image").toString()).into(image);
                            user_name.setText(task.getResult().get("name").toString());
                        }
                        else
                        {
                            user_name.setText(task.getResult().get("name").toString());
                        }
                    }
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, user_profile.class));
                }
            }
        });
    }
    @Subscribe( threadMode = ThreadMode.MAIN)
    public void eventbus(event_bus event)
    {
        if (event.getMsg().equals("main"))
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);


    }


}