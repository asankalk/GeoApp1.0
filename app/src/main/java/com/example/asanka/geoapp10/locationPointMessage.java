package com.example.asanka.geoapp10;

/**
 * Created by John on 22/10/2016.
 */

public class locationPointMessage {

    private String content;
    private String location;
    private String sender;

    locationPointMessage(){

    }

    public locationPointMessage(String sender, String content, String location) {
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
