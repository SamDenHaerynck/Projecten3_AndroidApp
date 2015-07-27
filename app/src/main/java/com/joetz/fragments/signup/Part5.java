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
public class Part5 extends Fragment implements Validator.ValidationListener {

    private View rootView;
    private SignUpActivity currentActivity;
    private TextView next;
    private Validator validator;
    private LinearLayout addPerson, infoSecondPerson;
    @Required(order = 1, message = "Gelieve de achternaam in te vullen")
    @Regex(order = 2, message = "Achternaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText lName1;
    @Required(order = 3, message = "Gelieve de voornaam in te vullen")
    @Regex(order = 4, message = "Voornaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText fName1;
    @Required(order = 5, message = "Gelieve het telefoonnummer in te vullen")
    private EditText phone1;
    @Required(order = 6, message = "Gelieve de achternaam in te vullen")
    @Regex(order = 7, message = "Achternaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText lName2;
    @Required(order = 8, message = "Gelieve de voornaam in te vullen")
    @Regex(order = 9, message = "Voornaam is ongeldig", pattern="[A-Za-z ]*")
    private EditText fName2;
    @Required(order = 10, message = "Gelieve het telefoonnummer in te vullen")
    private EditText phone2;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.currentActivity = (SignUpActivity) getActivity();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.part5, container, false);
        this.addPerson = (LinearLayout) rootView.findViewById(R.id.addPerson);

        this.lName1 = (EditText) rootView.findViewById(R.id.name1);
        this.fName1 = (EditText) rootView.findViewById(R.id.firstName1);
        this.phone1 = (EditText) rootView.findViewById(R.id.phone1);
        this.lName2 = (EditText) rootView.findViewById(R.id.name2);
        this.fName2 = (EditText) rootView.findViewById(R.id.firstName2);
        this.phone2 = (EditText) rootView.findViewById(R.id.phone2);

        this.infoSecondPerson = (LinearLayout) rootView.findViewById(R.id.infoSecondPerson);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(infoSecondPerson);
            }
        });
        infoSecondPerson.setVisibility(View.GONE);

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
        if(v.isShown()){
            slide(currentActivity, v, 'u');
            v.setVisibility(View.GONE);
        }
        else {
            v.setVisibility(View.VISIBLE);
            slide(currentActivity, v, 'd');
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

    protected Map createContact(String fName, String lName, String phone){
        Map contact1 = new HashMap();
        contact1.put("fName", fName);
        contact1.put("lName", lName);
        contact1.put("phone", phone);
        return contact1;
    }

    @Override
    public void onValidationSucceeded() {
        Map contacts = new HashMap();
        contacts.put("contact1", createContact(fName1.getText().toString(), lName1.getText().toString(), phone1.getText().toString()));
        if(!fName2.getText().toString().isEmpty()){
            contacts.put("contact2", createContact(fName2.getText().toString(), lName2.getText().toString(), phone2.getText().toString()));
        }
        currentActivity.addEmergencyContact(contacts);
        Fragment f = new Part6();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.answerpart, f).commit();
        currentActivity.nextQuestion();
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
         if((failedView.equals(lName2) || failedView.equals(fName2) || failedView.equals(phone2)) && !infoSecondPerson.isShown()){
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
