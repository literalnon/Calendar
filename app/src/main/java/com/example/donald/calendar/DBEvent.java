package com.example.donald.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Donald on 20.10.2016.
 */

public class DBEvent extends SQLiteOpenHelper implements BaseColumns{

    public  static  final String NAME_COLLUMN_DATE = "date";
    public  static  final String NAME_COLLUMN_TIME_IN = "time_in";
    public  static  final String NAME_COLLUMN_TIME_END = "time_end";
    public  static  final String NAME_COLLUMN_EVENT = "event";
    public  static  final String NAME_COLLUMN_REPEAT = "repeat";

    public static final String NAME_DB = "mydb.db";
    public static final String NAME_TABLE = "events";

    public  static  final int DB_VERSION = 1;

    private static final String DBScript = "create table "
            + NAME_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + NAME_COLLUMN_DATE
            + " string, " + NAME_COLLUMN_TIME_IN + " string, "
            + NAME_COLLUMN_TIME_END + " string, "
            + NAME_COLLUMN_EVENT + " string, "
            + NAME_COLLUMN_REPEAT + " integer);";

    public DBEvent(Context context) {
        super(context, NAME_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBScript);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
