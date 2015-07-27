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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.joetz.R;
import com.joetz.SignUpActivity;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
/**
 * The subscribtion is divided into 6 parts.
 * Each of these parts contains a couple of fields which are required to subscribe.
 * The methods in this class have exactly the same responsibility as the the first part.
 */
public class Part3 extends Fragment implements Validator.ValidationListener {

    private View rootView;
    private SignUpActivity currentActivity;
    private LinearLayout payableParent;
    private RadioButton yes, no;
    private TextView next;
    private Validator validator;
    @Required(order=1, message="Gelieve het rijksregisternummer in te vullen")
    @Regex(order=2, message="Ongeldig rijksregisternummer", pattern = "[0-9]{11}")
    private EditText registerNr;
    @Required(order = 3, message = "Gelieve de achternaam in te vullen")
    @Regex(order = 4, message = "Achternaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText name;
    @Required(order = 5, message = "Gelieve de voornaam in te vullen")
    @Regex(order = 6, message = "Voornaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText firstName;
    @Required(order = 7, message = "Gelieve de straatnaam in te vullen")
    @Regex(order = 8, message = "Straatnaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText street;
    @Required(order = 9, message = "Gelieve het huisnummer in te vullen")
    private EditText nr;
    private EditText bus;
    @Required(order = 10, message = "Gelieve de gemeente in te vullen")
    @Regex(order = 11, message = "Gemeente is ongeldig", pattern="[A-Za-z ]*")
    private EditText city;
    @Required(order = 12, message = "Gelieve de postcode in te vullen")
    @Regex(order = 13, message = "Postcode is ongeldig", pattern="[1-9]{1}[0-9]{3}")
    private EditText postalCode;
    @Required(order = 14, message = "Gelieve het e-mailadres in te vullen")
    @Email(order = 15)
    private EditText mail;
    @Required(order = 16, message = "Gelieve het telefoonnummer in te vullen")
    private EditText phone;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.currentActivity = (SignUpActivity) getActivity();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.part3, container, false);
        yes = (RadioButton) rootView.findViewById(R.id.yes);
        no = (RadioButton) rootView.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        this.payableParent = (LinearLayout) rootView.findViewById(R.id.payableParent);
        payableParent.setVisibility(View.GONE);

        this.registerNr = (EditText) rootView.findViewById(R.id.rijksnr);
        this.name = (EditText) rootView.findViewById(R.id.name);
        this.firstName = (EditText) rootView.findViewById(R.id.firstname);
        this.street = (EditText) rootView.findViewById(R.id.street);
        this.nr = (EditText) rootView.findViewById(R.id.nr);
        this.bus = (EditText) rootView.findViewById(R.id.bus);
        this.city = (EditText) rootView.findViewById(R.id.city);
        this.postalCode = (EditText) rootView.findViewById(R.id.postalcode);
        this.mail = (EditText) rootView.findViewById(R.id.mail);
        this.phone = (EditText) rootView.findViewById(R.id.phone);

        next = (TextView) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        return rootView;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.yes:
                if (checked) {
                    toggle_contents(payableParent);
                }
                break;
            case R.id.no:
                if (checked) {
                    toggle_contents(payableParent);
                }
                break;
        }
    }

    public void toggle_contents(View v) {
        if(no.isChecked() && !v.isShown()) {
            v.setVisibility(View.VISIBLE);
            slide(currentActivity, v, 'd');
        } else if (yes.isChecked() && v.isShown()){
            slide(currentActivity, v, 'u');
            v.setVisibility(View.GONE);
        }
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

    @Override
    public void onValidationSucceeded() {
        if(no.isChecked()){
        currentActivity.addParent("parent2", name.getText().toString(), firstName.getText().toString(),
                phone.getText().toString(), registerNr.getText().toString(), mail.getText().toString(),
                city.getText().toString(), postalCode.getText().toString(), street.getText().toString(),
                nr.getText().toString(), bus.getText().toString());}
        Fragment f = new Part4();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.answerpart, f).commit();
        currentActivity.nextQuestion();
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        if(no.isChecked()){
            String message = failedRule.getFailureMessage();
            if (failedView instanceof EditText) {
                failedView.requestFocus();
                ((EditText) failedView).setError(message);
            } else {
                Toast.makeText(currentActivity, message, Toast.LENGTH_SHORT).show();
            }
        } else if(yes.isChecked()){
            onValidationSucceeded();
        }
    }
}
