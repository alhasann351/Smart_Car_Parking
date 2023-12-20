package com.example.smartcarparking;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Booking_Activity extends AppCompatActivity {

    //variable declare
    private ImageButton backBtn;
    private CardView slot_1, slot_2, slot_3, slot_4;
    public static TextView timeLeft;
    String userId;
    private RecyclerView recyView;

    ArrayList<SlotModel> slotModelArrayList;
    SlotAdapter slotAdapter;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    public static LinearLayout le;
    public static RelativeLayout re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //find id
        backBtn = findViewById(R.id.backBtn);
        timeLeft = findViewById(R.id.timeLeft);
        le = findViewById(R.id.le);
        re = findViewById(R.id.re);
        /*slot_1 = findViewById(R.id.slot_1);
        slot_2 = findViewById(R.id.slot_2);
        slot_3 = findViewById(R.id.slot_3);
        slot_4 = findViewById(R.id.slot_4);*/
        recyView = findViewById(R.id.recyView);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        slotModelArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(Booking_Activity.this);
        //gridLayoutManager = new GridLayoutManager(AddslotActivity.this, 2, GridLayoutManager.VERTICAL, false);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        //gridLayoutManager.setReverseLayout(true);
        //gridLayoutManager.setStackFromEnd(false);
        recyView.setLayoutManager(linearLayoutManager);
        //recyclerView.setLayoutManager(gridLayoutManager);
        recyView.setHasFixedSize(true);
        slotLoad();

        //back to login page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Booking_Activity.this, Home_Activity.class));
                finish();
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(Booking_Activity.this, Home_Activity.class));
                finish();
            }
        };
        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);


        //slot booking page open
        /*slot_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Booking_Activity.this, Slot_Booking_Activity.class));
                finish();

            }
        });

        slot_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Booking_Activity.this, Slot_2_Activity.class));
                finish();
            }
        });

        slot_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Booking_Activity.this, Slot_3_Activity.class));
                finish();
            }
        });

        slot_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Booking_Activity.this, Slot_4_Activity.class));
                finish();
            }
        });*/
    }

    private void slotLoad() {
        progressDialog.setTitle("Please wait information loading...");
        progressDialog.show();
        String id = "omNtpcBTTKVhOMeKzjoSKUwX4o33";

            firebaseFirestore.collection("ParkingSlot").document(id).collection("Slots")
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

                                slotAdapter = new SlotAdapter(Booking_Activity.this, slotModelArrayList);
                                recyView.setAdapter(slotAdapter);

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
                                Toast.makeText(Booking_Activity.this, "Error...", Toast.LENGTH_SHORT).show();
                                Log.w("TAG", "Error getting documents", task.getException());
                            }
                        }
                    });
        }


    }

