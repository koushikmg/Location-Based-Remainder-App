package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> ,View.OnClickListener {

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    private long backPressedTime;
    Toast backToast;


    private Button submit;
    private EditText location;
    private EditText itemName;
    String userID;
    Geocoder geocoder;



    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference reference;

    // creating a variable for
    // our object class
    ExampleItem exampleItem;

    //Getting user id from firebase auth
    FirebaseAuth firebaseAuth;

    LatLng latLng;
    Address address;

    private ArrayList<ExampleItem> exampleItems;
    protected ArrayList<Geofence> mGeofenceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        submit = findViewById(R.id.submit);
        location = findViewById(R.id.location);
        itemName = findViewById(R.id.itemname);
        firebaseAuth = FirebaseAuth.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        reference = firebaseDatabase.getInstance().getReference(userID);

        exampleItem = new ExampleItem();
        exampleItems = new ArrayList<>();
        mGeofenceList = new ArrayList<>();

        getAllItems();



        submit.setOnClickListener(this);


    }


    private void getAllItems() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ExampleItem item = dataSnapshot.getValue(ExampleItem.class);
                    exampleItems.add(item);

                }

                setUpMapIfNeeded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void setUpMapIfNeeded()  {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            if (mMap != null) {
                setUpMap();
            }
        }

        for (int i=0;i<exampleItems.size();i++){
            Log.d("hey",exampleItems.get(i).getItem() + "\t" + exampleItems.get(i).getPlace());


            List<Address> addressList = null;
            if (exampleItems.get(i).getPlace() != null || !exampleItems.get(i).getPlace().equals("")) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(exampleItems.get(i).getPlace(), 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                address = addressList.get(0);
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,12 );
                mMap.moveCamera(update);
                mMap.addMarker(new MarkerOptions().position(latLng).title(exampleItems.get(i).getItem()));
                Constants.LANDMARKS.put(exampleItems.get(i).getItemId(),latLng);
            }
        }
    }

    private void setUpMap() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onResult( Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {

        }

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    private void addDatatoFirebase(String place, String item, Double latitude, Double longitude) {
        String uuid = place+"-"+item;
        exampleItem.setItem(item);
        exampleItem.setPlace(place);
        exampleItem.setItemId(uuid);
        exampleItem.setLatitude(Double.toString(latitude));
        exampleItem.setLongitude(Double.toString(longitude));
        reference.child(uuid).setValue(exampleItem);

    }

    @Override
    public void onClick(View v) {

        List<Address> addressList = null;
        geocoder= new Geocoder(this);

        String place = location.getText().toString();
        String item = itemName.getText().toString();




        if (place == null || item == null || TextUtils.isEmpty(place) || TextUtils.isEmpty(item)) {
            // if the text fields are empty
            // then show the below message.
            Toast.makeText(AddItemActivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
        } else {
            //getting latitude and longitude using geocoder
            try {
                addressList = geocoder.getFromLocationName(place,1);

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Unable to get Geo Cordinates",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(place +" , "+item));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


            // data to our database.

            addDatatoFirebase(place, item, latLng.latitude,latLng.longitude);

            Constants.LANDMARKS.put(place+"-"+item,latLng);

            populateGeofenceList();
            addGeofencesButtonHandler();

            Intent intent = new Intent(AddItemActivity.this,RemainderActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            finishAffinity();
        }
        else
        {
            backToast = Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    private void populateGeofenceList() {
        System.out.println("ConstantsCount : "+ Constants.LANDMARKS.size());
        for (Map.Entry<String, LatLng> entry : Constants.LANDMARKS.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }

    }


    public void addGeofencesButtonHandler() {

        for (int i=0;i<mGeofenceList.size();i++)
            Log.d("TAG1", "addGeofencesButtonHandler: "+mGeofenceList.get(i));
        if (!mGoogleApiClient.isConnected()) {

            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().

        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }



    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }


}