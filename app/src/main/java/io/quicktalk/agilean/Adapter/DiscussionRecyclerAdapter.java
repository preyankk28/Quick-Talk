package io.quicktalk.agilean.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.quicktalk.agilean.Model.BroadcastModel;
import io.quicktalk.agilean.BroadCastStartActivity;
import io.quicktalk.agilean.Constants;
import io.quicktalk.agilean.Fragment.BroadCastFragment;
import io.quicktalk.agilean.R;
import io.quicktalk.agilean.activities.BaseActivity;
import io.quicktalk.agilean.activities.MainActivity;

public class DiscussionRecyclerAdapter extends FirebaseRecyclerAdapter<BroadcastModel, DiscussionRecyclerAdapter.myviewholder>  {
    private Context context;
    String LinkforBroadCaster,LinkforAudience;
    String FullName;
    FirebaseAuth mAuth;
    BaseActivity baseActivity;
    FirebaseUser user;
    String Topic;
    String dateandtime;
   BroadCastFragment broadCastFragment;
   MainActivity mainActivity;
   DatabaseReference databaseReference;
    public DiscussionRecyclerAdapter(@NonNull FirebaseRecyclerOptions<BroadcastModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final BroadcastModel model) {

        holder.date.setText(model.getDate());
        holder.topic.setText(model.getTopic());
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          LinkforBroadCaster = model.getLinkforBroadCaster();
           LinkforAudience = model.getLinkforAudience();
           dateandtime = model.getDate();
           Topic = model.getTopic();

           onMoreClicked();

            }
        });
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context, LiveActivity.class);
//                intent.putExtra(Constants.KEY_CLIENT_ROLE, "1");

                Intent intent = new Intent(context, BroadCastStartActivity.class);
                intent.putExtra("Topic", model.getTopic());
//                intent.putExtra("Services", model.getServices());
//                intent.putExtra("BusinessType", model.getBusinessstype());
//                intent.putExtra("Photo", model.getUserId());
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();
                FullName= user.getDisplayName();
                intent.putExtra("FullName", FullName);
                intent.putExtra("meetingid",model.getRandomname());

//               baseActivity.config().setChannelName(Topic);
//               baseActivity.config().setDisplayName(FullName);
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String meetingId = model.getRandomname();
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                user = firebaseAuth.getCurrentUser();
                final String uid = user.getUid();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);



                alertDialogBuilder.setMessage("Do you wish to delete the meeting?");
                alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference().child("Discussions").child(uid).child(meetingId);
                        myRef.removeValue();

                        Toast.makeText(context , "Meeting Deleted", Toast.LENGTH_LONG);
                        return;
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       return;
                    }
                });
                alertDialogBuilder.show();
            }
        });
       String randomname = model.getRandomname();
       String topic = model.getTopic();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        final String uid = user.getUid();
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + randomname + uid + topic);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                Glide.with(context)
//                        .load(uri)
//                        .placeholder(R.drawable.gradient1)
//                        .error(R.drawable.gradient1)
//                        .into(holder.linearLayoutpicture);
                Glide.with(context).load(uri).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                       // yourRelativeLayout.setBackground(resource);
                        holder.linearLayoutpicture.setBackground(resource);
                    }



                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                });
            }
        });
    }

    private void onMoreClicked() {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        FullName= user.getDisplayName();
       final String DisplayName = user.getDisplayName();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.invite_message);

        alertDialogBuilder.setPositiveButton(R.string.broadcaster, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();

                String shareMessage = DisplayName +" is inviting you for Discussion at "+  dateandtime + "\n\n"  + LinkforBroadCaster + "\n" + "\nQuick Talk helps you to broadcast birthday parties, marriage functions or any kind of events to thousands of people\n" +
                        "\n" +
                        "https://play.google.com/store/apps/details?id=io.quicktalk.agilean";
                share(shareMessage);



            }
        });
        alertDialogBuilder.setNegativeButton(R.string.audience, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//
//                String shareMessage = "Inviting you for Discussion\n\n" + LinkforAudience;
//                share(shareMessage);
                String shareMessage = DisplayName +" is inviting you for Discussion at "+  dateandtime + "\n\n"  + LinkforAudience + "\n" + "\nQuick Talk helps you to broadcast birthday parties, marriage functions or any kind of events to thousands of people\n" +
                        "\n" +
                        "https://play.google.com/store/apps/details?id=io.quicktalk.agilean";
                share(shareMessage);

            }
        });
        alertDialogBuilder.show();
    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mydiscussioncard, parent,false);


        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        LinearLayout linearLayoutpicture;
        TextView topic , date;
        TextView MeetingId;
        Button start , share;
        ImageView delete;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            linearLayoutpicture = itemView.findViewById(R.id.linearlayoutpicture);
            topic = itemView.findViewById(R.id.topic);
            date = itemView.findViewById(R.id.date);
            //MeetingId = itemView.findViewById(R.id.meetingidcard);
          //start = itemView.findViewsWithText(R.id.startmeetingbutton);
            start = itemView.findViewById(R.id.startmeeting);
            share = itemView.findViewById(R.id.sharemeeting);
            delete = itemView.findViewById(R.id.deletemeeting);





        }
    }
    public void share(String shareMessage) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quick Talk");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
            //Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
