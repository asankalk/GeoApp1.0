package com.example.asanka.geoapp10;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOGIN_EMAIL = "com.bignerdranch.android.myapplication.login_email";
    private static final String LOGIN_PASSWORD = "com.bignerdranch.android.myapplication.login_password";

    private String mLoginEmail;
    private String mLoginPassword;
    private GoogleMap mMap;

    private ImageButton mInboxMessageButton;
    private ImageButton mOptionButton;
    private ImageButton mManageGroupButton;
    private ImageButton mSearchGroupButton;
    private ImageButton mCreateGroupButton;
    private ImageButton mMyGroupsButton;
    private DatabaseReference mFirebaseDatabaseReference;

    ArrayList<Message> mMessagePList;

    protected LocationManager locationManager;
    private LocationListener locationListener;  // will listen to location changes
    private Location mLastLocation;
    private double lat;
    private double lon;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // to send location to inbox
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mMessagePList = new ArrayList<>();

        //mFirebaseAuth = FirebaseAuth.getInstance();
        //mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseDatabaseReference.child("groups/Pokemon Fan Group/Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getValue(locationPointMessage.class).getUser();
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Message mess = postSnapshot.getValue(Message.class);
                    Log.e("Location", " " + mess.getLocation());
                    Log.e("Content", " " + mess.getContent());
                    Log.e("User", " " + mess.getSender());
                    mMessagePList.add(mess);

                }

                //Add markers to the map if not null for the particular group.
                String[] latlon;
                double lat;
                double lon;
                if (mMap != null) {
                    for (Message m : mMessagePList) {
                        latlon = m.getLocation().split(",");
                        lat = Double.parseDouble(latlon[0]);
                        lon = Double.parseDouble(latlon[1]);

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(m.getContent()).snippet("User: " + m.getSender()));
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // This method is called whenever the location is updated
                if (mLastLocation == null) {
                    mLastLocation = new Location("service Provider");
                }

                lat = location.getLatitude();
                lon = location.getLongitude();

                LatLng currLoc = new LatLng(lat, lon);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currLoc)              //Change to current location.
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(60)                   // Sets the tilt of the camera to 60 degrees
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mLastLocation.setLatitude(lat);
                mLastLocation.setLongitude(lon);

                Log.i("locationTest", mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                // this one checks if the GPS is turned off
                // Here write an Intent which will take the user to
                // the settings panel where he can enable GPS settings
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // M for Mashmallow version or later

            //configureButton();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                // Remember we had 3 user permissions from the manifest.
                // We must include them here
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.INTERNET
                        },
                        10 // this is an integer that indicates the permission. It can be any integer
                );


                //return;
            } else {
                //configureButton();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            //configureButton();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLoginEmail = getIntent().getStringExtra(LOGIN_EMAIL);
        mLoginPassword = getIntent().getStringExtra(LOGIN_PASSWORD);

        mInboxMessageButton = (ImageButton) findViewById(R.id.btnInbox);
        mCreateGroupButton = (ImageButton) findViewById(R.id.btnCreateGroup);
        mMyGroupsButton = (ImageButton) findViewById(R.id.btnViewGroup);
        mSearchGroupButton = (ImageButton) findViewById(R.id.btnSearchGroups);
        mManageGroupButton = (ImageButton) findViewById(R.id.btnManageGroups);

        mInboxMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start InboxMessageActivity
                Intent i = Inbox.newIntent(Dashboard.this, mLoginEmail, mLoginPassword, lat, lon);
                startActivity(i);
            }
        });

        mCreateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start CreateGroupActivity
                Intent i = CreateGroup.newIntent(Dashboard.this, mLoginEmail);
                startActivity(i);
            }
        });

        mMyGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start MyGroupActivity
                Intent i = ViewGroup.newIntent(Dashboard.this, mLoginEmail);
                startActivity(i);
            }
        });

        mSearchGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start SearchGroupActivity
                Intent i = Search.newIntent(Dashboard.this, mLoginEmail);
                startActivity(i);

            }
        });

        mManageGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start ManageGroupActivity
                Intent i = Manage.newIntent(Dashboard.this, mLoginEmail, mLoginPassword);
                startActivity(i);

            }
        });

        //txtLat = (TextView) findViewById(R.id.textview1);

        /**locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

         if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         // TODO: Consider calling
         //    ActivityCompat#requestPermissions
         // here to request the missing permissions, and then overriding
         //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
         //                                          int[] grantResults)
         // to handle the case where the user grants the permission. See the documentation
         // for ActivityCompat#requestPermissions for more details.
         return;
         }
         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);**/

    }

    // to send location to inbox

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    // to send location to inbox

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public static Intent newIntent(Context packageCOntext, String email, String password) {
        Intent i = new Intent(packageCOntext, Dashboard.class);
        i.putExtra(LOGIN_EMAIL, email);
        i.putExtra(LOGIN_PASSWORD, password);


        return i;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        //googleMap.setMapStyle(style);
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.setMinZoomPreference(16.0f);
        //mMap.setMaxZoomPreference(18.0f);

        //retrieve data from database and load the map with the retrieved data

        LatLng perth = new LatLng(-31.9505, 115.8605);
        mMap.addMarker(new MarkerOptions().position(new LatLng(-31.9505, 115.8605)).title("Title of message").snippet("User: MannieFresh"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(perth));
        mMap.setBuildingsEnabled(true);

        fillMapWithMarkers(1f, 1f);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(perth)              //Change to current location.
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(60)                   // Sets the tilt of the camera to 60 degrees
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void fillMapWithMarkers(double currLng, double currLat) {
        //Get messages from database based on users current location.
        //maximum of 20 messages. (just for now)
        //mMap.addMarker(new MarkerOptions().position(new LatLng(-31.9501, 115.8601)).title("Best food here!").snippet("User: MannieFresh"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(-31.9500, 115.8605)).title("Check out this").snippet("User: MannieFresh"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(-31.9520, 115.8620)).title("What to do in Perth").snippet("User: JoeBlogs"));

        String[] latlon;
        double lat;
        double lon;
        if (mMessagePList != null) {
            for (Message m : mMessagePList) {
                latlon = m.getLocation().split(",");
                lat = Double.parseDouble(latlon[0]);
                lon = Double.parseDouble(latlon[1]);

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(m.getContent()).snippet("User: " + m.getSender()));
            }
        }
    }

    // to send location to inbox

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
        }
    }
    // to send location to inbox

    @Override
    public void onConnectionSuspended(int i) {

    }
    // to send location to inbox

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
