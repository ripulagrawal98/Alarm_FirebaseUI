package com.nit.alarm_firebaseui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.FirebaseRecyclerViewAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Intent calendar_intent;
    private String mDate;
    private String mday_month,myear,mmonth;

    private RecyclerView mRecyclerView;
   private List<String> categories = new ArrayList<String>();
    private ArrayAdapter<String> dataAdapter;

//    private TextView empty_view;
    private FloatingActionButton fab;

    private FirebaseRecyclerAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;

    private ArrayList<Alarm> alarm_data;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar_intent = getIntent();
        mDate = calendar_intent.getStringExtra("Date");
        mday_month = calendar_intent.getStringExtra("day_month");
        mmonth = calendar_intent.getStringExtra("month");
        myear = calendar_intent.getStringExtra("year");
//        System.out.println("Cal")

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
//        empty_view = (TextView)findViewById(R.id.empty_view);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        categories.add("Delete");
        categories.add("Edit Alarm");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNewAlarm.class);
                intent.putExtra("Date",mDate);
                intent.putExtra("day_month",mday_month);
                intent.putExtra("month",mmonth);
                intent.putExtra("year",myear);

                startActivity(intent);
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);



        fetch();

    }

    private void fetch(){

        Query query = FirebaseDatabase.getInstance().getReference().child("Alarms");

        System.out.println("Check if value of Date is already there "+mDate);

        System.out.println("HII");
        FirebaseRecyclerOptions<Alarm> options = new FirebaseRecyclerOptions.Builder<Alarm>()
                                                                .setQuery(query, new SnapshotParser<Alarm>() {
                                                                    @NonNull
                                                                    @Override
                                                                    public Alarm parseSnapshot(@NonNull DataSnapshot snapshot) {
                                                                        return new Alarm(snapshot.child("Label").getValue().toString(),
                                                                                        snapshot.child("Time").getValue().toString(),
                                                                                        snapshot.child("Media URI").toString(),
                                                                                        snapshot.child("Date").getValue().toString(),
                                                                                        snapshot.child("DayMonth").getValue().toString(),
                                                                                        snapshot.child("Month").getValue().toString(),
                                                                                        snapshot.child("Year").getValue().toString(),
                                                                                snapshot.child("Minute Total").getValue().toString());
                                                                    }
                                                                })
                                                                .build();

        System.out.println("Checking the value of Options "+options);

        adapter = new FirebaseRecyclerAdapter<Alarm, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

               View view = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.list_item,parent,false);
               return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position, @NonNull Alarm alarm) {
                viewHolder.setLabelTitle(alarm.getMlabel());
                viewHolder.setTimeTitle(alarm.getMtime());
                viewHolder.setDateTitle(alarm.getDate());

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final DatabaseReference dataref_delete = FirebaseDatabase.getInstance().getReference("Alarms");


                        AlertDialog myQuittingDialogBox =  new AlertDialog.Builder(MainActivity.this)
                                 // set message, title, and icon
                                .setTitle("Delete")
                                .setMessage("Do you want to Delete")
                                .setIcon(R.drawable.ic_delete_black_24dp)

                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        dataref_delete.child(getRef(position).getKey())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(),"Item is Deleted",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }

                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                })
                                .create();

                        myQuittingDialogBox.show();




                    }
                });

                viewHolder.root.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                    }
                });

            }


        };
        mRecyclerView.setAdapter(adapter);

        scheduleALarm();

    }

    private void scheduleALarm(){

        alarm_data = new ArrayList<Alarm>();

        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Alarms");
        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot looping : dataSnapshot.getChildren()){
                    String label = looping.child("Label").getValue(String.class);
                    String time = looping.child("Time").getValue(String.class);
                   String total_minutes = looping.child("Minute Total").getValue(String.class);
                    String media = looping.child("Media URI").getValue(String.class);
                    String date = looping.child("Date").getValue(String.class);
                    String day_month = looping.child("DayMonth").getValue(String.class);
                    String month = looping.child("Month").getValue(String.class);
                    String year = looping.child("Year").getValue(String.class);

                    Alarm userref = new Alarm(label,time,media,date,day_month,month,year,total_minutes);

                    alarm_data.add(userref);

                }

                System.out.println("Size of Alarm_Data "+alarm_data.size());
                if(alarm_data.size() != 0)
                 SetAlarm();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetAlarm()
    {
        ArrayList<Integer> AlarmList = new ArrayList<Integer>();
        int i =0;
        size = alarm_data.size();

        Collections.sort(alarm_data);
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(calendar.MINUTE);

        // this variable store total number of minutes of current time from the starting of a new day i.e. "00:00"
        int total_currentminute = (currentHour)*60 + currentMinute;

        //his loop will make  a new array to store all the minute values in an array
        for(i = 0;i<size;i++)
        {
            Alarm user = alarm_data.get(i);
            String nextTime = user.getMtime().substring(0,2) + ":" + user.getMtime().substring(3);

            int Hour = Integer.parseInt(nextTime.substring(0,2));
            int Minute = Integer.parseInt(nextTime.substring(3));
            System.out.println("Print the time "+nextTime);
            System.out.println("Value of Minute" + Minute);

            int total_minute = (Hour*60 )+ Minute;

            AlarmList.add(total_minute);

        }
        // Now sort this array in ascending order i.e. which alarm to be scheduled before
        Collections.sort(AlarmList);

        int hour = AlarmList.get(0)/60;
        int minute = AlarmList.get(0) %60;

        Calendar cal = Calendar.getInstance();
        AlarmManager alarms = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<>();

        //        System.out.println("IS this Schedule time calling");
        for(i = 0;i<alarm_data.size();i++)
        {
            String media_uri = alarm_data.get(i).getMedia_uri();
            System.out.println("\n Value of totoal minutes of currenthour "+total_currentminute+"\n value of i is "+ i );
            System.out.println("\n"+ i + " element of ALarmList "+ AlarmList.get(i));
            if(total_currentminute <= AlarmList.get(i)){

                System.out.println("Part2 **"+ i + " element of ALarmList "+ AlarmList.get(i));
                hour = AlarmList.get(i)/60;
                minute = AlarmList.get(i) %60;
                System.out.println("Check the value of hour :"+hour+" Minute: "+minute);

                Intent activate = new Intent(MainActivity.this, AlarmReceiver.class);
                activate.putExtra("Media URL",media_uri);

//                System.out.println("Intent Initialization");

                PendingIntent alarmIntent = PendingIntent.getBroadcast(this, i, activate, 0);
//                System.out.println("Pending Intent");
                cal.set(Calendar.DAY_OF_MONTH,25);
                cal.set(Calendar.YEAR,2020);
                cal.set(Calendar.MONTH,4);
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 00);
                System.out.println("Calendar parameters set");

                alarms.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmIntent);
                Toast.makeText(getApplicationContext(),"Alarm Set",Toast.LENGTH_SHORT).show();
                System.out.println("Alarm is set");
                intentArray.add(alarmIntent);

//                if(i == AlarmList.size()-1)
//                {
//                    cal.add(Calendar.DATE,1);
//                    i = 0;
//                }

            }


        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout root;
        public TextView Label;
        public TextView Time;
        public TextView date;
        public ImageView delete;
//        public Spinner mspinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            Label = itemView.findViewById(R.id.Label);
            Time = itemView.findViewById(R.id.Time);
            date = itemView.findViewById(R.id.Date);
//            mspinner = itemView.findViewById(R.id.spinner1);

            delete = itemView.findViewById(R.id.delete);

        }

        public void setLabelTitle(String string)
        {
            Label.setText(string);
        }
        public void setTimeTitle(String string){ Time.setText(string); }
        public void setDateTitle(String dateTitle){ date.setText(dateTitle); }

    }
}
