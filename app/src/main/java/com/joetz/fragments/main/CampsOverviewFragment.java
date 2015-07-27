package com.joetz.fragments.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.joetz.R;
import com.joetz.adapters.CampRecycleAdapter;
import com.joetz.domain.Camp;
import com.joetz.sqlite.CampsDataSource;

import java.util.List;

/**
 * This class is responsible for showing all the camps stored in the database.
 */
public class CampsOverviewFragment extends Fragment {

    private Activity currentActivity;
    private RecyclerView campsOverview;
    private CampsDataSource dataSource;
    private ImageView allSeasons, summer, autumn, winter, spring;
    private LinearLayout filterOptions;
    private EditText age;
    public static final String[] seasons = {"Alle seizoenen", "Zomer", "Herfst", "Krokus", "Lente"};
    private SearchView searchView;
    View rootView;

    /**
     * In this method the datasource is opened.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
        dataSource = new CampsDataSource(currentActivity);
        dataSource.open();
        setHasOptionsMenu(true);
    }

    /**
     * This method provides filter options which allow to filter the list of camps.
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        final SearchView sv = new SearchView(getActivity());
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) sv.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_action_search);

        int searchPlateId = sv.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        // Getting the 'search_plate' LinearLayout.
        View searchPlate = sv.findViewById(searchPlateId);
        searchPlate.setBackgroundColor(Color.WHITE);

        // modifying the text inside edittext component
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setHint("Zoek een kamp");
        textView.setHintTextColor(getResources().getColor(R.color.grey_dark));
        textView.setTextColor(getResources().getColor(R.color.grey_dark));

        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterOptions.setVisibility(View.VISIBLE);
            }
        });

        // implementing the listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() < 3) {
                    Toast.makeText(currentActivity, "Uw zoekterm dient minstens 3 karakters lang te zijn.", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    filter(s, null, -1);
                    sv.clearFocus();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    filter("", null, -1);
                }
                return true;
            }
        });
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                showCamps(null);
                filterOptions.setVisibility(View.GONE);
                return false;
            }
        });

        searchView = sv;
        item.setActionView(sv);
    }

    /**
     * This method allows to search the camps by name.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        this.rootView = inflater.inflate(R.layout.campsoverviewpage, container, false);
        this.filterOptions = (LinearLayout) rootView.findViewById(R.id.filterOptions);
        filterOptions.setVisibility(View.GONE);
        this.age = (EditText) rootView.findViewById(R.id.age);
        age.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        String ageNr = age.getText().toString();
                        if(ageNr.isEmpty()) {
                            filter(null, null, -2);
                        } else {
                            filter(null, null, Integer.valueOf(ageNr));
                        }
                        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    } catch(NumberFormatException ex) {
                        Toast.makeText(currentActivity, "Gelieve een geldige leeftijd in te vullen.", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });

        age.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    filter(null, null, -2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });
        campsOverview = (RecyclerView) rootView.findViewById(R.id.campsOverview);
        campsOverview.setLayoutManager(new LinearLayoutManager(currentActivity));
        campsOverview.setItemAnimator(new DefaultItemAnimator());
        setActionsToSeasonFilters(rootView);
        showCamps(null);
        return rootView;
    }

    /**
     * When the fragment is destroyed the datasource is closed to prevent resource leaking.
     */
    @Override
    public void onDestroy() {
        dataSource.close();
        super.onDestroy();
    }

    /**
     * Adding OnClickListeners to the season tiles.
     * @param v
     */
    public void setActionsToSeasonFilters(View v) {
        allSeasons = (ImageView) v.findViewById(R.id.allSeasons);
        summer = (ImageView) v.findViewById(R.id.summer);
        autumn = (ImageView) v.findViewById(R.id.autumn);
        winter = (ImageView) v.findViewById(R.id.winter);
        spring = (ImageView) v.findViewById(R.id.spring);

        allSeasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(null, seasons[0], -1);
                setCurrentSeasonFilter(0);
            }
        });
        summer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(null, seasons[1], -1);
                setCurrentSeasonFilter(1);
            }
        });
        autumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(null, seasons[2], -1);
                setCurrentSeasonFilter(2);
            }
        });
        winter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(null, seasons[3], -1);
                setCurrentSeasonFilter(3);
            }
        });
        spring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(null, seasons[4], -1);
                setCurrentSeasonFilter(4);
            }
        });
        setCurrentSeasonFilter(0);
    }

    private void setCurrentSeasonFilter(int id) {
        ImageView[] seasonIcons = {allSeasons, summer, autumn, winter, spring};
        for (int i=0; i < seasonIcons.length; i++) {
            if(i == id) {
                seasonIcons[i].setColorFilter(null);
            } else {
                seasonIcons[i].setColorFilter(R.color.white);
            }
        }
    }

    private void showCamps(List<Camp> camps) {
        if(camps == null) {
            campsOverview.setAdapter(new CampRecycleAdapter(dataSource.getAllCamps(), R.layout.camps_list_item, getActivity()));
        } else {
            campsOverview.setAdapter(new CampRecycleAdapter(camps, R.layout.camps_list_item, getActivity()));
        }
    }

    private void filter(String title, String season, int a) {
        String titleCamp = null;
        String seasonCamp = null;
        int ageCamp = -1;

        if(title != null) {
            // Start searching by title
            titleCamp = title;
            seasonCamp = getActiveSeasonFilter();
            String ageQuery = age.getText().toString();
            ageCamp = ageQuery.isEmpty() ? -1 : Integer.parseInt(ageQuery);
        } else if(season != null) {
            // Start searching by season
            String titleQuery = searchView.getQuery().toString();
            titleCamp = titleQuery.isEmpty() ? null : titleQuery;
            seasonCamp = season;
            String ageQuery = age.getText().toString();
            ageCamp = ageQuery.isEmpty() ? -1 : Integer.parseInt(ageQuery);
        } else if(a != -1) {
            // Start searching by age
            String titleQuery = searchView.getQuery().toString();
            titleCamp = titleQuery.isEmpty() ? null : titleQuery;
            seasonCamp = getActiveSeasonFilter();
            ageCamp = a;
        }

        List<Camp> filteredCamps = (ageCamp != -1) ? dataSource.filterCamps(titleCamp, seasonCamp, ageCamp) : dataSource.filterCamps(titleCamp, seasonCamp);
        if(filteredCamps.size() == 0) {
            Toast.makeText(currentActivity, "Er werden geen kampen gevonden die aan uw zoekopdracht voldoen", Toast.LENGTH_LONG).show();
        }
        showCamps(filteredCamps);
    }

    private String getActiveSeasonFilter() {
        String season = "";
        if(allSeasons.getColorFilter() == null) season = seasons[0];
        else if(summer.getColorFilter() == null) season = seasons[1];
        else if(autumn.getColorFilter() == null) season = seasons[2];
        else if(winter.getColorFilter() == null) season = seasons[3];
        else if(spring.getColorFilter() == null) season = seasons[4];
        return season;
    }

}
