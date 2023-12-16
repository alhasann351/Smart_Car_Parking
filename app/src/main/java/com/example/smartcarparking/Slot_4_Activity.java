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
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Slot_4_Activity extends AppCompatActivity {

    //variable declare
    private ImageButton backBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseFirestore firebaseFirestore;
    private TextInputEditText inputName, inputCarNumber, inputRfidNumber;
    private TextInputEditText inputSlotNumber;
    String userId;
    private SeekBar seekbar;
    private TextView minutes, payTaka;
    private CardView pay;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot4);

        //find id
        backBtn = findViewById(R.id.backBtn);
        inputName = findViewById(R.id.inputName);
        inputCarNumber = findViewById(R.id.inputCarNumber);
        inputRfidNumber = findViewById(R.id.inputRfidNumber);
        inputSlotNumber = findViewById(R.id.inputSlotNumber);
        seekbar = findViewById(R.id.seekbar);
        minutes = findViewById(R.id.minutes);
        pay = findViewById(R.id.pay);
        payTaka = findViewById(R.id.payTaka);

        inputSlotNumber.setText("Booked Slot 4");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        //user data show
        userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("User").document(userId);
        progressDialog.setTitle("Please wait information loading...");
        progressDialog.show();
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                inputName.setText(value.getString("name"));
                inputCarNumber.setText(value.getString("carNumber"));
                inputRfidNumber.setText(value.getString("rfidNumber"));
                progressDialog.cancel();
            }
        });

        //back to login page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Slot_4_Activity.this, Booking_Activity.class));
                finish();
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(Slot_4_Activity.this, Booking_Activity.class));
                finish();
            }
        };
        // Add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);

        pay.setVisibility(View.GONE);

        //seekbar
        minutes.setText("Your selected time : "+seekbar.getProgress()+" minutes");
        payTaka.setText(String.valueOf(count));
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minutes.setText("Your selected time : "+progress * 10+" minutes");

                pay.setVisibility(View.VISIBLE);

                if(progress == 0){
                    pay.setVisibility(View.GONE);
                    count=0;
                    payTaka.setText(String.valueOf(count));
                }
                if(progress == 1){
                    count=20;
                    payTaka.setText(String.valueOf(count));
                }
                if (progress == 2){
                    count=30;
                    payTaka.setText(String.valueOf(count));
                }

                if(progress == 3){
                    count=40;
                    payTaka.setText(String.valueOf(count));
                }

                if (progress == 4){
                    count=50;
                    payTaka.setText(String.valueOf(count));
                }

                if(progress == 5){
                    count=60;
                    payTaka.setText(String.valueOf(count));
                }
                if (progress == 6){
                    count=70;
                    payTaka.setText(String.valueOf(count));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //pay and booked
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> bookingData = new HashMap<>();
                bookingData.put("name", inputName.getText().toString());
                bookingData.put("carNumber", inputCarNumber.getText().toString());
                bookingData.put("rfidNumber", inputRfidNumber.getText().toString());
                bookingData.put("slot", inputSlotNumber.getText().toString());
                bookingData.put("bookedTime", minutes.getText().toString());
                bookingData.put("payAmount", payTaka.getText().toString());
                bookingData.put("type", "booked");

                progressDialog.setTitle("Please wait...");
                progressDialog.show();

                DocumentReference documentReference = firebaseFirestore.collection("User").document(userId);
                documentReference.collection("Booking_Slot").add(bookingData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.cancel();
                        Toast.makeText(Slot_4_Activity.this, "Booked Successful...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Slot_4_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });

                /*firebaseFirestore.collection("Booking_Slot")
                        .add(bookingData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });*/

            }
        });
    }
}