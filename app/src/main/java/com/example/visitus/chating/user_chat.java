package com.example.visitus.chating;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.visitus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class user_chat extends Fragment {

    private reycleradapter adapter;
    private RecyclerView recyclerView;
    private Button send;
    private EditText massege_tex;
    private ArrayList<chatmodel> chat_arr;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private  String user_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_user_chat, container, false);
        send=v.findViewById(R.id.send_message);
        massege_tex=v.findViewById(R.id.message);
        database= FirebaseDatabase.getInstance().getReference("chat");
        auth=FirebaseAuth.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
        get_data(v);
        get_message_database();
        return v;
    }
    private void get_data(View v)
    {
        recyclerView=v.findViewById(R.id.chat_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chat_arr=new ArrayList<>();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmassege();
            }
        });
    }
    private void sendmassege () {
        String message = massege_tex.getText().toString();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), getString(R.string.enter_the_massage), Toast.LENGTH_SHORT).show();
        } else {
            chat_arr.add(new chatmodel(message,"1"));
            add_message_todatabase(message,"1");
            add_message_todatabase(chat_class.chatbot(message),"4");
            adapter.notifyDataSetChanged();
            massege_tex.setText("");

        }
    }
    private void add_message_todatabase(String message,String user_type)
    {
        chatmodel model=new chatmodel(message,user_type);
        database.child(user_id).push().setValue(model);
    }
    private void get_message_database()
    {

        database.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chat_arr.clear();
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        String message=snap.child("message").getValue().toString();
                        String user_type=snap.child("sender").getValue().toString();
                        chatmodel  model=new chatmodel(message,user_type);
                        chat_arr.add(model);
                    }
                    adapter=new reycleradapter(chat_arr);
                    recyclerView.setAdapter(adapter);
                    recyclerView.smoothScrollToPosition(chat_arr.size()-1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    }