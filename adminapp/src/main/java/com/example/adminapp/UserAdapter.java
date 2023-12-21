package com.example.adminapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    //static ClickListener clickListener;
    Context context;
    ArrayList<UserModel> userModelsArrayList;

    public UserAdapter(Context context, ArrayList<UserModel> userModelsArrayList) {
        this.context = context;
        this.userModelsArrayList = userModelsArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_details_design, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        UserModel userModel = userModelsArrayList.get(position);
        holder.uName.setText("Name : "+userModel.getName());
        holder.uPhone.setText("Phone: "+userModel.getPhone());
        holder.uEmail.setText("Email : "+userModel.getEmail());
        holder.uAddress.setText("Address : "+userModel.getAddress());
        holder.uCarNumber.setText("CarNumber : "+userModel.getCarNumber());
        holder.uRfidNumber.setText("RFID Number : "+userModel.getRfidNumber());

    }

    @Override
    public int getItemCount() {
        return userModelsArrayList.size();
    }

    /*public interface ClickListener{
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener){
        SlotAdapter.clickListener = clickListener;
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView uName, uPhone, uEmail, uAddress, uCarNumber, uRfidNumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            uName = itemView.findViewById(R.id.uName);
            uPhone = itemView.findViewById(R.id.uPhone);
            uEmail = itemView.findViewById(R.id.uEmail);
            uAddress = itemView.findViewById(R.id.uAddress);
            uCarNumber = itemView.findViewById(R.id.uCarNumber);
            uRfidNumber = itemView.findViewById(R.id.uRfidNumber);

            //slot.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }*/
    }
}
