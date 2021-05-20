package io.quicktalk.agilean.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import io.quicktalk.agilean.Adapter.LiveCountAdapter;
import io.quicktalk.agilean.Model.LiveCount;
import io.quicktalk.agilean.Model.LiveCountModel;
import io.quicktalk.agilean.ui.VideoGridContainer;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.quicktalk.agilean.Adapter.ChatRecyclerAdapter;
import io.quicktalk.agilean.Model.ChatModel;
import io.quicktalk.agilean.R;
import io.quicktalk.agilean.stats.LocalStatsData;
import io.quicktalk.agilean.stats.RemoteStatsData;
import io.quicktalk.agilean.stats.StatsData;

//import io.quicktalk.agilean.BuildConfig;

public class LiveActivity extends RtcBaseActivity {
    private static final String TAG = LiveActivity.class.getSimpleName();
    LiveCountAdapter liveCountAdapter;
    private TextView Live;
    private VideoGridContainer mVideoGridContainer;
    private ImageView mMuteAudioBtn;

    private ImageView mMoreBtn;
    private ImageView mCameraBtn;
    private ImageView mBueatyBtn;
    EditText messageEditText;
    private int usersCount = 0;
    private VideoEncoderConfiguration.VideoDimensions mVideoDimension;
    LinearLayout bottomcontainer;
    TextView roomName;
    Button send ;
    Context context;
    LinearLayout buttonsendlayout;
    RecyclerView chatrecyclerView;
    ChatRecyclerAdapter chatListAdapter;
    public static final String MESSAGES_CHILD = "chat";
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<ChatModel, MessageViewHolder> mFirebaseAdapter;
   ImageView mMuteVideoBtn;
    private ImageView mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private EmojiconEditText mMessageEditText;
    private String mEmail = "";
    String fullname;
    String topic;
    RelativeLayout rootView;
    int livecountfromfirebase ;
    EmojIconActions emojIcon;
    String meetingid;
    ImageView emojiImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        //        EmojiCompat.init(config);

