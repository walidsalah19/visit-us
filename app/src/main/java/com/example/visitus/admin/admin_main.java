package com.example.visitus.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.example.visitus.MainActivity;
import com.example.visitus.R;
import com.example.visitus.data.place_data;
import com.example.visitus.user.recycler_user_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class admin_main extends AppCompatActivity {
    private FloatingActionButton add_new;
    private RecyclerView recyclerView;
    private SearchView search;
    private  ArrayList<place_data>arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        add_new_place();
        searchview_method();
    }
    @Override
    protected void onStart() {
        super.onStart();
        RecyclerView_container();
    }
    private void add_new_place()
    {
        add_new=findViewById(R.id.add_new_touristic_place);
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_main.this,add_touristic_places.class));
            }
        });
    }
    private void RecyclerView_container()
    {
        recyclerView=findViewById(R.id.admin_recyclerView);
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
                    recycler_adapter adapter=new recycler_adapter(arr,admin_main.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
    private void searchview_method()
    {
        search=findViewById(R.id.admin_search_view);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                get_device_searched(s);
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
        for(int i=0;i<arr.size();i++) {
            if(arr.get(i).getName().contains(s))
            {
                arrayList.add(arr.get(i));
            }
        }
            recycler_adapter adapter=new recycler_adapter(arrayList,admin_main.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

    }

}