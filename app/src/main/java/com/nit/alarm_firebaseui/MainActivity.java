package com.nit.alarm_firebaseui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.FirebaseRecyclerViewAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
//    private TextView empty_view;
    private FloatingActionButton fab;

    private FirebaseRecyclerAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
//        empty_view = (TextView)findViewById(R.id.empty_view);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNewAlarm.class);
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

        FirebaseRecyclerOptions<Alarm> options = new FirebaseRecyclerOptions.Builder<Alarm>()
                                                                .setQuery(query, new SnapshotParser<Alarm>() {
                                                                    @NonNull
                                                                    @Override
                                                                    public Alarm parseSnapshot(@NonNull DataSnapshot snapshot) {
                                                                        return new Alarm(snapshot.child("Label").getValue().toString(),
                                                                                        snapshot.child("Time").getValue().toString());
                                                                    }
                                                                })
                                                                .build();

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

                viewHolder.root.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        mRecyclerView.setAdapter(adapter);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            Label = itemView.findViewById(R.id.Label);
            Time = itemView.findViewById(R.id.Time);
        }

        public void setLabelTitle(String string)
        {
            Label.setText(string);
        }
        public void setTimeTitle(String string){

            Time.setText(string);
        }

    }
}
