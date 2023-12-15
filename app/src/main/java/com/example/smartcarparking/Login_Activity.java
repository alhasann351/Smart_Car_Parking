package com.example.smartcarparking;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Login_Activity extends AppCompatActivity {

    //variable declare
    private TextView signUpPage, forgetPassword;
    private TextInputEditText loginEmail, loginPassword;
    private CardView login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;
    String userId;
    String name;
    String phone;
    String email;
    String address;
    String carNumber;
    String rfidNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //find id
        signUpPage = findViewById(R.id.signUpPage);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        forgetPassword = findViewById(R.id.forgetPassword);
        login = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()){
            startActivity(new Intent(Login_Activity.this, Home_Activity.class));
            finish();
        }

        //login here
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString();

                if (email.matches("") || password.matches("")){
                    Toast.makeText(Login_Activity.this, "Enter all field details", Toast.LENGTH_SHORT).show();
                }else {

                    progressDialog.setTitle("Please wait...");
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                                    startActivity(intent);
                                    sessionManager.setLogin(true);
                                    finish();

                                    progressDialog.cancel();
                                    Toast.makeText(Login_Activity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                /*loginEmail.setText("");
                loginPassword.setText("");*/
            }
        });

        //forget password page open
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Forgetpassword_Activity.class));
                finish();
            }
        });

        //signup page open
        signUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Signup_Activity.class));
                finish();
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Dialog dialog = new Dialog(Login_Activity.this);
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