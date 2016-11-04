package com.example.asanka.geoapp10;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class EditGroup extends AppCompatActivity {

    private static final String GROUP_ID = "com.bignerdranch.android.myapplication.group_identifier";
    public static final String GROUPS_LOCATION="https://geoapp-8a315.firebaseio.com/groups";

    private String mGroupId;
    private TextView mGroupName;
    private EditText mGroupDescription;
    private Button updateButton;
    private Button deleteButton;

    Firebase groupRef;
    String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        mGroupId = getIntent().getStringExtra(GROUP_ID);

        mGroupName = (TextView)findViewById(R.id.groupNameTextView);
        mGroupDescription = (EditText)findViewById(R.id.groupDescriptionEditText);

        // get description from database using GroupId

        mGroupName.setText(mGroupId);
        mGroupDescription.setText("Loading data from database....");
        getGroups(mGroupId);

        updateButton = (Button)findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDescription(mGroupId, mGroupDescription.getText().toString());
            }
        });

        deleteButton = (Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    public static Intent newIntent(Context packageContext, String groupId)
    {
        Intent i = new Intent(packageContext, EditGroup.class);
        i.putExtra(GROUP_ID, groupId);

        return i;
    }

    private void updateDescription(final String groupName, final String des)
    {
        groupRef = new Firebase(GROUPS_LOCATION);
        groupRef.child(groupName).child("Description").setValue(des);

    }

    private void getGroups(final String groupName)
    {
        groupRef = new Firebase(GROUPS_LOCATION);


        groupRef.child(groupName).child("Description").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if (snapshot.getValue() != null)  // the user is member of one or more groups
                {
                    group = (String)snapshot.getValue();
                    //separateStringAndPutThemIntoListOfString(groupsOfUserWithCommaDeliminated, groupIdentifier);
                    //adapter = new ArrayAdapter<String>(Manage.this, android.R.layout.simple_list_item_1, android.R.id.text1, groupIdentifier);
                    mGroupDescription.setText(group);

                    //Toast.makeText(EditGroup.this, "Description has been reloaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EditGroup.this, "System error", Toast.LENGTH_SHORT).show();
                    //freezeInteraction(false);
                    finish();
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {

            }
        });


    }
}
