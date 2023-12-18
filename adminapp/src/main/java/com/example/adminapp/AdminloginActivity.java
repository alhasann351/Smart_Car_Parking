package com.example.adminapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminloginActivity extends AppCompatActivity {

    //variable declare
    private TextView signUpPage, forgetPassword;
    private TextInputEditText loginEmail, loginPassword;
    private CardView login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        //find id
        //signUpPage = findViewById(R.id.signUpPage);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        forgetPassword = findViewById(R.id.forgetPassword);
        login = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()){
            startActivity(new Intent(AdminloginActivity.this, DashboardActivity.class));
            finish();
        }

        //login here
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString();

                if (email.matches("") || password.matches("")){
                    Toast.makeText(AdminloginActivity.this, "Enter all field details", Toast.LENGTH_SHORT).show();
                }else {

                    progressDialog.setTitle("Please wait...");
                    progressDialog.show();

                    if(email.matches("projectsdata100@gmail.com")){
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Intent intent = new Intent(AdminloginActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        sessionManager.setLogin(true);
                                        finish();

                                        progressDialog.cancel();
                                        Toast.makeText(AdminloginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.cancel();
                                        Toast.makeText(AdminloginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        progressDialog.cancel();
                        Toast.makeText(AdminloginActivity.this, "Email and Password are incorrect", Toast.LENGTH_SHORT).show();
                    }


                }

                /*loginEmail.setText("");
                loginPassword.setText("");*/
            }
        });

        //forget password page open
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminloginActivity.this, ForgetpasswordActivity.class));
                finish();
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Dialog dialog = new Dialog(AdminloginActivity.this);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations
                        = android.R.style.Animation_Dialog;
                dialog.setContentView(R.layout.exit_dialog);
                dialog.setCancelable(false);

                ImageButton no_btn = dialog.findViewById(R.id.no_btn);
                ImageButton yes_btn = dialog.findViewById(R.id.yes_btn);

                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                    }
                });

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAffinity();
                    }
                });

                dialog.show();
            }
        };
        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}