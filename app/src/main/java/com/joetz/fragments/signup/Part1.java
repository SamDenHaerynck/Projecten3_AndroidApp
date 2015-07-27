package com.joetz.fragments.signup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.joetz.R;
import com.joetz.SignUpActivity;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;

/**
 * The subscribtion is divided into 6 parts.
 * Each of these parts contains a couple of fields which are required to subscribe.
 * For the validation rules we used a library developed by Saripaar (https://github.com/ragunathjawahar/android-saripaar)
 */
public class Part1 extends Fragment implements Validator.ValidationListener {

    private TextView next;
    private View rootView;
    private ImageView info, info2;
    private SignUpActivity currentActivity;
    private LinearLayout numberinfo, answerPart;
    private Validator validator;
    private RadioButton yes, no;
    @Required(order = 1, message = "Gelieve het aansluitingsnummer in te vullen")
    @Regex(order = 2, message="Ongeldig aansluitingsnummer", pattern = "[0-9]{3}/[0-9]{7}")
    private EditText number1;
    @Required(order = 3, message = "Gelieve het aansluitingsnummer in te vullen")
    @Regex(order = 4, message="Ongeldig aansluitingsnummer", pattern = "[0-9]{3}/[0-9]{7}")
    private EditText number2;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.currentActivity = (SignUpActivity) getActivity();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.part1, container, false);
        yes = (RadioButton) rootView.findViewById(R.id.yes);
        no = (RadioButton) rootView.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(answerPart);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(answerPart);
            }
        });
        this.numberinfo = (LinearLayout) rootView.findViewById(R.id.number_info);
        this.info = (ImageView) rootView.findViewById(R.id.infoIcon);
        this.info2 = (ImageView) rootView.findViewById(R.id.infoIcon2);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(numberinfo);
            }
        });
        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(numberinfo);
            }
        });
        numberinfo.setVisibility(View.GONE);
        this.answerPart = (LinearLayout) rootView.findViewById(R.id.answerPart1);
        this.number1 = (EditText) rootView.findViewById(R.id.number1);
        this.number2 = (EditText) rootView.findViewById(R.id.number2);

        next = (TextView) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        return rootView;
    }

    /**
     * This method allows content to be toggled as in making multiple fields visible or invisible
     * when a View (button, radiobutton, ...) is activated.
     * @param v
     */
    public void toggle_contents(View v) {
        // when radio button is clicked
        if(v == answerPart){
            // when clicked on no
            if(no.isChecked() && v.isShown()) {
                slide(currentActivity, v, 'u');
                v.setVisibility(View.GONE);
            } else if (yes.isChecked() && !v.isShown()){
                v.setVisibility(View.VISIBLE);
                slide(currentActivity, v, 'd');
            }
        } else {
            if(v.isShown()){
                slide(currentActivity, v, 'u');
                v.setVisibility(View.GONE);
            }
            else {
                v.setVisibility(View.VISIBLE);
                slide(currentActivity, v, 'd');
            }
        }

    }

    /**
     * This method activates an animation to toggle content seamlesly.
     * @param context
     * @param v
     * @param key
     */
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

    /**
     * This method is responsible for checking wheter everything is filled in as it should be.
     */
    @Override
    public void onValidationSucceeded() {
        currentActivity.addMemberNumbers(number1.getText().toString(), number2.getText().toString());
        Fragment f = new Part2();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.answerpart, f).commit();
        currentActivity.nextQuestion();
    }

    /**
     * When a field is not filled in correctly this method will provide an error message.
     * @param failedView
     * @param failedRule
     */
    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        if(yes.isChecked()) {
            String message = failedRule.getFailureMessage();
            if (failedView instanceof EditText) {
                failedView.requestFocus();
                ((EditText) failedView).setError(message);
            } else {
                Toast.makeText(currentActivity, message, Toast.LENGTH_SHORT).show();
            }
        } else if(no.isChecked()) {
            onValidationSucceeded();
        }
    }
}
