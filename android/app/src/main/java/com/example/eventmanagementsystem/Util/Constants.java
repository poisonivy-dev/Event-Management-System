package com.example.eventmanagementsystem.Util;

public class Constants {

    public static final int EVENT_DB_VERSION = 4;
    public static final String EVENT_DB_NAME = "eventList";
    public static final String EVENT_TABLE_NAME = "event_tbl";
    public static final String PARTICIPANT_TABLE_NAME="participant_tbl";
    public static final String REGISTRATION_TABLE_NAME = "registration_tbl";


    //EVENT COLUMNS
    public static final String EVENT_ID = "id";

    public static final String EVENT_TITLE = "event_title";
    public static final String EVENT_TYPE = "event_type";
    public static final String EVENT_VENUE = "event_venue";
    public static final String EVENT_DATE = "event_date";
    public static final String EVENT_STIME = "event_stime";
    public static final String EVENT_ETIME = "event_etime";
    public static final String EVENT_DESCRIPTION = "event_description";

    // REGISTRATION COLUMNS
    //event id
    public static final String REGISTRATION_ID = "registration_ID";
    public static final String REGISTRATION_NAME = "registration_name";
    public static final String REGISTRATION_ROLL = "registration_roll";
    public static final String REGISTRATION_DEPARTMENT = "registration_department";
    public static final String REGISTRATION_CONTACT = "registration_contact";
    public static final String REGISTRATION_STATUS = "registration_status";
    public static final String R_EVENT_ID = "r_event_ID";

    //PARTICIPANT COLUMNS
    public static final String PARTICIPANT_ID = "participant_ID";
    public static final String PARTICIPANT_NAME = "participant_name";
    public static final String PARTICIPANT_ROLL = "participant_roll";
    public static final String PARTICIPANT_DEPARTMENT = "participant_department";
    public static final String PARTICIPANT_CONTACT = "participant_contact";
    public static final String PARTICIPANT_ROLE = "participant_role";
    public static final String P_EVENT_ID = "p_event_ID";


    public static final String KEY_DATE_NAME = "date_added";

    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;
    public static final String INSERT_EVENT_URL="https://mywebdevproj.000webhostapp.com/insertEvent.php";
    public static final String SYNC_STATUS="syncstatus";
    public static final String UI_UPDATE_BORADCAST="com.example.synctest.uiupdatebroadcast";


}

