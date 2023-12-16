package com.example.smartcarparking;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class History_Activity extends AppCompatActivity {

    //variable declare
    private ImageButton backBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    ArrayList<HistoryModel> historyModelArrayList;
    HistoryAdapter historyAdapter;
    LinearLayoutManager linearLayoutManager;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //find id
        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.recyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        historyModelArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(History_Activity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        historyLoad();


        //back to login page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(History_Activity.this, Home_Activity.class));
                finish();
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(History_Activity.this, Home_Activity.class));
                finish();
            }
        };
        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    //history load
    public void historyLoad(){
        //user data show
        userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("User").document(userId);
        CollectionReference subcollectionRef = documentReference.collection("Booking_Slot");
        progressDialog.setTitle("Please wait information loading...");
        progressDialog.show();
        subcollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        // Access data from each document in the subcollection
                        HistoryModel historyModel = new HistoryModel(
                                document.getString("name"),
                                document.getString("carNumber"),
                                document.getString("rfidNumber"),
                                document.getString("bookedTime"),
                                document.getString("payAmount"),
                                document.getString("slot")
                                //document.getString("type")
                        );
                        historyModelArrayList.add(historyModel);
                        progressDialog.cancel();
                    }

                    historyAdapter = new HistoryAdapter(History_Activity.this, historyModelArrayList);
                    recyclerView.setAdapter(historyAdapter);

                } else {
                    progressDialog.cancel();
                    Toast.makeText(History_Activity.this, "Error...", Toast.LENGTH_SHORT).show();
                    Log.w("TAG", "Error getting documents", task.getException());
                }

            }
        });
    }
}