        setContentView(R.layout.activity_live_room);
        //..screen will stay on during this section..
       // wl.release();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        rootView = findViewById(R.id.rootView);
        emojiImageView = findViewById(R.id.emojiimageview);
        mMessageEditText = findViewById(R.id.messageEditText);
        emojIcon = new EmojIconActions(this, rootView, mMessageEditText, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });
        Intent intent = getIntent();
        fullname = intent.getStringExtra("name");
        meetingid = intent.getStringExtra("meetingid");
        //int CountLive  = intent.getIntExtra("IntLiveCount", 1);
       //livecount = livecount + CountLive;
        topic = intent.getStringExtra("topic");
        Log.e("TAG","Name" + fullname);
        Log.e("TAG","Name" + topic);
        initUI();
        initData();


        mMessageEditText = findViewById(R.id.messageEditText);
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        Live = findViewById(R.id.livecount);
        Live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveActivity.this, LiveCountActivity.class);
                intent.putExtra("topic", topic);
                intent.putExtra("fullname", fullname);
                intent.putExtra("meetingid", meetingid);
                //meetingid = intent.getStringExtra("meetingid");
                int role = getIntent().getIntExtra(
                        io.quicktalk.agilean.Constants.KEY_CLIENT_ROLE,
                        Constants.CLIENT_ROLE_AUDIENCE);

                boolean isBroadcaster = (role == Constants.CLIENT_ROLE_BROADCASTER);
                if (isBroadcaster){
                    intent.putExtra(io.quicktalk.agilean.Constants.KEY_CLIENT_ROLE,io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER);
                }
                startActivity(intent);
                finish();
            }
        });


        //LiveCount
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        mFirebaseDatabaseReference.child("BroadCastChannelModel").child(topic).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                LiveCountModel liveCountModel = dataSnapshot.getValue(LiveCountModel.class);
//                livecountfromfirebase = liveCountModel.getLivecount();
//                Log.e("TAG", "LiveCount" + livecountfromfirebase);
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        Live = findViewById(R.id.livecount);
//        Live.setText("LIVE" +" " +livecountfromfirebase);





        // Initialize Firebase Auth
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Query query = mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(meetingid);
        FirebaseRecyclerOptions<ChatModel> options = new FirebaseRecyclerOptions.Builder<ChatModel>()
                .setQuery(query, ChatModel.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatModel, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(MessageViewHolder viewHolder, int position, ChatModel friendlyMessage) {
                if (friendlyMessage.getFullName().equals(mEmail)) {
                    viewHolder.row.setGravity(Gravity.END);
                } else {
                    viewHolder.row.setGravity(Gravity.START);
                }
                viewHolder.messageTextView.setText(friendlyMessage.getMessage());
                viewHolder.messengerTextView.setText(friendlyMessage.getFullName());
            }

            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.chatrecyclercard, viewGroup, false));
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel friendlyMessage = new ChatModel(fullname,mMessageEditText.getText().toString() );
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(meetingid).push().setValue(friendlyMessage);
                mMessageEditText.setText("");
            }
        });

        //llTextViews=findViewById(R.id.llTextViews);



    }


    @Override
    protected void onResume() {
        super.onResume();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount=0f;
        getWindow().setAttributes(lp);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
   //..screen will stay on during this section..
        wl.release();
        lp.screenBrightness = 0.01f + 200000 / 100.0f;
        // PowerManager.WakeLock wl = p.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
      //  wl.acquire();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAdapter != null) {
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFirebaseAdapter != null) {
            mFirebaseAdapter.stopListening();
        }
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout row;
        TextView messageTextView;
        TextView messengerTextView;

        MessageViewHolder(View v) {
            super(v);
            row = itemView.findViewById(R.id.row);
            messageTextView = itemView.findViewById(R.id.namemessagechat);
            messengerTextView = itemView.findViewById(R.id.messagechat);
        }


    }



    private void initUI() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        LiveCountModel liveCountModel = new LiveCountModel(fullname);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
      String uid = firebaseUser.getUid();
      Intent intent = getIntent();
        meetingid = intent.getStringExtra("meetingid");
        mFirebaseDatabaseReference.child("BroadCastChannelModel").child(meetingid).child(uid).setValue(liveCountModel);
        roomName = findViewById(R.id.live_room_name);
        roomName.setText(config().getChannelName());
        //roomName.setText(topic);
        roomName.setSelected(true);
        TextView name = findViewById(R.id.live_room_broadcaster_uid);
        name.setText(config().getDisplayName());
        bottomcontainer = findViewById(R.id.bottom_container);


        int role = getIntent().getIntExtra(
                io.quicktalk.agilean.Constants.KEY_CLIENT_ROLE,
                Constants.CLIENT_ROLE_AUDIENCE);

        boolean isBroadcaster = (role == Constants.CLIENT_ROLE_BROADCASTER);
        mCameraBtn = findViewById(R.id.live_btn_switch_camera);
        mBueatyBtn = findViewById(R.id.live_btn_beautification);
       mMuteVideoBtn = findViewById(R.id.live_btn_mute_video);
        mMuteVideoBtn.setActivated(isBroadcaster);
        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio);
        mMuteAudioBtn.setActivated(isBroadcaster);
        mMoreBtn = findViewById(R.id.live_btn_more);
        mMoreBtn.setActivated(isBroadcaster);

        if(!isBroadcaster) {
            mCameraBtn.setVisibility(View.INVISIBLE);
            mMuteVideoBtn.setVisibility(View.INVISIBLE);
            mMuteAudioBtn.setVisibility(View.INVISIBLE);
            mMoreBtn.setVisibility(View.INVISIBLE);
            mBueatyBtn.setVisibility(View.INVISIBLE);
        }else{
            buttonsendlayout= findViewById(R.id.linearLayoutbuttonsend);
            buttonsendlayout.setVisibility(View.GONE);
        }

        ImageView beautyBtn = findViewById(R.id.live_btn_beautification);
        beautyBtn.setActivated(true);
        rtcEngine().setBeautyEffectOptions(beautyBtn.isActivated(),
                io.quicktalk.agilean.Constants.DEFAULT_BEAUTY_OPTIONS);

        mVideoGridContainer = findViewById(R.id.live_video_grid_layout);
        mVideoGridContainer.setStatsManager(statsManager());

        rtcEngine().setClientRole(role);
        if (isBroadcaster) startBroadcast();
    }

