package emmyb.flush;

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

public class LoginActivityF extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_MESSAGE = "emmyb.flush";
    private static final String TAG = "LoginActivity";
    private Button signIn;
    private Button verifyEmail;
    private Button popUpButt;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextEmail1;
    private EditText editTextPassword2;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(IntialScreen.isSignIn){
            setContentView(R.layout.activity_login_f);
            signIn = (Button) findViewById(R.id.signIn);
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            editTextPassword = (EditText) findViewById(R.id.editTextPassword);
            signIn.setOnClickListener(this);
        } else {
            setContentView(R.layout.activity_login_g);
            verifyEmail = (Button) findViewById(R.id.verifyEmail);
            editTextEmail1 = (EditText) findViewById(R.id.editTextEmail1);
            editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
            verifyEmail.setOnClickListener(this);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

    }

    private void registerUser(){
        String email = editTextEmail1.getText().toString().trim();
        String password = editTextPassword2.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Firebase magic */
        createAccount(email, password);
        /* End of firebase magic */

        progressDialog.setMessage("Register User");
        progressDialog.show();

    }

    @Override
    public void onClick(View view) {
        if(view == verifyEmail){
            registerUser();
            //sendEmailVerification();
        } else if (view == signIn) {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            /* Firebase magic */
            signIn(email, password);
            /* End of firebase magic */
        }
    }

    public void sendMessage(View view){
        Intent login = new Intent(this, MapsActivity.class);
        startActivity(login);
    }

    //------------------ Fire base functions  --------------------------------- //
git
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivityF.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        signIn(email, password);

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
                            Toast.makeText(LoginActivityF.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void goToMaps() {
        Intent maps = new Intent(this, MapsActivity.class);
        startActivity(maps);
    }

    private void signOut() {
        firebaseAuth.signOut();
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
                            Toast.makeText(LoginActivityF.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivityF.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

}
