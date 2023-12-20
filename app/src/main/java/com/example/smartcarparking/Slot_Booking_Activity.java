package com.example.smartcarparking;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.Locale;
import java.util.Map;

public class Slot_Booking_Activity extends AppCompatActivity {

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
    CountDownTimer countDownTimer;
    boolean mTimerRunning;
    //long mTimeLeftInMillis = minutes.getText().toString();
    private static final long sMinute = 6000;
    private SharedPreferences sharedPreferences;
    private static final String TIMER_KEY = "timer_key";
    private static long timeLeftInMillis;
    String id1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);

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

        //inputSlotNumber.setText("Booked Slot 1");
        //long min = sMinute * 60000;

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        id1 = intent.getStringExtra("id");
        String sName = intent.getStringExtra("slotName");
        String sStatus = intent.getStringExtra("status");
        inputSlotNumber.setText(sName);
        //inputAvailableUnavailable.setText(sStatus);

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
                startActivity(new Intent(Slot_Booking_Activity.this, Booking_Activity.class));
            }
        });

        // Set up the OnBackPressedCallback
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(Slot_Booking_Activity.this, Booking_Activity.class));
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
                minutes.setText(String.valueOf(progress * 10));

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
                        Toast.makeText(Slot_Booking_Activity.this, "Booked Successful...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Slot_Booking_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });

                //available unavailable
                String id = "omNtpcBTTKVhOMeKzjoSKUwX4o33";
                //String ava = "Available";
                String unav = "Unavailable";
                Map<String, Object> edit = new HashMap<>();
                //edit.put("slotName", ava);
                edit.put("status", unav);

                firebaseFirestore.collection("ParkingSlot").document(id).collection("Slots").document(id1)
                        .update(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(Slot_Booking_Activity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(Slot_Booking_Activity.this, "Update error", Toast.LENGTH_SHORT).show();
                            }
                        });

                if(mTimerRunning){
                    Toast.makeText(Slot_Booking_Activity.this, "Time", Toast.LENGTH_SHORT).show();
                }else{
                    startTimer();


                }


            }
        });
    }

    private void startTimer() {
        long min = Long.parseLong(minutes.getText().toString());
        long m = min*sMinute;
        countDownTimer = new CountDownTimer(m, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = (millisUntilFinished / 1000) / 3600;
                long minu = ((millisUntilFinished / 1000) % 3600) / 60;
                long second = (millisUntilFinished / 1000) % 60;
                String timeFormate = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minu, second);
                Booking_Activity.timeLeft.setText(timeFormate);
                Booking_Activity.le.setVisibility(View.GONE);
                Booking_Activity.re.setVisibility(View.VISIBLE);


                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                long startTime = System.currentTimeMillis();
                editor.putLong("countdown_start_time", startTime);
                editor.apply(); // Apply changes to preferences
                long s = preferences.getLong("countdown_start_time", 0);
                long currentTime = System.currentTimeMillis();
                long timeRemaining = s + (m - currentTime);

            }

            @Override
            public void onFinish() {
                Booking_Activity.timeLeft.setText("Slot available now");
                Booking_Activity.le.setVisibility(View.VISIBLE);
                Booking_Activity.re.setVisibility(View.GONE);

                //available unavailable
                String id = "omNtpcBTTKVhOMeKzjoSKUwX4o33";
                String ava = "Available";
                //String unav = "Unavailable";
                Map<String, Object> edit = new HashMap<>();
                edit.put("status", ava);
                //edit.put("status", unav);

                firebaseFirestore.collection("ParkingSlot").document(id).collection("Slots").document(id1)
                        .update(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //progressDialog.cancel();
                                //Toast.makeText(Slot_Booking_Activity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Fail", e.getMessage());
                                //progressDialog.cancel();
                                //Toast.makeText(Slot_Booking_Activity.this, "Update error", Toast.LENGTH_SHORT).show();
                            }
                        });
                startActivity(new Intent(Slot_Booking_Activity.this, Booking_Activity.class));
            }
        }.start();
    }
}