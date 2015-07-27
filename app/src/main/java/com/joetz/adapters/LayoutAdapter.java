package com.joetz.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.joetz.R;
import com.joetz.domain.Camp;
import com.joetz.fragments.main.CampFragment;
import com.squareup.picasso.Picasso;
import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.SpannableGridLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;
import java.util.List;

/**
 * This class alows to load items into the RecyclerView.
 * All credits for the TwoWayView go to lucasr's project (https://github.com/lucasr/twoway-view)
 * All credits for the Picasso library go to Square, Inc. (http://square.github.io/picasso/)
 */

public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {

    private final Context mContext;
    private final TwoWayView featuredRecyclerView;
    private final List<Camp> featuredCamps;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView campImage;
        public final TextView campTitle, campCity;
        public final RelativeLayout wrapper;
        public final LinearLayout item;

        public SimpleViewHolder(View view) {
            super(view);
            campImage = (ImageView) view.findViewById(R.id.campImage);
            campTitle = (TextView) view.findViewById(R.id.campTitle);
            campCity = (TextView) view.findViewById(R.id.campCity);
            wrapper = (RelativeLayout) view.findViewById(R.id.wrapper);
            item = (LinearLayout) view.findViewById(R.id.featuredItemContent);
        }
    }

    public LayoutAdapter(Context context, TwoWayView recyclerView, List<Camp> camps) {
        mContext = context;
        featuredCamps = camps;
        featuredRecyclerView = recyclerView;
    }

    /**
     * This method is called when a new ViewHolder is created.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.featureditem, parent, false);
        return new SimpleViewHolder(view);
    }

    /**
     * This method is called when an item is loaded into the screen.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        boolean isVertical = (featuredRecyclerView.getOrientation() == TwoWayLayoutManager.Orientation.VERTICAL);
        final View itemView = holder.itemView;
        final int itemId = position % 4;
        final SpannableGridLayoutManager.LayoutParams lp = (SpannableGridLayoutManager.LayoutParams) itemView.getLayoutParams();
        int patternRow = 0;

        switch (itemId) {
            case 0: patternRow = position / 4;
                break;
            case 1: patternRow = (position - 1) / 4;
                break;
            case 2: patternRow = (position - 2) / 4;
                break;
            case 3: patternRow = (position - 3) / 4;
                break;
        }

        final int span1 = (itemId == 0 || itemId == 3 ? 2 : 1);
        final int span2 = (itemId == 0 ? 2 : (itemId == 3 ? 3 : 1));
        final int colSpan = (isVertical ? span2 : span1);
        final int rowSpan = (isVertical ? span1 : span2);
        if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
            lp.rowSpan = rowSpan;
            lp.colSpan = colSpan;
            itemView.setLayoutParams(lp);
        }

        final Camp camp = featuredCamps.get(position);

        Picasso.with(mContext).load("http://project-groep6.azurewebsites.net/public/img/camps/" +
                camp.getLocation()).fit().into(holder.campImage);

        holder.campTitle.setText(camp.getTitle());
        holder.campCity.setText(camp.getCity());
        if(span1 == 1) {
            holder.wrapper.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
            holder.wrapper.setGravity(Gravity.CENTER_VERTICAL);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CampFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("camp", camp);
                arguments.putParcelable("campImage", CampRecycleAdapter.scaleDownBitmap(((BitmapDrawable)holder.campImage.getDrawable()).getBitmap(), 100, mContext));
                fragment.setArguments(arguments);
                FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
            }
        });
    }

    /**
     * This method returns the amount of items in the list of featured camps.
     * @return
     */
    @Override
    public int getItemCount() {
        return featuredCamps.size();
    }
}
