package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventmanager.models.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EventSelection extends AppCompatActivity {

    private Button save;
    private EditText editTextEvent, editTextLocation, editTextStart, editTextDescription;
    private Spinner spinnerReminder, spinnerStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_selection);

        String date = getIntent().getExtras().getString("currentDate");

        save=findViewById(R.id.saveButton);
        editTextEvent=findViewById(R.id.EventNameId);
        editTextLocation=findViewById(R.id.locationId);
        editTextStart=findViewById(R.id.StartTimeId);
        spinnerStart=findViewById(R.id.spinner2);

        editTextStart.setText(date);
        //editTextEnd=findViewById(R.id.endTimeId);
        editTextDescription=findViewById(R.id.descriptionId);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedEvent();
            }
        });
    }


    private void saveSelectedEvent(){


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference("users/"+ EventActivity.firebaseUser.getUid()+"/events").push().getRef();



            ref.setValue(new Event(editTextEvent.getText().toString(),editTextLocation.getText().toString(),editTextStart.getText().toString(),String.valueOf(spinnerStart.getSelectedItem()) ,editTextDescription.getText().toString()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getApplicationContext(), "Event is added.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

    }



}
