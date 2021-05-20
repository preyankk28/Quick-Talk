package io.quicktalk.agilean;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.quicktalk.agilean.activities.LiveActivity;
import io.quicktalk.agilean.activities.BaseActivity;
import io.quicktalk.agilean.activities.RoleActivity;
import io.quicktalk.agilean.activities.SettingsActivity;

public class BroadCastStartActivity extends BaseActivity {

    private static final int MIN_INPUT_METHOD_HEIGHT = 200;
    private static final int ANIM_DURATION = 200;
    public static final int CLIENT_ROLE_BROADCASTER = 1;
    public static final int CLIENT_ROLE_AUDIENCE = 2;
    // Permission request code of any integer value
    private static final int PERMISSION_REQ_CODE = 1 << 4;

    private String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Rect mVisibleRect = new Rect();
    private int mLastVisibleHeight = 0;
    private RelativeLayout mBodyLayout;
    private int mBodyDefaultMarginTop;
    private TextView mTopicEdit, mDisplayNameEdit;
    private TextView mStartBtn;
    private ImageView mLogo;
    private RadioButton broadcasterRadio, audienceRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_start);
        mTopicEdit = findViewById(R.id.topic_edit);
        mDisplayNameEdit = findViewById(R.id.display_name);
        Intent intent = getIntent();
        String topic = intent.getStringExtra("Topic");
        String fullname = intent.getStringExtra("FullName");
        mTopicEdit.setText(topic);
        mDisplayNameEdit.setText(fullname);
      //  mBodyLayout = findViewById(R.id.middle_layout);
        mLogo = findViewById(R.id.main_logo);

        broadcasterRadio = findViewById(R.id.radio_broadcaster);
        audienceRadio = findViewById(R.id.radio_audience);

        mStartBtn = findViewById(R.id.start_broadcast_button);
        if (TextUtils.isEmpty(mTopicEdit.getText()) && TextUtils.isEmpty((mDisplayNameEdit.getText()))){
            mStartBtn.setEnabled(false);
        }




    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_broadcaster:
                if (checked){
                    config().setRole(CLIENT_ROLE_BROADCASTER);
                }
                break;
            case R.id.radio_audience:
                if (checked){
                    config().setRole(CLIENT_ROLE_AUDIENCE);
                }
                break;
        }
    }
    public void onSettingClicked(View view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
    public void onStartBroadcastClicked(View view) {

        checkPermission();
    }

    private void checkPermission() {
        boolean granted = true;
        for (String per : PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {

            resetLayoutAndForward();
        } else {
            requestPermissions();
        }
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                resetLayoutAndForward();
            } else {
                toastNeedPermissions();
            }
        }
    }
    private void resetLayoutAndForward() {
        closeImeDialogIfNeeded();
        // gotoRoleActivity();
        gotoLiveActivity();
    }

    private void closeImeDialogIfNeeded() {
        InputMethodManager manager = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mTopicEdit.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void gotoRoleActivity() {
        Intent intent = new Intent(BroadCastStartActivity.this, RoleActivity.class);
        String room = mTopicEdit.getText().toString();
        String name = mDisplayNameEdit.getText().toString();
        config().setChannelName(room);
        config().setDisplayName(name);
        startActivity(intent);
    }
    public void gotoLiveActivity(){
        Intent intent = new Intent(BroadCastStartActivity.this, LiveActivity.class);
        intent.putExtra(Constants.KEY_CLIENT_ROLE, config().getRole());
        intent.setClass(getApplicationContext(), LiveActivity.class);
        Intent intent1 = getIntent();
        String meetingid = intent1.getStringExtra("meetingid");

        String room = mTopicEdit.getText().toString();
        String name = mDisplayNameEdit.getText().toString();
        intent.putExtra("IntLiveCount", 1);
        intent.putExtra("name",name);
        intent.putExtra("meetingid", meetingid);

        intent.putExtra("topic",room);
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        mFirebaseDatabaseReference.child("BroadCastChannelModel").child(room).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                LiveCountModel liveCountModel = dataSnapshot.getValue(LiveCountModel.class);
//                livecountfromfirebase = liveCountModel.getLivecount();
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        int livecount = livecountfromfirebase + 1;
//        LiveCountModel liveCountModel = new LiveCountModel(livecount);
//        mFirebaseDatabaseReference.child("BroadCastChannelModel").child(room).setValue(liveCountModel);

        config().setChannelName(room);
        config().setDisplayName(name);
        startActivity(intent);
    }


    private void toastNeedPermissions() {
        Toast.makeText(this, R.string.need_necessary_permissions, Toast.LENGTH_LONG).show();

    }


}