package com.example.visitus.user.user_class;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.visitus.R;
import com.example.visitus.data.place_data;
import com.example.visitus.user.show_data;

import java.util.ArrayList;

public class recycler_user_adapter extends RecyclerView.Adapter<recycler_user_adapter.help> {
    ArrayList<place_data> arrayList;
    Fragment main;

    public recycler_user_adapter(ArrayList<place_data> arrayList, Fragment main) {
        this.arrayList = arrayList;
        this.main = main;
    }

    @NonNull
    @Override
    public help onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_recycler_layout,parent,false);
        return new help(v);
    }

    @Override
    public void onBindViewHolder(@NonNull help holder, int position) {
        holder.city.setText(arrayList.get(position).getCity());
        holder.name.setText(arrayList.get(position).getName());
        Glide.with(main.getActivity()).load(arrayList.get(position).getImage()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_data m=new show_data();
                Bundle b=new Bundle();
                b.putString("image",arrayList.get(position).getImage());
                b.putString("name",arrayList.get(position).getName());
                b.putString("long",arrayList.get(position).getLongitude());
                b.putString("lati",arrayList.get(position).getLatitude());
                b.putString("city",arrayList.get(position).getCity());
                b.putString("about",arrayList.get(position).getAbout());
                m.setArguments(b);
                main.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_frameLayout,m).addToBackStack(null).commitAllowingStateLoss();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class help extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name,city;
        public help(@NonNull View itemView) {
            super(itemView);
            image=(ImageView) itemView.findViewById(R.id.recycler_tourist_image);
            name=(TextView) itemView.findViewById(R.id.recycler_tourist_name);
            city=(TextView) itemView.findViewById(R.id.recycler_tourist_city);
        }
    }
}
