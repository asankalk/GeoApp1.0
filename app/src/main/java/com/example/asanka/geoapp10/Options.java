package com.example.asanka.geoapp10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Options extends AppCompatActivity {

    private Button btnOptionsBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        btnOptionsBack = (Button) findViewById(R.id.btnOptionsBack);
        btnOptionsBack.setOnClickListener(new View.OnClickListener()
        {
             @Override
             public void onClick(View v)
             {
                 Intent options = new Intent(Options.this,Dashboard.class);
                 startActivity(options);
             }
        }
        );

    }
}
