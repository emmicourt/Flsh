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

        Spinner Spinner = (Spinner) findViewById(R.id.spinner);

        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long row_id) {
                final Intent intent;
                switch(position){
                    case 1:
                        intent = new Intent(IntialScreen.this, MapsActivity.class);
                        break;
                    case 2:
                        intent = new Intent(IntialScreen.this, ProfileScreen.class);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }
    public void sendMessage(View view){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }
}
