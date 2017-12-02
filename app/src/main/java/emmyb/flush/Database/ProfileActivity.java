package emmyb.flush.Database;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import emmyb.flush.Maps.MapsActivity;
import emmyb.flush.Profiles.Profile;

/**
 * Created by EmmyB on 11/14/17.
 */

public class ProfileActivity extends MapsActivity {

    private static final String TAG = "ProfileActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
    private DatabaseReference mProfileReference;
    private ValueEventListener mProfileListener;
    private Profile newProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState  ){
        super.onCreate(savedInstanceState);
        //if()setContentView();

        //initialize database
        mDatabase =  FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart(){
        super.onStart();
        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener profileListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
               Profile profile = dataSnapshot.getValue(Profile.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(ProfileActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mProfileReference.addValueEventListener(profileListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mProfileListener = profileListener;

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mProfileListener != null) {
            mProfileReference.removeEventListener(mProfileListener);
        }
    }


    // make new profile and adds it to firebase
    public void newProfile (String latitiude, String longitude) {
        newProfile = new Profile(latitiude, longitude);

        DatabaseReference myRef = mDatabase.child("Profiles").child(newProfile.getUUID()).push();
        Map<String, Object> profileValues = newProfile.toMap();
        myRef.setValue(profileValues);
    }

    // queries the database for a profile based on uuid
    // then creates and returns a profile object
    public Profile getProfileFromDatabase(final String uuid){
        final Profile aProfile = new Profile(uuid);
        final DatabaseReference ref = mDatabase.child("Profiles").child(uuid);
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    aProfile.setRating((double)messageSnapshot.child("rating").getValue());
                    aProfile.setUsers((List<String>) messageSnapshot.child("users").getValue());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return aProfile;
    }

    // gets an existing profile from the database and takes a new rating from the user
    // if the user has not already rated this particular profile then it takes an
    // average of the existing and updates the value in firebsae
    public void postNewRating(String uuid, int rate){
        Profile aProfile = getProfileFromDatabase(uuid);
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        int newValue = (int) aProfile.calcRating(aProfile.getRating(), rate);
        aProfile.setRating(newValue);
        aProfile.addUser(userId);
        mDatabase.child("Profiles").child(aProfile.getUUID()).child("users").setValue(aProfile.getUsers());

        DatabaseReference ref = mDatabase.child("Profiles").child(uuid).child("rates");
        ref.setValue(newValue);
    }

}
