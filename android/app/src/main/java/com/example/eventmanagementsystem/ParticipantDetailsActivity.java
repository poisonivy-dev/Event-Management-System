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
import com.example.eventmanagementsystem.data.ParticipantDatabaseHandler;
import com.example.eventmanagementsystem.model.Participant;
import com.google.android.material.snackbar.Snackbar;

public class ParticipantDetailsActivity extends AppCompatActivity {

    Button pCancelButton;
    Button pSaveButton;
    EditText participantName;
    EditText participantRoll;
    EditText rDepartment;
    EditText pContact;
    EditText rRole;
    TextView participantTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_participant_activity);
        
        Intent getintent=getIntent();
        int eventID=getintent.getIntExtra("eventID",-1);
        participantTitle = findViewById(R.id.pTitle);
        participantName = findViewById(R.id.participationTitle);
        participantRoll = findViewById(R.id.participantRole);
        rDepartment = findViewById(R.id.participantDepartment);
        pContact = findViewById(R.id.participantContact);
        rRole = findViewById(R.id.participantRole);
        pSaveButton = findViewById(R.id.pSaveButton);
        pCancelButton = findViewById(R.id.pCancelButton);

        pSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!participantName.getText().toString().isEmpty()
                        && !participantRoll.getText().toString().isEmpty()
                        && !rDepartment.getText().toString().isEmpty()
                        && !pContact.getText().toString().isEmpty()
                        && !rRole.getText().toString().isEmpty()) {

                    saveParticipant(v,eventID);

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

        pCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.putExtra("eventID",eventID);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }






    private void saveParticipant(View view,int eventID) {
        //Todo: save each event item to db
        Participant participant= new Participant();

        String rName = participantName.getText().toString().trim();
        String participantRollN = participantRoll.getText().toString().trim();
        String rDept = rDepartment.getText().toString().trim();
        String pCont = pContact.getText().toString().trim();
        String rStat = rRole.getText().toString().trim();

        participant.setParticipantName(rName);
        participant.setParticipantRollNumber(participantRollN);
        participant.setParticipantDepartment(rDept);
        participant.setParticipantContact(pCont);
        participant.setParticipantRole(rStat);
        participant.setSync_status(Constants.SYNC_STATUS_FAILED);

        ParticipantDatabaseHandler participantDatabaseHandler= new ParticipantDatabaseHandler(getApplicationContext());
        participantDatabaseHandler.addParticipant(participant,eventID);

        Log.d("participantAdded", "saveParticipant: "+ participant.getParticipantName() );
        Snackbar.make(view, "Item Saved",Snackbar.LENGTH_LONG)
                .show();

    }
}