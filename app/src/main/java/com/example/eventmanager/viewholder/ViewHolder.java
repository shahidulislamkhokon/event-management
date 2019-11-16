package com.example.eventmanager.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.adapter.ClickListener;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView title, startTime,eventDate, eventLocation;
    public ImageView eventImage;
    private Button deleteButton, editButton;

    public ViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        title = (TextView) itemView.findViewById(R.id.title);
        startTime= (TextView) itemView.findViewById(R.id.startTimeId);
        eventLocation = (TextView) itemView.findViewById(R.id.locationId);
        eventDate = (TextView) itemView.findViewById(R.id.eventDate);
        eventImage = (ImageView) itemView.findViewById(R.id.imageview);
    }

    private ClickListener mClickListener;


    public void setOnClickListener(ClickListener clickListener){
        mClickListener = clickListener;
    }
}