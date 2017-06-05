package com.example.donald.calendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Donald on 20.10.2016.
 */
public class AdapterForEvents extends BaseAdapter {

    ArrayList<Event> events;
    LayoutInflater inflater;
    Context context;
    private final String HALF = "adapter";

    public AdapterForEvents(Context context, ArrayList<Event> events)
    {
        this.events = events;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = inflater.inflate(R.layout.event_custom_view, parent, false);
        }

        view.setTag(HALF + position);

        Event event = (Event) getItem(position);

        ((TextView)view.findViewById(R.id.time_start_view)).setText(event.getTime_start());
        ((TextView)view.findViewById(R.id.time_finish_view)).setText(event.getTime_end());
        ((TextView)view.findViewById(R.id.name_event_view)).setText(event.getEvent_name());

        Button btn = (Button)view.findViewById(R.id.btn_delete_event);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = new DBEvent(context).getWritableDatabase();
                Event event1 = (Event)getItem(position);
                int d = Integer.parseInt(event1.getDate().substring(0, 1));
                String halfDate = event1.getDate().substring(1);
                try{
                    d = d*10 + Integer.parseInt(event1.getDate().substring(1, 2));
                    halfDate = event1.getDate().substring(2);
                }catch (Exception e)
                {

                }

                ArrayList<Integer> repeatArr = new ArrayList<Integer>();
                repeatArr.add(d);

                int repeat = event1.getRepeat();

                if(repeat == 1 || repeat == 7){
                    while (d < EventCreateActivity.daysInMonth(new GregorianCalendar()) + 1){
                        repeatArr.add(d);
                        d+=repeat;
                    }
                }

                events.remove(event1);
                notifyDataSetChanged();
                for(int m : repeatArr) {
                    String date =
                            //(m > 9 ? m + ""  : (m == 0 ?  "00" : "0" + m))
                            m + halfDate;
                    Toast.makeText(context, date, Toast.LENGTH_SHORT).show();
                    database.delete(DBEvent.NAME_TABLE, DBEvent.NAME_COLLUMN_DATE + " = '" + date + "' and " + DBEvent.NAME_COLLUMN_EVENT + " = '" + event1.getEvent_name() + "';", null);
                }
            }
        });

        return view;
    }
}
