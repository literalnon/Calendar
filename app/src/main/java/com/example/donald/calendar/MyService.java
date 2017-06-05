package com.example.donald.calendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;

public class MyService extends Service {

    final String TAG = "my_logs";
    /*Context context;

    public MyService(Context context) {
        this.context = context;
    }*/

    public MyService(){

    }

    public void OnCreate(){
        super.onCreate();
        Log.d(TAG, "OnCreate: ");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand");
        sometask();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "OnDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void sometask(){

        Context context = getApplicationContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.mk)
                .setContentTitle("Настя!")
                .setContentText("Дай Диме шанс, пожалуйста:*");
        Intent resInt = new Intent(context, CalendarActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resInt);
        PendingIntent resPenInt = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resPenInt);
        NotificationManager mNotMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotMan.notify(1, mBuilder.build());

    }
}

