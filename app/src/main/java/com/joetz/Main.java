package com.joetz;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.joetz.domain.RequestMethod;
import com.joetz.domain.RestClient;
import com.joetz.drawer.DrawerItemCustomAdapter;
import com.joetz.drawer.ObjectDrawerItem;
import com.joetz.fragments.main.CampsOverviewFragment;
import com.joetz.fragments.main.ContactFragment;
import com.joetz.fragments.main.StartpageFragment;

/**
 * This class is responsible for displaying the right content.
 * When an item is clicked in the navigation drawer it handles the loading of fragments.
 */

public class Main extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLinearLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView mDrawerList;
    private Dialog dialog;

    /**
     * This method sets up navigation with a drawer.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the drawer navigation
        setDrawerNavigation();

        // Receive camps
        new CampsReceiver().execute();
        // Show startpage
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new StartpageFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        //dataSource.close();
        super.onDestroy();
    }

    private void setDrawerNavigation(){
        mTitle = mDrawerTitle = getTitle();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);

        // Get item string values
        String[] options = getResources().getStringArray(R.array.options);
        mDrawerList = (ListView) findViewById(R.id.list_view_drawer);

        // Set drawer items
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[options.length];
        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_action_home, options[0]);
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_vacations, options[1]);
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_contact, options[2]);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItem));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment
        Fragment fragment;
        switch(position)
        {
            case 0: fragment = new StartpageFragment();
                break;
            case 1: fragment = new CampsOverviewFragment();
                break;
            case 2: fragment = new ContactFragment();
                break;
            default: fragment = null;
                break;
        }

        // Insert the fragment by replacing any existing fragment if it is not already being displayed
        FragmentManager fragmentManager = getFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);
        if(!fragment.getClass().equals(currentFragment.getClass())){
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
            //Highlight selected item if it is not already being displayed
            mDrawerList.setItemChecked(position, true);
        }

        //Close drawer after item is clicked
        mDrawerLayout.closeDrawer(mDrawerLinearLayout);

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private class CampsReceiver extends AsyncTask<Void, Void, String> {
        private static final String TAG = "CampsReceiver";
        public static final String SERVER_URL = "http://project-groep6.azurewebsites.net/api/camps/all";
        private long time;

        @Override
        protected void onPreExecute() {
            dialog = new Dialog(Main.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.setContentView(R.layout.loading);
            dialog.show();
            time = System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(Void... params) {
            RestClient client = new RestClient(Main.this, SERVER_URL);
            boolean executed = client.Execute(RequestMethod.GET);
            if(!executed) failedLoadingPosts();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //Minimum time for loading screen to show in milliseconds
            final long MINIMUM_TIME = 2000;
            long diffTime = System.currentTimeMillis() - time;
            if(diffTime < MINIMUM_TIME){
                try{
                    Thread.sleep(MINIMUM_TIME - diffTime);
                }catch(InterruptedException e){
                    Log.v("THREAD", "Sleeping threading interrupted.");
                }
            }
            dialog.dismiss();
            //initialize the View
        }

    }

    private void failedLoadingPosts() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Main.this, "Er kon geen kampenoverzicht geladen worden. Controleer of uw toestel met het internet verbonden is.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
