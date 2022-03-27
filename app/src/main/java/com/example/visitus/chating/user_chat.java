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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;


public class user_chat extends Fragment {

    private List<massageclass> malist = new ArrayList<>();
    private reycleradapter adapter;
    private RecyclerView recyclerView;
    private Button send;
    private EditText massege_tex;
    private ArrayList<chatmodel> chat_arr;
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
        get_data(v);
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


        adapter=new reycleradapter(chat_arr);
        recyclerView.setAdapter(adapter);
       // adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
    }
    private void sendmassege () {
        String ma = massege_tex.getText().toString();

        if (TextUtils.isEmpty(ma)) {
            Toast.makeText(getActivity(), "inter the massage", Toast.LENGTH_SHORT).show();
        } else {
            chat_arr.add(new chatmodel(ma,"1"));
            adapter.notifyDataSetChanged();
            String url="http://api.brainshop.ai/get?bid=164750&key=IefjI0KMg1rt0rM9&uid=[uid]&msg="+ma;
            Retrofit ret=retrofit.callretrofit();
            api_interface api=ret.create(api_interface.class);
            Call<massageclass> message_call=api.call_message(url);
            message_call.enqueue(new Callback<massageclass>() {
                @Override
                public void onResponse(Call<massageclass> call, Response<massageclass> response) {
                    if (response.isSuccessful())
                    {
                        massageclass model=response.body();
                        chat_arr.add(new chatmodel(model.getCnt(),"4"));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<massageclass> call, Throwable t) {
                    chat_arr.add(new chatmodel("change you'r  question","4"));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
    }