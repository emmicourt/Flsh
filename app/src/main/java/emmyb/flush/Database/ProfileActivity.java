package emmyb.flush.Database;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

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

public class ProfileActivity extends FragmentActivity {

    private static final String TAG = "ProfileActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase  =  FirebaseDatabase.getInstance().getReference();
    private Profile newProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState  ){
        super.onCreate(savedInstanceState);

    }


    // make new profile and adds it to firebase
    public void newProfile (String latitiude, String longitude) {
        newProfile = new Profile(latitiude, longitude);

        DatabaseReference myRef = mDatabase.child("Profiles");
        myRef.push().setValue(newProfile);
    }

    // queries the database for a profile based on uuid
    // then creates and returns a profile object
    public Profile getProfileFromDatabase(String latitude, String longitude){
        final Profile aProfile = new Profile(latitude, longitude);
        final DatabaseReference ref = mDatabase.child("Profiles").child(aProfile.getUUID());
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
    public void postNewRating(String latitude, String longitude, int rate){
        Profile aProfile = getProfileFromDatabase(latitude, longitude);
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        int newValue = (int) aProfile.calcRating(aProfile.getRating(), rate);
        aProfile.setRating(newValue);
        aProfile.addUser(userId);
        mDatabase.child("Profiles").child(aProfile.getUUID()).child("users").setValue(aProfile.getUsers());

        DatabaseReference ref = mDatabase.child("Profiles").child(aProfile.getUUID()).child("rates");
        ref.setValue(newValue);
    }

}
