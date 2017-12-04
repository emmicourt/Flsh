package emmyb.flush.Auth;

/**
 * Created by EmmyB on 12/2/17.
 *
 *  This class is responsible for registering a new user. It connects the UI to the Firebase methods.
 *  If the registration is successful it redirects the user to MapActivity.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import emmyb.flush.IntialScreen;
import emmyb.flush.R;


public class LoginActivityG extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "emmyb.flush";
    private static final String TAG = "LoginActivityG";
    private Button verifyEmail;
    private EditText editTextEmail1;
    private EditText editTextPassword2;
    private EditText editTextPassword3;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    IntialScreen mIntialScreen;

    /**
     * onCreate()
     * required method that instantiates the database authorization and onClickListener
     * for the buttons and field editors
     * @param savedInstanceState - instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntialScreen = new IntialScreen();
        setContentView(R.layout.activity_login_g);
        verifyEmail = (Button) findViewById(R.id.verifyEmail);
        editTextEmail1 = (EditText) findViewById(R.id.editTextEmail1);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        editTextPassword3 = (EditText) findViewById(R.id.editTextPassword3);
        verifyEmail.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    /**
     * registerUser()
     * grabs the email and password from the textEditors and check to make sure they are not empty
     * and the passwords match. Then uses these values to call the firebase createAccount method
     */
    private void registerUser(){
        String email = editTextEmail1.getText().toString().trim();
        String password1 = editTextPassword2.getText().toString().trim();
        String password2 = editTextPassword3.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password2)) {
            Toast.makeText(this, "Please Re-enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password1.equals(password2)){
            Toast.makeText(this, "Please Enter Matching Passwords", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Firebase magic */
        createAccount(email, password1);
        /* End of firebase magic */

        progressDialog.setMessage("Register User");
        progressDialog.show();
    }

    //------------------ Fire base functions  --------------------------------- //
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    public void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            goToMaps();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(emmyb.flush.Auth.LoginActivityG.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            goToMaps();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(emmyb.flush.Auth.LoginActivityG.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private boolean validateForm(){
        return true;
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verifyEmail).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verifyEmail).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(emmyb.flush.Auth.LoginActivityG.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(emmyb.flush.Auth.LoginActivityG.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }
    // ----------------- End of Firebase Functions -----------------------------------------//

    /**
     * onClick()
     * Listens for the verify email button to be clicked
     * @param view : view object
     */
    @Override
    public void onClick(View view) {
        if(view == verifyEmail) {
            registerUser();
        }
    }

    /**
     * sendMessage
     * starts the MapActivity and directs user to that page
     * @param view - view object
     */
    public void sendMessage(View view){
        Intent login = new Intent(this, LoginActivityF.class);
        startActivity(login);
    }

    /**
     * sendMessage
     * starts the MapActivity and directs user to that page
     */
    private void goToMaps() {
        Intent maps = new Intent(this, LoginActivityF.class);
        startActivity(maps);
    }

}

