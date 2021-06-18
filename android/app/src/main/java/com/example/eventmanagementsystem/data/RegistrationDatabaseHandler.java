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

public class RegistrationDatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public RegistrationDatabaseHandler(@Nullable Context context) {
        super(context, Constants.EVENT_DB_NAME, null, Constants.EVENT_DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        group_id      INTEGER NOT NULL,
//        FOREIGN KEY (group_id)
//                REFERENCES supplier_groups (group_id)
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

        db.execSQL(CREATE_REGISTRATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.REGISTRATION_TABLE_NAME );

        onCreate(db);
    }

    // CRUD operations
    public void addRegistration(Registration registration,int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.REGISTRATION_NAME, registration.getrName());
        values.put(Constants.REGISTRATION_ROLL, registration.getrRollNumber());
        values.put(Constants.REGISTRATION_DEPARTMENT, registration.getrDepartment());
        values.put(Constants.REGISTRATION_CONTACT, registration.getrContact());
        values.put(Constants.REGISTRATION_STATUS, registration.getrStatus());
        values.put(Constants.R_EVENT_ID, String.valueOf(id));
        values.put(Constants.SYNC_STATUS,registration.getSync_status());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system

        //Inset the row
        db.insert(Constants.REGISTRATION_TABLE_NAME, null, values);

        Log.d("REGISTRATION_DBHandler", "added Item: ");
    }

    //Get a registration
    public Registration getRegistration(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.REGISTRATION_TABLE_NAME,
                new String[]{Constants.REGISTRATION_ID,
                        Constants.REGISTRATION_NAME,
                        Constants.REGISTRATION_ROLL,
                        Constants.REGISTRATION_DEPARTMENT,
                        Constants.REGISTRATION_CONTACT,
                        Constants.REGISTRATION_STATUS,
                        Constants.EVENT_ID,
                        Constants.SYNC_STATUS,
                        Constants.KEY_DATE_NAME},
                Constants.REGISTRATION_ID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Registration registration =new Registration();
        if (cursor != null) {
            registration.setRegistrationID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_ID))));
            registration.setrName(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_NAME)));
            registration.setrRollNumber(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_ROLL)));
            registration.setrDepartment(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_DEPARTMENT)));
            registration.setrContact(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_CONTACT)));
            registration.setrStatus(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_STATUS)));
            registration.setEventID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.EVENT_ID))));
            registration.setSync_status(cursor.getInt(cursor.getColumnIndex(Constants.SYNC_STATUS)));

            //convert Timestamp to something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                    .getTime()); // Feb 23, 2020

            registration.setDateItemAdded(formattedDate);


        }

        return registration;
    }


    //Get all registrations

    public List<Registration> getAllRegistrations(int eventID) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Registration> registrationList = new ArrayList<>();
        Cursor cursor = db.query(Constants.REGISTRATION_TABLE_NAME,
                new String[]{Constants.REGISTRATION_ID,
                        Constants.REGISTRATION_NAME,
                        Constants.REGISTRATION_ROLL,
                        Constants.REGISTRATION_DEPARTMENT,
                        Constants.REGISTRATION_CONTACT,
                        Constants.REGISTRATION_STATUS,
                        Constants.R_EVENT_ID,
                        Constants.SYNC_STATUS,
                        Constants.KEY_DATE_NAME},
                Constants.R_EVENT_ID + "=? " ,
                new String[]{String.valueOf(eventID)}, null, null, Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Registration registration = new Registration();

                registration.setRegistrationID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_ID))));
                registration.setrName(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_NAME)));
                registration.setrRollNumber(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_ROLL)));
                registration.setrDepartment(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_DEPARTMENT)));
                registration.setrContact(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_CONTACT)));
                registration.setrStatus(cursor.getString(cursor.getColumnIndex(Constants.REGISTRATION_STATUS)));
                registration.setEventID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.R_EVENT_ID))));
                registration.setSync_status(cursor.getInt(cursor.getColumnIndex(Constants.SYNC_STATUS)));
                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME)))
                        .getTime()); // Feb 23, 2020
                registration.setDateItemAdded(formattedDate);

                //Add to arraylist
                registrationList.add(registration);
            }
            while (cursor.moveToNext());
        }
        return registrationList;

    }
    //Todo: Add updateRegistration

    public int updateRegistration(Registration registration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.REGISTRATION_NAME, registration.getrName());
        values.put(Constants.REGISTRATION_ROLL, registration.getrRollNumber());
        values.put(Constants.REGISTRATION_DEPARTMENT, registration.getrDepartment());
        values.put(Constants.REGISTRATION_CONTACT, registration.getrContact());
        values.put(Constants.REGISTRATION_STATUS, registration.getrStatus());
        values.put(Constants.SYNC_STATUS,registration.getSync_status());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());//timestamp of the system

        //Update the row
        return  db.update(Constants.REGISTRATION_TABLE_NAME, values,Constants.REGISTRATION_ID + "=?",
                new String[]{String.valueOf(registration.getRegistrationID())});



    }
    //Todo: Add Delete registration
    public void deleteRegistration(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( Constants.REGISTRATION_TABLE_NAME ,
                Constants.REGISTRATION_ID + "=?",
                new String[]{String.valueOf(id)});

        //close
        db.close();

    }

    //Todo: getRegistrationssCount
    public int getRegistrationsCount() {
        String countQuery = " SELECT * FROM " + Constants.REGISTRATION_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();

    }

}


