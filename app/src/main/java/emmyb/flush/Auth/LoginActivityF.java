package emmyb.flush.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import emmyb.flush.Maps.MapsActivity;
import emmyb.flush.R;

/**
 *  This class is responsible for user sign in. It connects the UI to the Firebase methods.
 *  If the sign in is successful it redirects the user to MapActivity.
 */

public class LoginActivityF extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_MESSAGE = "emmyb.flush";
    private static final String TAG = "LoginActivityF";
    private Button signIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
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

        // set content view and
        setContentView(R.layout.activity_login_f);
        signIn = (Button) findViewById(R.id.signIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        signIn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

    }

    //------------------------ Firebase functions  ---------------------------------------

    /**
     * onStart()
     * method from Firebase tutorial that gets the current user during start up
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    /**
     * signIn()
     * method from Firebase tutorial that signs in the firebase user
     * @param email - email for firebase user
     * @param password - email for firebase user
     */
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
                            Toast.makeText(LoginActivityF.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    //-------------------- End of Firebase functions -------------------------------------------



    /**
     * onClick()
     * Listens for the user to click on the sign in button, then grabs the email and
     * password from respective text box and uses these values
     * @param view : gets the View object from on click listener
     */
    @Override
    public void onClick(View view) {
        if (view == signIn) {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            /* Firebase magic */
            signIn(email, password);
            /* End of firebase magic */
        }
    }

    /**
     * sendMessage()
     * This function starts the MapActivity Activity and tells the UI to go to the coresponding map
     * layout file
     */
    public void sendMessage(View view){
        Intent login = new Intent(this, MapsActivity.class);
        startActivity(login);
    }

    /**
     * goToMaps()
     * This function starts the MapActivity Activity and tells the UI to go to the coresponding map
     * layout file
     */
    private void goToMaps() {
        Intent maps = new Intent(this, MapsActivity.class);
        startActivity(maps);
    }

}
