package emmyb.flush;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.w3c.dom.Text;

public class LoginActivityF extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_MESSAGE = "emmyb.flush";
    private Button signUp;
    private Button verifyEmail;
    private EditText editTextEmail;
    private EditText editTextPassword;
    //private TextView textViewSignIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_f);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        signUp = (Button) findViewById(R.id.signUp);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        verifyEmail = (Button) findViewById(R.id.verifyEmail);

        //textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);

        signUp.setOnClickListener(this);
        //textViewSignIn.setOnClickListener(this);

    }
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //is empty
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Register User");
        progressDialog.show();

       /* firebaseAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override


        } */
    }

    @Override
    public void onClick(View view) {
        if(view == signUp){
            registerUser();
        }
    }

    public void sendMessage(View view){
        Intent login = new Intent(this, MapsActivity.class);
        startActivity(login);
    }
}
