package com.example.smartwallet;

import android.widget.ArrayAdapter;

import com.example.smartwallet.models.Payment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppState {
    private static AppState singleton;
    private Payment payment;
    private DatabaseReference dbref;
    private ArrayAdapter<String> typesAdapter;
    public AppState()
    {
        dbref = FirebaseDatabase.getInstance("https://smart-wallet-30b48-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    }
    public static AppState instance()
    {
        if(singleton==null)
        {
            singleton=new AppState();
        }
        return singleton;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public DatabaseReference getDBref() {
        return dbref;
    }

    public void setDBref(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public ArrayAdapter<String> getTypesAdapter() {
        return typesAdapter;
    }

    public void setTypesAdapter(ArrayAdapter<String> typesAdapter) {
        this.typesAdapter = typesAdapter;
    }
}
