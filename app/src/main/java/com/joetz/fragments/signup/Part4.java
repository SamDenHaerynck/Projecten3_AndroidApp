package com.joetz.fragments.signup;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
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
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;

import java.util.HashMap;
import java.util.Map;
/**
 * The subscribtion is divided into 6 parts.
 * Each of these parts contains a couple of fields which are required to subscribe.
 * The methods in this class have exactly the same responsibility as the the first part.
 */
public class Part4 extends Fragment implements Validator.ValidationListener{
    private View rootView;
    private SignUpActivity currentActivity;
    private TextView next;
    private Validator validator;
    private LinearLayout addParticipant, morePartcipants, address;
    private RadioButton yes, no;
    @Required(order=1, message="Gelieve het rijksregisternummer in te vullen")
    @Regex(order=2, message="Ongeldig rijksregisternummer", pattern = "[0-9]{11}")
    private EditText registerNr;
    @Required(order = 3, message = "Gelieve de achternaam in te vullen")
    @Regex(order = 4, message = "Achternaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText name;
    @Required(order = 5, message = "Gelieve de voornaam in te vullen")
    @Regex(order = 6, message = "Voornaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText firstName;
    @Required(order = 7, message = "Gelieve de geboortedatum in te vullen")
    @Regex(order = 8, message="Geboortedatum is ongeldig", pattern = "[0-9]{2}-[0-9]{2}-[0-9]{4}")
    private EditText birthDate;
    @Required(order = 9, message = "Gelieve de straatnaam in te vullen")
    @Regex(order = 10, message = "Straatnaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText street;
    @Required(order = 11, message = "Gelieve het huisnummer in te vullen")
    private EditText nr;
    private EditText bus;
    @Required(order = 12, message = "Gelieve de gemeente in te vullen")
    @Regex(order = 13, message = "Gemeente is ongeldig", pattern="[A-Za-z ]*")
    private EditText city;
    @Required(order = 14, message = "Gelieve de postcode in te vullen")
    @Regex(order = 15, message = "Postcode is ongeldig", pattern="[1-9]{1}[0-9]{3}")
    private EditText postalCode;
    protected int currentExtraParticipants;
    protected EditText[][] edits = new EditText[5][4];

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.currentActivity = (SignUpActivity) getActivity();
        validator = new Validator(this);
        validator.setValidationListener(this);
        this.currentExtraParticipants = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.part4, container, false);
        this.addParticipant = (LinearLayout) rootView.findViewById(R.id.addParticipant);
        this.morePartcipants = (LinearLayout) rootView.findViewById(R.id.moreParticipants);
        this.registerNr = (EditText) rootView.findViewById(R.id.rijksnr);
        edits[0][0] = registerNr;
        this.name = (EditText) rootView.findViewById(R.id.name);
        edits[0][1] = name;
        this.firstName = (EditText) rootView.findViewById(R.id.firstname);
        edits[0][2] = firstName;
        this.birthDate = (EditText) rootView.findViewById(R.id.birthdate);
        edits[0][3] = birthDate;
        this.street = (EditText) rootView.findViewById(R.id.street);
        this.nr = (EditText) rootView.findViewById(R.id.nr);
        this.bus = (EditText) rootView.findViewById(R.id.bus);
        this.city = (EditText) rootView.findViewById(R.id.city);
        this.postalCode = (EditText) rootView.findViewById(R.id.postalcode);
        this.yes = (RadioButton) rootView.findViewById(R.id.yes);
        this.no = (RadioButton) rootView.findViewById(R.id.no);
        this.address = (LinearLayout) rootView.findViewById(R.id.addressInformation);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(address);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(address);
            }
        });

        addParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentExtraParticipants < 4){
                LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsText.setMargins(10,20,0,10);
                LinearLayout.LayoutParams paramsEdit = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsEdit.setMargins(0, 0, 0, 10);

                /*Shows the number of this participant*/
                TextView participant = new TextView(currentActivity);
                participant.setText("Deelnemer " + (currentExtraParticipants + 2));
                participant.setLayoutParams(paramsText);
                participant.setTypeface(null, Typeface.BOLD);
                morePartcipants.addView(participant);

                TextView natnr = new TextView(currentActivity);
                natnr.setText(R.string.registernumber);
                natnr.setLayoutParams(paramsText);
                morePartcipants.addView(natnr);

                /*Add editText for National Number*/
                EditText natNr = new EditText(currentActivity);
                natNr.setHint(R.string.natnr_example);
                natNr.setLayoutParams(paramsEdit);
                natNr.setTextColor(getResources().getColor(R.color.grey_dark));
                morePartcipants.addView(natNr);
                edits[currentExtraParticipants + 1][0] = natNr;

                TextView name = new TextView(currentActivity);
                name.setText(R.string.name);
                name.setLayoutParams(paramsText);
                morePartcipants.addView(name);

                /*Add editText for Name*/
                EditText editName = new EditText(currentActivity);
                editName.setLayoutParams(paramsEdit);
                editName.setTextColor(getResources().getColor(R.color.grey_dark));
                morePartcipants.addView(editName);
                edits[currentExtraParticipants + 1][1] = editName;

                TextView fName = new TextView(currentActivity);
                fName.setText(R.string.firstname);
                fName.setLayoutParams(paramsText);
                morePartcipants.addView(fName);

                /*Add editText for FirstName*/
                EditText editFName = new EditText(currentActivity);
                editFName.setLayoutParams(paramsEdit);
                editFName.setTextColor(getResources().getColor(R.color.grey_dark));
                morePartcipants.addView(editFName);
                edits[currentExtraParticipants + 1][2] = editFName;

                TextView bday = new TextView(currentActivity);
                bday.setText(R.string.birthdate);
                bday.setLayoutParams(paramsText);
                morePartcipants.addView(bday);

                /*Add editText for Birthdate*/
                EditText bd = new EditText(currentActivity);
                bd.setHint(R.string.birthdate_example);
                bd.setLayoutParams(paramsEdit);
                bd.setTextColor(getResources().getColor(R.color.grey_dark));
                morePartcipants.addView(bd);
                edits[currentExtraParticipants + 1][3] = bd;

                currentExtraParticipants++;
                if(currentExtraParticipants == 4) addParticipant.setVisibility(View.GONE);
                }
            }
        });

        address.setVisibility(View.GONE);
        next = (TextView) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        return rootView;
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

    protected Map createParticipant(String fName, String lName, String natNr, String birthDate){
        Map participant = new HashMap();
        participant.put("fName", fName);
        participant.put("lName", lName);
        participant.put("birthdate", birthDate);
        participant.put("natNr", natNr);
        return participant;
    }

    @Override
    public void onValidationSucceeded() {
        Map allParticipants = new HashMap();
        for(int i = 0; i <= currentExtraParticipants; i++){
            EditText lN = edits[i][1];
            EditText fN = edits[i][2];
            EditText rN = edits[i][0];
            EditText bD = edits[i][3];
            allParticipants.put("participant".concat(String.valueOf(i + 1)), createParticipant(fN.getText().toString(), lN.getText().toString(), rN.getText().toString(), bD.getText().toString()));
        }
        //allParticipants.put("participant1", createParticipant(firstName.getText().toString(), name.getText().toString(), registerNr.getText().toString(), birthDate.getText().toString()));
        currentActivity.addParticipants(allParticipants);
        if(address.isShown()){
            currentActivity.addAddressParticipants(city.getText().toString(), postalCode.getText().toString(), street.getText().toString(), nr.getText().toString(), bus.getText().toString());
        }
        Fragment f = new Part5();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.answerpart, f).commit();
        currentActivity.nextQuestion();
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        if((failedView.equals(street) || failedView.equals(nr) || failedView.equals(bus) || failedView.equals(city) || failedView.equals(postalCode)) && !address.isShown()){
            onValidationSucceeded();
        } else {
            String message = failedRule.getFailureMessage();
            if (failedView instanceof EditText) {
                failedView.requestFocus();
                ((EditText) failedView).setError(message);
            } else {
                Toast.makeText(currentActivity, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
