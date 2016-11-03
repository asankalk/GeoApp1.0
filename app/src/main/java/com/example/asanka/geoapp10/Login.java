package com.example.asanka.geoapp10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Login extends AppCompatActivity {

    public static final String USERS_LOCATION="https://geoapp-8a315.firebaseio.com/users";

    //private Button btnLogin;
    private Button mLoginButton;
    private Button mRegisterUser;
   // private TextView mRegisterButton;
    private EditText mEmail;
    private EditText mPassword;
    private DatabaseReference mDatabase;

    private FirebaseApp app;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    private LinearLayout mLinearLayout;
    Firebase mRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        mLoginButton = (Button) findViewById(R.id.btnLogin);
        mRegisterUser = (Button) findViewById(R.id.btnRegister);

       // mRegisterButton = (TextView) findViewById(R.id.txtRegister);

        mEmail = (EditText) findViewById(R.id.etUserEmail);
        mPassword = (EditText) findViewById(R.id.etPassWord);

        //mRef = new Firebase("https://geomate-8a315.firebaseio.com/condition");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // start MainActivity
                freezeInteraction(true);
                verifyAccount(mEmail.getText().toString(), mPassword.getText().toString());

            }
        });

        mRegisterUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                freezeInteraction(true);

                writeNewUser(mEmail.getText().toString(), mPassword.getText().toString());
            };
        });



        /**mRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
        String text = dataSnapshot.getValue(String.class);
        mWelcomeMessage.setText(text);
        Toast.makeText(LoginActivity.this, "Changes",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
        });**/


    }
    private void freezeInteraction(boolean command)
    {
        if(command == true)
        {
            mLoginButton.setVisibility(View.GONE); //Or View.INVISIBLE to keep its bounds
            mEmail.setEnabled(false);
            mPassword.setEnabled(false);
        }
        else
        {
            mLoginButton.setVisibility(View.VISIBLE); //Or View.INVISIBLE to keep its bounds
            mEmail.setEnabled(true);
            mPassword.setEnabled(true);
        }

    }

    UserProfile user;
    String tempUserName;
    private void writeNewUser(String username, String password)
    {
        user = new UserProfile(username, password);
        tempUserName = username;
        // check if email exists
        Firebase userRef= new Firebase(USERS_LOCATION);
        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                freezeInteraction(false); // when receiving respond from database, unfreeze interaction

                if (snapshot.getValue() != null)
                {
                    Toast.makeText(Login.this, "User already exists", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Login.this, "The account is successfully registered.", Toast.LENGTH_SHORT).show();
                    mDatabase.child("users").child(tempUserName).setValue(user);
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {

            }
        });


    }

    boolean userNameIsCorrect = false;
    boolean passwordIsCorrect = false;
    private void verifyAccount(String username, String password)
    {
        user = new UserProfile(username, password);
        tempUserName = username;
        // check if email exists
        Firebase userRef= new Firebase(USERS_LOCATION);
        userRef.child(username).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if (snapshot.getValue() != null)  // user exists
                {
                    userNameIsCorrect = true;
                    freezeInteraction(false);
                }
                else  // user does not exists
                {
                    userNameIsCorrect = false;
                    Toast.makeText(Login.this, "User does not exists", Toast.LENGTH_SHORT).show();
                    freezeInteraction(false);
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {

            }
        });

        userRef.child(username).child(password).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if (snapshot.getValue() != null)  // password exists
                {
                    freezeInteraction(false);
                    passwordIsCorrect = true;
                    if(userNameIsCorrect&&passwordIsCorrect)
                        login();

                }
                else  // password does not exists
                {
                    passwordIsCorrect = false;
                    Toast.makeText(Login.this, "User does not exists", Toast.LENGTH_SHORT).show();
                    freezeInteraction(false);
                }
            }
            @Override
            public void onCancelled(FirebaseError arg0) {

            }
        });


    }
    private void login()
    {
        Intent i = Dashboard.newIntent(Login.this, mEmail.getText().toString(), mPassword.getText().toString());
        startActivity(i);
    }
}