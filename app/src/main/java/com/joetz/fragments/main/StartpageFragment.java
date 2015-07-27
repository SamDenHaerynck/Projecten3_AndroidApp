package com.joetz.fragments.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joetz.R;
import com.joetz.adapters.LayoutAdapter;
import com.joetz.domain.Camp;
import com.joetz.sqlite.CampsDataSource;

import org.lucasr.twowayview.widget.TwoWayView;

import java.util.List;

/**
 * This classed is used to display the front/mainpage of the application.
 * All credits for the TwoWayView go to lucasr's project (https://github.com/lucasr/twoway-view)
 */
public class StartpageFragment extends Fragment {

    private Activity currentActivity;
    private CampsDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
        dataSource = new CampsDataSource(currentActivity);
        dataSource.open();
    }

    /**
     * This method sets the adapter for the TwoWayView.
     * See the LayoutAdapter class from the adapters package.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.startpage, container, false);
        List<Camp> camps = dataSource.getFeaturedCamps();
        TwoWayView featuredRecyclerView = (TwoWayView) rootView.findViewById(R.id.featuredItemsList);
        featuredRecyclerView.setAdapter(new LayoutAdapter(currentActivity, featuredRecyclerView, camps));
        return rootView;
    }

    /**
     * This method closes any open resources.
     */
    @Override
    public void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }
}
