package com.example.asanka.geoapp10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Search extends AppCompatActivity {

    private Button btnSearchBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSearchBack = (Button) findViewById(R.id.btnSearchBack);
        btnSearchBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            Intent search = new Intent(Search.this,Dashboard.class);
            startActivity(search);
            }
           }
        );
    }
}
