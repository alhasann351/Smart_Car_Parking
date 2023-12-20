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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddslotActivity extends AppCompatActivity {

    //variable declare
    private CardView add, update, delete;
    private ImageButton backBtn;
    private RecyclerView recyclerView;
    private TextInputEditText inputAvailableUnavailable, inputSlotName;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<SlotModel> slotModelArrayList;
    SlotAdapter slotAdapter;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addslot);

        //find id
        add = findViewById(R.id.add);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        inputSlotName = findViewById(R.id.inputSlotName);
        inputAvailableUnavailable = findViewById(R.id.inputAvailableUnavailable);
        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.recyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        slotModelArrayList = new ArrayList<>();

        //linearLayoutManager = new LinearLayoutManager(AddslotActivity.this);
        gridLayoutManager = new GridLayoutManager(AddslotActivity.this, 2, GridLayoutManager.VERTICAL, false);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(false);
        //recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        slotLoad();

        //update delete
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String sName = intent.getStringExtra("slotName");
        String sStatus = intent.getStringExtra("status");
        inputSlotName.setText(sName);
        inputAvailableUnavailable.setText(sStatus);

        //back to login page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddslotActivity.this, DashboardActivity.class));
                finish();
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(AddslotActivity.this, DashboardActivity.class));
                finish();
            }
        };
        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

        //add slot firebase database
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slotName = inputSlotName.getText().toString();
                String slotAvailableUnavailable = inputAvailableUnavailable.getText().toString();

                if (slotName.matches("") || slotAvailableUnavailable.matches("")){
                    Toast.makeText(AddslotActivity.this, "Enter all field details", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setTitle("Please wait...");
                    progressDialog.show();

                    String id = UUID.randomUUID().toString();

                    Map<String, Object> slodAdd = new HashMap<>();
                    slodAdd.put("id", id);
                    slodAdd.put("slotName", slotName);
                    slodAdd.put("status", slotAvailableUnavailable);

                    firebaseFirestore.collection("ParkingSlot").document(firebaseAuth.getUid()).collection("Slots").document(id)
                            .set(slodAdd).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.cancel();
                                    Toast.makeText(AddslotActivity.this, "Added successful...", Toast.LENGTH_SHORT).show();
                                    inputSlotName.setText("");
                                    inputAvailableUnavailable.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddslotActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            });
                }

                Intent intent = new Intent(AddslotActivity.this, AddslotActivity.class);
                startActivity(intent);
            }
        });

        //update data
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uSlot = inputSlotName.getText().toString();
                String uStatus = inputAvailableUnavailable.getText().toString();

                if (uSlot.matches("") || uStatus.matches("")){
                    Toast.makeText(AddslotActivity.this, "Enter all field details", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setTitle("Please wait...");
                    progressDialog.show();

                    Map<String, Object> edit = new HashMap<>();
                    edit.put("slotName", uSlot);
                    edit.put("status", uStatus);

                    firebaseFirestore.collection("ParkingSlot").document(firebaseAuth.getUid()).collection("Slots").document(id)
                            .update(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.cancel();
                                    Toast.makeText(AddslotActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(AddslotActivity.this, "Update error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                Intent intent = new Intent(AddslotActivity.this, AddslotActivity.class);
                startActivity(intent);
            }
        });

        //delete data
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dSlot = inputSlotName.getText().toString();
                String dStatus = inputAvailableUnavailable.getText().toString();

                if (dSlot.matches("") || dStatus.matches("")){
                    Toast.makeText(AddslotActivity.this, "Enter all field details", Toast.LENGTH_SHORT).show();
                }else {
                progressDialog.setTitle("Please wait...");
                progressDialog.show();

                firebaseFirestore.collection("ParkingSlot").document(firebaseAuth.getUid()).collection("Slots").document(id)
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(AddslotActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(AddslotActivity.this, "Delete error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
                Intent intent = new Intent(AddslotActivity.this, AddslotActivity.class);
                startActivity(intent);
            }
        });
    }

    private void slotLoad() {
        progressDialog.setTitle("Please wait information loading...");
        progressDialog.show();

        firebaseFirestore.collection("ParkingSlot").document(firebaseAuth.getUid()).collection("Slots")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult()){
                                SlotModel slotModel = new SlotModel(
                                        documentSnapshot.getString("id"),
                                        documentSnapshot.getString("slotName"),
                                        documentSnapshot.getString("status")
                                );
                                slotModelArrayList.add(slotModel);
                                progressDialog.cancel();
                            }
                            slotAdapter = new SlotAdapter(AddslotActivity.this, slotModelArrayList);
                            recyclerView.setAdapter(slotAdapter);

                            /*//item update delete
                            slotAdapter.setOnItemClickListener((position, v) -> {
                                firebaseFirestore.collection("ParkingSlot").document(firebaseAuth.getUid()).collection("Slots")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (DocumentSnapshot ds : task.getResult()){
                                                        inputSlotName.setText(ds.getString("slotName"));
                                                        inputAvailableUnavailable.setText(ds.getString("status"));
                                                    }
                                                }
                                            }
                                        });
                            });*/

                        }else {
                            progressDialog.cancel();
                            Toast.makeText(AddslotActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error getting documents", task.getException());
                        }
                    }
                });
    }
}