package com.joetz.drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joetz.R;

/**
 * This class is used to populate the navigation drawer.
 * The navigationdrawer is populated with the items passed in the constructor.
 * See the ObjectDrawerItem Array.
 */
public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

    private Context mContext;
    private int layoutResourceId;
    private ObjectDrawerItem data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.icon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.option);

        ObjectDrawerItem folder = data[position];

        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }

}
