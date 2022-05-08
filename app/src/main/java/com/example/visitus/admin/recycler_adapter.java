package com.example.visitus.admin;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.visitus.R;
import com.example.visitus.data.place_data;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.help> {
   ArrayList<place_data> arrayList;
   admin_main admin;
    public recycler_adapter(ArrayList<place_data> arrayList,    admin_main admin) {
        this.arrayList = arrayList;
        this.admin=admin;
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
        Glide.with(admin).load(arrayList.get(position).getImage()).into(holder.image);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent b=new Intent(admin,maintanace_place.class);
               b.putExtra("id",arrayList.get(position).getId());
               admin.startActivity(b);

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
