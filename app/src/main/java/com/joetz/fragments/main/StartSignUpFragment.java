package com.joetz.fragments.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joetz.R;
import com.joetz.SignUpActivity;
import com.joetz.domain.Camp;

/**
 * This class is used to show the fragment which allows to start a subscription procedure.
 */
public class StartSignUpFragment extends Fragment {

    private View rootView;
    private Activity currentActivity;
    private Camp camp;
    private TextView cancel, start;
    private Bitmap image;
    private ImageView campImage;

    /**
     * Retrieve the camp of which the subscribtion should be started.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.camp = (Camp) this.getArguments().getSerializable("camp");
        this.image = this.getArguments().getParcelable("campImage");
        this.currentActivity = getActivity();
    }

    /**
     * Creating the View and setting its listeners.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.startsignuppage, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.campTitle);
        TextView location = (TextView) rootView.findViewById(R.id.campCity);
        title.setText(camp.getTitle());
        location.setText(camp.getCity());

        campImage = (ImageView) rootView.findViewById(R.id.campImage);
        campImage.setImageBitmap(image);


        cancel = (TextView) rootView.findViewById(R.id.cancelSignUp);
        start = (TextView) rootView.findViewById(R.id.startSignUp);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.onBackPressed();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, SignUpActivity.class);
                Bundle arguments = new Bundle();
                arguments.putSerializable("camp", camp);
                intent.putExtras(arguments);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
