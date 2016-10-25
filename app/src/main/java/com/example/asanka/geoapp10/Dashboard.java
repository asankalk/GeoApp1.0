package com.example.asanka.geoapp10;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Dashboard extends AppCompatActivity {

    private ImageButton btnInbox;
    private ImageButton btnOptions;
    private ImageButton btnManage;
    private ImageButton btnSearchGroups;
    private ImageButton btnCreateGroup;
    private ImageButton btnViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //View Groups Screen

        btnViewGroup = (ImageButton) findViewById(R.id.btnViewGroup);
        btnViewGroup.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
              Intent viewGroups = new Intent(Dashboard.this,ViewGroup.class);
              startActivity(viewGroups);
           }
        }
        );


        //Create Group Screen

        btnCreateGroup = (ImageButton) findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener()

        {
           @Override
           public void onClick(View v)
           {
              Intent createGroups = new Intent(Dashboard.this,CreateGroup.class);
              startActivity(createGroups);
           }
        }
        );


        //Search Groups Screen

        btnSearchGroups = (ImageButton) findViewById(R.id.btnSearchGroups);
        btnSearchGroups.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               Intent searchGroups = new Intent(Dashboard.this,Search.class);
               startActivity(searchGroups);
           }
        }
        );

        //Inbox Screeen

        btnInbox = (ImageButton) findViewById(R.id.btnInbox);
        btnInbox.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               Intent inbox = new Intent(Dashboard.this,Inbox.class);
               startActivity(inbox);
           }
        }
        );


        //App Options screen

        btnOptions = (ImageButton) findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
                Intent options = new Intent(Dashboard.this,Options.class);
                startActivity(options);
           }
        }
        );

        //Manage Groups

        btnManage = (ImageButton) findViewById(R.id.btnManageGroups);
        btnManage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                   Intent manageGroups = new Intent(Dashboard.this,Manage.class);
                   startActivity(manageGroups);
            }
        }
        );


    }
}
