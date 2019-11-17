package com.example.eventmanager.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.Edit;
import com.example.eventmanager.EventActivity;
import com.example.eventmanager.EventSelection;
import com.example.eventmanager.MainActivity;
import com.example.eventmanager.MyEvent;
import com.example.eventmanager.R;
import com.example.eventmanager.models.Event;
import com.example.eventmanager.viewholder.ViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context context;
    List<Event> events;

    public RecyclerViewAdapter(Context context, List<Event> TempList) {

        this.events = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.setOnClickListener(new ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                gotoViewEvent(events.get(position));
            }
        });


        return viewHolder;
    }

    private void gotoViewEvent(Event event){

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Event event = events.get(position);
        try{
            Picasso.get().load(event.getSaveEventImage()).into(holder.eventImage);
        }catch (Exception ex)
        {
           // holder.eventImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.evetn_manager_icon,null));
            Log.d("Error","Invalid Image");
        }

        holder.title.setText(event.getEventName() == null ? "" : event.getEventName());
        holder.startTime.setText(context.getResources().getStringArray(R.array.time_arrays)[event.getTimeSpinnerposition()]);
        holder.eventLocation.setText(event.getLocation() == null ? "" : event.getLocation());
        holder.eventDate.setText(event.getStartingDate() == null ? "" : event.getStartingDate().toString());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Edit.class);
                i.putExtra("event",event);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(event);
            }
        });
    }

    private void deleteEvent(Event event) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users/"+ EventActivity.firebaseUser.getUid()+"/events/"+event.getKey());
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(context, "Event is deleted.",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, MyEvent.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error!! Try Again ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {

        return events.size();
    }



}