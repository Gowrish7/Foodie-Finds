package com.example.madlabproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
public class MainActivity extends AppCompatActivity {

    Button button,refresh;
    TextView demotext;
    private ListenerRegistration fieldListenerRegistration;
    TextView text;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.logout);
        text=findViewById(R.id.note);
        demotext=findViewById(R.id.demotext);
        refresh=findViewById(R.id.refresh);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent=new Intent(getApplicationContext(),login.class);
            startActivity(intent);
            finish();
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String collectionPath,documentId,fieldname;
                collectionPath="demo";
                documentId="demo";
                fieldname="text";
                retrieveFieldValue(collectionPath,documentId,fieldname);

            }

            private void retrieveFieldValue(String collectionPath, String documentId, String fieldName) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentRef = db.collection(collectionPath).document(documentId);

                documentRef.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Object fieldValue = documentSnapshot.get(fieldName);
                                // Use the fieldValue as needed
                                demotext.setText(fieldValue.toString());
                                Toast.makeText(MainActivity.this, "Refresh successful",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Error in refreshing",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(error -> {
                            // Error occurred while retrieving the field value
                            Toast.makeText(MainActivity.this, "Error in refreshing",
                                    Toast.LENGTH_SHORT).show();
                        });
            }
        });
        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(),choose.class);
            startActivity(intent);
            finish();
        });



    }
}