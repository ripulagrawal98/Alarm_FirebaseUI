package com.nit.alarm_firebaseui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mCalendar = (CalendarView) findViewById(R.id.calendar);

        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String Date = null;
//                if(dayOfMonth < 10 && month < 9)
//                {
//                    Date = "0"+dayOfMonth + "-"+ "0"+ (month + 1) + "-" + year ;
//                }
//                else if(dayOfMonth<10 && month >8)
//                {
//                    Date = "0" + dayOfMonth + "-"+ (month + 1) + "-" + year ;
//                }
//
//                else if(dayOfMonth>9 && month <9)
//                {
//                    Date = dayOfMonth + "-"+ "0"+ (month + 1) + "-" + year ;
//                }

                Date = dayOfMonth + "-"+(month + 1) + "-" + year;

                Toast.makeText(getApplicationContext(),Date,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CalendarActivity.this,MainActivity.class);
                intent.putExtra("Date",Date);
                intent.putExtra("day_month",String.valueOf(dayOfMonth));
                intent.putExtra("month",String.valueOf(month));
                intent.putExtra("year",String.valueOf(year));

                startActivity(intent);
            }
        });

    }
}
