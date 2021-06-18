package com.example.eventmanagementsystem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.eventmanagementsystem.Util.Constants;
import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.model.Registration;

import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public EventDatabaseHandler(@Nullable Context context) {
        super(context, Constants.EVENT_DB_NAME, null, Constants.EVENT_DB_VERSION);
        this.context = context;
    }
    //creating the database and tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENT_TABLE = "CREATE TABLE " + Constants.EVENT_TABLE_NAME + "("
                + Constants.EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.EVENT_TITLE + " TEXT,"
                + Constants.EVENT_TYPE + " TEXT,"
                + Constants.EVENT_VENUE + " TEXT,"
                + Constants.EVENT_DATE + " TEXT,"
                + Constants.EVENT_STIME + " TEXT,"
                + Constants.EVENT_ETIME + " TEXT,"
                + Constants.EVENT_DESCRIPTION + " TEXT,"
                + Constants.SYNC_STATUS + " INTEGER, "
                + Constants.KEY_DATE_NAME + " LONG);";

        String CREATE_REGISTRATION_TABLE = "CREATE TABLE " + Constants.REGISTRATION_TABLE_NAME + "("
                + Constants.REGISTRATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.REGISTRATION_NAME + " TEXT,"
                + Constants.REGISTRATION_ROLL + " TEXT,"
                + Constants.REGISTRATION_DEPARTMENT + " TEXT,"
                + Constants.REGISTRATION_CONTACT + " TEXT,"
                + Constants.REGISTRATION_STATUS + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG,"
                + Constants.R_EVENT_ID + " INTEGER ,"
                + Constants.SYNC_STATUS + " INTEGER, "
                + " FOREIGN KEY (" + Constants.R_EVENT_ID + ") REFERENCES " + Constants.EVENT_TABLE_NAME + "(" + Constants.EVENT_ID + "));";

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

        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_REGISTRATION_TABLE);
        db.execSQL(CREATE_PARTICIPANT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.EVENT_TABLE_NAME);

        onCreate(db);
    }

    // CRUD operations
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.EVENT_TITLE, event.getEventName());
        values.put(Constants.EVENT_TYPE, event.getEventType());
        values.put(Constants.EVENT_VENUE, event.getEventVenue());
        values.put(Constants.EVENT_DATE, event.getEventDate());
        values.put(Constants.EVENT_STIME, event.getEventStime());
        values.put(Constants.EVENT_ETIME, event.getEventEtime());
        values.put(Constants.EVENT_DESCRIPTION, event.getEventDescription());
        values.put(Constants.SYNC_STATUS,event.getSync_status());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system

        //Inset the row
        db.insert(Constants.EVENT_TABLE_NAME, null, values);
        db.close();
        Log.d("EVENT_DBHandler", "added Item: ");
    }

    //Get an Event
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.EVENT_TABLE_NAME,
                new String[]{Constants.EVENT_ID,
                        Constants.EVENT_TITLE,
                        Constants.EVENT_TYPE,
                        Constants.EVENT_VENUE,
                        Constants.EVENT_DATE,
                        Constants.EVENT_STIME,
                        Constants.EVENT_ETIME,
                        Constants.EVENT_DESCRIPTION,
                        Constants.SYNC_STATUS,
                        Constants.KEY_DATE_NAME},
                Constants.EVENT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Event event=new Event();
        if (cursor != null) {
            event.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.EVENT_ID))));
            event.setEventName(cursor.getString(cursor.getColumnIndex(Constants.EVENT_TITLE)));
            event.setEventType(cursor.getString(cursor.getColumnIndex(Constants.EVENT_TYPE)));
            event.setEventVenue(cursor.getString(cursor.getColumnIndex(Constants.EVENT_VENUE)));
            event.setEventDate(cursor.getString(cursor.getColumnIndex(Constants.EVENT_DATE)));
            event.setEventStime(cursor.getString(cursor.getColumnIndex(Constants.EVENT_STIME)));
            event.setEventEtime(cursor.getString(cursor.getColumnIndex(Constants.EVENT_ETIME)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(Constants.EVENT_DESCRIPTION)));
            event.setSync_status(cursor.getInt(cursor.getColumnIndex(Constants.SYNC_STATUS)));

            //convert Timestamp to something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                    .getTime()); // Feb 23, 2020

            event.setDateItemAdded(formattedDate);


        }
            db.close();
        return event;
    }

    //Get all events
    public List<Event> getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Event> eventList = new ArrayList<>();

        Cursor cursor = db.query(Constants.EVENT_TABLE_NAME,
                new String[]{Constants.EVENT_ID,
                        Constants.EVENT_TITLE,
                        Constants.EVENT_TYPE,
                        Constants.EVENT_VENUE,
                        Constants.EVENT_DATE,
                        Constants.EVENT_STIME,
                        Constants.EVENT_ETIME,
                        Constants.EVENT_DESCRIPTION,
                        Constants.SYNC_STATUS,
                        Constants.KEY_DATE_NAME},
                null, null, null, null,
                Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.EVENT_ID))));
                event.setEventName(cursor.getString(cursor.getColumnIndex(Constants.EVENT_TITLE)));
                event.setEventType(cursor.getString(cursor.getColumnIndex(Constants.EVENT_TYPE)));
                event.setEventVenue(cursor.getString(cursor.getColumnIndex(Constants.EVENT_VENUE)));
                event.setEventDate(cursor.getString(cursor.getColumnIndex(Constants.EVENT_DATE)));
                event.setEventStime(cursor.getString(cursor.getColumnIndex(Constants.EVENT_STIME)));
                event.setEventEtime(cursor.getString(cursor.getColumnIndex(Constants.EVENT_ETIME)));
                event.setEventDescription(cursor.getString(cursor.getColumnIndex(Constants.EVENT_DESCRIPTION)));
                event.setSync_status(cursor.getInt(cursor.getColumnIndex(Constants.SYNC_STATUS)));
                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime()); // Feb 23, 2020
                event.setDateItemAdded(formattedDate);

                //Add to arraylist
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        db.close();
        return eventList;

    }

    //Todo: Add updateEvent
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.EVENT_TITLE, event.getEventName());
        values.put(Constants.EVENT_TYPE, event.getEventType());
        values.put(Constants.EVENT_VENUE, event.getEventVenue());
        values.put(Constants.EVENT_DATE, event.getEventDate());
        values.put(Constants.EVENT_STIME, event.getEventStime());
        values.put(Constants.EVENT_ETIME, event.getEventEtime());
        values.put(Constants.EVENT_DESCRIPTION, event.getEventDescription());
        values.put(Constants.SYNC_STATUS,event.getSync_status());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system


        //update row
        return db.update(Constants.EVENT_TABLE_NAME, values,
                Constants.EVENT_ID + "=?",
                new String[]{String.valueOf(event.getId())});

    }

    //Todo: Add Delete event
    public void deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.EVENT_TABLE_NAME,
                Constants.EVENT_ID + "=?",
                new String[]{String.valueOf(id)});

        //close
        db.close();

    }

    //Todo: getEventsCount
    public int getEventsCount() {
        String countQuery = "SELECT * FROM " + Constants.EVENT_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();

    }

}

