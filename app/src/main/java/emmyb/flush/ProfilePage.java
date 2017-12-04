package emmyb.flush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.view.View.OnClickListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import android.widget.Toast;


import emmyb.flush.Database.ProfileActivity;
import emmyb.flush.Maps.MapsActivity;

public class ProfilePage extends AppCompatActivity {

    ProfileActivity a = new ProfileActivity();
    private RatingBar ratingBar;
    private Button addRating;
    RatingBar displayRating;
    private DatabaseReference mDatabase  =  FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        displayRating = findViewById(R.id.OverallRating);
        addListenerOnRatingBar();
        addListenerOnButton();
        existingRating(MapsActivity.currentLatitude, MapsActivity.currentLongitude);
      //  updateRating(MapsActivity.currentLatitude, MapsActivity.currentLongitude, ratingBar.getRating() );

    }


    public void existingRating(double latitude, double longitude){
        System.out.println();
        getRatingFromDatabase(latitude,longitude);

    }
    // queries the database for a profile based
    // then creates and returns a profile object
    public void getRatingFromDatabase(final double latitude, final double longitude){
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

                            if((Double.doubleToLongBits(getLatt) == Double.doubleToLongBits(latitude))&&((Double.doubleToLongBits(getLongg) == Double.doubleToLongBits(longitude)))){
                                //System.out.println("trueee though");
                                final float f = (float)rat;
                                //System.out.println("hiiiiiiiii"+f+"whattttt");
                                displayRating.setRating(f);
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
    public void updateRating(double latitude, double longitude, float y){
        double w = y;
        // lat, long, flout->double
        a.postNewRating(latitude, longitude, w);

    }

    public void addListenerOnRatingBar() {
        ratingBar = (RatingBar)findViewById(R.id.UserRating);

        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final int numStars = ratingBar.getNumStars();
                ratingBar.getRating() ;
                final float ratingBarStepSize = ratingBar.getStepSize();
            }
        });
    }

    public void addListenerOnButton() {
        ratingBar = (RatingBar)findViewById(R.id.UserRating);
        addRating = (Button)findViewById(R.id.AddRating);

        addRating.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfilePage.this, "Thanks for the Rating", Toast.LENGTH_SHORT).show();
                updateRating(MapsActivity.currentLatitude, MapsActivity.currentLongitude, ratingBar.getRating());
                finish();
                startActivity(getIntent());
            }
        });
    }

}
