package io.quicktalk.agilean.activities;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.agora.rtc.Constants;
import io.quicktalk.agilean.R;

public class MainActivity extends BaseActivity {
//    DatabaseReference mFirebaseDatabaseReference;
//    int livecountfromfirebase;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MIN_INPUT_METHOD_HEIGHT = 200;
    private static final int ANIM_DURATION = 200;

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
    private EditText mDisplayNameEdit;
    private TextView mTopicEdit;
    private TextView mStartBtn;
    private ImageView mLogo;
    private RadioButton broadcasterRadio, audienceRadio;
    String meetingid;

    private Animator.AnimatorListener mLogoAnimListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            // Do nothing
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mLogo.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mLogo.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            // Do nothing
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mStartBtn.setEnabled(!TextUtils.isEmpty(editable));
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener mLayoutObserverListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    checkInputMethodWindowState();
                }
            };

    private void checkInputMethodWindowState() {
        getWindow().getDecorView().getRootView().getWindowVisibleDisplayFrame(mVisibleRect);
        int visibleHeight = mVisibleRect.bottom - mVisibleRect.top;
        if (visibleHeight == mLastVisibleHeight) return;

        boolean inputShown = mDisplayMetrics.heightPixels - visibleHeight > MIN_INPUT_METHOD_HEIGHT;
        mLastVisibleHeight = visibleHeight;
        if (inputShown) {
            if (mLogo.getVisibility() == View.VISIBLE) {
                mBodyLayout.animate().translationYBy(-mLogo.getMeasuredHeight())
                        .setDuration(ANIM_DURATION).setListener(null).start();
                mLogo.setVisibility(View.INVISIBLE);
            }
        } else if (mLogo.getVisibility() != View.VISIBLE) {
            mBodyLayout.animate().translationYBy(mLogo.getMeasuredHeight())
                    .setDuration(ANIM_DURATION).setListener(mLogoAnimListener).start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

    }

    private void initUI() {
        mBodyLayout = findViewById(R.id.middle_layout);
        mLogo = findViewById(R.id.main_logo);

        broadcasterRadio = findViewById(R.id.radio_broadcaster);
        audienceRadio = findViewById(R.id.radio_audience);

        mTopicEdit = findViewById(R.id.topic_edit);
        mTopicEdit.addTextChangedListener(mTextWatcher);
        Intent intent = getIntent();
        String topic = intent.getStringExtra("Topic");
        String fullname = intent.getStringExtra("FullName");

    // mTopicEdit.setText(topic);


        mDisplayNameEdit = findViewById(R.id.display_name);
        mDisplayNameEdit.addTextChangedListener(mTextWatcher);
       //mDisplayNameEdit.setText(fullname);

        Uri data = intent.getData();


        mStartBtn = findViewById(R.id.start_broadcast_button);
        if (TextUtils.isEmpty(mTopicEdit.getText()) && TextUtils.isEmpty((mDisplayNameEdit.getText()))){
            mStartBtn.setEnabled(false);
        }
        if(data != null) {
//            Toast.makeText(this, data.getQuery(), Toast.LENGTH_LONG).show();
            String topictext = data.getQuery().split("__")[0];
            String topictextreplace = topictext.replace("%20", " ");
            mTopicEdit.setText(topictextreplace);
           meetingid = data.getQuery().split("__")[3];

           // mTopicEdit.setText(data.getQuery().split("__")[0]);
            if(data.getQuery().split("__")[2].equals("2")) {
                config().setRole(Constants.CLIENT_ROLE_AUDIENCE);
                audienceRadio.setChecked(true);
                broadcasterRadio.setEnabled(false);
            }else{
                config().setRole(Constants.CLIENT_ROLE_BROADCASTER);
                broadcasterRadio.setChecked(true);
                audienceRadio.setEnabled(false);
            }
        }
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        adjustViewPositions();
    }

    private void adjustViewPositions() {
        // Setting btn move downward away the status bar
        ImageView settingBtn = findViewById(R.id.setting_button);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) settingBtn.getLayoutParams();
        param.topMargin += mStatusBarHeight;
        settingBtn.setLayoutParams(param);

        // Logo is 0.48 times the screen width
        // ImageView logo = findViewById(R.id.main_logo);
        param = (RelativeLayout.LayoutParams) mLogo.getLayoutParams();
        int size = (int) (mDisplayMetrics.widthPixels * 0.48);
        param.width = size;
        param.height = size;
        mLogo.setLayoutParams(param);

        // Bottom margin of the main body should be two times it's top margin.
        param = (RelativeLayout.LayoutParams) mBodyLayout.getLayoutParams();
        param.topMargin = (mDisplayMetrics.heightPixels -
                mBodyLayout.getMeasuredHeight() - mStatusBarHeight) / 3;
        mBodyLayout.setLayoutParams(param);
        mBodyDefaultMarginTop = param.topMargin;

        // The width of the start button is roughly 0.72
        // times the width of the screen
        mStartBtn = findViewById(R.id.start_broadcast_button);
        param = (RelativeLayout.LayoutParams) mStartBtn.getLayoutParams();
        param.width = (int) (mDisplayMetrics.widthPixels * 0.72);
        mStartBtn.setLayoutParams(param);
    }

    public void onSettingClicked(View view) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_broadcaster:
                if (checked){
                    config().setRole(Constants.CLIENT_ROLE_BROADCASTER);
                }
                break;
            case R.id.radio_audience:
                if (checked){
                    config().setRole(Constants.CLIENT_ROLE_AUDIENCE);
                }
                break;
        }
    }
    public void onStartBroadcastClicked(View view) {
       TextView topicDiscussion = findViewById(R.id.topic_edit);
        String topic = topicDiscussion.getText().toString().trim();


        if ((topic.isEmpty())) {
            topicDiscussion.setError("Please Enter Topic");
            topicDiscussion.requestFocus();
            return;
        }
        EditText displayname = findViewById(R.id.display_name);
        String displaynamestring = displayname.getText().toString().trim();
        if ((displaynamestring.isEmpty())) {
            displayname.setError("Please Enter Name");
            displayname.requestFocus();
            return;
        }
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
        Intent intent = new Intent(MainActivity.this, RoleActivity.class);
        String room = mTopicEdit.getText().toString();

        String name = mDisplayNameEdit.getText().toString();
        config().setChannelName(room);
        config().setDisplayName(name);
        startActivity(intent);
    }
    public void gotoLiveActivity(){
        Intent intent = new Intent(MainActivity.this, LiveActivity.class);
        intent.putExtra(io.quicktalk.agilean.Constants.KEY_CLIENT_ROLE, config().getRole());
        intent.putExtra("IntLiveCount", 1);
        intent.putExtra("meetingid", meetingid);
        intent.setClass(getApplicationContext(), LiveActivity.class);
        String room = mTopicEdit.getText().toString();
        String name = mDisplayNameEdit.getText().toString();

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
//


        intent.putExtra("name",name);
        intent.putExtra("topic",room);
        config().setChannelName(room);
        config().setDisplayName(name);
        startActivity(intent);
    }


    private void toastNeedPermissions() {
        Toast.makeText(this, R.string.need_necessary_permissions, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetUI();
        registerLayoutObserverForSoftKeyboard();
    }

    private void resetUI() {
        resetLogo();
        closeImeDialogIfNeeded();
    }

    private void resetLogo() {
        mLogo.setVisibility(View.VISIBLE);
        mBodyLayout.setY(mBodyDefaultMarginTop);
    }

    private void registerLayoutObserverForSoftKeyboard() {
        View view = getWindow().getDecorView().getRootView();
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(mLayoutObserverListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeLayoutObserverForSoftKeyboard();
    }

    private void removeLayoutObserverForSoftKeyboard() {
        View view = getWindow().getDecorView().getRootView();
        view.getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutObserverListener);
    }
}
