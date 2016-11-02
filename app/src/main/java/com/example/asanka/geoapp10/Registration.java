package com.example.asanka.geoapp10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registration extends AppCompatActivity {

    private EditText txtName;
    private EditText txtEmail;
    private EditText txtConfirmEmail;
    private EditText txtPassword;
    private  EditText txtPwConfirm;
    private Button btnRegister;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtConfirmEmail = (EditText) findViewById(R.id.txtConfirmEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPwConfirm = (EditText) findViewById(R.id.txtPwConfirm);
        txtName = (EditText) findViewById(R.id.txtRegName);




    }

    private void userRegistration()
    {
        String name = txtName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String confirmEmail = txtConfirmEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String confirmPass = txtPwConfirm.getText().toString().trim();


        if(TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(Registration.this,"Please enter Email address",Toast.LENGTH_SHORT);
            return;
        }


        if(TextUtils.isEmpty(confirmEmail))
        {
            Toast.makeText(Registration.this,"Please check both email addreseses",Toast.LENGTH_SHORT);
            return;

        }

        if(TextUtils.isEmpty(password))
        {
            //passwrod empty

            Toast.makeText(Registration.this,"Please enter password",Toast.LENGTH_SHORT);
            return;
        }

        if(TextUtils.isEmpty(confirmPass))
        {
            //passwrod empty

            Toast.makeText(Registration.this,"Please check both passwords",Toast.LENGTH_SHORT);
            return;
        }


    }
}
