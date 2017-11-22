package emmyb.flush.Database;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.firebase.auth.FirebaseAuth;

import emmyb.flush.Auth.EmailSignIn;
import emmyb.flush.R;

public class MainActivity extends FragmentActivity {


    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{

            };
            private final String[] mFragmentNames = new String[]{

            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
    }

        /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, EmailSignIn.class));
                finish();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
        */

}
