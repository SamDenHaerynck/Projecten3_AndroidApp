package com.joetz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joetz.domain.Camp;
import com.joetz.domain.LatitudeLongitudeReceiver;


public class MapsActivity extends ActionBarActivity {

    private Camp camp;
    private Double longitude, latitude;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        camp = (Camp) getIntent().getSerializableExtra("camp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.maps);
        getSupportActionBar().setTitle(camp.getTitle());

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        new LatLongReceiver(this, camp).execute();
    }

    /**
     * Method called after LatLongReceiver AsyncTask is completed
     */
    public void markerAdded(){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        switch(item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }

    private class LatLongReceiver extends AsyncTask<Void, Void, String> {

        private Camp camp;
        private MapsActivity m;

        public LatLongReceiver(MapsActivity m, Camp camp) {
            this.camp = camp;
            this.m = m;
        }

        @Override
        protected String doInBackground(Void... params) {
            LatitudeLongitudeReceiver receiver = new LatitudeLongitudeReceiver();
            receiver.execute(camp.getCity());
            m.setLatitude(receiver.getLatitude().doubleValue());
            m.setLongitude(receiver.getLongitude().doubleValue());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(camp.getTitle()));
            markerAdded();
        }
    }

}
