package com.joetz.fragments.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joetz.R;
import com.joetz.gallery.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *  This fragment is used to display all contact information.
 */
public class ContactFragment extends Fragment {

    Activity currentActivity;
    private final String contactUrl = "http://project-groep6.azurewebsites.net/api/contact/info";
    private String phone, email, street, place, postal, fax, website;
    private View fragmentRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentRoot = inflater.inflate(R.layout.contactpage, container, false);
        new DataFetcher().execute();
        return fragmentRoot;
    }


    /**
     * This private class extends AsyncTask and is used to load data asynchronously.
     * When all the data is loaded it will be set into its corresponding UI element. (see onPostExecute())
     */
    private class DataFetcher extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = jsonParser.getJSONFromUrl(contactUrl);
                JSONObject jobj = jsonArray.getJSONObject(0);

                phone = jobj.getString("phoneNr");
                email = jobj.getString("email");
                street = jobj.getString("streetName");
                place = jobj.getString("place");
                postal = jobj.getString("postal");
                fax = jobj.getString("fax");

                website = jobj.getString("website");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView txtStreet = (TextView) fragmentRoot.findViewById(R.id.txtStreet);
            txtStreet.setText(street + ", " + postal + " " + place);

            TextView txtPhone = (TextView) fragmentRoot.findViewById(R.id.txtPhone);
            txtPhone.setText(phone);

            TextView txtFax = (TextView) fragmentRoot.findViewById(R.id.txtFax);
            txtFax.setText(fax);

            TextView txtMail = (TextView) fragmentRoot.findViewById(R.id.txtEmail);
            txtMail.setText(email);

            TextView txtWeb = (TextView) fragmentRoot.findViewById(R.id.txtWebsite);
            txtWeb.setText(website);
        }
    }

}


