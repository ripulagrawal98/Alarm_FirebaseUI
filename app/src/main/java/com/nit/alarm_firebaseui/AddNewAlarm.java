package com.nit.alarm_firebaseui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddNewAlarm extends AppCompatActivity {

    private Intent main_intent;
    private String mDate;

    private EditText label_alarm;
    private TextView time_alarm,media_for_alarm;
    private String time;
    private Button upload;
    private Uri downloadUri;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    StorageReference storageRef = mStorage.getReference();

    private int REQUEST_CODE = 122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);

        main_intent = getIntent();
        mDate = main_intent.getStringExtra("Date");
        System.out.println("Date from Calendar "+mDate);


        label_alarm = (EditText)findViewById(R.id.label_alarm);
        time_alarm = (TextView) findViewById(R.id.time_alarm);
        media_for_alarm = (TextView)findViewById(R.id.media_for_alarm);

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

        media_for_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video for Alarm"),REQUEST_CODE);

            }
        });



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Alarms").push();
                Map<String,Object> map = new HashMap<>();
                map.put("Label",label_alarm.getText().toString());
                map.put("Time",time);
                map.put("Media URI",downloadUri.toString());
                map.put("Date",mDate);

                databaseReference.setValue(map);
                Intent intent = new Intent(AddNewAlarm.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }


    protected void onActivityResult(int request_code, int result_code, Intent data) {

        super.onActivityResult(request_code, result_code, data);
        if(request_code == REQUEST_CODE)
        {
            if(result_code == RESULT_OK)
            {
                if (data.getData() != null)
                {
                    Uri uri = data.getData();
                    upload_to_firebase(uri);
                }

            }
        }

    }

    private void upload_to_firebase(Uri uri)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading..");
        progressDialog.show();

        StorageReference videoref = storageRef.child("Alarms");
        final StorageReference video2ref = videoref.child(UUID.randomUUID().toString());
        UploadTask uploadTask = video2ref.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded "+(int)progress + "%");
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                System.out.println("Either it is the downloading URL "+video2ref.getDownloadUrl());
                return video2ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    System.out.println("Or it is the downloadable URI "+ downloadUri);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
}
