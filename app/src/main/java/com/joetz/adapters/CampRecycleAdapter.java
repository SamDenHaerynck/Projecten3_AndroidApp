package com.joetz.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joetz.R;
import com.joetz.domain.Camp;
import com.joetz.fragments.main.CampFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for RecyclerView. This class alows to load items into the RecyclerView.
 */
public class CampRecycleAdapter extends RecyclerView.Adapter<CampRecycleAdapter.ViewHolder>{

    private List<Camp> camps;
    private int rowLayout;
    private static Context mContext;

    public CampRecycleAdapter(List<Camp> camps, int rowLayout, Context mContext) {
        this.rowLayout = rowLayout;
        this.camps = camps;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    /**
     * Set the content of each RecyclerView Item.
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Camp camp = camps.get(i);
        Picasso.with(mContext).load("http://project-groep6.azurewebsites.net/public/img/camps/" + camp.getLocation()).placeholder(R.drawable.image_unavailable).into(viewHolder.campImage);
        viewHolder.campTitle.setText(camp.getTitle());
        viewHolder.campPromoText.setText(camp.getPromotext());
        viewHolder.campPeriod.setText(camp.getPeriod());
        viewHolder.campCity.setText(camp.getCity());
        viewHolder.campAge.setText(camp.getMinimumAge() + " - " + camp.getMaximumAge() + " jarigen");
        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CampFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("camp", camp);
                arguments.putParcelable("campImage", scaleDownBitmap(((BitmapDrawable)viewHolder.campImage.getDrawable()).getBitmap(), 100, mContext));
                fragment.setArguments(arguments);
                FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return camps == null ? 0 : camps.size();
    }


    /**
     * Viewholder for RecyclerView. This allows the items to load faster.
     * By applying the Viewholder Pattern.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView campImage;
        public TextView campTitle;
        public TextView campPromoText;
        public TextView campPeriod;
        public TextView campCity;
        public CardView cv;
        public TextView campAge;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.campItem);
            campImage = (ImageView) itemView.findViewById(R.id.campImage);
            campTitle = (TextView) itemView.findViewById(R.id.campTitle);
            campPromoText = (TextView) itemView.findViewById(R.id.campPromoText);
            campPeriod = (TextView) itemView.findViewById(R.id.campPeriod);
            campCity = (TextView) itemView.findViewById(R.id.campCity);
            campAge = (TextView) itemView.findViewById(R.id.campAge);
        }
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}
