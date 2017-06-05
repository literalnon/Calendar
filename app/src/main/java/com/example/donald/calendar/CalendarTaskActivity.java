package com.example.donald.calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class CalendarTaskActivity extends AppCompatActivity {

    Intent intent;
    public static String KEY_DATE = "date_from_cta";
    ArrayList<Event> events;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        setContentView(R.layout.activity_calendar_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = new Intent(this, EventCreateActivity.class);

        int day = getIntent().getExtras().getInt(CalendarActivity.KEY_DAY);
        int month = getIntent().getExtras().getInt(CalendarActivity.KEY_MONTH);
        int year = getIntent().getExtras().getInt(CalendarActivity.KEY_YEAR);

        String date = day + ":" + month + ":" + year;

        toolbar.setTitle(date);

        Toast.makeText(this, date , Toast.LENGTH_LONG).show();

        intent.putExtra(CalendarActivity.KEY_YEAR, year);
        intent.putExtra(CalendarActivity.KEY_MONTH, month);
        intent.putExtra(CalendarActivity.KEY_DAY, day);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                    finish();
                }
            });

        SQLiteDatabase database = new DBEvent(this).getReadableDatabase();

        Cursor cursor = database.query(DBEvent.NAME_TABLE, null, DBEvent.NAME_COLLUMN_DATE + " = ?", new String[]{date}, null, null, null);

        events = new ArrayList<>();

        while (cursor.moveToNext()) {
            String dt = cursor.getString(cursor.getColumnIndex(DBEvent.NAME_COLLUMN_DATE));
            String t_s = cursor.getString(cursor.getColumnIndex(DBEvent.NAME_COLLUMN_TIME_IN));
            String t_e = cursor.getString(cursor.getColumnIndex(DBEvent.NAME_COLLUMN_TIME_END));
            String ev = cursor.getString(cursor.getColumnIndex(DBEvent.NAME_COLLUMN_EVENT));
            int rpt = cursor.getInt(cursor.getColumnIndex(DBEvent.NAME_COLLUMN_REPEAT));
            events.add(new Event(t_s, t_e, dt, ev, rpt, cursor.getInt(cursor.getColumnIndex(DBEvent._ID))));
        }

        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                return lhs.getTime_start().compareTo(rhs.getTime_start());
            }
        });


        if(events.isEmpty()){
            TextView tvNull = (TextView)findViewById(R.id.tv_null);
            tvNull.setText("no events");
        }

        ((ListView) findViewById(R.id.listViewEvent)).setAdapter(new AdapterForEvents(this, events));

    }
}
