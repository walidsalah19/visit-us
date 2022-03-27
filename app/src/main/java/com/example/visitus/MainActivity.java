package com.example.visitus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.visitus.data.place_data;
import com.example.visitus.user.about_as;
import com.example.visitus.user.recycler_user_adapter;
import com.example.visitus.user.setting;
import com.example.visitus.chating.user_chat;
import com.example.visitus.user.user_profile;
import com.example.visitus.user_access.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private  FirebaseFirestore database;
    private CircleImageView image;
    private TextView user_name;
    private SearchView search;
    private  ArrayList<place_data> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        toolpar_intialize();
        navigation_items();
        RecyclerView_container();
        get_user_data();
        searchview_method();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    private void RecyclerView_container()
    {
        recyclerView=findViewById(R.id.user_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arr=new ArrayList<>();
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        database.collection("places").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    arr.clear();
                    for (QueryDocumentSnapshot snap:task.getResult()) {
                        place_data data =new place_data(snap.get("image").toString(),snap.get("name").toString(),snap.get("city").toString()
                                ,snap.get("longitude").toString(),snap.get("latitude").toString(),snap.get("about").toString(),snap.get("id").toString(),snap.get("image_id").toString());
                        arr.add(data);
                    }
                    recycler_user_adapter adapter=new recycler_user_adapter(arr, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

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
    private void searchview_method()
    {
        search=findViewById(R.id.user_search_view);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //get_device_fromdatabase(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                get_device_searched(s);
                return false;
            }
        });
    }
    private void get_device_searched(String s) {
        ArrayList<place_data>arrayList=new ArrayList<>();
        for(int i=0;i< arr.size();i++) {
            if(arr.get(i).getName().contains(s))
            {
                arrayList.add(arr.get(i));
            }
        }
            recycler_user_adapter adapter=new recycler_user_adapter(arrayList, MainActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

    }

}