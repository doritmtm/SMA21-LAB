package com.example.smartwallet.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartwallet.R;
import com.example.smartwallet.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder> {
    private List<Payment> paymentDataList=new ArrayList<>();

    public PaymentListAdapter() {

    }

    public PaymentListAdapter(List<Payment> paymentDataList) {
        this.paymentDataList = paymentDataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView listNameText,listDateText,listTypeText,listCostText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listNameText=itemView.findViewById(R.id.listNameText);
            listDateText=itemView.findViewById(R.id.listDateText);
            listTypeText=itemView.findViewById(R.id.listTypeText);
            listCostText=itemView.findViewById(R.id.listCostText);
        }

        public TextView getListNameText() {
            return listNameText;
        }

        public TextView getListDateText() {
            return listDateText;
        }

        public TextView getListTypeText() {
            return listTypeText;
        }

        public TextView getListCostText() {
            return listCostText;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment payment=paymentDataList.get(position);
        holder.listNameText.setText(payment.getName());
        holder.listCostText.setText(Double.toString(payment.getCost())+" RON");
        holder.listTypeText.setText(payment.getType());
        holder.listDateText.setText(payment.getDate());
    }

    @Override
    public int getItemCount() {
        return paymentDataList.size();
    }


    public List<Payment> getPaymentDataList() {
        return paymentDataList;
    }

    public void setPaymentDataList(List<Payment> paymentDataList) {
        this.paymentDataList = paymentDataList;
    }
}
