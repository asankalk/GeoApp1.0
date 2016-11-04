package com.example.asanka.geoapp10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ViewGroup extends AppCompatActivity {

    private static final String LOGIN_EMAIL = "com.bignerdranch.android.myapplication.login_email";
    private Button btnViewGroupBack;
    public static final String USERS_LOCATION="https://geoapp-8a315.firebaseio.com/users";
    ListView listView;
    List<String> groupIdentifier;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        groupIdentifier = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, groupIdentifier);
        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String data = (String)parent.getItemAtPosition(position);
                //String[] part = data.split(" ");

                Intent i = EditGroup.newIntent(ViewGroup.this, data);

                startActivity(i);

            }});*/

        getGroups(getIntent().getStringExtra(LOGIN_EMAIL));
    }

    public static Intent newIntent(Context packageContext, String email)
    {
        Intent i = new Intent(packageContext, ViewGroup.class);
        i.putExtra(LOGIN_EMAIL, email);

        return i;
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
                    listView = (ListView) findViewById(R.id.mobile_list);
                    listView.setAdapter(adapter);

                }
                else  // the user is not a member of any group
                {
                    Toast.makeText(ViewGroup.this, "User does not belong to any group", Toast.LENGTH_SHORT).show();
                    //freezeInteraction(false);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
