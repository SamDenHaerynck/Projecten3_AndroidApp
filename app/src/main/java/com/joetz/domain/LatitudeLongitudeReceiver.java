package com.joetz.domain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class allows to convert the name of a location to its corresponding latitude and longtitude
 * by making a call to the Google Geocoding API which is developed by Google Inc.
 */
public class LatitudeLongitudeReceiver {

    private Double latitude;
    private Double longitude;

    public void execute(String address){
        JSONObject a = getLocationInfo(address);
        getLatLong(a);
    }

    public JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //Creating a HttpClient to make calls to a webservice.
            address = address.replaceAll(" ","%20");
            HttpPost httppost = new HttpPost("https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=AIzaSyAKXK8jsnZSL1R4ZeeRb-CHzTf4jhqvdXQ");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();
            //Making the call using the HttpClient
            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            //Parsing the webservice's response to a JSONObject
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * This method extracts the latitude and longtitude from the JSONObject which is returned by getLocationInfo(..)
     * @param jsonObject
     * @return
     */
    public boolean getLatLong(JSONObject jsonObject) {
        try {
            longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public Double getLatitude() { return latitude; }

    public Double getLongitude() { return longitude; }
}
