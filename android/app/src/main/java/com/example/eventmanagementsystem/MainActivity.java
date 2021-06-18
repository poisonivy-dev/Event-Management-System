package com.example.eventmanagementsystem;

import android.content.Intent;
import android.os.Bundle;

import com.example.eventmanagementsystem.data.EventDatabaseHandler;
import com.example.eventmanagementsystem.model.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EventDatabaseHandler eventDatabaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventDatabaseHandler=new EventDatabaseHandler(this);
        //This activity shows a different interface, the first time the app is run.
        // If there are already events in the list , move to the other activity
        byPassEventActivity();

        //check if event was saved
        List<Event> events= eventDatabaseHandler.getAllEvents();
        for (Event event : events) {
            Log.d("Main", "onCreate: " + event.getDateItemAdded());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEventDetailsActivity();
            }
        });
    }

    private void byPassEventActivity() {
        if (eventDatabaseHandler.getEventsCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListEventActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK){
            //Todo: move to next screen - listevent screen
            startActivity(new Intent(MainActivity.this, ListEventActivity.class));
        }
    }
    private void startEventDetailsActivity() {
        Intent intent = new Intent(MainActivity.this,EventDetailsActivity.class);
        startActivityForResult(intent,2);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar Item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}