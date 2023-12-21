package com.example.smartcarparking;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Addmoney_Activity extends AppCompatActivity {

    //variable declare
    private CardView amountAdd;
    private TextInputEditText amount;
    private ImageButton backBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    String userId;
    private TextView totalAmount;
    private int toAmo = 0;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);

        //find id
        backBtn = findViewById(R.id.backBtn);
        amountAdd = findViewById(R.id.amountAdd);
        amount = findViewById(R.id.amount);
        totalAmount = findViewById(R.id.totalAmount);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        //user data show
        userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("User").document("id").collection("UserDetails").document(userId);
        CollectionReference subcollectionRef = documentReference.collection("Money");
        CollectionReference collectionReference = documentReference.collection("Booking_Slot");
        progressDialog.setTitle("Please wait information loading...");
        //progressDialog.show();
        subcollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        // Access data from each document in the subcollection
                        //totalAmount.setText(document.getString("moneyAdd"));

                        int amo = Integer.parseInt(document.getString("moneyAdd"));
                        if (document.getString("type").equals("Total")){
                            toAmo = toAmo + amo;

                        }else {
                            totalAmount.setText("Total Amount : 0");
                            //progressDialog.cancel();
                        }

                    }
                    totalAmount.setText(String.valueOf(toAmo));


                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int i = Integer.parseInt(totalAmount.getText().toString());
                                for (DocumentSnapshot document : task.getResult()) {
                                    // Access data from each document in the subcollection
                                    //totalAmount.setText(document.getString("moneyAdd"));

                                    int a = Integer.parseInt(document.getString("payAmount"));
                                    if (document.getString("type").equals("booked")){
                                        i = i - a;

                                    }else {
                                        totalAmount.setText("Total Amount : 0");
                                        //progressDialog.cancel();
                                    }

                                }
                                totalAmount.setText(String.valueOf(i));
                            }
                        }
                    });




                } else {
                    progressDialog.cancel();
                    Toast.makeText(Addmoney_Activity.this, "Not Successful...", Toast.LENGTH_SHORT).show();
                    Log.w("TAG", "Error getting documents", task.getException());
                }

            }
        });

        //back to login page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Addmoney_Activity.this, Home_Activity.class));
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(Addmoney_Activity.this, Home_Activity.class));
            }
        };
        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

        //add money
        amountAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addAmount = amount.getText().toString();

                if (addAmount.matches("")){
                    Toast.makeText(Addmoney_Activity.this, "Enter all field details", Toast.LENGTH_SHORT).show();
                }else {
                Map<String, String> bookingData = new HashMap<>();
                bookingData.put("moneyAdd", addAmount);
                bookingData.put("type", "Total");

                progressDialog.setTitle("Please wait...");
                progressDialog.show();

                DocumentReference documentReference = firebaseFirestore.collection("User").document("id").collection("UserDetails").document(userId);
                documentReference.collection("Money").add(bookingData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.cancel();
                        Toast.makeText(Addmoney_Activity.this, "Money Added Successful...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Addmoney_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
            }
                amount.setText("");

                Intent intent = new Intent(Addmoney_Activity.this, Addmoney_Activity.class);
                startActivity(intent);
                //finish();

            }

        });
    }
}