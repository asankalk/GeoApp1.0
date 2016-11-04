package com.example.asanka.geoapp10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.password;

public class CreateGroup extends AppCompatActivity {
    private static final String LOGIN_EMAIL = "com.bignerdranch.android.myapplication.login_email";

    private Button btnCreateGroupBack;
    private Button createGroupButton;
    private Button resetButton;
    private EditText groupName;
    private EditText groupDesc;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        btnCreateGroupBack = (Button) findViewById(R.id.btnCreateGroupBack);
        btnCreateGroupBack.setOnClickListener(new View.OnClickListener()
                                              {
                                                  @Override
                                                  public void onClick(View v)
                                                  {
                                                      Intent createGroup = new Intent(CreateGroup.this,Dashboard.class);
                                                      startActivity(createGroup);
                                                  }
                                              }
        );

        userName = getIntent().getStringExtra(LOGIN_EMAIL);

        groupName = (EditText) findViewById(R.id.edGroupName);
        groupDesc = (EditText) findViewById(R.id.edGroupDesc);
        createGroupButton = (Button)findViewById(R.id.btnCreate);

        createGroupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // start MainActivity
                //freezeInteraction(true);
                createGroup(groupName.getText().toString(), groupDesc.getText().toString());

            }
        });

        resetButton = (Button)findViewById(R.id.btnReset) ;
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupName.setText("");
                groupDesc.setText("");
            }
        });
    }

    public static Intent newIntent(Context packageContext, String email)
    {
        Intent i = new Intent(packageContext, CreateGroup.class);
        i.putExtra(LOGIN_EMAIL, email);

        return i;
    }

    DatabaseReference dataRef;
    DatabaseReference dataRef1;
    DatabaseReference dataRef2;
    Group group;
    String tempGroupName;
    StringBuilder groupsOfUserWithCommaDeliminated = new StringBuilder();
    StringBuilder temp = new StringBuilder();
    Message msg;
    private void createGroup(final String groupName, String desc)
    {
        group = new Group(desc, msg);
        tempGroupName = groupName;
        dataRef = FirebaseDatabase.getInstance().getReference();

        dataRef.child("groups").child(groupName).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener()
        {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null)
                {
                    Toast.makeText(CreateGroup.this, "The group name has been used.", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    dataRef.child("groups").child(tempGroupName).setValue(group);
                    Toast.makeText(CreateGroup.this, "The group is successfully registered.", Toast.LENGTH_SHORT).show();

                    dataRef1 = FirebaseDatabase.getInstance().getReference();

                    dataRef1.child("users").child(userName).child("manage").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener()
                    {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null)
                            {
                                groupsOfUserWithCommaDeliminated.append(dataSnapshot.getValue());
                                Toast.makeText(CreateGroup.this, groupsOfUserWithCommaDeliminated.toString(), Toast.LENGTH_SHORT).show();
                                groupsOfUserWithCommaDeliminated.append(","+groupName);
                                dataRef.child("users").child(userName).child("manage").setValue(groupsOfUserWithCommaDeliminated.toString());
                            }
                            else
                            {
                                dataRef.child("users").child(userName).child("manage").setValue(groupName);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                    dataRef2 = FirebaseDatabase.getInstance().getReference();

                    dataRef2.child("users").child(userName).child("groups").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener()
                    {
                        @Override
                        public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null)
                            {
                                temp.append(dataSnapshot.getValue());
                                Toast.makeText(CreateGroup.this, temp.toString(), Toast.LENGTH_SHORT).show();
                                temp.append(","+groupName);
                                dataRef.child("users").child(userName).child("groups").setValue(temp.toString());
                            }
                            else
                            {
                                dataRef.child("users").child(userName).child("groups").setValue(groupName);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
}
