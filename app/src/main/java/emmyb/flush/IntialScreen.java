package emmyb.flush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class IntialScreen extends AppCompatActivity implements View.OnClickListener{
    public static final String EXTRA_MESSAGE = "emmyb.flush";

    public static Boolean isSignIn;

    private Button googleButt;
    private Button signInButt;
    private Button registerButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intial_screen);

        googleButt = (Button) findViewById(R.id.button5);
        signInButt = (Button) findViewById(R.id.button2);
        registerButt = (Button) findViewById(R.id.button3);

        googleButt.setOnClickListener(this);
        signInButt.setOnClickListener(this);
        registerButt.setOnClickListener(this);
    }

    public void sendMessage(View view){

        if(view == googleButt){
            //
        } else if ( view == signInButt ){
            isSignIn = true;
            Intent login = new Intent(this, LoginActivityF.class);
            startActivity(login);
        } else {
            isSignIn = false;
            Intent login = new Intent(this, LoginActivityF.class);
            startActivity(login);
        }
    }

    @Override
    public void onClick(View v) {
        sendMessage(v);
    }
}
