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

import com.example.eventmanagementsystem.ParticipantListActivity;
import com.example.eventmanagementsystem.R;
import com.example.eventmanagementsystem.RegistrationListActivity;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.data.EventDatabaseHandler;
import com.example.eventmanagementsystem.data.syncStorage;
import com.example.eventmanagementsystem.data.updateEventRemote;
import com.example.eventmanagementsystem.model.Event;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<com.example.eventmanagementsystem.UI.EventRecyclerViewAdapter.ViewHolder>  {

        private Context context;
        private List<Event> eventList;
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private LayoutInflater inflater;

        public EventRecyclerViewAdapter(Context context, List<Event> eventList) {
            this.context = context;
            this.eventList = eventList;

        }

        @NonNull
        @Override
        public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.event_row, viewGroup, false);


            return new ViewHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.ViewHolder viewHolder, int position) {

            Event event = eventList.get(position); // object Item

            viewHolder.eventTitle.setText(MessageFormat.format("Event Title: {0}", event.getEventName()));
            viewHolder.eventType.setText(MessageFormat.format("Event Type: {0}", event.getEventType()));
            viewHolder.eventDate.setText(MessageFormat.format("Event Date: {0}", event.getEventDate()));
            viewHolder.dateAdded.setText(MessageFormat.format("Added on: {0}", event.getDateItemAdded()));
            int sync_status=eventList.get(position).getSync_status();
            if(sync_status== Constants.SYNC_STATUS_OK){
                viewHolder.Sync_status.setImageResource(R.drawable.ok);
            }
            else{

                viewHolder.Sync_status.setImageResource(R.drawable.sync);
            }

        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



            private TextView eventTitle;
            private TextView eventType;
            private TextView eventDate;
            public TextView dateAdded;
            public Button editButton;
            public Button deleteButton;
            public Button infoButton;
            public Button registrationButton;
            public Button participantsButton;
            ImageView Sync_status;

            public ViewHolder(@NonNull View eventView, Context ctx) {
                super(eventView);
                context = ctx;
                eventTitle = eventView.findViewById(R.id.event_name);
                eventType = eventView.findViewById(R.id.event_type);
                eventDate = eventView.findViewById(R.id.event_date);
                dateAdded = eventView.findViewById(R.id.added_date);
                Sync_status=eventView.findViewById(R.id.imgSync);

                infoButton=eventView.findViewById(R.id.infoButton);
                editButton = eventView.findViewById(R.id.editButton);
                deleteButton = eventView.findViewById(R.id.deleteButton);
                registrationButton=eventView.findViewById(R.id.registrationButton);
                participantsButton=eventView.findViewById(R.id.participantsButton);
                infoButton.setOnClickListener(this);
                editButton.setOnClickListener(this);
                deleteButton.setOnClickListener(this);
                registrationButton.setOnClickListener(this);
                participantsButton.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                int position;
                position = getAdapterPosition();
                Event event = eventList.get(position);

                switch (v.getId()) {
                    case R.id.infoButton:
                        //show event
                        showEvent(event);
                        break;
                    case R.id.editButton:
                        //edit event
                        editEvent(event);
                        break;
                    case R.id.deleteButton:
                        //delete event
                        deleteEvent(event.getId());
                        break;
                    case R.id.registrationButton:
                        //delete item
                        registerForEvent(event.getId());
                        break;
                    case R.id.participantsButton:
                        //delete item
                        participantsInEvents(event.getId());
                        break;
                }

            }
            private void participantsInEvents(int id) {

                //Todo: acitivity for participants
                Intent intent= new Intent(context, ParticipantListActivity.class);
                intent.putExtra("eventID",id);
                context.startActivity(intent);
            }



            private void registerForEvent(int id) {
                Intent intent= new Intent(context, RegistrationListActivity.class);
                intent.putExtra("eventID",id);
                context.startActivity(intent);
            }

                private void showEvent(Event newEvent) {
                    builder = new AlertDialog.Builder(context);
                    inflater = LayoutInflater.from(context);
                    final View view = inflater.inflate(R.layout.show_event, null);
                    Button backButton;
                    TextView eventTitle;
                    TextView eventType;
                    TextView eventVenue;
                    TextView eventDate;
                    TextView eventStime;
                    TextView eventEtime;
                    TextView eventDescription;
                    TextView title;

                    eventTitle = view.findViewById(R.id.showEventTitle);
                    eventType = view.findViewById(R.id.showEventType);
                    eventVenue = view.findViewById(R.id.showEventVenue);
                    eventDate = view.findViewById(R.id.showEventDate);
                    eventStime = view.findViewById(R.id.showEventStime);
                    eventEtime = view.findViewById(R.id.showEventEtime);
                    eventDescription=view.findViewById(R.id.showEventDescription);
                    backButton = view.findViewById(R.id.backButton);
                    title = view.findViewById(R.id.showETitle);

                    title.setText(R.string.event_info);
                    eventTitle.setText("Event Title: "+newEvent.getEventName());
                    eventType.setText("Event Type: "+newEvent.getEventType());
                    eventVenue.setText("Event Venue: "+newEvent.getEventVenue());
                    eventDate.setText("Event Date: "+newEvent.getEventDate());
                    eventStime.setText("Event Start: "+newEvent.getEventStime());
                    eventEtime.setText("Event End: "+newEvent.getEventEtime());
                    eventDescription.setText("Event Description: "+newEvent.getEventDescription());

                    builder.setView(view);
                    dialog = builder.create();
                    dialog.show();

                    backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            go back to activity
                            dialog.dismiss();

                        }
                    });
                }

            private void deleteEvent(final int id) {

                builder = new AlertDialog.Builder(context);

                inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.confirmation_pop, null);

                Button noButton = view.findViewById(R.id.conf_no_button);
                Button yesButton = view.findViewById(R.id.conf_yes_button);
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                WorkRequest deleteRequest =
                        new OneTimeWorkRequest.Builder(syncStorage.class)
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
                        EventDatabaseHandler db = new EventDatabaseHandler(context);
                        WorkManager.getInstance(context).enqueue(deleteRequest);
                        db.deleteEvent(id);
                        eventList.remove(getAdapterPosition());
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

            private void editEvent(final Event newEvent) {

                builder = new AlertDialog.Builder(context);
                inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.details_event_activity, null);
                 Button cancelButton;
                 Button saveButton;
                 EditText eventTitle;
                 EditText eventType;
                 EditText eventVenue;
                 EditText eventDate;
                 EditText eventStime;
                 EditText eventEtime;
                 EditText eventDescription;
                 TextView title;


                eventTitle = view.findViewById(R.id.eventTitle);
                eventType = view.findViewById(R.id.eventType);
                eventVenue = view.findViewById(R.id.eventVenue);
                eventDate = view.findViewById(R.id.eventDate);
                eventStime = view.findViewById(R.id.eventStime);
                eventEtime = view.findViewById(R.id.eventEtime);
                eventDescription=view.findViewById(R.id.eventDescription);
                saveButton = view.findViewById(R.id.saveButton);
                cancelButton=view.findViewById(R.id.cancelButton);
                title = view.findViewById(R.id.eTitle);

                title.setText(R.string.edit_event);
                eventTitle.setText(newEvent.getEventName());
                eventType.setText(newEvent.getEventType());
                eventVenue.setText(newEvent.getEventVenue());
                eventDate.setText(newEvent.getEventDate());
                eventStime.setText(newEvent.getEventStime());
                eventEtime.setText(newEvent.getEventEtime());
                eventDescription.setText(newEvent.getEventDescription());
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();


                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //update our event
                        EventDatabaseHandler eventDatabaseHandler = new EventDatabaseHandler(context);


                        //update events
                        newEvent.setEventName(eventTitle.getText().toString().trim());
                        newEvent.setEventType(eventType.getText().toString().trim());
                        newEvent.setEventVenue(eventVenue.getText().toString().trim());
                        newEvent.setEventDate(eventDate.getText().toString().trim());
                        newEvent.setEventStime(eventStime.getText().toString().trim());
                        newEvent.setEventEtime(eventEtime.getText().toString().trim());
                        newEvent.setEventDescription(eventDescription.getText().toString().trim());

                        if (!eventTitle.getText().toString().isEmpty()
                                && !eventType.getText().toString().isEmpty()
                                && !eventVenue.getText().toString().isEmpty()
                                && !eventDate.getText().toString().isEmpty()
                                && !eventStime.getText().toString().isEmpty()
                                && !eventEtime.getText().toString().isEmpty()
                                && !eventDescription.getText().toString().isEmpty()) {


                            WorkRequest updateRequest =
                                    new OneTimeWorkRequest.Builder(updateEventRemote.class)
                                            .setConstraints(constraints)
                                            .setInputData(
                                                    new Data.Builder()
                                                            .putString("event", newEvent.toString())
                                                            .build()
                                            )
                                            .build();
                            WorkManager.getInstance(context).enqueue(updateRequest);
                            eventDatabaseHandler.updateEvent(newEvent);
                            notifyItemChanged(getAdapterPosition(),newEvent); //important!


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

