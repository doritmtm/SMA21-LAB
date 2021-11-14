package com.example.smartwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartwallet.models.Payment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPaymentActivity extends AppCompatActivity {
    private EditText nameInput,costInput,typeInput;
    private DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        nameInput=findViewById(R.id.nameInput);
        costInput=findViewById(R.id.costInput);
        typeInput=findViewById(R.id.typeInput);
        dbref= FirebaseDatabase.getInstance("https://smart-wallet-30b48-default-rtdb.europe-west1.firebasedatabase.app").getReference();
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
                } catch(NumberFormatException e)
                {
                    Toast.makeText(this,"Invalid cost introduced!",Toast.LENGTH_SHORT);
                }
                pay.setType(typeInput.getText().toString());
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=new Date();
                String currentDate=dateFormat.format(date);
                pay.setDate(currentDate);
                dbref.child("wallet").child(currentDate).setValue(pay);
                break;
        }
    }
}