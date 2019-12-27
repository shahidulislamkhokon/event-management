package com.example.eventmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.eventmanager.adapter.RecyclerViewAdapter;
import com.example.eventmanager.models.Event;
import com.example.eventmanager.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class  AllEvent extends AppCompatActivity {

    SearchView searchView;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;

    FirebaseDatabase mFirebaseDatabase;
    Query mRef;

    List<Event> events = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_event);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        searchView = findViewById(R.id.searchViewId);

//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                List<Event> filteredEvent = new ArrayList<>();
//                for (Event e : events) {
//                    if(e.getEventName().contains(searchView.getQuery())){
//                        filteredEvent.add(e);
//                    }
//                }
//                adapter = new RecyclerViewAdapter(getApplicationContext(), filteredEvent);
//                mRecyclerView.setAdapter(adapter);
//
//            }
//        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Event> filteredEvent = new ArrayList<>();
                if(query==null || query.equals(""))
                {
                    filteredEvent.addAll(events);
                }
                else{
                    for (Event e : events) {
                        if(e.getEventName().toLowerCase().contains(query.toLowerCase())){
                            filteredEvent.add(e);
                        }
                    }
                }
                adapter = new RecyclerViewAdapter(getApplicationContext(), filteredEvent,AllEvent.this);
                mRecyclerView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==null || newText.equals(""))
                {
                    List<Event> filteredEvent = new ArrayList<>();
                    filteredEvent.addAll(events);
                    adapter = new RecyclerViewAdapter(getApplicationContext(), filteredEvent,AllEvent.this);
                    mRecyclerView.setAdapter(adapter);
                }
                return false;
            }


        });





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
                    //User user = postSnapshot.getValue(User.class);
                    if (objectMap.containsKey("events")) {
                        for (DataSnapshot eventSnap : postSnapshot.child("events").getChildren()) {
                            Event event = eventSnap.getValue(Event.class);
                            event.setKey(eventSnap.getKey());
                            event.setEventAddedBy( postSnapshot.getKey());
                            List<String> likes = new ArrayList<>();
                            for (DataSnapshot likeId : eventSnap.child("likes").getChildren()) {
                                String likedUserId = likeId.getValue(String.class);
                                likes.add(likedUserId);
                            }
                            event.setLikes(likes);

                            List<String> comments = new ArrayList<>();
                            for (DataSnapshot commentId : eventSnap.child("comments").getChildren()) {
                                String commentsUserId = commentId.getValue(String.class);
                                comments.add(commentsUserId);
                            }
                            event.setComments(comments);


                            events.add(event);
                        }
                    }
                }
                adapter = new RecyclerViewAdapter(getApplicationContext(), events,AllEvent.this);
                mRecyclerView.setAdapter(adapter);
            }
		


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });

    }

    private void SearchEvent(){

    }


}

