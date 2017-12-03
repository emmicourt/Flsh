package emmyb.flush.Profiles;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by EmmyB on 11/14/17.
 */


public class Profile {

    private double latitude;
    private double longitude;
    private double rating;
    public Map<String, Object> profiles;

    public Profile(double latitude, double longitude ){
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = 0;
    }

    // ------------------------ Getters and Setters ------------------------------------------------

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getRating(){
        return rating;
    }

    public void setRating(double newRating){
        this.rating = newRating;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }


    // ------------------------ Profile Functions --------------------------------------------------


    public double calcRating(double oldRating, double newRate){
        return (oldRating + newRate) / 2;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("rating", rating);
        return result;
    }

}
