package com.example.eventmanager.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.models.Event;
import com.example.eventmanager.viewholder.ViewHolder;
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
                gotoViewEventFragment(events.get(position));
            }
        });


        return viewHolder;
    }

    private void gotoViewEventFragment(Event event){

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Event event = events.get(position);
        try{
            Picasso.get().load(event.getSaveEventImage()).into(holder.eventImage);
        }catch (Exception ex)
        {
           // holder.eventImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.evetn_manager_icon,null));
            Log.d("Error","Invalid Image");
        }

        holder.title.setText(event.getEventName() == null ? "" : event.getEventName());
        holder.startTime.setText(event.getTimeSpinner() == null ? "" : event.getTimeSpinner());
        holder.eventLocation.setText(event.getLocation() == null ? "" : event.getLocation());
        holder.eventDate.setText(event.getStartingDate() == null ? "" : event.getStartingDate().toString());
    }

    @Override
    public int getItemCount() {

        return events.size();
    }



}