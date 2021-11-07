package com.example.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartwallet.models.MonthlyExpenses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SmartWalletActivity extends AppCompatActivity {
    private TextView monthText;
    private Button updateBut,searchBut;
    private EditText monthInput,incomeInput,expensesInput;
    private DatabaseReference dbref;
    private MonthlyExpenses mexpense=null;
    private String month=null;
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
        dbref= FirebaseDatabase.getInstance("https://smart-wallet-30b48-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    }

    public void clicked(View view)
    {
        switch(view.getId())
        {
            case R.id.searchButton:
                month=monthInput.getText().toString();
                dbref.child("calendar").child(month).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        mexpense =task.getResult().getValue(MonthlyExpenses.class);
                        if(mexpense!=null)
                        {
                            monthText.setText(month);
                            incomeInput.setText(Float.toString(mexpense.getIncome()));
                            expensesInput.setText(Float.toString(mexpense.getExpenses()));
                        }
                        else
                        {
                            monthText.setText("Error finding "+month+" in database");
                        }
                    }
                });
                break;
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
}