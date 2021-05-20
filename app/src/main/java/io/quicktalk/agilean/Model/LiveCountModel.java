package io.quicktalk.agilean.Model;

public class LiveCountModel {
    String Name ;
    public LiveCountModel(){

    }
    public LiveCountModel(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //    int Livecount;
//    public LiveCountModel(){
//
//    }
//    public LiveCountModel(int livecount) {
//        Livecount = livecount;
//    }
//
//    public int getLivecount() {
//        return Livecount;
//    }
//
//    public void setLivecount(int livecount) {
//        Livecount = livecount;
//    }
}
