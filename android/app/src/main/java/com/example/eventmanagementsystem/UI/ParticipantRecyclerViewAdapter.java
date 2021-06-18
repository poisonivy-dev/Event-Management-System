package com.example.eventmanagementsystem.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.eventmanagementsystem.R;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.data.ParticipantDatabaseHandler;
import com.example.eventmanagementsystem.data.deleteParticipantRemote;
import com.example.eventmanagementsystem.data.syncStorage;
import com.example.eventmanagementsystem.data.updateParticipantRemote;
import com.example.eventmanagementsystem.data.updateRegistrationRemote;
import com.example.eventmanagementsystem.model.Participant;
import com.example.eventmanagementsystem.model.Participant;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class ParticipantRecyclerViewAdapter extends RecyclerView.Adapter<com.example.eventmanagementsystem.UI.ParticipantRecyclerViewAdapter.ViewHolder> {

   
        private Context context;
        private List<Participant> participantList;
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private LayoutInflater inflater;

        public ParticipantRecyclerViewAdapter(Context context, List<Participant> participantList) {
            this.context = context;
            this.participantList = participantList;

        }

        @NonNull
        @Override
        public com.example.eventmanagementsystem.UI.ParticipantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.participant_row, viewGroup, false);


            return new com.example.eventmanagementsystem.UI.ParticipantRecyclerViewAdapter.ViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.eventmanagementsystem.UI.ParticipantRecyclerViewAdapter.ViewHolder viewHolder, int position) {

            Participant participant = participantList.get(position); // object Item

            viewHolder.participantName.setText(MessageFormat.format("Participant Name: {0} ",participant.getParticipantName()));
            viewHolder.participantRoll.setText(MessageFormat.format( "Roll Number: ",participant.getParticipantRollNumber()));
            viewHolder.participantDepartment.setText(MessageFormat.format("Department: {0}", participant.getParticipantDepartment()));
            viewHolder.dateAdded.setText(MessageFormat.format("Added on: {0}", participant.getDateItemAdded()));
            int sync_status=participantList.get(position).getSync_status();
            if(sync_status== Constants.SYNC_STATUS_OK){
                viewHolder.Sync_status.setImageResource(R.drawable.ok);
            }
            else{

                viewHolder.Sync_status.setImageResource(R.drawable.sync);
            }


        }

        @Override
        public int getItemCount() {
            return participantList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



            private TextView participantName;
            private TextView participantRoll;
            private TextView participantDepartment;
            public TextView dateAdded;
            public Button pEditButton;
            public Button pDeleteButton;
            public Button pInfoButton;
            ImageView Sync_status;

            public ViewHolder(@NonNull View participantView, Context ctx) {
                super(participantView);
                context = ctx;
                participantName = participantView.findViewById(R.id.participant_name);
                participantRoll = participantView.findViewById(R.id.participant_Roll);
                participantDepartment = participantView.findViewById(R.id.participant_Department);
                dateAdded = participantView.findViewById(R.id.rAdded_date);
                Sync_status=participantView.findViewById(R.id.pimgSync);

                pInfoButton=participantView.findViewById(R.id.pInfoButton);
                pEditButton = participantView.findViewById(R.id.pEditButton);
                pDeleteButton = participantView.findViewById(R.id.pDeleteButton);
                pInfoButton.setOnClickListener(this);
                pEditButton.setOnClickListener(this);
                pDeleteButton.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                int position;
                position = getAdapterPosition();
                Participant participant = participantList.get(position);

                switch (v.getId()) {
                    case R.id.pInfoButton:
                        //show registered users
                        showRegistered(participant);
                        break;
                    case R.id.pEditButton:
                        //edit registered users
                        editRegistered(participant);
                        break;
                    case R.id.pDeleteButton:
                        //delete participant
                        deleteParticipant(participant.getParticipantID());
                        break;
                }

            }

            private void showRegistered( Participant newParticipant) {
                builder = new AlertDialog.Builder(context);
                inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.show_participant, null);
                Button pbackButton;
                TextView pTitle;
                TextView participantName;
                TextView participantRoll;
                TextView participantDepartment;
                TextView pContact;
                TextView participantRole;

                pTitle = view.findViewById(R.id.showPTitle);
                participantName = view.findViewById(R.id.showParticipantTitle);
                participantRoll = view.findViewById(R.id.showParticipantRollNumber);
                participantDepartment = view.findViewById(R.id.showParticipantDepartment);
                pContact = view.findViewById(R.id.showParticipantContact);
                participantRole = view.findViewById(R.id.showParticipantRole);
                pbackButton = view.findViewById(R.id.pBackButton);

                pTitle.setText(R.string.participant_info);
                participantName.setText("Name: "+newParticipant.getParticipantName());
                participantRoll.setText("Roll Number: "+newParticipant.getParticipantRollNumber());
                participantDepartment.setText("Department: "+newParticipant.getParticipantDepartment());
                pContact.setText("Contact: "+newParticipant.getParticipantContact());
                participantRole.setText("Participant Role: "+newParticipant.getParticipantRole());

                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                pbackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                            go back to activity
                        dialog.dismiss();

                    }
                });
            }

            private void deleteParticipant(final int id) {

                builder = new AlertDialog.Builder(context);

                inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.confirmation_pop, null);

                Button noButton = view.findViewById(R.id.conf_no_button);
                Button yesButton = view.findViewById(R.id.conf_yes_button);
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                WorkRequest deleteRequest =
                        new OneTimeWorkRequest.Builder(deleteParticipantRemote.class)
                                .setConstraints(constraints)
                                .setInputData(
                                        new Data.Builder()
                                                .putString("id", String.valueOf(id))
                                                .build()
                                )
                                .build();
                builder.setView(view);
                dialog = builder.create();
                dialog.show();


                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParticipantDatabaseHandler db = new ParticipantDatabaseHandler(context);
                        WorkManager.getInstance(context).enqueue(deleteRequest);
                        db.deleteParticipant(id);
                        participantList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());

                        dialog.dismiss();
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }

            private void editRegistered(final Participant newParticipant) {

                builder = new AlertDialog.Builder(context);
                inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.details_participant_activity, null);
                Button pCancelButton;
                Button pSaveButton;
                TextView participantName;
                TextView participantRoll;
                TextView participantDepartment;
                TextView pContact;
                TextView participantRole;
                TextView pTitle;

                pTitle = view.findViewById(R.id.pTitle);
                participantName = view.findViewById(R.id.participationTitle);
                participantRoll = view.findViewById(R.id.participantRoll);
                participantDepartment = view.findViewById(R.id.participantDepartment);
                pContact = view.findViewById(R.id.participantContact);
                participantRole = view.findViewById(R.id.participantRole);
                pSaveButton = view.findViewById(R.id.pSaveButton);
                pCancelButton = view.findViewById(R.id.pCancelButton);


                pTitle.setText(R.string.participant_edit);
                participantName.setText(newParticipant.getParticipantName());
                participantRoll.setText(newParticipant.getParticipantRollNumber());
                participantDepartment.setText(newParticipant.getParticipantDepartment());
                pContact.setText(newParticipant.getParticipantContact());
                participantRole.setText(newParticipant.getParticipantRole());
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                pCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                pSaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //update our Participant
                        ParticipantDatabaseHandler participantDatabaseHandler = new ParticipantDatabaseHandler(context);

                        //update participants
                        newParticipant.setParticipantName(participantName.getText().toString().trim());
                        newParticipant.setParticipantRollNumber(participantRoll.getText().toString().trim());
                        newParticipant.setParticipantDepartment(participantDepartment.getText().toString().trim());
                        newParticipant.setParticipantContact(pContact.getText().toString().trim());
                        newParticipant.setParticipantRole(participantRole.getText().toString().trim());

                        if (!participantName.getText().toString().isEmpty()
                                && !participantRoll.getText().toString().isEmpty()
                                && !participantDepartment.getText().toString().isEmpty()
                                && !pContact.getText().toString().isEmpty()
                                && !participantRole.getText().toString().isEmpty()) {

                            WorkRequest updateRequest =
                                    new OneTimeWorkRequest.Builder(updateParticipantRemote.class)
                                            .setConstraints(constraints)
                                            .setInputData(
                                                    new Data.Builder()
                                                            .putString("participant", newParticipant.toString())
                                                            .build()
                                            )
                                            .build();
                            WorkManager.getInstance(context).enqueue(updateRequest);

                            participantDatabaseHandler.updateParticipant(newParticipant);
                            notifyItemChanged(getAdapterPosition(),newParticipant); //important!


                        }else {
                            Snackbar.make(view, "Fields Empty",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                        }

                        dialog.dismiss();

                    }
                });
            }


        }


    }


