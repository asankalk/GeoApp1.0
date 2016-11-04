package com.example.asanka.geoapp10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private static final String LOGIN_EMAIL = "com.bignerdranch.android.myapplication.login_email";

    private Button btnSearchBack;
    private Button searchButton;
    private EditText group;

    List<String> groupIdentifier;
    ArrayAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        groupIdentifier = new ArrayList<String>();
        groupIdentifier.add("1");
        groupIdentifier.add("1");
        groupIdentifier.add("1");
        groupIdentifier.add("1");
        groupIdentifier.add("1");
        groupIdentifier.add("1");
        groupIdentifier.add("1");
        groupIdentifier.add("1");

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, groupIdentifier);
        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        group = (EditText) findViewById(R.id.txtSearch);

        searchButton = (Button)findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchResult(group.getText().toString());
            }
        });
    }

    public static Intent newIntent(Context packageContext, String email)
    {
        Intent i = new Intent(packageContext, Search.class);
        i.putExtra(LOGIN_EMAIL, email);

        return i;
    }


    DatabaseReference dataRef;
    private void showSearchResult(final String groupName)
    {
        dataRef = FirebaseDatabase.getInstance().getReference();
        //new Firebase(USERS_LOCATION);


        dataRef.child("groups").child(groupName).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null)  // the user is member of one or more groups
                {
                    groupIdentifier.add(groupName);
                    listView = (ListView) findViewById(R.id.mobile_list);
                    listView.setAdapter(adapter);
                    Toast.makeText(Search.this, "The group exists", Toast.LENGTH_SHORT).show();

                }
                else  // the user is not a member of any group
                {
                    Toast.makeText(Search.this, "The group does not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
