package com.example.madlabproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.util.Patterns;

import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    EditText text,text1;
    TextView text2;
    Button button1;

    String email,password;
    FirebaseAuth mAuth;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-z])"+
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{6,12}" +                // at least 6 characters
                    "$");
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),user_panel.class);
            startActivity(intent);
            finish();
        }
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        text=findViewById(R.id.email);
        text1=findViewById(R.id.password);
        button1=findViewById(R.id.button1);
        text2=findViewById(R.id.RegisterNow);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=text.getText().toString().trim();
                password=text1.getText().toString().trim();
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(register.this, "Enter email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(register.this, "Enter Password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!emailValidator(email)){}
                // if password does not matches to the pattern
                // it will display an error message "Password is too weak"
                else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    Toast.makeText(register.this, "Password is too weak",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(register.this, "Registration successful",
                                            Toast.LENGTH_SHORT).show();
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent=new Intent(getApplicationContext(),login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });}
//                Intent com.example.madlabproject.intent=new Intent(getApplicationContext(),login.class);
//                startActivity(com.example.madlabproject.intent);
////                finish();
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), choose.class);
        startActivity(intent);
        finish();
    }
    public boolean emailValidator(String email) {

        // extract the entered data from the EditText

        // Android offers the inbuilt patterns which the entered
        // data from the EditText field needs to be compared with
        // In this case the entered data needs to compared with
        // the EMAIL_ADDRESS, which is implemented same below
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(register.this, "Email Verified !", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(register.this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}