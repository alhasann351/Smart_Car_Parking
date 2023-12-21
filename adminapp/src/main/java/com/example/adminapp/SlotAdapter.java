package com.example.adminapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.MyViewHolder> {

    //static ClickListener clickListener;
    Context context;
    ArrayList<SlotModel> slotModelsArrayList;

    public SlotAdapter(Context context, ArrayList<SlotModel> slotModelsArrayList) {
        this.context = context;
        this.slotModelsArrayList = slotModelsArrayList;
    }

    @NonNull
    @Override
    public SlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_slot_design, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotAdapter.MyViewHolder holder, int position) {
        SlotModel slotModel = slotModelsArrayList.get(position);
        holder.slotName.setText(slotModel.getSlotName());
        holder.status.setText(slotModel.getStatus());
        if(slotModel.getStatus().equals("Unavailable")){
            holder.status.setTextColor(Color.RED);
        }
        holder.slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddslotActivity.class);
                intent.putExtra("id", slotModel.getId());
                intent.putExtra("slotName", slotModel.getSlotName());
                intent.putExtra("status", slotModel.getStatus());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return slotModelsArrayList.size();
    }

    /*public interface ClickListener{
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener){
        SlotAdapter.clickListener = clickListener;
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView slotName, status;
        CardView slot;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            slotName = itemView.findViewById(R.id.slotName);
            status = itemView.findViewById(R.id.status);
            slot = itemView.findViewById(R.id.slot);

            //slot.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }*/
    }
}
