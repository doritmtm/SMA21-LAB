package com.example.smartwallet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.smartwallet.models.Payment;
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
import java.util.List;

public class AppState {
    private static AppState singleton;
    private Payment payment;
    private DatabaseReference dbref;
    private ArrayAdapter<String> typesAdapter;
    private Context context;
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
