package com.joetz;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.joetz.domain.Camp;
import com.joetz.domain.RequestMethod;
import com.joetz.domain.RestClient;
import com.joetz.fragments.signup.Part1;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for doing the subscription.
 */
public class SignUpActivity extends ActionBarActivity {

    private Camp camp;
    private TextView question;
    private int questionNr = 0;
    private static Map params;
    private static Map parents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle(R.string.signup);
        this.camp = (Camp) getIntent().getSerializableExtra("camp");
        this.question = (TextView) findViewById(R.id.question);
        params = new HashMap();
        parents = new HashMap();
        nextQuestion();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * Asks for confirmation to quit the procedure when the backkey is pressed.
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bent u zeker dat u de inschrijvingsprocedure wilt stopzetten?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { finish(); }
                }).setNegativeButton("Nee", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void nextQuestion(){
        if(questionNr == 0){
            Fragment f = new Part1();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.answerpart, f).commit();
        }
        String[] questions = getResources().getStringArray(R.array.questions);
        String q = questions[questionNr];
        question.setText(q);
        questionNr++;
    }

    public void addMemberNumbers(String memberNumber1, String memberNumber2){
        params.put("memberNumber1", memberNumber1);
        params.put("memberNumber2", memberNumber2);
    }

    public void addParent(String key, String fName, String lName, String phone, String natNr, String mail, String place, String postalCode, String street, String nr, String bus){
        Map parents;
        if(params.get("parents") instanceof Map) {
            parents = (Map) params.get("parents");
        } else {
            parents = new HashMap();
        }
        Map parent = new HashMap();
        parent.put("fName", fName);
        parent.put("lName", lName);
        parent.put("phone", phone);
        parent.put("natNr", natNr);
        parent.put("mail", mail);
        parent.put("place", place);
        parent.put("postal", postalCode);
        parent.put("street", street);
        parent.put("nr", nr);
        parent.put("bus", bus);
        parents.put(key, parent);
        params.put("parents", parents);
    }

    public void addEmergencyContact(Map contacts){
        params.put("emergencyContacts", contacts);
    }

    public void addExtraInfo(String extraInfo){
        params.put("extraInfo", extraInfo);
    }

    public void addAddressParticipants(String place, String postal, String street, String nr, String bus){
        params.put("place", place);
        params.put("postal", postal);
        params.put("street", street);
        params.put("nr", nr);
        params.put("bus", bus);
    }

    public void addParticipants(Map participants){
        params.put("participants", participants);
    }

    public void completeSignUp(){
        new SignUp().execute();
    }


    /**
     * This method makes an asynchronous API call to save a registration.
     */
    private class SignUp extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            RestClient client = new RestClient(SignUpActivity.this, "http://project-groep6.azurewebsites.net/api/camps/" + camp.getId() + "/signup");
            client.setParams(SignUpActivity.params);
            client.Execute(RequestMethod.POST);
            return null;
        }
    }
}
