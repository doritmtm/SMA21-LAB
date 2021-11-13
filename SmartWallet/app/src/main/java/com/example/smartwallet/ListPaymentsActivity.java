package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartwallet.models.Payment;
import com.example.smartwallet.ui.PaymentListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        paymentsList.setLayoutManager(new LinearLayoutManager(this));
        dbref=FirebaseDatabase.getInstance("https://smart-wallet-30b48-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        dbref.child("wallet").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot datasnapParent=task.getResult();
                Iterator<DataSnapshot> it=datasnapParent.getChildren().iterator();
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
        });
        monthText.setText("All Payments");
    }
}