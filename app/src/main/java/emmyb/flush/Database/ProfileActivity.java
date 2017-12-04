package emmyb.flush.Database;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.lang.*;

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
    public void postNewRating(final double latitude, final double longitude, final double rate){
        final DatabaseReference ref = mDatabase.child("Profiles");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Double> latt = new ArrayList<>();
                        ArrayList<Double> longg = new ArrayList<>();
                        ArrayList<Double> ratings = new ArrayList<>();
                        ArrayList<String> keys = new ArrayList<>();
                        String wantKey;
                        int n = 0;
                        Map<String,Object> profiles = (Map<String,Object>)dataSnapshot.getValue();

                        for(Map.Entry<String,Object> entry : profiles.entrySet()){
                            //Get a profile map
                            Map singlePlace = (Map) entry.getValue();

                            //get latitude and append to list
                            latt.add(Double.parseDouble(String.valueOf(singlePlace.get("latitude"))));
                            longg.add(Double.parseDouble(String.valueOf(singlePlace.get("longitude"))));
                            ratings.add(Double.parseDouble(String.valueOf(singlePlace.get("rating"))));
                            double getLatt = latt.get(n);
                            double getLongg = longg.get(n);
                            double rat = ratings.get(n);

                            if((Double.doubleToLongBits(getLatt) == Double.doubleToLongBits(latitude))&&((Double.doubleToLongBits(getLongg) == Double.doubleToLongBits(longitude)))){
                                double average = calcRating(rat,rate);
                                wantKey = entry.getKey();
                                ref.child(wantKey).child("rating").setValue(average);
                            }
                            n++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }

        );


    }

}
