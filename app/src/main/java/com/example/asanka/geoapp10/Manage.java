package com.example.asanka.geoapp10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Manage extends AppCompatActivity {

    private Button btnManageGroupBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        btnManageGroupBack = (Button) findViewById(R.id.btnManageGroupBack);
        btnManageGroupBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent manage = new Intent(Manage.this,Dashboard.class);
                startActivity(manage);
            }
        }
        );
    }
}
