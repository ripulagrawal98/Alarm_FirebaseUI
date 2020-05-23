package com.nit.alarm_firebaseui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.khizar1556.mkvideoplayer.MKPlayerActivity;

public class AfterAlarm extends AppCompatActivity {




    private String alarm_video;
    private String videourl = "https://firebasestorage.googleapis.com/v0/b/alarmfirebaseui.appspot.com/o/videoplayback.mp4?alt=media&token=3fae4f15-5fbc-4971-833e-91d3dc651c2e";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent mintent = getIntent();
        alarm_video  = mintent.getStringExtra("URL_MEDIA");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_alarm);
        Uri uri1 = Uri.parse(alarm_video);
        MKPlayerActivity.configPlayer(this).play(alarm_video);




    }

}
