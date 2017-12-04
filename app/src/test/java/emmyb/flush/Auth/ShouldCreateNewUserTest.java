package emmyb.flush.Auth;


import android.support.annotation.NonNull;
import emmyb.flush.Auth.LoginActivityG;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import junit.framework.Assert;

/**
 * Created by EmmyB on 12/1/17.
 */


public class ShouldCreateNewUserTest {

    FirebaseUser firebaseUser = null;
    private FirebaseAuth mAuth;
    boolean inRegister = false;

    //LoginActivityG.createAccount()





    public void isEmailAlreadyUsed() {
        boolean inRegister = false;
        String email = "test@gmail.com";
        String password = "password";
        mAuth = FirebaseAuth.getInstance();

///// here I am gonna create dummy user at **Firebase** with the Entered email Id (Email to check against)

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /////user do not exit so good to initialize firebase user.

                            firebaseUser = task.getResult().getUser();
                        } else {
                            //inRegister = false;
                        }
                    }
                });
    }

}
