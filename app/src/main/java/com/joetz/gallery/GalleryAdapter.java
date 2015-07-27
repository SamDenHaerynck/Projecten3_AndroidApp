package com.joetz.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.joetz.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for populating the galery for each camp.
 */
public class GalleryAdapter extends BaseAdapter {

    private Context context;
    private List<String> urls;
    private LayoutInflater inflater;

    public GalleryAdapter(Context context, String[] urls) {

        this.context = context;
        inflater = LayoutInflater.from(context);

        this.urls = new ArrayList<String>(Arrays.asList(urls));
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return urls.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ImageView picture;

        if(v==null){
            v = inflater.inflate(R.layout.gallery_item, parent, false);
        }

        picture = (ImageView)v.findViewById(R.id.imageItem);
        Picasso.with(v.getContext()).load(urls.get(position)).fit().centerCrop().into(picture);
        return v;
    }
}
