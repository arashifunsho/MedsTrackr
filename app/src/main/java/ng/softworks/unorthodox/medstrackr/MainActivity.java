package ng.softworks.unorthodox.medstrackr;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import ng.softworks.unorthodox.medstrackr.navDrawer.NavigationDrawerCallbacks;
import ng.softworks.unorthodox.medstrackr.navDrawer.NavigationDrawerFragment;
import ng.softworks.unorthodox.medstrackr.layout.app_home;
import ng.softworks.unorthodox.medstrackr.layout.fragment_add_prescription;
import ng.softworks.unorthodox.medstrackr.layout.fragment_prescription_history;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        switch (position) {
            //TODO: check out what it looks like to set the title of the actionbar corresponding to
            //current fragment
            case 1: //loads fragment to add new prescription
                fragment = getSupportFragmentManager().findFragmentByTag(fragment_add_prescription.TAG);
                if (fragment == null) {
                    fragment = new fragment_add_prescription();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment,
                        fragment_add_prescription.TAG).commit();
                //mToolbar.setTitle(R.string.new_pres);
                break;

            case 0: //loads home fragment displayed when app is first launched
                fragment= getSupportFragmentManager().findFragmentByTag(app_home.TAG);
                if (fragment==null) {
                    fragment = new app_home();
                }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment,app_home.TAG).commit();
                    break;

            case 3: //loads the prescription history fragment
                fragment=getSupportFragmentManager().findFragmentByTag(fragment_prescription_history.TAG);
                if (fragment==null){
                    fragment= new fragment_prescription_history();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment,
                        fragment_prescription_history.TAG).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */

}
