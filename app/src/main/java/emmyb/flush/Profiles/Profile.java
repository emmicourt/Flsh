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

    private String latitude;
    private String longitude;
    private String uuid;
    private double rating;
    private List<String> users;
    public Map<String, Object> profiles;

    public Profile(String latitude, String longitude ){
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = 0;
        this.uuid = setUUID(latitude,longitude);
    }

    public Profile ( String uuid){
        this.rating = 0;
        this.uuid = uuid;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("rating", rating);
        result.put("users", users);
        return result;
    }

    private String setUUID(String latitude, String longitude) {
        return latitude + latitude;
    }

    public double calcRating(double oldRating, double newRate){
        return (oldRating + newRate) / 2;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public double getRating(){
        return rating;
    }

    public String getUUID() {return uuid; }

    public void setRating(double newRating){
        this.rating = newRating;
    }

    public void setLatitude(String latitude){
        this.latitude = latitude;
    }

    public void setLongitude(String longitude){
        this.longitude = longitude;
    }

    public void addUser(String userId){
        if(!users.contains(userId)){
            users.add(userId);
        }
    }
    public List<String> getUsers(){
        return users;
    }

}
