package com.example.donald.calendar;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    static String APPID = "bfae53bc446bae91c1b96e12186b7ea9";

    public static String KEY_YEAR = "year";
    public static String KEY_DAY = "day";
    public static String KEY_MONTH = "month";

    CalendarView calendarView;
    Intent intent;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        intent = new Intent(this, CalendarTaskActivity.class);

        textView = (TextView)findViewById(R.id.tvWeather);

        calendarView = (CalendarView)findViewById(R.id.mainCalendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                intent.putExtra(KEY_YEAR, year);
                intent.putExtra(KEY_MONTH, month);
                intent.putExtra(KEY_DAY, dayOfMonth);

                startActivity(intent);
            }
        });

        new JsonGetTask().execute();

        ((Button)findViewById(R.id.btnWeather)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });


    }

    private void displaySpeechRecognizer(){
        Intent intentS = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentS.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intentS, 0);
        //stopService(new Intent(this, MyService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resCode, Intent data){
        if(requestCode == 0 && resCode == RESULT_OK)
        {
            List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = result.get(0);
            Date date = new Date();

            try{
                int day = Integer.parseInt(spokenText);

                if(day <= EventCreateActivity.daysInMonth(new GregorianCalendar())) {

                    intent.putExtra(KEY_YEAR, date.getYear() + 1900);
                    intent.putExtra(KEY_MONTH, date.getMonth());
                    intent.putExtra(KEY_DAY, day);

                    startActivity(intent);
                }
            }catch (Exception e){
                Toast.makeText(this, "повторите запрос!", Toast.LENGTH_LONG).show();
            }

        }
        super.onActivityResult(requestCode, resCode, data);
    }

    class JsonGetTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute(){
            textView.setText("Loading...");
        }

        @Override
        protected String doInBackground(Void... params) {
            String query = "http://api.openweathermap.org/data/2.5/forecast?id=501175&cnt=7&appid=";

            try {
                URL url = new URL(query + APPID);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            }catch (Exception e){
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response){
            if(response == null)
                response = "NOT CONNECTED...";

            ArrayList<String> stringArrayList = getTemp(response);

            String str = "";

            for (String s : stringArrayList){
                try {
                    double d = Double.parseDouble(s);
                    int i = (int)(d - 273.15);
                    str += (i + "     "  );
                }catch (Exception e)
                {

                }
                    textView.setText(str);

            Log.d("res", response);
        }

    }

        ArrayList<String> getTemp(String s)
        {
            ArrayList<Integer> a = new ArrayList<>();
            int i = 0;

            i = s.indexOf("temp", i);

            while (i > 0) {
                a.add(i);
                i = s.indexOf("temp", i + 12);
            }

            ArrayList<String> stringArrayList = new ArrayList<>();

            for (int x: a)
                stringArrayList.add(s.substring(x + 6, x + 12));

            return stringArrayList;
        }
    }
}
