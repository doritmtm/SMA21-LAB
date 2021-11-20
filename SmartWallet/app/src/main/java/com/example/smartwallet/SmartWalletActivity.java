package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartwallet.models.MonthlyExpenses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class SmartWalletActivity extends AppCompatActivity {
    private TextView monthText;
    private Button updateBut;
    private EditText incomeInput,expensesInput;
    private DatabaseReference dbref;
    private MonthlyExpenses mexpense=null;
    private String month=null;
    private Spinner monthSpinner;
    private ArrayAdapter monthsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthText = findViewById(R.id.monthText);
        updateBut = findViewById(R.id.updateButton);
        incomeInput = findViewById(R.id.incomeInput);
        expensesInput = findViewById(R.id.expensesInput);
        monthSpinner = findViewById(R.id.monthSpinner);
        monthsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthsAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = monthsAdapter.getItem(position).toString();
                dbref.child("calendar").child(month).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        mexpense = task.getResult().getValue(MonthlyExpenses.class);
                        if (mexpense != null) {
                            monthText.setText(month);
                            incomeInput.setText(Float.toString(mexpense.getIncome()));
                            expensesInput.setText(Float.toString(mexpense.getExpenses()));
                        } else {
                            monthText.setText("Error finding " + month + " in database");
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbref = FirebaseDatabase.getInstance("https://smart-wallet-30b48-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        dbref.child("calendar").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snap = task.getResult();
                updateMonthsFromDB(snap);
            }
        });
        dbref.child("calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateMonthsFromDB(snapshot);
                if(mexpense!=null && month!=null)
                {
                    mexpense = snapshot.child(month).getValue(MonthlyExpenses.class);
                    incomeInput.setText(Float.toString(mexpense.getIncome()));
                    expensesInput.setText(Float.toString(mexpense.getExpenses()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void clicked(View view)
    {
        switch(view.getId())
        {
            case R.id.updateButton:
                if(mexpense!=null && month!=null)
                {
                    try {
                        mexpense.setIncome(Float.parseFloat(incomeInput.getText().toString()));
                        mexpense.setExpenses(Float.parseFloat(expensesInput.getText().toString()));
                        dbref.child("calendar").child(month).setValue(mexpense);
                    } catch (NumberFormatException e)
                    {
                        Toast.makeText(getApplicationContext(),"Incorrect number introduced!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void updateMonthsFromDB(DataSnapshot snap) {
        DataSnapshot itsnap;
        Iterator<DataSnapshot> it= snap.getChildren().iterator();
        monthsAdapter.clear();
        while(it.hasNext())
        {
            itsnap=it.next();
            monthsAdapter.add(itsnap.getKey());
        }
    }
}