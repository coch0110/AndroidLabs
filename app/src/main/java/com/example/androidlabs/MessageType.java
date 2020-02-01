package com.example.androidlabs;

public class MessageType {

    private String message;
    private boolean type; // send is true, receive is false.

    public MessageType(String message, boolean type){
        setMessage(message);
        setType(type);
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public boolean getType(){return type;}

    public void setType(boolean type) {this.type = type;}
}