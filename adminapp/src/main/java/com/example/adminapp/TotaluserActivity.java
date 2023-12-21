package com.example.adminapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TotaluserActivity extends AppCompatActivity {

    //variable declare
    private CardView add, update, delete;
    private ImageButton backBtn;
    private RecyclerView recyclerView;
    private TextInputEditText inputAvailableUnavailable, inputSlotName;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<UserModel> userModelArrayList;
    UserAdapter userAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totaluser);

        //find id
        //add = findViewById(R.id.add);
        //update = findViewById(R.id.update);
        //delete = findViewById(R.id.delete);
        //inputSlotName = findViewById(R.id.inputSlotName);
        //inputAvailableUnavailable = findViewById(R.id.inputAvailableUnavailable);
        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.recycler);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        userModelArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(TotaluserActivity.this);
        //gridLayoutManager = new GridLayoutManager(AddslotActivity.this, 2, GridLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        slotLoad();

        //back to login page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TotaluserActivity.this, DashboardActivity.class));
                finish();
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(TotaluserActivity.this, DashboardActivity.class));
                finish();
            }
        };
        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void slotLoad() {
        progressDialog.setTitle("Please wait information loading...");
        progressDialog.show();

        firebaseFirestore.collection("User").document("id").collection("UserDetails")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                UserModel userModel = new UserModel(
                                        documentSnapshot.getString("name"),
                                        documentSnapshot.getString("phone"),
                                        documentSnapshot.getString("email"),
                                        documentSnapshot.getString("address"),
                                        documentSnapshot.getString("carNumber"),
                                        documentSnapshot.getString("rfidNumber")
                                );
                                userModelArrayList.add(userModel);
                                progressDialog.cancel();
                            }
                            userAdapter = new UserAdapter(TotaluserActivity.this, userModelArrayList);
                            recyclerView.setAdapter(userAdapter);

                        }else {
                            progressDialog.cancel();
                            Toast.makeText(TotaluserActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error getting documents", task.getException());
                        }
                    }
                });
    }
}