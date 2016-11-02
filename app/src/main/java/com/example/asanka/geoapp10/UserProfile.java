package com.example.asanka.geoapp10;

/**
 * Created by Asanka on 2/11/2016.
 */

public class UserProfile {
    public String email;
    public String password;
    public String groups;

    public UserProfile()
    {

    }

    public UserProfile(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

}
