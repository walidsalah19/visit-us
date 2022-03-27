package com.example.visitus.chating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visitus.R;


import java.util.List;

public class reycleradapter extends RecyclerView.Adapter<reycleradapter.recyler> {
    private List<chatmodel> massagelist;
   public reycleradapter(List<chatmodel> massagelist)
   {
       this.massagelist=massagelist;
   }

    @NonNull
    @Override
    public recyler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recive_data_chat,parent,false);
       recyler jk=new recyler(v);
       return jk;
    }

    @Override
    public void onBindViewHolder(@NonNull recyler holder, int position) {
        chatmodel massage = massagelist.get(position);
        String idsend = massage.getSender();
        holder.sendertextmassage.setVisibility(View.INVISIBLE);
        holder.recivertextmassage.setVisibility(View.INVISIBLE);
        if (idsend.equals("1")) {
                holder.sendertextmassage.setVisibility(View.VISIBLE);
                holder.sendertextmassage.setText(massage.getMessage());

            } else {
                holder.recivertextmassage.setVisibility(View.VISIBLE);
                holder.recivertextmassage.setText(massage.getMessage());
            }
    }

    @Override
    public int getItemCount() {
        return massagelist.size();
    }

    public class recyler extends RecyclerView.ViewHolder{
             TextView recivertextmassage,sendertextmassage;
        public recyler(@NonNull View itemView) {
            super(itemView);
            recivertextmassage=(TextView) itemView.findViewById(R.id.chateresiver);
            sendertextmassage=(TextView)itemView.findViewById(R.id.chatesender);
        }
    }

}
