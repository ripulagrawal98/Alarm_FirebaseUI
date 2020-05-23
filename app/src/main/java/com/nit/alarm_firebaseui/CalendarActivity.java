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

                String Date = dayOfMonth + "-"+ (month + 1) + "-" + year ;

                Toast.makeText(getApplicationContext(),Date,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CalendarActivity.this,MainActivity.class);
                intent.putExtra("Date",Date);
                startActivity(intent);
            }
        });

    }
}
