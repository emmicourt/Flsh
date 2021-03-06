package emmyb.flush.Maps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import emmyb.flush.Database.ProfileActivity;
import emmyb.flush.IntialScreen;
import emmyb.flush.ProfilePage;
import emmyb.flush.R;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        OnMapLongClickListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMarkerClickListener {

        public static double currentLatitude;
        public static double currentLongitude;

        private static final String TAG = MapsActivity.class.getSimpleName();
        private GoogleMap mMap;
        private CameraPosition mCameraPosition;
        private FirebaseAuth firebaseAuth;

        private ChildEventListener mChildEventListener;
        private DatabaseReference mProfileRef = FirebaseDatabase.getInstance()
                .getReference("Profiles");

        ProfileActivity mProfileActivity = new ProfileActivity();
        ProfilePage mProfilePage = new ProfilePage();

        // The entry points to the Places API.
        private GeoDataClient mGeoDataClient;
        private PlaceDetectionClient mPlaceDetectionClient;

        // The entry point to the Fused Location Provider.
        private FusedLocationProviderClient mFusedLocationProviderClient;

        // A default location (Sydney, Australia) and default zoom to use when location permission is
        // not granted.
        private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
        private static final int DEFAULT_ZOOM = 15;
        private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        private boolean mLocationPermissionGranted;

        // The geographical location where the device is currently located. That is, the last-known
        // location retrieved by the Fused Location Provider.
        private Location mLastKnownLocation;

        // Keys for storing activity state.
        private static final String KEY_CAMERA_POSITION = "camera_position";
        private static final String KEY_LOCATION = "location";

        // flag for add button
        boolean addClickFlag = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Retrieve location and camera position from saved instance state.
            if (savedInstanceState != null) {
                mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
                mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            }

            firebaseAuth = FirebaseAuth.getInstance();

            // Retrieve the content view that renders the map.
            setContentView(R.layout.activity_maps);

            // Construct a GeoDataClient.
            mGeoDataClient = Places.getGeoDataClient(this, null);

            // Construct a PlaceDetectionClient.
            mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

            // Construct a FusedLocationProviderClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            // Build the map.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        /**
         * Saves the state of the map when the activity is paused.
         */
        @Override
        protected void onSaveInstanceState(Bundle outState) {
            if (mMap != null) {
                outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
                outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
                super.onSaveInstanceState(outState);
            }
        }

        /**
         * Sets up the options menu.
         * @param menu The options menu.
         * @return Boolean.
         */
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.current_place_menu, menu);
            return true;
        }

        /**
         * Handles a click on the Restroom add button, enables the Map Long Click Listener.
         * Handles a click on the logout button, signs the user out
         * @param item The menu item to handle.
         * @return Boolean.
         */
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.add) {
                if(addClickFlag){
                    mMap.setOnMapLongClickListener(null);
                    addClickFlag = false;
                }
                if(!addClickFlag) {
                    mMap.setOnMapLongClickListener(this);
                }
            }
            if (item.getItemId() == R.id.logout) {
                Intent maps = new Intent(this, IntialScreen.class);
                signOut();
                startActivity(maps);
            }
            return true;
        }

        /**
         * Manipulates the map when it's available.
         * This callback is triggered when the map is ready to be used.
         */
        @Override
        public void onMapReady(GoogleMap map) {
            mMap = map;

            mMap.setOnCameraIdleListener(this);
            mMap.setOnMarkerClickListener(this);
            
            // Prompt the user for permission.
            getLocationPermission();

            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();

            // Add markers to map
            addMarkersToMap(mMap);
        }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * When user long clicks, add a marker to map
     * @param position - latitude and longitude coordinates
     */
    public void onMapLongClick(LatLng position){
        if(!addClickFlag) {
            mMap.addMarker(new MarkerOptions().position(position));
            double latitudeDec = position.latitude;
            double longitudeDec = position.longitude;
            mProfileActivity.newProfile(latitudeDec, longitudeDec);
            addClickFlag = true;
        }
    }

    /**
     * addMarkersToMap()
     * queries the database for all surrounding bathrooms and adds markers to the
     * map at those postions
     * @param map - this map
     */
    private void addMarkersToMap(final GoogleMap map){
        mProfileRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Double> latt = new ArrayList<>();
                        ArrayList<Double> longg = new ArrayList<>();
                        Map<String,Object> profiles = (Map<String,Object>)dataSnapshot.getValue();
                        int n = 0;
                        for(Map.Entry<String,Object> entry : profiles.entrySet()){
                            //Get a profile map
                            Map singlePlace = (Map) entry.getValue();

                            //get latitude and append to list
                            latt.add(Double.parseDouble(String.valueOf(singlePlace.get("latitude"))));
                            longg.add(Double.parseDouble(String.valueOf(singlePlace.get("longitude"))));
                            LatLng pos = new LatLng(latt.get(n), longg.get(n));
                            map.addMarker(new MarkerOptions().position(pos));
                            n++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }

        );

    }

    /**
     * isInBound()
     * supposed to check to see if a given latitude and longitude are withing the bounds of the
     * current map view
     * @param latt - latitude
     * @param longg - longitude
     * @return - returns LatLng
     */
    private boolean isInBound(double latt, double longg) {
        LatLng currentPosition = new LatLng(latt, longg);
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        return  bounds.contains(currentPosition);
    }

    /**
     * signOut
     * firebase authentication API method to sign out
     */
    private void signOut(){
            FirebaseAuth.getInstance().signOut();
        }

    /**
     * onCameraIdle
     * checks when the user stops moving then map, then updates the markers in the area
     * by calling add markers to map again
      */
    @Override
    public void onCameraIdle() {
        addMarkersToMap(mMap);
    }

    /**
     * onMarkerClick()
     * If a marker is clicked, the position is recorded into current lat and long
     * then the user is redirected to
     * @param marker - gets marker from map
     * @return - boolean if the aforementioned thing is
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent profilePage = new Intent(this, ProfilePage.class);
        LatLng position = marker.getPosition();
        startActivity(profilePage);
        currentLatitude = position.latitude;
        currentLongitude = position.longitude;
        return false;
    }
}

