package com.example.smartwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SmartWalletActivity extends AppCompatActivity {
    private TextView monthText;
    private Button updateBut,searchBut;
    private EditText monthInput,incomeInput,expensesInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthText=findViewById(R.id.monthText);
        monthInput=findViewById(R.id.monthInput);
        updateBut=findViewById(R.id.updateButton);
        searchBut=findViewById(R.id.searchButton);
        incomeInput=findViewById(R.id.incomeInput);
        expensesInput=findViewById(R.id.expensesInput);
    }
}