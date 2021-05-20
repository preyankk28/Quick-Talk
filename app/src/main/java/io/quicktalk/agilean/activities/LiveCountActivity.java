package io.quicktalk.agilean.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import io.quicktalk.agilean.Adapter.LiveCountAdapter;
import io.quicktalk.agilean.Constants;
import io.quicktalk.agilean.Model.LiveCountModel;
import io.quicktalk.agilean.R;

public class LiveCountActivity extends AppCompatActivity {
    LiveCountAdapter liveCountAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livecountpeople);
        RecyclerView livecountrecyclervew = findViewById(R.id.livecountpeople);

        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        //int CountLive  = intent.getIntExtra("IntLiveCount", 1);
        //livecount = livecount + CountLive;
        String meetingid = intent.getStringExtra("meetingid");

        String topic = intent.getStringExtra("topic");

        livecountrecyclervew.setLayoutManager(new LinearLayoutManager(LiveCountActivity.this));
        FirebaseRecyclerOptions<LiveCountModel> options = new FirebaseRecyclerOptions.Builder<LiveCountModel>()
                //    .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(Uid).child("Discussions"), BroadcastModel.class)
                .setQuery(FirebaseDatabase.getInstance().getReference().child("BroadCastChannelModel").child(meetingid), LiveCountModel.class)
                .build();


        liveCountAdapter = new LiveCountAdapter(options,this);
        livecountrecyclervew.setAdapter(liveCountAdapter);

        int i = liveCountAdapter.getItemCount();
        String s = Integer.toString(i);
//        Live = findViewById(R.id.livecount);
//        Live.setText("LIVE" +" " + s);
        Log.e("TAG", "LIVE" + s);



    }
    @Override
    public void onStart() {
        super.onStart();
        liveCountAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        liveCountAdapter.stopListening();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2 = getIntent();
        String fullname = intent2.getStringExtra("fullname");
        String meetingid = intent2.getStringExtra("meetingid");
        //int CountLive  = intent.getIntExtra("IntLiveCount", 1);
        //livecount = livecount + CountLive;
        String topic = intent2.getStringExtra("topic");
        Intent intent = new Intent(LiveCountActivity.this, LiveActivity.class);
        intent.putExtra("name",fullname);
        intent.putExtra("topic",topic);
        intent.putExtra("meetingid", meetingid);

        int role = getIntent().getIntExtra(
                Constants.KEY_CLIENT_ROLE,
                io.agora.rtc.Constants.CLIENT_ROLE_AUDIENCE);

        boolean isBroadcaster = (role == io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);
        if (isBroadcaster){
            intent.putExtra(Constants.KEY_CLIENT_ROLE,io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);
        }
        startActivity(intent);
        finish();
    }

    public void closeclick(View view) {
        Intent intent2 = getIntent();
        String fullname = intent2.getStringExtra("fullname");
        //int CountLive  = intent.getIntExtra("IntLiveCount", 1);
        String meetingid = intent2.getStringExtra("meetingid");
        //livecount = livecount + CountLive;
        String topic = intent2.getStringExtra("topic");
        Intent intent = new Intent(LiveCountActivity.this, LiveActivity.class);
        intent.putExtra("name",fullname);
        intent.putExtra("topic",topic);
        intent.putExtra("meetingid", meetingid);

        int role = getIntent().getIntExtra(
                Constants.KEY_CLIENT_ROLE,
                io.agora.rtc.Constants.CLIENT_ROLE_AUDIENCE);

        boolean isBroadcaster = (role == io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);
        if (isBroadcaster){
            intent.putExtra(Constants.KEY_CLIENT_ROLE,io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);
        }
        startActivity(intent);
        finish();
    }
}
