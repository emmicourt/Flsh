package emmyb.flush;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

public class IntialScreen extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "emmyb.flush";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intial_screen);

    }
    public void sendMessage(View view){
        Intent login = new Intent(this, LoginActivityF.class);
        startActivity(login);
    }
}
