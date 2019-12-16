package com.example.eventmanager.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MediaController;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context context;
    List<Event> events;
    private DatabaseReference mDatabaseLike;
    private boolean mProcessLike=false;
    private boolean mProcessComment=false;
    List<String> oldLikes, oldComments;
    private String m_Text;

    Activity activity;

    boolean isLiked;


    public RecyclerViewAdapter(Context context, List<Event> TempList,Activity activity) {

        this.events = TempList;
        this.context = context;
        this.activity = activity;

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

        try{
            if(event.getSaveEventVideo()!=null && !event.getSaveEventVideo().equals(" "))
            {
                //control video
                MediaController controller=new MediaController(activity);
                controller.setAnchorView(holder.videoView);
                holder.videoView.setMediaController(controller);

                Log.d("VideoUrl",event.getEventName()+"  "+event.getSaveEventVideo());
                holder.videoView.setVideoPath(event.getSaveEventVideo());
                holder.videoView.seekTo(2);
                holder.videoView.start();

            }

        }catch (Exception ex)
        {
           // holder.eventImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.evetn_manager_icon,null));
            Log.d("Error","Invalid Video");
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


        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                //sendIntent.putExtra("event",event);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, event.getEventName()+"\n"+event.getDescription()+"\n"+event.getLocation()+"\n"+event.getStartingDate()+"\n"+event.getSaveEventImage()+"\n\n"+event.getSaveEventVideo());
                sendIntent.setType("text/plain");

                Intent i = Intent.createChooser(sendIntent,"Share with");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            }
        });


        String comments = "";
        for (String comment :
                event.getComments()) {
            comments += "\n" + comment;

        }
        holder.comments.setText(comments);



        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent(event);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users/"+ event.getEventAddedBy()+"/events/"+event.getKey()+"/likes");


        if(event.getLikes().contains(EventActivity.firebaseUser.getUid()))
        {
            holder.likeButton.setBackgroundColor(Color.GREEN);
        }else{
            holder.likeButton.setBackgroundColor(Color.GRAY);
        }


        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "You like the Event",Toast.LENGTH_SHORT).show();

                final View likeButton = v;


                oldLikes = event.getLikes();

                if(oldLikes.contains(EventActivity.firebaseUser.getUid()))
                {
                    oldLikes.remove(EventActivity.firebaseUser.getUid());
                    isLiked = false;
                }else{
                    oldLikes.add(EventActivity.firebaseUser.getUid());
                    isLiked = true;
                }


                mProcessLike=true;

                if(mProcessLike){

                    ref.setValue(oldLikes)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    if(isLiked)
                                    {
                                        likeButton.setBackgroundColor(Color.GREEN);
                                        Toast.makeText(context,"Liked",Toast.LENGTH_SHORT).show();
                                    }else{
                                        likeButton.setBackgroundColor(Color.GRAY);
                                        Toast.makeText(context,"Disliked",Toast.LENGTH_SHORT).show();
                                    }
                                    event.setLikes(oldLikes);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });


        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference("users/"+ event.getEventAddedBy()+"/events/"+event.getKey()+"/comments");
                final View likeComment = v;
                oldComments = event.getComments();


                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Your Comment");

// Set up the input
                final EditText input = new EditText(activity);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        oldComments.add(m_Text);
                        ref.setValue(oldComments)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        event.setComments(oldComments);
                                        Toast.makeText(context,"Comment is added",Toast.LENGTH_LONG).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                        //database
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });




        if(!event.getEventAddedBy().equals(EventActivity.firebaseUser.getUid()))
        {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
            //holder.shareButton.setVisibility(View.GONE);
        }
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