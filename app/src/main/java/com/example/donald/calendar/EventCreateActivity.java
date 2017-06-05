package com.example.donald.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

public class EventCreateActivity extends AppCompatActivity {

    String timeStart, timeEnd, date, event;
    TimePicker timePicker1, timePicker2 ;

    Context context;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        timePicker2 = ((TimePicker) findViewById(R.id.time_finish_event));
        timePicker2.setIs24HourView(true);

        timePicker1 = ((TimePicker) findViewById(R.id.time_start_event));
        timePicker1.setIs24HourView(true);

        database = new DBEvent(this).getWritableDatabase();

        context = this;

        ((Button)findViewById(R.id.btnSaveEvent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                timeStart = createTime(timePicker1.getHour(), timePicker1.getMinute());


                timeEnd =createTime(timePicker2.getHour(), timePicker2.getMinute());

                date = getIntent().getExtras().getString(CalendarTaskActivity.KEY_DATE);

                event = ((TextView)findViewById(R.id.mainEventText)).getText().toString();

                int repeat = getRepeatTime();

                ContentValues contentValues = new ContentValues();
                ArrayList<Integer> repeatArr = new ArrayList<Integer>();

                int d = getIntent().getExtras().getInt(CalendarActivity.KEY_DAY);
                int y = getIntent().getExtras().getInt(CalendarActivity.KEY_YEAR);
                int m = getIntent().getExtras().getInt(CalendarActivity.KEY_MONTH);

                repeatArr.add(d);
                d+=repeat;

                if(repeat == 1 || repeat == 7){
                    while (d < daysInMonth(new GregorianCalendar()) + 1){
                        repeatArr.add(d);
                        d+=repeat;
                    }
                }

                for(int x : repeatArr) {
                    contentValues.put(DBEvent.NAME_COLLUMN_DATE, x + ":" + m + ":" + y);
                    contentValues.put(DBEvent.NAME_COLLUMN_EVENT, event);
                    contentValues.put(DBEvent.NAME_COLLUMN_TIME_IN, timeStart);
                    contentValues.put(DBEvent.NAME_COLLUMN_TIME_END, timeEnd);
                    contentValues.put(DBEvent.NAME_COLLUMN_REPEAT, repeat);

                    database.insert(DBEvent.NAME_TABLE, null, contentValues);

                    //Toast.makeText(context, x + ":" + m + ":" + y, Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(context, "database update", Toast.LENGTH_SHORT).show();

                mt();
            }
        });

        ((Button)findViewById(R.id.btnCancelEventSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mt();
            }
        });
    }

    public static int daysInMonth(GregorianCalendar c){
        int [] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        daysInMonth[1]+=c.isLeapYear(c.get(GregorianCalendar.YEAR)) ? 1 : 0;

        return daysInMonth[c.get(GregorianCalendar.MONTH)];
    }

    @Override
    public boolean onKeyDown(int kC, KeyEvent keyEvent){
        mt();
        return super.onKeyDown(kC, keyEvent);
    }

    private int getRepeatTime(){
        int id = ((RadioGroup)findViewById(R.id.rg)).getCheckedRadioButtonId();
        switch (id){
            case R.id.rbEveryDay:
                return 1;

            case R.id.rbEveryWeek:
                return 7;

            case R.id.rbNever:
                return 0;
        }
        return 0;
    }

    private String createTime(int h, int m)
    {
        String tm = new String();
        tm = h > 9 ? h + ":"  : (h == 0 ?  "00:" : "0" + h + ":");
        tm += m > 9 ? m + ""  : (m == 0 ?  "00" : "0" + m);
        return tm;
    }

    void mt()
    {
        finish();
        Intent intent = new Intent(this, CalendarTaskActivity.class);
        intent.putExtra(CalendarActivity.KEY_DAY, getIntent().getExtras().getInt(CalendarActivity.KEY_DAY));
        intent.putExtra(CalendarActivity.KEY_MONTH, getIntent().getExtras().getInt(CalendarActivity.KEY_MONTH));
        intent.putExtra(CalendarActivity.KEY_YEAR, getIntent().getExtras().getInt(CalendarActivity.KEY_YEAR));
        startActivity(intent);
    }
}
