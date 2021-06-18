package com.example.eventmanagementsystem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.model.Participant;

import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParticipantDatabaseHandler extends SQLiteOpenHelper{

        private final Context context;

        public ParticipantDatabaseHandler(@Nullable Context context) {
            super(context, Constants.EVENT_DB_NAME, null, Constants.EVENT_DB_VERSION);
            this.context = context;

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//        group_id      INTEGER NOT NULL,
//        FOREIGN KEY (group_id)
//                REFERENCES supplier_groups (group_id)
            String CREATE_PARTICIPANT_TABLE = "CREATE TABLE " + Constants.PARTICIPANT_TABLE_NAME + "("
                    + Constants.PARTICIPANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Constants.PARTICIPANT_NAME + " TEXT,"
                    + Constants.PARTICIPANT_ROLL + " TEXT,"
                    + Constants.PARTICIPANT_DEPARTMENT + " TEXT,"
                    + Constants.PARTICIPANT_CONTACT + " TEXT,"
                    + Constants.PARTICIPANT_ROLE + " TEXT,"
                    + Constants.KEY_DATE_NAME + " LONG,"
                    + Constants.P_EVENT_ID + " INTEGER ,"
                    + Constants.SYNC_STATUS + " INTEGER, "
                    + " FOREIGN KEY (" + Constants.P_EVENT_ID + ") REFERENCES " + Constants.EVENT_TABLE_NAME + "(" + Constants.EVENT_ID + "));";

            db.execSQL(CREATE_PARTICIPANT_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + Constants.PARTICIPANT_TABLE_NAME );

            onCreate(db);
        }

        // CRUD operations
        public void addParticipant(Participant participant, int id) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Constants.PARTICIPANT_NAME, participant.getParticipantName());
            values.put(Constants.PARTICIPANT_ROLL, participant.getParticipantRollNumber());
            values.put(Constants.PARTICIPANT_DEPARTMENT, participant.getParticipantDepartment());
            values.put(Constants.PARTICIPANT_CONTACT, participant.getParticipantContact());
            values.put(Constants.PARTICIPANT_ROLE, participant.getParticipantRole());
            values.put(Constants.P_EVENT_ID, String.valueOf(id));
            values.put(Constants.SYNC_STATUS,participant.getSync_status());
            values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system

            //Inset the row
            db.insert(Constants.PARTICIPANT_TABLE_NAME, null, values);

            Log.d("PARTICIPANT_DBHandler", "added Item: ");
        }

        //Get a participant
        public Participant getParticipant(int id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(Constants.PARTICIPANT_TABLE_NAME,
                    new String[]{Constants.PARTICIPANT_ID,
                            Constants.PARTICIPANT_NAME,
                            Constants.PARTICIPANT_ROLL,
                            Constants.PARTICIPANT_DEPARTMENT,
                            Constants.PARTICIPANT_CONTACT,
                            Constants.PARTICIPANT_ROLE,
                            Constants.P_EVENT_ID,
                            Constants.SYNC_STATUS,
                            Constants.KEY_DATE_NAME},
                    Constants.PARTICIPANT_ID + " =? ",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Participant participant =new Participant();
            if (cursor != null) {
                participant.setParticipantID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_ID))));
                participant.setParticipantName(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_NAME)));
                participant.setParticipantRollNumber(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_ROLL)));
                participant.setParticipantDepartment(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_DEPARTMENT)));
                participant.setParticipantContact(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_CONTACT)));
                participant.setParticipantRole(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_ROLE)));
                participant.setEventID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.P_EVENT_ID))));
                participant.setSync_status(cursor.getInt(cursor.getColumnIndex(Constants.SYNC_STATUS)));

                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime()); // Feb 23, 2020

                participant.setDateItemAdded(formattedDate);


            }

            return participant;
        }


        //Get all participants

        public List<Participant> getAllParticipants(int eventID) {
            SQLiteDatabase db = this.getReadableDatabase();

            List<Participant> participantList = new ArrayList<>();
            Cursor cursor = db.query(Constants.PARTICIPANT_TABLE_NAME,
                    new String[]{Constants.PARTICIPANT_ID,
                            Constants.PARTICIPANT_NAME,
                            Constants.PARTICIPANT_ROLL,
                            Constants.PARTICIPANT_DEPARTMENT,
                            Constants.PARTICIPANT_CONTACT,
                            Constants.PARTICIPANT_ROLE,
                            Constants.P_EVENT_ID,
                            Constants.SYNC_STATUS,
                            Constants.KEY_DATE_NAME},
                    Constants.P_EVENT_ID + "=? " ,
                    new String[]{String.valueOf(eventID)}, null, null, Constants.KEY_DATE_NAME + " DESC");

            if (cursor.moveToFirst()) {
                do {
                    Participant participant = new Participant();

                    participant.setParticipantID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_ID))));
                    participant.setParticipantName(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_NAME)));
                    participant.setParticipantRollNumber(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_ROLL)));
                    participant.setParticipantDepartment(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_DEPARTMENT)));
                    participant.setParticipantContact(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_CONTACT)));
                    participant.setParticipantRole(cursor.getString(cursor.getColumnIndex(Constants.PARTICIPANT_ROLE)));
                    participant.setEventID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.P_EVENT_ID))));
                    participant.setSync_status(cursor.getInt(cursor.getColumnIndex(Constants.SYNC_STATUS)));
                    //convert Timestamp to something readable
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                            .getTime()); // Feb 23, 2020
                    participant.setDateItemAdded(formattedDate);

                    //Add to arraylist
                    participantList.add(participant);
                }
                while (cursor.moveToNext());
            }
            return participantList;

        }
        //Todo: Add updateParticipant

        public int updateParticipant(Participant participant) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Constants.PARTICIPANT_NAME, participant.getParticipantName());
            values.put(Constants.PARTICIPANT_ROLL, participant.getParticipantRollNumber());
            values.put(Constants.PARTICIPANT_DEPARTMENT, participant.getParticipantDepartment());
            values.put(Constants.PARTICIPANT_CONTACT, participant.getParticipantContact());
            values.put(Constants.PARTICIPANT_ROLE, participant.getParticipantRole());
            values.put(Constants.SYNC_STATUS,participant.getSync_status());
            values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system

            //Update the row
            return  db.update(Constants.PARTICIPANT_TABLE_NAME, values,Constants.PARTICIPANT_ID + "=?",
                    new String[]{String.valueOf(participant.getParticipantID())});



        }
        //Todo: Add Delete participant
        public void deleteParticipant(int id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete( Constants.PARTICIPANT_TABLE_NAME ,
                    Constants.PARTICIPANT_ID + "=?",
                    new String[]{String.valueOf(id)});

            //close
            db.close();

        }

        //Todo: getParticipantssCount
        public int getParticipantsCount() {
            String countQuery = " SELECT * FROM " + Constants.PARTICIPANT_TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(countQuery, null);

            return cursor.getCount();

        }

    }


