package com.nit.alarm_firebaseui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String EMPTY_ENTRIES = "Please fill all the Entries";
    private static final String EMAIL_REGISTRATION_ERROR = "Incorrect Email or Already registered";
    private static final String REGISTRATION_SUCCESS = "User Successfully Registered and Added";

    private EditText FirstName, LastName,mEmail,mPassword;
    private Button Register;
    private TextView loginText;
    private ProgressBar spinner;

    private String first_name, last_name, email,password;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        FirstName = (EditText)findViewById(R.id.FirstName);
        LastName = (EditText)findViewById(R.id.LastName);
        mEmail = (EditText)findViewById(R.id.Email);
        mPassword = (EditText)findViewById(R.id.password);
        Register = (Button)findViewById(R.id.register_btn);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        loginText = (TextView)findViewById(R.id.loginText);

        spinner.setVisibility(View.GONE);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginActivity();
            }
        });

//        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser()!=null) {
//            finish();
//            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
//        }
    }

    public void userRegister()
    {
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        first_name = FirstName.getText().toString().trim();
        last_name = LastName.getText().toString().trim();

        // Any Empty EditText
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name)) {
            Toast.makeText(this, EMPTY_ENTRIES, Toast.LENGTH_SHORT).show();
        }
        else
        {
            spinner.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Login with registered Email
                                firebaseAuth.signInWithEmailAndPassword(email, password);

                                firebaseUser = firebaseAuth.getCurrentUser();
                                // Add User to Firebase Database
                                databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
//                                databaseReference.child(firebaseUser.getUid());
                                Map<String,Object> map = new HashMap<>();
                                map.put("FirstName",first_name);
                                map.put("LastName",last_name);

                                databaseReference.setValue(map);

                                Toast.makeText(AuthenticationActivity.this, REGISTRATION_SUCCESS, Toast.LENGTH_SHORT).show();

                                // To new Activity
                                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                                finish();
                                startActivity(intent);
                            }
                            else {
                                // Incorrect or Already registered
                                Toast.makeText(AuthenticationActivity.this, EMAIL_REGISTRATION_ERROR, Toast.LENGTH_SHORT).show();
                                Log.i("Registration Failed", EMAIL_REGISTRATION_ERROR);
                            }
                            spinner.setVisibility(View.GONE);
                        }
                    });
        }


    }

    public void toLoginActivity() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

}
