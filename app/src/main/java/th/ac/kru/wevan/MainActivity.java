package th.ac.kru.wevan;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements PromotionFragment.OnFragmentInteractionListener,
    ReservFragment.OnFragmentInteractionListener, HistoryReservFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;

    private String id_card, name, type, email, phone;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new ReservFragment();
                    break;
                case R.id.navigation_dashboard:
                   fragment = new HistoryReservFragment();
                   break;
                case R.id.navigation_notifications:
                    fragment = new PromotionFragment();
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.navigation_reserv:
                    fragment = new SettingFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new ReservFragment());

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        id_card = getIntent().getExtras().getString("id_card");
        name = getIntent().getExtras().getString("name");
        type = getIntent().getExtras().getString("user_type");
        email = getIntent().getExtras().getString("email");
        phone = getIntent().getExtras().getString("phone");
        //Log.d("onCreate: ", id_card + name + type + email + phone);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
