package com.joetz.fragments.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joetz.CampGallery;
import com.joetz.MapsActivity;
import com.joetz.R;
import com.joetz.domain.Camp;

/**
 * This class extends the Fragment class and is loaded when the corresponding item in CampsOverviewFragment is selected.
 * It is a representation of a single camp. And is used to load all data into the right field.
 */
public class CampFragment extends Fragment{

    private Activity currentActivity;
    View rootView;
    private TextView title, city, promotext, period, place, extrainfo, lastregistrations, price, starprice1, starprice2, deductible, transport, age;
    private Camp camp;
    private LinearLayout title_prices, title_info, prices, info;
    private RelativeLayout signup;
    private ImageView campImage, galleryIcon;
    private Bitmap image;

    /**
     * This method is used to retrieve an instance of a Camp object
     * so the information can be shown.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.camp = (Camp) this.getArguments().getSerializable("camp");
        this.image = this.getArguments().getParcelable("campImage");
        currentActivity = getActivity();
        setHasOptionsMenu(true);
    }

    /**
     * This method is used to populate the Actionbar with a Icon.
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Google Maps");
        item.setIcon(R.drawable.ic_action_place);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        //If the icon is clicked an Intent is started which redirects to a MapsActivity.
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.equals(item)) {
                    Intent intent = new Intent(currentActivity, MapsActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("camp", camp);
                    intent.putExtras(args);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    /**
     * This method is called after the onCreate() method and is used to retrieve all elements declared in the XML file.
     * When all objects, which were declared in the XML file, are received it will call the setContent(..) method.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.camppage, container, false);
        title = (TextView) rootView.findViewById(R.id.campTitle);
        city = (TextView) rootView.findViewById(R.id.campCity);
        promotext = (TextView) rootView.findViewById(R.id.campPromoText);
        period = (TextView) rootView.findViewById(R.id.campPeriod);
        place = (TextView) rootView.findViewById(R.id.campPlace);
        age  = (TextView) rootView.findViewById(R.id.campAge);
        extrainfo = (TextView) rootView.findViewById(R.id.extraInfo);
        lastregistrations = (TextView) rootView.findViewById(R.id.lastRegistrations);
        price = (TextView) rootView.findViewById(R.id.price);
        starprice1 = (TextView) rootView.findViewById(R.id.starprice1);
        starprice2 = (TextView) rootView.findViewById(R.id.starprice2);
        deductible = (TextView) rootView.findViewById(R.id.deductible);
        transport = (TextView) rootView.findViewById(R.id.transport);
        signup = (RelativeLayout) rootView.findViewById(R.id.signupbutton);
        campImage = (ImageView) rootView.findViewById(R.id.campImage);
        galleryIcon = (ImageView) rootView.findViewById(R.id.galleryIcon);

        title_prices = (LinearLayout) rootView.findViewById(R.id.title_prices);
        title_prices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(v, "prices");
            }
        });
        title_info = (LinearLayout) rootView.findViewById(R.id.title_info);
        title_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(v, "info");
            }
        });

        prices = (LinearLayout) rootView.findViewById(R.id.prices);
        info = (LinearLayout) rootView.findViewById(R.id.info);
        // hide until its title is clicked
        prices.setVisibility(View.GONE);
        info.setVisibility(View.GONE);

        // put the content in the view
        setContent();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new StartSignUpFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("camp", camp);
                arguments.putParcelable("campImage", image);
                fragment.setArguments(arguments);
                FragmentManager fragmentManager = currentActivity.getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_frame, fragment).commit();
            }
        });

        galleryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(currentActivity, CampGallery.class);
                Bundle args = new Bundle();
                args.putString("campTitle", camp.getTitle());
                args.putInt("campId", camp.getId());
                i.putExtras(args);
                startActivity(i);
            }
        });

        return rootView;
    }


    /**
     * This method is called by the onCreateView(..) method and is used to set all values in the UI elements.
     */
    public void setContent() {
        campImage.setImageBitmap(image);
        title.setText(camp.getTitle());
        city.setText(camp.getCity());
        promotext.setText(camp.getPromotext());
        period.setText(camp.getPeriod());
        place.setText(camp.getPlace());
        age.setText(camp.getMinimumAge() + " - " + camp.getMaximumAge() + " jarigen");
        extrainfo.setText(camp.getExtraInfo());
        if(camp.lastRegistrations()) {
            int possible = camp.availableRegistrations();
            String message = "";
            if(possible <= 0) {
                message = "Helaas dit kamp is volzet.";
                signup.setVisibility(View.INVISIBLE);
            } else {
                message = possible > 1 ? "Wees er snel bij!  Nog slechts " + possible + " plaatsen beschikbaar." : (possible == 1 ? " Nog slechts 1 plaats beschikbaar." : "");
            }

            if(!message.equals("")) {
                lastregistrations.setText(message);
                lastregistrations.setVisibility(View.VISIBLE);
            }
        }
        price.setText("€ " + Double.toString(camp.getPrice()));
        starprice1.setText("€ " + Double.toString(camp.getStarPrice1()));
        starprice2.setText("€ " + Double.toString(camp.getStarPrice2()));
        deductible.setText(camp.getIsDeductible() > 0? "Ja" : "Nee");
        transport.setText(camp.getTransport());
    }

    public void slide(Context context, View v, char key) {
        Animation a = null;
        switch(key) {
            case 'd': a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                break;
            case 'u': a = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                break;
        }
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public void toggle_contents(View v, String key) {
        LinearLayout l = key.equals("prices") ? prices : info;
        if(l.isShown()){
            slide(currentActivity, l, 'u');
            l.setVisibility(View.GONE);
        }
        else {
            l.setVisibility(View.VISIBLE);
            slide(currentActivity, l, 'd');
        }
    }
}
