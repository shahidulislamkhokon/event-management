package com.example.eventmanager.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmanager.R;
import com.example.eventmanager.adapter.ClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView title, startTime,eventDate, eventLocation,comments;
    public ImageView eventImage;
    public VideoView videoView;
    public Button deleteButton, editButton,shareButton,likeButton,commentButton;


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
        videoView=(VideoView)itemView.findViewById(R.id.chooseVideo);



        editButton = (Button) itemView.findViewById(R.id.editButton);
        deleteButton=(Button) itemView.findViewById(R.id.deleteButtonId);
        shareButton= (Button) itemView.findViewById(R.id.shareButtonId);
        likeButton=(Button) itemView.findViewById(R.id.likeButtonId);
        commentButton=(Button) itemView.findViewById(R.id.commentButtonId);
        comments=(TextView) itemView.findViewById(R.id.commentsTextId);





    }

    private ClickListener mClickListener;


    public void setOnClickListener(ClickListener clickListener){
        mClickListener = clickListener;
    }
}