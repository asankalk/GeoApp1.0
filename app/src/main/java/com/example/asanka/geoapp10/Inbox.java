package com.example.asanka.geoapp10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inbox extends AppCompatActivity {

    private Button btnInboxBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        btnInboxBack = (Button) findViewById(R.id.btnInboxBack);
        btnInboxBack.setOnClickListener(new View.OnClickListener()
        {
             @Override
             public void onClick(View v)
             {
                 Intent inBox = new Intent(Inbox.this,Dashboard.class);
                 startActivity(inBox);
             }
             }
        );
    }
}
