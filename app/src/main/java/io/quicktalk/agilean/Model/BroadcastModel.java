package io.quicktalk.agilean.Model;

public class BroadcastModel {
    String topic;

    String date;
    String randomname;
    String LinkforBroadCaster;
    String LinkforAudience;
    public  BroadcastModel(){

    }

    public BroadcastModel(String topic, String date, String randomname, String linkforBroadCaster, String linkforAudience) {

        this.topic = topic;
        this.date = date;
        this.randomname = randomname;
        LinkforBroadCaster = linkforBroadCaster;
        LinkforAudience = linkforAudience;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRandomname() {
        return randomname;
    }

    public void setRandomname(String randomname) {
        this.randomname = randomname;
    }

    public String getLinkforBroadCaster() {
        return LinkforBroadCaster;
    }

    public void setLinkforBroadCaster(String linkforBroadCaster) {
        LinkforBroadCaster = linkforBroadCaster;
    }

    public String getLinkforAudience() {
        return LinkforAudience;
    }

    public void setLinkforAudience(String linkforAudience) {
        LinkforAudience = linkforAudience;
    }
}
