package com.nit.alarm_firebaseui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewAlarm extends AppCompatActivity {


    private EditText label_alarm;
    private TextView time_alarm;
    private String time;
    private Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);

        label_alarm = (EditText)findViewById(R.id.label_alarm);
        time_alarm = (TextView) findViewById(R.id.time_alarm);
        upload = (Button)findViewById(R.id.upload);

        time_alarm.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time_icon,0,0,0);

        time_alarm.setOnClickListener(new View.OnClickListener() {

                                          @Override
                                          public void onClick(View v) {
                                              final Calendar mcurrentTime = Calendar.getInstance();
                                              int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                              int minute = mcurrentTime.get(Calendar.MINUTE);


                                              TimePickerDialog mTimePicker;

                                              mTimePicker = new TimePickerDialog(AddNewAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                                                  @Override
                                                  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                      time = ((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute);

                                                      time_alarm.setText(((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute));
                                                      mcurrentTime.set(mcurrentTime.HOUR_OF_DAY, hourOfDay);

                                          }

                                              }, hour, minute, true);


                                              mTimePicker.setTitle("Select Time");

                                              mTimePicker.show();

                                          }
                                      });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Alarms").push();
                Map<String,Object> map = new HashMap<>();
                map.put("Label",label_alarm.getText().toString());
                map.put("Time",time);

                databaseReference.setValue(map);
                Intent intent = new Intent(AddNewAlarm.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }
}
