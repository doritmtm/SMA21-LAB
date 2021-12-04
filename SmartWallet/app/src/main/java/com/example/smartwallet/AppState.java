package com.example.smartwallet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.smartwallet.models.Payment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppState {
    private static AppState singleton;
    private Payment payment;
    private DatabaseReference dbref;
    private ArrayAdapter<String> typesAdapter;
    private Context context;
    private int nrNetworksAvailable=0;
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
    public List<Payment> loadBackup()
    {
        List<Payment> paymentDataList=new ArrayList<>();
        File paymentsDir=new File(context.getFilesDir().toURI().resolve("payments"));
        if(!paymentsDir.exists())
        {
            if(!paymentsDir.mkdir())
            {
                try {
                    throw new IOException();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for(File f:paymentsDir.listFiles())
        {
            try {
                FileInputStream fis=new FileInputStream(f);
                ObjectInputStream ois=new ObjectInputStream(fis);
                Payment payment=(Payment) ois.readObject();
                paymentDataList.add(payment);
                ois.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return paymentDataList;
    }
    public void updateBackup(Payment payment)
    {
        File paymentsDir=new File(context.getFilesDir().toURI().resolve("payments"));
        if(!paymentsDir.exists())
        {
            if(!paymentsDir.mkdir())
            {
                try {
                    throw new IOException();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Date paymentDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payment.getDate());
            String paymentFileDate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(paymentDate);
            File paymentFile=new File(paymentsDir.toURI().resolve(paymentFileDate));
            if(!paymentFile.exists())
            {
                paymentFile.createNewFile();
            }
            FileOutputStream fos=new FileOutputStream(paymentFile);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(payment);
            oos.close();
            fos.close();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteFromBackup(Payment payment)
    {
        File paymentsDir=new File(context.getFilesDir().toURI().resolve("payments"));
        if(!paymentsDir.exists())
        {
            if(!paymentsDir.mkdir())
            {
                try {
                    throw new IOException();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Date paymentDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(payment.getDate());
            String paymentFileDate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(paymentDate);
            File paymentFile=new File(paymentsDir.toURI().resolve(paymentFileDate));
            if(paymentFile.exists())
            {
                if(!paymentFile.delete())
                {
                    throw new IOException();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncBackupWithFirebase()
    {
        List<Payment> paymentDataList=loadBackup();
        Map<String,Object> paymentDataMap=new HashMap<>();
        for(Payment p:paymentDataList)
        {
            paymentDataMap.put(p.getDate(),p);
        }
        dbref.child("wallet").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Iterator<DataSnapshot> it=task.getResult().getChildren().iterator();
                DataSnapshot datasnap;
                Payment payment;
                while(it.hasNext())
                {
                    datasnap=it.next();
                    payment=datasnap.getValue(Payment.class);
                    payment.setDate(datasnap.getKey());
                    if(!paymentDataMap.containsKey(payment.getDate()))
                    {
                        dbref.child("wallet").child(payment.getDate()).removeValue();
                    }
                }
                dbref.child("wallet").updateChildren(paymentDataMap);
            }
        });

    }

    public void performInitialSync()
    {
        List<Payment> paymentDataList=loadBackup();
        Map<String,Object> paymentDataMap=new HashMap<>();
        for(Payment p:paymentDataList)
        {
            paymentDataMap.put(p.getDate(),p);
        }
        dbref.child("wallet").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Iterator<DataSnapshot> it=task.getResult().getChildren().iterator();
                DataSnapshot datasnap;
                Payment payment;
                while(it.hasNext())
                {
                    datasnap=it.next();
                    payment=datasnap.getValue(Payment.class);
                    payment.setDate(datasnap.getKey());
                    if(!paymentDataMap.containsKey(payment.getDate()))
                    {
                        dbref.child("wallet").child(payment.getDate()).removeValue();
                    }
                }
                dbref.child("wallet").updateChildren(paymentDataMap);
            }
        });
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void countUpNetwork()
    {
        nrNetworksAvailable++;
    }
    public void countDownNetwork()
    {
        nrNetworksAvailable--;
    }

    public int getNrNetworksAvailable() {
        return nrNetworksAvailable;
    }
}
