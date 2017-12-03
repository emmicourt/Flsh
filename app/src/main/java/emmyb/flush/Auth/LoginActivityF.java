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

public class LoginActivityF extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_MESSAGE = "emmyb.flush";
    private static final String TAG = "LoginActivityF";
    private Button signIn;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    IntialScreen mIntialScreen;


    @Override
    /**
     * OnCreate()
     * Takes a boolean from Initial screen to determiine if the user is trying
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntialScreen = new IntialScreen();

        setContentView(R.layout.activity_login_f);
        signIn = (Button) findViewById(R.id.signIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        signIn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

    }

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

    public void sendMessage(View view){
        Intent login = new Intent(this, MapsActivity.class);
        startActivity(login);
    }

    //------------------ Fire base functions  --------------------------------- //
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
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

}
