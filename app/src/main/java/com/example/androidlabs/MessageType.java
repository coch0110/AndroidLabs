package com.example.androidlabs;

public class MessageType {

    private long id;
    private String message;
    private String type; // send is true, receive is false.

    public MessageType(String message, String type, long id){
        setMessage(message);
        setType(type);
        setId(id);
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public String getType(){return type;}

    public void setType(String type) {this.type = type;}

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}
}