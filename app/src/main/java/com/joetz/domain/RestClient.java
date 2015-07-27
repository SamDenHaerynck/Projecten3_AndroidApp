package com.joetz.domain;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joetz.sqlite.CampsDataSource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthiasdegroote on 24/11/14.
 */
public class RestClient {
    private String url;
    private CampsDataSource dataSource;
    private String TAG = "RestClient";
    private Map params;

    public RestClient(Context context, String url) {
        this.url = url;
        dataSource = new CampsDataSource(context);
        params = new HashMap();
    }

    public void setParams(Map params){
        this.params = params;
    }

    public boolean Execute(RequestMethod method) {
        boolean executed = false;
        switch(method) {
            case GET:
                executed =  executeRequest(new HttpGet(url));
                break;
            case POST:
                try{
                    HttpPost request = new HttpPost(url);
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-type", "application/json");
                    JSONObject holder = getJsonObjectFromPairs(params);
                    Log.e("", holder.toString());
                    StringEntity se = new StringEntity(holder.toString());
                    request.setEntity(se);
                    executed = executeRequest(request);
                } catch(Exception e) {
                    return false;
                }
                break;

        }
        return executed;
    }

    private boolean executeRequest(HttpUriRequest request) {
        //Create an HTTP client
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;

        try{
            response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null && request instanceof HttpGet) {
                    InputStream content = entity.getContent();
                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        //gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();
                        List<Camp> camps = Arrays.asList(gson.fromJson(reader, Camp[].class));
                        content.close();
                        dataSource.open();
                        if(camps.size() > 0)
                            dataSource.insertCamps(camps);
                        dataSource.close();
                        return true;
                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                        return false;
                    }
                } else {
                    if(request instanceof HttpPost) {
                        return true;
                    } else {
                        Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
                        return false;
                    }
                }
            }
        } catch (IOException e){
            Log.e(TAG, e.getMessage());
            return false;
        }
        return false;
    }

    private JSONObject getJsonObjectFromPairs(Map params) throws JSONException{
        return getHolder(params);
    }

    private JSONObject getHolder(Map params) throws JSONException{
        JSONObject holder = new JSONObject();
        Set<String> keys = params.keySet();
        for(String key : keys){
            if(params.get(key) instanceof Map){
                Map data = (Map) params.get(key);
                JSONObject secondHolder = getHolder(data);
                holder.put(key, secondHolder);
            } else {
                holder.put(key, params.get(key));
            }
        }
        return holder;
    }
}
