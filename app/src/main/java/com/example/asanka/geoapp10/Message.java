package com.example.asanka.geoapp10;

/**
 * Created by ck on 3/11/16.
 */

public class Message {

    public String content="";
    public String location="";
    public String sender="";

    public Message()
    {
        content="";
        location="";
        sender="";
    }

    public Message(String content, String location, String sender)
    {
        this.content = content;
        this.location = location;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String lat) {
        this.content = lat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String lon) {
        this.location = lon;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String user) {
        this.sender = user;
    }
}
