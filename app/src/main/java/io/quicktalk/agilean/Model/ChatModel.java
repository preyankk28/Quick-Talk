package io.quicktalk.agilean.Model;

public class ChatModel {
    String FullName;
    String message;

    public ChatModel(){

    }
    public ChatModel(String fullName, String message) {
        FullName = fullName;
        this.message = message;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
