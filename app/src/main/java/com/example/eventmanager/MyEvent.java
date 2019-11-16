package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.eventmanager.adapter.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.eventmanager.models.Event;
import com.google.firebase.database.ValueEventListener;

public class MyEvent extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;

    FirebaseDatabase mFirebaseDatabase;
    Query mRef;

    List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("users");

        //Get List

        events = new ArrayList<>();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                events.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Map<String, Object> objectMap = (HashMap<String, Object>)
                            postSnapshot.getValue();
                    if (objectMap.containsKey("events")) {
                        for (DataSnapshot eventSnap : postSnapshot.child("events").getChildren()) {
                            Event event = eventSnap.getValue(Event.class);
                            event.setKey(eventSnap.getKey());
                            events.add(event);
                        }
                    }
                }
                adapter = new RecyclerViewAdapter(getApplicationContext(), events);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });

    }

}
