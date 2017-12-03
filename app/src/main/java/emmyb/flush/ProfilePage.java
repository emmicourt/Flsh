package emmyb.flush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.view.View.OnClickListener;

public class ProfilePage extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button addRating;
    private RatingBar displayrating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        addListenerOnRatingBar();
        addListenerOnButton();
        double a=0;
        exisitingRating((double) a);

    }

    private void exisitingRating(double x){
        displayrating = (RatingBar)findViewById(R.id.OverallRating);
        float f = (float) x;
        displayrating.setRating((float)  x);

    }
    private void updatingRating(double x, float y){


    }

    private void addListenerOnRatingBar() {
        ratingBar = (RatingBar)findViewById(R.id.UserRating);

        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
              //  updatingRating();
            }
        });
    }

    private void addListenerOnButton() {
        ratingBar = (RatingBar)findViewById(R.id.UserRating);
        addRating = (Button)findViewById(R.id.AddRating);

        addRating.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String
            }
        });
    }

    public void onRatingChanged(RatingBar ratingBar, double rating, boolean fromTouch) {

        final int numStars = ratingBar.getNumStars();
        ratingBar.getRating() ;
        final double ratingBarStepSize = ratingBar.getStepSize();

    }

}
