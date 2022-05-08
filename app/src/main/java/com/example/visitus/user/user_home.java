package com.example.visitus.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.visitus.R;
import com.example.visitus.data.place_data;
import com.example.visitus.user.user_class.recycler_user_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class user_home extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private SearchView search;
    private ArrayList<place_data> arr;
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
        View v= inflater.inflate(R.layout.fragment_user_home, container, false);
        initialization();
        RecyclerView_container(v);
        searchview_method(v);
        return v;
    }
    private void initialization()
    {
        auth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();
    }
    private void RecyclerView_container(View v)
    {
        recyclerView=v.findViewById(R.id.user_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arr=new ArrayList<>();
        database.collection("places").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    arr.clear();
                    for (QueryDocumentSnapshot snap:task.getResult()) {
                        place_data data =new place_data(snap.get("image").toString(),snap.get("name").toString()
                                ,snap.get("city").toString()
                                ,snap.get("longitude").toString(),snap.get("latitude").toString()
                                ,snap.get("about").toString(),snap.get("id").toString(),snap.get("image_id").toString());
                        arr.add(data);
                    }
                    recycler_user_adapter adapter=new recycler_user_adapter(arr, user_home.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
    private void searchview_method(View v)
    {
        search=v.findViewById(R.id.user_search_view);
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
        recycler_user_adapter adapter=new recycler_user_adapter(arrayList, user_home.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}