//    private void initUserIcon() {
//        Bitmap origin = BitmapFactory.decodeResource(getResources(), R.drawable.fake_user_icon);
//        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), origin);
//        drawable.setCircular(true);
//        ImageView iconView = findViewById(R.id.live_name_board_icon);
//        iconView.setImageDrawable(drawable);
//    }

    private void initData() {
        mVideoDimension = io.quicktalk.agilean.Constants.VIDEO_DIMENSIONS[
                config().getVideoDimenIndex()];
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        RelativeLayout topLayout = findViewById(R.id.live_room_top_layout);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
        params.height = mStatusBarHeight + topLayout.getMeasuredHeight();
        topLayout.setLayoutParams(params);
        topLayout.setPadding(0, mStatusBarHeight, 0, 0);
    }


    private void startBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        SurfaceView surface = prepareRtcVideo(0, true);
        mVideoGridContainer.addUserVideoSurface(0, surface, true);
        mMuteAudioBtn.setActivated(true);
    }

    private void stopBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        removeRtcVideo(0, true);
        mVideoGridContainer.removeUserVideo(0, true);

//        mMuteAudioBtn.setActivated(false);
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        // Do nothing at the moment
        Log.i(TAG, "UID =>" + uid);

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        // Do nothing at the moment
        Toast.makeText(this,"User UID"+uid, Toast.LENGTH_LONG);
    }

    @Override
    public void onUserOffline(final int uid, int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeRemoteUser(uid);
            }
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                renderRemoteUser(uid);
            }
        });
    }

    private void renderRemoteUser(int uid) {
        SurfaceView surface = prepareRtcVideo(uid, false);
        mVideoGridContainer.addUserVideoSurface(uid, surface, false);
        usersCount += 1;
        roomName.setText(config().getChannelName()+"("+usersCount+")");
    }

    private void removeRemoteUser(int uid) {
        removeRtcVideo(uid, false);
        mVideoGridContainer.removeUserVideo(uid, false);
        usersCount -= 1;
        if(usersCount > 0)
        roomName.setText(config().getChannelName()+"("+usersCount+")");
    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setWidth(mVideoDimension.width);
        data.setHeight(mVideoDimension.height);
        data.setFramerate(stats.sentFrameRate);
    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setLastMileDelay(stats.lastmileDelay);
        data.setVideoSendBitrate(stats.txVideoKBitRate);
        data.setVideoRecvBitrate(stats.rxVideoKBitRate);
        data.setAudioSendBitrate(stats.txAudioKBitRate);
        data.setAudioRecvBitrate(stats.rxAudioKBitRate);
        data.setCpuApp(stats.cpuAppUsage);
        data.setCpuTotal(stats.cpuAppUsage);
        data.setSendLoss(stats.txPacketLossRate);
        data.setRecvLoss(stats.rxPacketLossRate);
    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        if (!statsManager().isEnabled()) return;

        StatsData data = statsManager().getStatsData(uid);
        if (data == null) return;

        data.setSendQuality(statsManager().qualityToString(txQuality));
        data.setRecvQuality(statsManager().qualityToString(rxQuality));
    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setWidth(stats.width);
        data.setHeight(stats.height);
        data.setFramerate(stats.rendererOutputFrameRate);
        data.setVideoDelay(stats.delay);
    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setAudioNetDelay(stats.networkTransportDelay);
        data.setAudioNetJitter(stats.jitterBufferDelay);
        data.setAudioLoss(stats.audioLossRate);
        data.setAudioQuality(statsManager().qualityToString(stats.quality));
    }

    @Override
    public void finish() {
        super.finish();
        statsManager().clearAllData();
    }

    public void onLeaveClicked(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.alert_message);
        alertDialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.show();
    }

    public void onSwitchCameraClicked(View view) {
        rtcEngine().switchCamera();
    }

    public void onBeautyClicked(View view) {
        view.setActivated(!view.isActivated());
        rtcEngine().setBeautyEffectOptions(view.isActivated(),
                io.quicktalk.agilean.Constants.DEFAULT_BEAUTY_OPTIONS);
    }

    public void onMoreClicked(View view) {

        String topicDiscussionString = config().getChannelName();
        final String topicDiscussionStringfinal =  topicDiscussionString.replace(" ", "%20");
        int role = getIntent().getIntExtra(
                io.quicktalk.agilean.Constants.KEY_CLIENT_ROLE,
                Constants.CLIENT_ROLE_AUDIENCE);
        boolean isBroadcaster = (role == Constants.CLIENT_ROLE_BROADCASTER);
        if (isBroadcaster) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.invite_message);
            alertDialogBuilder.setPositiveButton(R.string.broadcaster, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // finish();
                    String shareMessage = "Inviting you for Discussion\n\n" + "https://www.quicktalk.io/?" + topicDiscussionStringfinal + "__" + getRandomString()+"__1" + "__" + meetingid + "\n\n";
                    share(shareMessage);

                }
            });
            alertDialogBuilder.setNegativeButton(R.string.audience, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String shareMessage = "Inviting you for Discussion\n\n" + "https://www.quicktalk.io/?" + topicDiscussionStringfinal + "__" + getRandomString()+"__2" + "__" +meetingid+ "\n\n";
                    share(shareMessage);

                }
            });
            alertDialogBuilder.show();
        } else {
            Toast.makeText(this, "You can not invite other people for discussion", Toast.LENGTH_LONG).show();
        }
    }

    public static String getRandomString() {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; ++i)
            sb.append(io.quicktalk.agilean.Constants.ALLOWED_CHARACTERS.charAt(random.nextInt(io.quicktalk.agilean.Constants.ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public void onPushStreamClicked(View view) {
        // Do nothing at the moment
        String shareMessage = "Quick Talk helps you to broadcast birthday parties, marriage functions or any kind of events to thousands of people\n\n" + "https://play.google.com/store/apps/details?id=io.quicktalk.agilean";
        share(shareMessage);
    }

    public void share(String shareMessage) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quick Talk");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onMuteAudioClicked(View view) {
        if (!mMuteVideoBtn.isActivated()) return;

        rtcEngine().muteLocalAudioStream(view.isActivated());
        view.setActivated(!view.isActivated());
    }


    public void onMuteVideoClicked(View view) {
        if (view.isActivated()) {
            stopBroadcast();

        } else {
            startBroadcast();
        }
        view.setActivated(!view.isActivated());
    }

    public  void setLiveCount(){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        fullname = intent.getStringExtra("name");


        meetingid = intent.getStringExtra("meetingid");
        mFirebaseDatabaseReference.child("BroadCastChannelModel").child(meetingid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int livecount = 1;
                LiveCount liveCount = new LiveCount(livecount);

//                LiveCountModel liveCountModel = dataSnapshot.getValue(LiveCountModel.class);
//                livecountfromfirebase = liveCountModel.getLivecount();
                int livecountfirebase = liveCount.getLiveCount();
                mFirebaseDatabaseReference.child("BroadCastChannelModel").child(meetingid).setValue(liveCount);
//                String s = Integer.toString(livecountfromfirebase);
//                Live = findViewById(R.id.livecount);
//                Live.setText("LIVE" +" " + livecountfromfirebase);
//                Log.e("TAG", "LIVE" + s);


          }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        int livecount = livecountfromfirebase + 1;
//        LiveCountModel liveCountModel = new LiveCountModel(livecount);
//        mFirebaseDatabaseReference.child("BroadCastChannelModel").child(topic).setValue(liveCountModel);



    }

    public void livecountclick2(View view) {
        Live = findViewById(R.id.livecount);


//        AlertDialog.Builder builder = new AlertDialog.Builder(LiveActivity.this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.activity_livecountpeople,null);
//
//
//        builder.setView(dialogView).show();
//      //  final AlertDialog dialog = builder.create();
//        RecyclerView livecountrecyclervew = dialogView.findViewById(R.id.livecountpeople);
//        livecountrecyclervew.setLayoutManager(new LinearLayoutManager(context));
//        FirebaseRecyclerOptions<LiveCountModel> options = new FirebaseRecyclerOptions.Builder<LiveCountModel>()
//                //    .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(Uid).child("Discussions"), BroadcastModel.class)
//                .setQuery(FirebaseDatabase.getInstance().getReference().child("BroadCastChannelModel").child(topic), LiveCountModel.class)
//                .build();
//
//        LiveCountAdapter liveCountAdapter;
//        liveCountAdapter = new LiveCountAdapter(options,context);
//        livecountrecyclervew.setAdapter(liveCountAdapter);
//
//      int i = liveCountAdapter.getItemCount();
//        String s = Integer.toString(i);
//                Live = findViewById(R.id.livecount);
//                Live.setText("LIVE" +" " + s);
//                Log.e("TAG", "LIVE" + s);
//

    }
    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @SuppressLint("InvalidWakeLockTag")
    public void turnOnScreen(){
        // turn on screen
        Log.v("ProximityActivity", "ON!");
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire();
    }

    @SuppressLint("InvalidWakeLockTag")
    @TargetApi(21) //Suppress lint error for PROXIMITY_SCREEN_OFF_WAKE_LOCK
    public void turnOffScreen(){
        // turn off screen
        Log.v("ProximityActivity", "OFF!");
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "tag");
        mWakeLock.acquire();
    }

}
