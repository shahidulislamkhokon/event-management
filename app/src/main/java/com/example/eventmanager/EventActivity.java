package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EventActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TextView textView;
    private Button button, goEventButton,myEventButton,allEventButton;
    private FirebaseAuth mAuth;
    static FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

            finish();
        }

        firebaseUser = mAuth.getCurrentUser();

        datePicker=findViewById(R.id.datePicker);
        //textView=findViewById(R.id.textViewId);
        button=findViewById(R.id.ButtonId);
        goEventButton=findViewById(R.id.goEventButton);
        myEventButton=findViewById(R.id.MyEventId);
        allEventButton=findViewById(R.id.AllEvent);

        goEventButton.setText(currentDate());
        myEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MyEvent.class);
                startActivity(intent);
            }
        });

        allEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AllEvent.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEventButton.setText(currentDate());
            }
        });


        goEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EventSelection.class);
                intent.putExtra("currentDate",currentDate());
                startActivity(intent);
            }
        });
    }

    String currentDate(){

        StringBuilder stringBuilder=new StringBuilder();

        stringBuilder.append("Current Date: ");
        stringBuilder.append(datePicker.getDayOfMonth()+"/");
        stringBuilder.append((datePicker.getMonth()+1)+"/");
        stringBuilder.append(datePicker.getYear());

        return stringBuilder.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.signOutId)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AutoCompleteTextView textView = (AutoCompleteTextView)  findViewById(R.id.autocomplete_country);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, COUNTRIES);
        textView.setAdapter(adapter);
    }
}
