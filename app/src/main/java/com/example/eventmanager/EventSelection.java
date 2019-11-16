package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventmanager.models.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EventSelection extends AppCompatActivity {

    private Button save, setImageButton;
    private ImageView setImageView;
    private EditText editTextEvent, editTextLocation, editTextStart, editTextDescription;
    private Spinner spinnerReminder, spinnerStart;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_selection);

        String date = getIntent().getExtras().getString("currentDate");

        save=findViewById(R.id.saveButton);
        setImageButton=findViewById(R.id.setImageButtonId);
        editTextEvent=findViewById(R.id.EventNameId);
        editTextLocation=findViewById(R.id.locationId);
        editTextStart=findViewById(R.id.StartTimeId);
        spinnerStart=findViewById(R.id.spinner2);
        setImageView=findViewById(R.id.imageViewId);

        editTextStart.setText(date);
        //editTextEnd=findViewById(R.id.endTimeId);
        editTextDescription=findViewById(R.id.descriptionId);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedEvent();

            }
        });

        setImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }


    private void saveSelectedEvent(){


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference("users/"+ EventActivity.firebaseUser.getUid()+"/events").push().getRef();



            ref.setValue(new Event(editTextEvent.getText().toString(),editTextLocation.getText().toString(),editTextStart.getText().toString(),String.valueOf(spinnerStart.getSelectedItem()) ,editTextDescription.getText().toString(),"No"))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           if(filePath!=null)
                           {saveImage(ref);}

                           else{ Toast.makeText(getApplicationContext(), "Event is added.",
                                    Toast.LENGTH_SHORT).show();}
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 71 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                setImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void saveImage(final DatabaseReference ref) {

        if(filePath != null)
        {
            storage = FirebaseStorage.getInstance("gs://event-manager-4feb6.appspot.com");
            storageReference = storage.getReference();

            //final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
           // progressDialog.setTitle("Uploading...");
           // progressDialog.show();

            final StorageReference sRef = storageReference.child("images/"+ ref.getKey());
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ref.child("saveEventImage").setValue(uri.toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Event is added",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(getApplicationContext(),MyEvent.class);
                                                    startActivity(intent);

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
                            });

                           // progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          //  progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                          //  progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }





}
