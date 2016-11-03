package com.example.asanka.geoapp10;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements OnMapReadyCallback{

    private static final String LOGIN_EMAIL = "com.bignerdranch.android.myapplication.login_email";
    private static final String LOGIN_PASSWORD = "com.bignerdranch.android.myapplication.login_password";

    private String mLoginEmail;
    private String mLoginPassword;


    private static final int FIFTEEN_SECONDS = 1000 * 15;

    private ImageButton btnInbox;
    private ImageButton btnOptions;
    private ImageButton btnManage;
    private ImageButton btnSearchGroups;
    private ImageButton btnCreateGroup;
    private ImageButton btnViewGroup;

    private GoogleMap mMap;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    private LocationManager locationManager;
    private LocationListener locationListener;  // will listen to location changes
    private Location mLastLocation;
    private double lat;
    private double lon;

    ArrayList<locationPointMessage> mMessagePList;

    //private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        mLoginEmail = getIntent().getStringExtra(LOGIN_EMAIL);
        mLoginPassword = getIntent().getStringExtra(LOGIN_PASSWORD);

        mMessagePList = new ArrayList<>();

        //mFirebaseAuth = FirebaseAuth.getInstance();
        //mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        mFirebaseDatabaseReference.child("groups/Pokemon Fan Group/Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //dataSnapshot.getValue(locationPointMessage.class).getUser();
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    locationPointMessage mess = postSnapshot.getValue(locationPointMessage.class);
                    Log.e("Location", " " + mess.getLocation());
                    Log.e("Content", " " + mess.getContent());
                    Log.e("User", " " + mess.getSender());
                    mMessagePList.add(mess);

                }

                //Add markers to the map if not null for the particular group.
                String[] latlon;
                double lat;
                double lon;
                if(mMap != null){
                    for(locationPointMessage m :mMessagePList){
                        latlon = m.getLocation().split(",");
                        lat = Double.parseDouble(latlon[0]);
                        lon = Double.parseDouble(latlon[1]);

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(m.getContent()).snippet("User: "+ m.getSender()));
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
                if(mLastLocation == null){
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
                requestPermissions(new String []{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.INTERNET
                        },
                        10 // this is an integer that indicates the permission. It can be any integer
                );


                //return;
            }
            else{
                //configureButton();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }else{
            //configureButton();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //View Groups Screen

        btnViewGroup = (ImageButton) findViewById(R.id.btnViewGroup);
        btnViewGroup.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent viewGroups = new Intent(Dashboard.this, ViewGroup.class);
                                                startActivity(viewGroups);
                                            }
                                        }
        );


        //Create Group Screen

        btnCreateGroup = (ImageButton) findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent createGroups = new Intent(Dashboard.this, CreateGroup.class);
                                                  startActivity(createGroups);
                                              }
                                          }
        );


        //Search Groups Screen

        btnSearchGroups = (ImageButton) findViewById(R.id.btnSearchGroups);
        btnSearchGroups.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent searchGroups = new Intent(Dashboard.this, Search.class);
                                                   startActivity(searchGroups);
                                               }
                                           }
        );

        //Inbox Screeen

        btnInbox = (ImageButton) findViewById(R.id.btnInbox);
        btnInbox.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent inbox = new Intent(Dashboard.this, Inbox.class);
                                            startActivity(inbox);
                                        }
                                    }
        );


        //App Options screen

        btnOptions = (ImageButton) findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent options = new Intent(Dashboard.this, Options.class);
                                              startActivity(options);
                                          }
                                      }
        );

        //Manage Groups

        btnManage = (ImageButton) findViewById(R.id.btnManageGroups);
        btnManage.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent manageGroups = new Intent(Dashboard.this, Manage.class);
                                             startActivity(manageGroups);
                                         }
                                     }
        );
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(-31.9501, 115.8601)).title("Best food here!").snippet("User: MannieFresh"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-31.9500, 115.8605)).title("Check out this").snippet("User: MannieFresh"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-31.9520, 115.8620)).title("What to do in Perth").snippet("User: JoeBlogs"));

        String[] latlon;
        double lat;
        double lon;
        if(mMessagePList != null){
            for(locationPointMessage m :mMessagePList){
                latlon = m.getLocation().split(",");
                lat = Double.parseDouble(latlon[0]);
                lon = Double.parseDouble(latlon[1]);

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(m.getContent()).snippet("User: "+ m.getSender()));
            }
        }
    }


    public static Intent newIntent(Context packageCOntext, String email, String password)
    {
        Intent i = new Intent(packageCOntext, Dashboard.class);
        i.putExtra(LOGIN_EMAIL, email);
        i.putExtra(LOGIN_PASSWORD, password);


        return i;
    }

    // Now after requesting the permissions, we need to handle their results
    // DO this by implementing the method onRequestPermissionResult
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG);
        switch (requestCode){
            case 10: // the code we have previously entered
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    //configureButton();

                    return;
        }

    }


}
