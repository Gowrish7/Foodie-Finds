package com.example.madlabproject;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class admin_login extends AppCompatActivity {
    EditText text,text1;
    TextView text2;
    Button button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseAuth mAuth=FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//            finish();;
//        }
//    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        text=findViewById(R.id.email);
        text1=findViewById(R.id.password);
        button=findViewById(R.id.button);
        text2=findViewById(R.id.LoginNow);
//        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;
                email=text.getText().toString();
                password=text1.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(admin_login.this, "Enter email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(admin_login.this, "Enter Password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                validateLogin(email,password);
            }
            private void validateLogin(String email, String password) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Query query = db.collection("admin")
                        .whereEqualTo("email", email)
                        .whereEqualTo("password", password);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Login successful, user exists in Firestore
                                // Perform the necessary actions (e.g., navigate to another screen)
                                Toast.makeText(admin_login.this, "Login Successful",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),admin_panel.class);
                                startActivity(intent);
                                finish();


                            } else {
                                Toast.makeText(admin_login.this, "Login failed",
                                        Toast.LENGTH_SHORT).show();

                                // Invalid login credentials, user does not exist or incorrect password
                                // Display an error message or perform any other required actions
                            }
                        }
                    }
                });
            }
        });
    }
}








