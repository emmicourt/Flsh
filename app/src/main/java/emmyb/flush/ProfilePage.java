package emmyb.flush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.view.View.OnClickListener;
import android.widget.Toast;

import emmyb.flush.Database.ProfileActivity;

public class ProfilePage extends AppCompatActivity {

    ProfileActivity a = new ProfileActivity();
    private RatingBar ratingBar;
    private Button addRating;
    private RatingBar displayrating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        displayrating = (RatingBar)findViewById(R.id.OverallRating);
        addListenerOnRatingBar();
        addListenerOnButton();
        //double a=0;
        //exisitingRating((double) a);

    }


    public void existingRating(double latitude, double longitude){

        double x = a.getRatingFromDatabase(latitude, longitude);
        System.out.println("whatsx="+x+"######");
        float f = (float)x;
        System.out.println("WTTTTTTTTHHHHHHHHH"+f);
       // displayrating.setRating(f);

    }
    private void updatingRating(double x, float y){
        // lat, long, flout->double
        //a.postNewRating(,);
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
                Toast.makeText(ProfilePage.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
