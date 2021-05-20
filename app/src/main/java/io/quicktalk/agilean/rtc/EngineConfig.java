package io.quicktalk.agilean.rtc;

import io.quicktalk.agilean.Constants;

public class EngineConfig {
    // private static final int DEFAULT_UID = 0;
    // private int mUid = DEFAULT_UID;

    private String mChannelName, mDisplayName;
    private int role = 1;
    private boolean mShowVideoStats;
    private int mDimenIndex = Constants.DEFAULT_PROFILE_IDX;
    private int mMirrorLocalIndex;
    private int mMirrorRemoteIndex;
    private int mMirrorEncodeIndex;


    public int getVideoDimenIndex() {
        return mDimenIndex;
    }

    public void setVideoDimenIndex(int index) {
        mDimenIndex = index;
    }


    public String getChannelName() {
        return mChannelName;
    }
    public String getDisplayName() { return mDisplayName; }
    public int getRole(){ return role;}

    public void setChannelName(String mChannel) {
        this.mChannelName = mChannel;
    }
    public void setDisplayName(String mDisplayName){
        this.mDisplayName = mDisplayName;
    }
    public void setRole(int role){this.role = role;}

    public boolean ifShowVideoStats() {
        return mShowVideoStats;
    }

    public void setIfShowVideoStats(boolean show) {
        mShowVideoStats = show;
    }

    public int getMirrorLocalIndex() {
        return mMirrorLocalIndex;
    }

    public void setMirrorLocalIndex(int index) {
        mMirrorLocalIndex = index;
    }

    public int getMirrorRemoteIndex() {
        return mMirrorRemoteIndex;
    }

    public void setMirrorRemoteIndex(int index) {
        mMirrorRemoteIndex = index;
    }

    public int getMirrorEncodeIndex() {
        return mMirrorEncodeIndex;
    }

    public void setMirrorEncodeIndex(int index) {
        mMirrorEncodeIndex = index;
    }
}
