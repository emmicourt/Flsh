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
 */

public class ProfileActivity extends FragmentActivity {

    private static final String TAG = "ProfileActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase  =  FirebaseDatabase.getInstance().getReference();
    private Profile newProfile;
    private double rating = 90.0;

    @Override
    protected void onCreate(Bundle savedInstanceState  ){
        super.onCreate(savedInstanceState);

    }


    // make new profile and adds it to firebase
    public void newProfile (double latitiude, double longitude) {
        newProfile = new Profile(latitiude, longitude);

        DatabaseReference myRef = mDatabase.child("Profiles");
        myRef.push().setValue(newProfile);
    }


    // queries the database for a profile based
    // then creates and returns a profile object
    public double getRatingFromDatabase(final double latitude, final double longitude){
        final DatabaseReference ref = mDatabase.child("Profiles");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Double> latt = new ArrayList<>();
                        ArrayList<Double> longg = new ArrayList<>();
                        ArrayList<Double> ratings = new ArrayList<>();
                        //double latt = 0;
                        //double longg = 0;
                        int n = 0;
                        Map<String,Object> profiles = (Map<String,Object>)dataSnapshot.getValue();

                        for(Map.Entry<String,Object> entry : profiles.entrySet()){
                            //Get a profile map
                            Map singlePlace = (Map) entry.getValue();
                            //get latitude and append to list
                            //String a = (String)singlePlace.get("latitude");
                            latt.add(Double.parseDouble(String.valueOf(singlePlace.get("latitude"))));
                            longg.add(Double.parseDouble(String.valueOf(singlePlace.get("longitude"))));
                            ratings.add(Double.parseDouble(String.valueOf(singlePlace.get("rating"))));
                            System.out.print("heyyyyyyyyy!!!!!!!!!!!!");
                            double getLatt = latt.get(n);
                            double getLongg = longg.get(n);
                            double rat = ratings.get(n);
                            rating = ratings.get(n);
                            //int longgCheck = Double.compareTo(longg.get(n),longitude);
                            //if (getLatt.equals(latitude)&&getLongg.equals(longitude)){
                                System.out.println(">>>>>>>"+rating + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+n);
                              //  rating =  (Double.parseDouble(String.valueOf(singlePlace.get("rating"))));
                            //}
                            n++;

                        }
                        //double latt = collectLatt((Map<String,Object>)dataSnapshot.getValue());
                        //collectLongg((Map<String,Object>)dataSnapshot.getValue()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );

        /*ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double lati;
                double longg;
                for (DataSnapshot profileSnapshot: dataSnapshot.getChildren()) {
                    lati = (Double) profileSnapshot.child("latitude").getValue();
                    longg = (Double) profileSnapshot.child("longitude").getValue();

                    if (lati == latitude && longg == longitude){
                        rating =  (Double)profileSnapshot.child("rating").getValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });*/
        System.out.println("Jesssica is "+rating+"super >>>");
        return rating;
    }

    public double calcRating(double oldRating, double newRate){
        return (oldRating + newRate) / 2;
    }

    // gets an existing profile from the database and takes a new rating from the user
    // if the user has not already rated this particular profile then it takes an
    // average of the existing and updates the value in firebsae
    public void postNewRating(final double latitude, final double longitude, double rate){
        final DatabaseReference ref = mDatabase.child("Profiles");
        double oldrating = getRatingFromDatabase(latitude, longitude);

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
