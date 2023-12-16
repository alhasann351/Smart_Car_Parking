package com.example.smartcarparking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<HistoryModel> historyModelsArrayList;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyModelsArrayList) {
        this.context = context;
        this.historyModelsArrayList = historyModelsArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_design, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        HistoryModel historyModel = historyModelsArrayList.get(position);
        holder.name.setText("Name : "+historyModel.getName());
        holder.carNumber.setText("Car Number : "+historyModel.getCarNumber());
        holder.rfidNumber.setText("RFID Number : "+historyModel.getRfidNumber());
        holder.bookedTime.setText(historyModel.getBookedTime());
        holder.payAmount.setText("Pay Amount : "+historyModel.getPayAmount());
        holder.slot.setText("Slot : "+historyModel.getSlot());
        //holder.slot.setText(historyModel.getType());
    }

    @Override
    public int getItemCount() {
        return historyModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, carNumber, rfidNumber, bookedTime, payAmount, slot;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            carNumber = itemView.findViewById(R.id.carNumber);
            rfidNumber = itemView.findViewById(R.id.rfidNumber);
            bookedTime = itemView.findViewById(R.id.bookedTime);
            payAmount = itemView.findViewById(R.id.payAmount);
            slot = itemView.findViewById(R.id.slot);
        }
    }
}
