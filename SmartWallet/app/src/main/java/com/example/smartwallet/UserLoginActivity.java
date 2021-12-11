package com.example.smartwallet;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class UserLoginActivity extends AppCompatActivity {
    private int loginResult=RESULT_CANCELED;
    private ActivityResultLauncher<Intent> authUILauncher=registerForActivityResult(new FirebaseAuthUIActivityResultContract(), new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
        @Override
        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
            if(result.getResultCode()==RESULT_OK)
            {
                loginResult=RESULT_OK;
                Intent intent=new Intent(getApplicationContext(),ListPaymentsActivity.class);
                intent.putExtra("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }
            else
            {
                loginResult=RESULT_CANCELED;
                Intent authUIintent= AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build()
                        )).build();
                authUILauncher.launch(authUIintent);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Intent authUIintent= AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()
                )).build();
        authUILauncher.launch(authUIintent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}