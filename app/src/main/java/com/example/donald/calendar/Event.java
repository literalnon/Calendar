package com.example.donald.calendar;

/**
 * Created by Donald on 20.10.2016.
 */
public class Event {
    private String time_start;
    private String time_end;
    private String date;
    private String event_name;
    private int repeat;
    private int id;

    public String getTime_start(){return time_start;}
    public String getTime_end(){return time_end;}
    public String getDate(){return date;}
    public String getEvent_name(){return event_name;}
    public int getRepeat(){return repeat;}
    public int getId(){return id;}



    public Event(String t_s, String t_e, String dt, String e_n, int rpt){
        time_start = t_s;
        time_end = t_e;
        date = dt;
        event_name = e_n;
        repeat = rpt;
    }


    public Event(String t_s, String t_e, String dt, String e_n, int rpt, int id){
        time_start = t_s;
        time_end = t_e;
        date = dt;
        event_name = e_n;
        repeat = rpt;
        this.id = id;
    }
}
