package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Edit extends AppCompatActivity {
    Button editSaveButton, editGoogleMapButton,editSetImageButton;
    EditText editEventName, editLocation, editDescription, editEventDate;
    Spinner editSpinner;
    ImageView editSetImage;
    Event event;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        MapUtility.apiKey = getResources().getString(R.string.google_api_key);

        event =(Event) getIntent().getSerializableExtra("event");

       editSaveButton=findViewById(R.id.EditsaveButton);
       editGoogleMapButton=findViewById(R.id.EditGoogleMapId);
       editSetImageButton=findViewById(R.id.EditsetImageButtonId);
       editSetImage=findViewById(R.id.EditimageViewId);
       editEventName=findViewById(R.id.EditEventNameId);
       editLocation=findViewById(R.id.EditlocationId);
       editDescription=findViewById(R.id.EditdescriptionId);
       editEventDate=findViewById(R.id.EditEventDateId);
       editSpinner=findViewById(R.id.Editspinner);


       editEventName.setText(event.getEventName());
       editLocation.setText(event.getLocation());
       editDescription.setText(event.getDescription());
       editEventDate.setText(event.getStartingDate());
       editSpinner.setSelection(event.getTimeSpinnerposition());


        try{
            Picasso.get().load(event.getSaveEventImage()).into(editSetImage);
        }catch (Exception ex)
        {
            // holder.eventImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.evetn_manager_icon,null));
            Log.d("Error","Invalid Image");
        }

        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditEvent();
            }
        });

        editSetImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        editGoogleMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentPlaceItems();
            }
        });


    }

    private void getCurrentPlaceItems() {
        if (isLocationAccessPermitted()) {
            showPlacePicker();
        } else {
            requestLocationAccessPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void showPlacePicker() {
        Intent i = new Intent(getApplicationContext(), LocationPickerActivity.class);
        startActivityForResult(i, 1020);

    }

    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }

    private void saveEditEvent() {


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference("users/"+ EventActivity.firebaseUser.getUid()+"/events/"+event.getKey());

                event.setEventName(editEventName.getText().toString());
                event.setLocation(editLocation.getText().toString());
                event.setDescription(editDescription.getText().toString());
                event.setStartingDate(editEventDate.getText().toString());
                event.setTimeSpinnerposition(editSpinner.getSelectedItemPosition());


            ref.setValue(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(filePath!=null)
                            {saveImage(ref);}

                            else{ Toast.makeText(getApplicationContext(), "Event is added.",
                                    Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),MyEvent.class);
                                startActivity(intent);

                            }
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
                editSetImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (requestCode == 1020) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    editLocation.setText(address);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
