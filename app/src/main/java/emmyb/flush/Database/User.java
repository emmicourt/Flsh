package emmyb.flush.Database;

/**
 * Created by EmmyB on 10/31/17.
 */

public class User {
    private String email;
    private String password;

    public User(){

    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public void changePassword(String password){
        this.password = password;
    }
}
