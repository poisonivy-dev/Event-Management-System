package com.example.eventmanagementsystem.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.eventmanagementsystem.RegistrationListActivity;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.data.EventDatabaseHandler;
import com.example.eventmanagementsystem.data.RegistrationDatabaseHandler;
import com.example.eventmanagementsystem.data.deleteParticipantRemote;
import com.example.eventmanagementsystem.data.deleteRegistrationRemote;
import com.example.eventmanagementsystem.data.updateEventRemote;
import com.example.eventmanagementsystem.data.updateRegistrationRemote;
import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.model.Registration;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RegistrationRecyclerViewAdapter extends RecyclerView.Adapter<com.example.eventmanagementsystem.UI.RegistrationRecyclerViewAdapter.ViewHolder>  {


    private Context context;
    private List<Registration> registrationList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RegistrationRecyclerViewAdapter(Context context, List<Registration> registrationList) {
        this.context = context;
        this.registrationList = registrationList;

    }

    @NonNull
    @Override
    public RegistrationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.registration_row, viewGroup, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistrationRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Registration registration = registrationList.get(position); // object Item

        viewHolder.registrationName.setText(MessageFormat.format("Name: {0} ",registration.getrName()));
        viewHolder.rRoll.setText(MessageFormat.format("Roll Number: {0}",registration.getrRollNumber()));
        viewHolder.rDepartment.setText(MessageFormat.format("Department: {0}", registration.getrDepartment()));
        viewHolder.dateAdded.setText(MessageFormat.format("Added on: {0}", registration.getDateItemAdded()));
        int sync_status=registrationList.get(position).getSync_status();
        if(sync_status== Constants.SYNC_STATUS_OK){
            viewHolder.Sync_status.setImageResource(R.drawable.ok);
        }
        else{

            viewHolder.Sync_status.setImageResource(R.drawable.sync);
        }


    }

    @Override
    public int getItemCount() {
        return registrationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        private TextView registrationName;
        private TextView rRoll;
        private TextView rDepartment;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public Button infoButton;
        ImageView Sync_status;

        public ViewHolder(@NonNull View registrationView, Context ctx) {
            super(registrationView);
            context = ctx;
            registrationName = registrationView.findViewById(R.id.registration_name);
            rRoll = registrationView.findViewById(R.id.r_Roll);
            rDepartment = registrationView.findViewById(R.id.r_Department);
            dateAdded = registrationView.findViewById(R.id.rAdded_date);
            Sync_status=registrationView.findViewById(R.id.rimgSync);

            infoButton=registrationView.findViewById(R.id.rInfoButton);
            editButton = registrationView.findViewById(R.id.rEditButton);
            deleteButton = registrationView.findViewById(R.id.rDeleteButton);
            infoButton.setOnClickListener(this);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position;
            position = getAdapterPosition();
           Registration registration = registrationList.get(position);

            switch (v.getId()) {
                case R.id.rInfoButton:
                    //show registered users
                    showRegistered(registration);
                    break;
                case R.id.rEditButton:
                    //edit registered users
                    editRegistered(registration);
                    break;
                case R.id.rDeleteButton:
                    //delete registration
                    deleteRegistration(registration.getRegistrationID());
                    break;
            }

        }

        private void showRegistered( Registration newRegistration) {
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.show_registration, null);
            Button rBackButton;
            TextView rTitle;
             TextView registrationName;
             TextView rRoll;
             TextView rDepartment;
             TextView rContact;
             TextView rStatus;

            rTitle = view.findViewById(R.id.showRTitle);
            registrationName = view.findViewById(R.id.showRegistrationTitle);
            rRoll = view.findViewById(R.id.showRegistrationRollNumber);
            rDepartment = view.findViewById(R.id.showRegistrationDepartment);
            rContact = view.findViewById(R.id.showRegisrationContact);
            rStatus = view.findViewById(R.id.showRegistrationStatus);
            rBackButton = view.findViewById(R.id.rBackButton);

            rTitle.setText(R.string.r_info);
            registrationName.setText("Name: "+newRegistration.getrName());
            rRoll.setText("Roll Number: "+newRegistration.getrRollNumber());
            rDepartment.setText("Department: "+newRegistration.getrDepartment());
            rContact.setText("Contact: "+newRegistration.getrContact());
            rStatus.setText(" Registration Status: "+newRegistration.getrStatus());

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            rBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                            go back to activity
                    dialog.dismiss();

                }
            });
        }

        private void deleteRegistration(final int id) {

            builder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_pop, null);

            Button noButton = view.findViewById(R.id.conf_no_button);
            Button yesButton = view.findViewById(R.id.conf_yes_button);
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            WorkRequest deleteRequest =
                    new OneTimeWorkRequest.Builder(deleteRegistrationRemote.class)
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
                    RegistrationDatabaseHandler db = new RegistrationDatabaseHandler(context);
                    WorkManager.getInstance(context).enqueue(deleteRequest);
                    db.deleteRegistration(id);
                    registrationList.remove(getAdapterPosition());
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

        private void editRegistered(final Registration newRegistration) {

            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.details_registration_activity, null);
            Button rCancelButton;
            Button rSaveButton;
            TextView registrationName;
            TextView rRoll;
            TextView rDepartment;
            TextView rContact;
            TextView rStatus;
            TextView rTitle;

            rTitle = view.findViewById(R.id.rTitle);
            registrationName = view.findViewById(R.id.registrationTitle);
            rRoll = view.findViewById(R.id.rRoll);
            rDepartment = view.findViewById(R.id.rDepartment);
            rContact = view.findViewById(R.id.rContact);
            rStatus = view.findViewById(R.id.rStatus);
            rSaveButton = view.findViewById(R.id.rSaveButton);
            rCancelButton = view.findViewById(R.id.rCancelButton);


            rTitle.setText(R.string.r_edit);
            registrationName.setText(newRegistration.getrName());
            rRoll.setText(newRegistration.getrRollNumber());
            rDepartment.setText(newRegistration.getrDepartment());
            rContact.setText(newRegistration.getrContact());
            rStatus.setText(newRegistration.getrStatus());
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            builder.setView(view);
            dialog = builder.create();
            dialog.show();
            rCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            rSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update our Registration
                    RegistrationDatabaseHandler registrationDatabaseHandler = new RegistrationDatabaseHandler(context);

                    //update registrations
                    newRegistration.setrName(registrationName.getText().toString().trim());
                    newRegistration.setrRollNumber(rRoll.getText().toString().trim());
                    newRegistration.setrDepartment(rDepartment.getText().toString().trim());
                    newRegistration.setrContact(rContact.getText().toString().trim());
                    newRegistration.setrStatus(rStatus.getText().toString().trim());

                    if (!registrationName.getText().toString().isEmpty()
                            && !rRoll.getText().toString().isEmpty()
                            && !rDepartment.getText().toString().isEmpty()
                            && !rContact.getText().toString().isEmpty()
                            && !rStatus.getText().toString().isEmpty()) {

                        WorkRequest updateRequest =
                                new OneTimeWorkRequest.Builder(updateRegistrationRemote.class)
                                        .setConstraints(constraints)
                                        .setInputData(
                                                new Data.Builder()
                                                        .putString("registration", newRegistration.toString())
                                                        .build()
                                        )
                                        .build();
                        WorkManager.getInstance(context).enqueue(updateRequest);

                        registrationDatabaseHandler.updateRegistration(newRegistration);
                        notifyItemChanged(getAdapterPosition(),newRegistration); //important!


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

