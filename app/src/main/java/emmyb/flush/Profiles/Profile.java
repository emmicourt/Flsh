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
        this.users = null;
    }

    public Profile() {

    }

    // ------------------------ Getters and Setters ------------------------------------------------

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

    public void setUsers(List<String> users) {this.users = users; }

    // ------------------------ Profile Functions --------------------------------------------------

    public void addUser(String userId){
        if(!users.contains(userId)){
            users.add(userId);
        }
    }

    public double calcRating(double oldRating, double newRate){
        return (oldRating + newRate) / 2;
    }

    public List<String> getUsers(){
        return users;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("rating", rating);
        result.put("users", users);
        return result;
    }

    private String setUUID(String latitude, String longitude) {
        int a = latitude.indexOf('.');
        int b = longitude.indexOf('.');
        String lat = latitude.substring(0,a) + "-" + latitude.substring(a+1);
        String lng = longitude.substring(0, b) + "-" + longitude.substring(b+1);

        return lat + "$" + lng;
    }

    public String getLatFromUUID(String uuid){
        int a = uuid.indexOf('x');
        String latitude = uuid.substring(0, a);
        int b = latitude.indexOf('-');
        return latitude.substring(0, b) + '.' + latitude.substring(b+1);
    }

    public String getLongFromUUID(String uuid){
        int a = uuid.indexOf('x');
        String longitude = uuid.substring(a);
        int b = longitude.indexOf('-');
        return longitude.substring(0, b) + '.' + longitude.substring(b+1);
    }

}
