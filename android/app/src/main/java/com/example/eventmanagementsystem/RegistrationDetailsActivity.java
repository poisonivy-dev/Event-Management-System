package com.example.eventmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.data.RegistrationDatabaseHandler;
import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.model.Registration;
import com.google.android.material.snackbar.Snackbar;

public class RegistrationDetailsActivity extends AppCompatActivity {
    Button rCancelButton;
    Button rSaveButton;
    EditText registrationName;
    EditText rRoll;
    EditText rDepartment;
    EditText rContact;
    EditText rStatus;
    TextView rTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_registration_activity);
        Intent getintent=getIntent();
        int eventID=getintent.getIntExtra("eventID",-1);
        rTitle = findViewById(R.id.rTitle);
        registrationName = findViewById(R.id.registrationTitle);
        rRoll = findViewById(R.id.rRoll);
        rDepartment = findViewById(R.id.rDepartment);
        rContact = findViewById(R.id.rContact);
        rStatus = findViewById(R.id.rStatus);
        rSaveButton = findViewById(R.id.rSaveButton);
        rCancelButton = findViewById(R.id.rCancelButton);

        rSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!registrationName.getText().toString().isEmpty()
                        && !rRoll.getText().toString().isEmpty()
                        && !rDepartment.getText().toString().isEmpty()
                        && !rContact.getText().toString().isEmpty()
                        && !rStatus.getText().toString().isEmpty()) {

                    saveRegistration(v,eventID);

                    Intent i=new Intent();
                    i.putExtra("eventID",eventID);
                    setResult(RESULT_OK,i);
                    finish();

                }else {
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });

        rCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.putExtra("eventID",eventID);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }






    private void saveRegistration(View view,int eventID) {
        //Todo: save each event item to db
        Registration registration= new Registration();

        String rName = registrationName.getText().toString().trim();
        String rRollN = rRoll.getText().toString().trim();
        String rDept = rDepartment.getText().toString().trim();
        String rCont = rContact.getText().toString().trim();
        String rStat = rStatus.getText().toString().trim();

        registration.setrName(rName);
        registration.setrRollNumber(rRollN);
        registration.setrDepartment(rDept);
        registration.setrContact(rCont);
        registration.setrStatus(rStat);
        registration.setSync_status(Constants.SYNC_STATUS_FAILED);

        RegistrationDatabaseHandler registrationDatabaseHandler= new RegistrationDatabaseHandler(getApplicationContext());
        registrationDatabaseHandler.addRegistration(registration,eventID);

        Log.d("regAdded", "saveRegistration: "+ registration.getrName() );
        Snackbar.make(view, "Item Saved",Snackbar.LENGTH_LONG)
                .show();

    }
}