package com.joetz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.joetz.gallery.GalleryAdapter;
import com.joetz.gallery.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is displays a series of images.
 */
public class CampGallery extends ActionBarActivity {
    private ActionBarActivity currentActivity;
    private String currentCamp;
    private int currentCampId;

    private final String IMAGESERVERLOCATION = "http://project-groep6.azurewebsites.net/public/img/camps/";

    private List<String> imageUrls = new ArrayList<String>();

    /**
     * This method fetches all the locations of images that are related to a specific campId?
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camp_gallery);

        Bundle extras = this.getIntent().getExtras();
        currentCamp = extras.getString("campTitle");
        currentCampId = extras.getInt("campId");

        getSupportActionBar().setTitle(currentCamp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONParser jp = new JSONParser();
                JSONArray arr = jp.getJSONFromUrl("http://project-groep6.azurewebsites.net/api/camps/" + currentCampId + "/images");
                for(int i = 0; i < arr.length(); i++){
                    try {
                        JSONObject jobj = arr.getJSONObject(i);
                        String imgLocation = jobj.getString("location");
                        imageUrls.add(IMAGESERVERLOCATION + imgLocation);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadImages();
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * This method sets the adapter fot the listView and passes the image URI's to it.
     */
    private void loadImages() {
        GalleryAdapter adapter = new GalleryAdapter(currentActivity.getApplicationContext(), imageUrls.toArray(new String[imageUrls.size()]));
        ListView lv = (ListView)currentActivity.findViewById(R.id.galleryImageList);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return true;
    }
}
