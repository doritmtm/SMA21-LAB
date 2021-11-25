package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartwallet.models.Payment;
import com.example.smartwallet.ui.PaymentListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListPaymentsActivity extends AppCompatActivity {
    private Button leftButton,rightButton;
    private TextView monthText,statusText;
    private RecyclerView paymentsList;
    private int month;
    private DatabaseReference dbref;
    private List<Payment> paymentDataList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_payments);
        leftButton=findViewById(R.id.leftButton);
        rightButton=findViewById(R.id.rightButton);
        monthText=findViewById(R.id.monthText2);
        statusText=findViewById(R.id.statusText);
        paymentsList=findViewById(R.id.paymentsList);
        month=3;
        PaymentListAdapter paymentAdapter=new PaymentListAdapter();
        paymentsList.setAdapter(paymentAdapter);
        LinearLayoutManager listMgr=new LinearLayoutManager(this);
        paymentsList.setLayoutManager(listMgr);
        dbref=AppState.instance().getDBref();
        dbref.child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PaymentListAdapter paymentAdapter=new PaymentListAdapter();
                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
                DataSnapshot datasnap;
                Payment payment;
                paymentDataList=new ArrayList<>();
                while(it.hasNext())
                {
                    datasnap=it.next();
                    payment=datasnap.getValue(Payment.class);
                    payment.setDate(datasnap.getKey());
                    paymentDataList.add(payment);
                }
                paymentAdapter.setPaymentDataList(paymentDataList);
                statusText.setText("Success!");
                paymentsList.setAdapter(paymentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        monthText.setText("All Payments");
    }

    public void clicked(View view)
    {
        switch(view.getId())
        {
            case R.id.addPaymentFAB:
                AppState.instance().setPayment(null);
                Intent intent=new Intent(this,AddPaymentActivity.class);
                startActivity(intent);
                break;
        }
    }
}