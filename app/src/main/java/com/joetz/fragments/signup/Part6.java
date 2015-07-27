package com.joetz.fragments.signup;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.joetz.R;
import com.joetz.SignUpActivity;
/**
 * The subscribtion is divided into 6 parts.
 * Each of these parts contains a couple of fields which are required to subscribe.
 * The methods in this class have exactly the same responsibility as the the first part.
 */
public class Part6 extends Fragment {
    private View rootView;
    private SignUpActivity currentActivity;
    private EditText extraInfo;
    private TextView next;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.currentActivity = (SignUpActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.part6, container, false);
        this.extraInfo = (EditText) rootView.findViewById(R.id.extra);
        this.next = (TextView) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = extraInfo.getText().toString();
                if(!info.isEmpty()) currentActivity.addExtraInfo(info);
                currentActivity.completeSignUp();
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setMessage("U hebt de inschrijving succesvol afgerond.")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { currentActivity.finish(); }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return rootView;
    }
}
