package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
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
    private DatabaseReference dbref;
    private List<Payment> paymentDataList=new ArrayList<>();
    private int currentMonth;
    private PaymentListAdapter paymentAdapter=new PaymentListAdapter();
    private SharedPreferences pref;

    public class PaymentDataListener implements ValueEventListener
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Iterator<DataSnapshot> it = snapshot.getChildren().iterator();
            DataSnapshot datasnap;
            Payment payment;
            paymentDataList = new ArrayList<>();
            while (it.hasNext()) {
                datasnap = it.next();
                payment = datasnap.getValue(Payment.class);
                payment.setDate(datasnap.getKey());
                paymentDataList.add(payment);
                AppState.instance().updateBackup(payment);
            }
            updatePaymentsListUI();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_payments);
        AppState.instance().setContext(getApplicationContext());
        leftButton=findViewById(R.id.leftButton);
        rightButton=findViewById(R.id.rightButton);
        monthText=findViewById(R.id.monthText2);
        statusText=findViewById(R.id.statusText);
        paymentsList=findViewById(R.id.paymentsList);
        pref=getPreferences(Context.MODE_PRIVATE);
        currentMonth=pref.getInt("currentMonth",0);
        PaymentListAdapter paymentAdapter=new PaymentListAdapter();
        paymentsList.setAdapter(paymentAdapter);
        LinearLayoutManager listMgr=new LinearLayoutManager(this);
        paymentsList.setLayoutManager(listMgr);
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm.registerNetworkCallback(new NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED).build(),new ConnectivityManager.NetworkCallback()
            {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    AppState.instance().countUpNetwork();
                    AppState.instance().syncBackupWithFirebase();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    AppState.instance().countDownNetwork();
                }
            });
            network=cm.getActiveNetwork();
        }
        if(network!=null)
        {
            AppState.instance().syncBackupWithFirebase();
        }
        dbref=AppState.instance().getDBref();
        dbref.child("wallet").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                dbref.child("wallet").addValueEventListener(new PaymentDataListener());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            network=cm.getActiveNetwork();
        }
        if(network==null)
        {
            updatePaymentsListUI();
        }
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
            case R.id.leftButton:
                currentMonth--;
                if(currentMonth<=0)
                {
                    currentMonth=12;
                }
                pref.edit().putInt("currentMonth",currentMonth).apply();
                paymentAdapter.setPaymentDataList(filterDBPayments(currentMonth));
                paymentsList.setAdapter(paymentAdapter);
                monthText.setText("Payments from "+getMonthName(currentMonth));
                break;
            case R.id.rightButton:
                currentMonth++;
                if(currentMonth>=13)
                {
                    currentMonth=1;
                }
                pref.edit().putInt("currentMonth",currentMonth).apply();
                paymentAdapter.setPaymentDataList(filterDBPayments(currentMonth));
                paymentsList.setAdapter(paymentAdapter);
                monthText.setText("Payments from "+getMonthName(currentMonth));
                break;
        }
    }

    private ArrayList<Payment> filterDBPayments(int month)
    {
        ArrayList<Payment> paymentsList=new ArrayList<>();
        for(Payment p:paymentDataList)
        {
            if(Integer.parseInt(p.getDate().substring(5,7))==month)
            {
                paymentsList.add(p);
            }
        }
        return paymentsList;
    }

    private String getMonthName(int month)
    {
        switch(month)
        {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
        }
        return "Error";
    }

    public void updatePaymentsListUI()
    {
        paymentDataList=AppState.instance().loadBackup();
        if(currentMonth==0 && paymentDataList.size()>0)
        {
            currentMonth=Integer.parseInt(paymentDataList.get(0).getDate().substring(5,7));
            pref.edit().putInt("currentMonth",currentMonth).apply();
        }
        paymentAdapter.setPaymentDataList(filterDBPayments(currentMonth));
        monthText.setText("Payments from "+getMonthName(currentMonth));
        statusText.setText("Success!");
        paymentsList.setAdapter(paymentAdapter);
    }

}