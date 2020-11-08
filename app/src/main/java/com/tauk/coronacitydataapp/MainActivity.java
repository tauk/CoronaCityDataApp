package com.tauk.coronacitydataapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //Add the required attributes for EditText ....
    private FirebaseAuth firebaseAuth;

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvStatus;
    boolean loginWasSuccessfull;
    boolean registerWasSuccessfull;

    //attribute for FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the EditTexts from which you have to read email and password
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvStatus = findViewById(R.id.tvStatus);

        //initilize FirebaseApp
        FirebaseApp.initializeApp(this);

        //initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void doLogin(View view) {
        //read the email and password from EditTexts .......
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        //verify the login using email/password FirebaseAuth ......
            //if login is invalid show error message in toast and in tvStatus textview
            //if login credentials are OK start intent to go to AddRemoveCoronaDataActivity
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("doLogin() ", "signInUserWithEmail:success");
                            loginWasSuccessfull = true;
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //user;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("doRegister", "createUserWithEmail:failure", task.getException());
                            tvStatus.setText("Login failed!!!" +task.getException());
                        }
                        // ...
                    }
                });

        //login was OK move to another activity
        if (loginWasSuccessfull) {
            Intent intent = new Intent(this, AddRemoveCoronaDataActivity.class);
            startActivity(intent);
        }

    }

    public void doRegister(View view) {
        //read the email and password from EditTexts .....
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        //register the with email/password by using FirebaseAuth....
            //if sucessfull show Success message in Toast and in tvStatus textview
            //if NOT successfull show Error message in Toast and in tvStatus textView
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("doLogin() ", "signInUserWithEmail:success");
                            registerWasSuccessfull = true;
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //user;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("doRegister", "createUserWithEmail:failure", task.getException());
                            tvStatus.setText("Login failed!!!" +task.getException());
                        }
                        // ...
                    }
                });

        //login was OK
        if (registerWasSuccessfull) {
            tvStatus.setText("Register OK. Login now!");
        }

    }
}
