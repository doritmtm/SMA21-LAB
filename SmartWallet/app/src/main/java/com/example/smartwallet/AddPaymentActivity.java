package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartwallet.models.Payment;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPaymentActivity extends AppCompatActivity {
    private EditText nameInput,costInput;
    private Spinner typeSpinner;
    private DatabaseReference dbref;
    private ArrayAdapter<String> typesAdapter;
    private int selectedPosTypeSpinner;
    private Button deleteButton;
    private Payment payment;
    private TextView addPaymentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_payment);
        Log.d("MYAPPP","NR networks IN ADD:"+Integer.toString(AppState.instance().getNrNetworksAvailable()));
        nameInput=findViewById(R.id.nameInput);
        costInput=findViewById(R.id.costInput);
        typeSpinner=findViewById(R.id.typeSpinner);
        typesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        typesAdapter.add("food");
        typesAdapter.add("electronics");
        typesAdapter.add("other");
        AppState.instance().setTypesAdapter(typesAdapter);
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typesAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosTypeSpinner=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbref=AppState.instance().getDBref();
        deleteButton=findViewById(R.id.deleteButton);
        addPaymentText=findViewById(R.id.addPaymentText);
        payment=AppState.instance().getPayment();
        if(payment!=null)
        {
            addPaymentText.setText(payment.getDate());
            nameInput.setText(payment.getName());
            costInput.setText(Double.toString(payment.getCost()));
            typeSpinner.setSelection(typesAdapter.getPosition(payment.getType()));
            deleteButton.setEnabled(true);
        }
    }

    public void clicked(View view)
    {
        Payment pay=new Payment();
        switch(view.getId())
        {
            case R.id.saveButton:

                pay.setName(nameInput.getText().toString());
                try {
                    pay.setCost(Double.parseDouble(costInput.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid cost introduced!", Toast.LENGTH_SHORT);
                }
                pay.setType(typesAdapter.getItem(selectedPosTypeSpinner));
                if(payment==null)
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String currentDate = dateFormat.format(date);
                    pay.setDate(currentDate);
                }
                else
                {
                    pay.setDate(payment.getDate());
                }
                dbref.child("wallet").child(pay.getDate()).setValue(pay);
                AppState.instance().updateBackup(pay);
                break;
            case R.id.deleteButton:
                dbref.child("wallet").child(payment.getDate()).removeValue();
                AppState.instance().deleteFromBackup(payment);
                break;
        }
        finish();
    }
}