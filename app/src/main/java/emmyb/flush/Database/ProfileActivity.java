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
    private double rating;

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




    public double calcRating(double oldRating, double newRate){
        return (oldRating + newRate) / 2;
    }

    // gets an existing profile from the database and takes a new rating from the user
    // if the user has not already rated this particular profile then it takes an
    // average of the existing and updates the value in firebsae
    public void postNewRating(final double latitude, final double longitude,final double rate){
        final DatabaseReference ref = mDatabase.child("Profiles");
        //double oldrating = getRatingFromDatabase(latitude, longitude);


        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Double> latt = new ArrayList<>();
                        ArrayList<Double> longg = new ArrayList<>();
                        ArrayList<Double> ratings = new ArrayList<>();
                        ArrayList<String> keys = new ArrayList<>();
                        //double latt = 0;
                        //double longg = 0;
                        String wantKey;
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
                            //keys.add(String.valueOf(singlePlace()));
                            double getLatt = latt.get(n);
                            double getLongg = longg.get(n);
                            double rat = ratings.get(n);


                            if((Double.doubleToLongBits(getLatt) == Double.doubleToLongBits(latitude))&&((Double.doubleToLongBits(getLongg) == Double.doubleToLongBits(longitude)))){
                                //System.out.println("trueee though");
                              //  final float f = (float)rat;
                                double average = calcRating(rat,rate);
                                System.out.println("ASDFSHSGHDF" + rate);
                                wantKey = entry.getKey();
                                ref.child(wantKey).child("rating").setValue(average);
                                System.out.println("@@@@@@@@@@@@@@@@HIII"+average+"hiiii"+wantKey);
                                //String wantKey = entry.getKey().toString();
                               // String wantKey = (String.valueOf(singlePlace.get("latitude"))).getParent();
                                //wantKey = (String.valueOf(singlePlace.get("longitude")).getKey());
                                //System.out.println("YOOO" +wantKey+ "YODF");
                               // wantKey.child(wantKey).setValue(average);
                               // displayRating.setRating(f);
                            }
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


    }

}
