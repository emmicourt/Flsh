package emmyb.flush.Database;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import emmyb.flush.Profiles.Profile;
import emmyb.flush.R;

/**
 * Created by EmmyB on 11/14/17.
 */

public class ProfileActivity extends FragmentActivity {

    private static final String TAG = "ProfileActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;
    private DatabaseReference mProfileReference;
    private ValueEventListener mProfileListener;
    private Profile newProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState  ){
        super.onCreate(savedInstanceState);
        //setContentView();

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
                // [START_EXCLUDE]

                // [END_EXCLUDE]
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


    // make new profile
    public void newProfile (String latitiude, String longitude) {
        newProfile = new Profile(latitiude, longitude);

        DatabaseReference myRef = mDatabase.child("Profiles").child(newProfile.getUUID()).push();
        Map<String, Object> profileValues = newProfile.toMap();
        myRef.setValue(profileValues);
    }


    public void setEditingEnabled(boolean editingEnabled) {
        //this.editingEnabled = editingEnabled;
    }

    @Override
    public void onStop() {
        super.onStop();


        if (mProfileListener != null) {
            mProfileReference.removeEventListener(mProfileListener);
        }
    }

    /**
    public void postNewRating(String uuid, int rate){
        setEditingEnabled(false);
        Profile aProfile;

        aProfile = new Profile(uuid);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = mDatabase.child("Profiles").child(uuid).child("rating")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get profile information
                        Profile profile = dataSnapshot.getValue(Profile.class);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        int newValue = (int) aProfile.calcRating(aProfile.getRating(), rate);
        aProfile.setRating(newValue);
        aProfile.addUser(userId);
        mDatabase.child("Profiles").child(aProfile.getUUID()).child("users").setValue(aProfile.getUsers());

        myRef.setValue(newValue);

    }
     */

}