package emmyb.flush.Database;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.*;
import emmyb.flush.Maps.MapsActivity;
import emmyb.flush.Profiles.Profile;

/**
 * Created by EmmyB on 11/14/17.
 * This class is responsible for database functions related to Profiles
 */

public class ProfileActivity extends FragmentActivity {

    private static final String TAG = "ProfileActivity";
    private static final String REQUIRED = "Required";
    private DatabaseReference mDatabase  =  FirebaseDatabase.getInstance().getReference();
    private Profile newProfile;
    private double rating;

    /**
     * required function - does nothing in this case
     * @param savedInstanceState - saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState  ){
        super.onCreate(savedInstanceState);

    }

    /**
     * make new profile and adds it to firebase
     * @param latitiude - this latitue
     * @param longitude - this long
     */
    public void newProfile (double latitiude, double longitude) {
        newProfile = new Profile(latitiude, longitude);

        DatabaseReference myRef = mDatabase.child("Profiles");
        myRef.push().setValue(newProfile);
    }


    /**
     * averages two
     * @param oldRating - the rating stored in the database
     * @param newRate - rating stored in the database
     * @return - result of averaging these two pieces
     */
    public double calcRating(double oldRating, double newRate){
        return (oldRating + newRate) / 2;
    }

    /**
     * gets an existing profile from the database and takes a new rating from the user
     * average of the existing and updates the value in firebsae
     * @param latitude - this latitude
     * @param longitude - this longitude
     * @param rate - this rating
     */
    public void postNewRating(final double latitude, final double longitude, double rate){
        final DatabaseReference ref = mDatabase.child("Profiles");
        //double oldrating = getRatingFromDatabase(latitude, longitude);
        double oldrating = 0;
        final double rating = calcRating(oldrating, rate);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double lati;
                double longg;
                for (DataSnapshot profileSnapshot: dataSnapshot.getChildren()) {
                    lati = (Double) profileSnapshot.child("latitude").getValue();
                    longg = (Double) profileSnapshot.child("longitude").getValue();

                    if (lati == latitude && longg == longitude){
                        String key = profileSnapshot.getKey();

                        ref.child(key).child("rating").setValue(rating);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

}
