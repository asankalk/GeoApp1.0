package com.example.asanka.geoapp10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewGroup extends AppCompatActivity {

    private Button btnViewGroupBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        btnViewGroupBack = (Button) findViewById(R.id.btnCreateGroupBack);
        btnViewGroupBack.setOnClickListener(new View.OnClickListener()
                                              {
                                                  @Override
                                                  public void onClick(View v)
                                                  {
                                                      Intent viewGroup = new Intent(ViewGroup.this,Dashboard.class);
                                                      startActivity(viewGroup);
                                                  }
                                              }
        );


    }
}
