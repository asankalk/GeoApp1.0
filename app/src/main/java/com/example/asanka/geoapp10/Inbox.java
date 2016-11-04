package com.example.asanka.geoapp10;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Inbox extends AppCompatActivity{

    public static final String GROUPS_LOCATION="https://geoapp-8a315.firebaseio.com/groups";

    public static final String USERS_LOCATION="https://geoapp-8a315.firebaseio.com/users";
    private static final String LOGIN_EMAIL = "com.bignerdranch.android.myapplication.login_email";
    private static final String LOGIN_PASSWORD = "com.bignerdranch.android.myapplication.login_password";
    private static final String CURRENT_LONG = "com.bignerdranch.android.myapplication.longtitude";
    private static final String CURRENT_LAT = "com.bignerdranch.android.myapplication.latitude";
    private static final String MESSAGE = "https://geoapp-8a315.firebaseio.com/groups/Murdoch/Messages";
    ArrayAdapter adapter;
    ListView listView;
    private Button btnInboxBack;
    private Button sendButton;
    private EditText mMessageToBeSent;
    List<String> groupIdentifier;
    List<Message> message = new ArrayList<Message>();
    List<String> contentList;
    protected LocationManager locationManager;
    private double currentLong;
    private double currentLat;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        groupIdentifier = new ArrayList<String>();
        contentList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, contentList);
        listView = (ListView) findViewById(R.id.messsage_content_list);
        listView.setAdapter(adapter);


        currentLat = getIntent().getDoubleExtra(CURRENT_LAT, 0);
        currentLong = getIntent().getDoubleExtra(CURRENT_LONG, 0);

        sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageToBeSent = (EditText) findViewById(R.id.messageTobeSent);

                String temp = mMessageToBeSent.getText().toString();

                Message tempMessage = new Message(temp, String.valueOf(currentLat)+","+String.valueOf(currentLong), getIntent().getStringExtra(LOGIN_EMAIL).toString() );

                Firebase ref = new Firebase(MESSAGE).push();
                ref.setValue(tempMessage);
            }
        });

        Toast.makeText(Inbox.this, Integer.toString(groupIdentifier.size()), Toast.LENGTH_SHORT).show();
        getGroups(getIntent().getStringExtra(LOGIN_EMAIL));

    }
    DatabaseReference a;

    private void getMessageByGroupName(String groupId)
    {
        a = FirebaseDatabase.getInstance().getReference();



        a.child("groups").child(groupId).child("Messages").addValueEventListener(new com.google.firebase.database.ValueEventListener()
        {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null)  // the user is member of one or more groups
                {
                    for (com.google.firebase.database.DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Message post = postSnapshot.getValue(Message.class);
                        Log.e("Get Data", post.content);
                        contentList.add(post.content);
                        Log.e("Context list size", Integer.toString(contentList.size()));
                        message.add(new Message(post.content, post.location, post.sender));
                    }
                    listView = (ListView) findViewById(R.id.messsage_content_list);
                    listView.setAdapter(adapter);
                }
                else  // the user is not a member of any group
                {
                    //Toast.makeText(Inbox.this, "User does not belong to any group", Toast.LENGTH_SHORT).show();
                    //freezeInteraction(false);
                    //finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public static boolean separateStringAndPutThemIntoListOfString(String line, List<String> list)
    {
        StringTokenizer tk = new StringTokenizer(line, ",");
        while(tk.hasMoreTokens())
        {
            list.add(tk.nextToken());
        }
        return true;
    }


    DatabaseReference dataRef;
    String groupsOfUserWithCommaDeliminated;
    private void getGroups(final String username)
    {
        dataRef = FirebaseDatabase.getInstance().getReference();
        //new Firebase(USERS_LOCATION);


        dataRef.child("users").child(username).child("groups").addValueEventListener(new com.google.firebase.database.ValueEventListener()
        {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null)  // the user is member of one or more groups
                {
                    groupIdentifier.clear();
                    //freezeInteraction(false);
                    groupsOfUserWithCommaDeliminated = (String)dataSnapshot.getValue();
                    separateStringAndPutThemIntoListOfString(groupsOfUserWithCommaDeliminated, groupIdentifier);
                    //listView = (ListView) findViewById(R.id.mobile_list);
                    //listView.setAdapter(adapter);
                    for (String group: groupIdentifier)
                    {
                        getMessageByGroupName(group);
                    }
                }
                else  // the user is not a member of any group
                {
                    Toast.makeText(Inbox.this, "User does not belong to any group", Toast.LENGTH_SHORT).show();
                    //freezeInteraction(false);
                    //finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static Intent newIntent(Context packageContext, String email, String password, double lng, double lat)
    {
        Intent i = new Intent(packageContext, Inbox.class);
        i.putExtra(LOGIN_EMAIL, email);
        i.putExtra(LOGIN_PASSWORD, password);
        i.putExtra(CURRENT_LONG, lng);
        i.putExtra(CURRENT_LAT, lat);
        return i;
    }

}
