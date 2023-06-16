package com.example.madlabproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class admin_panel extends AppCompatActivity {

    Button button,set;
    EditText demotextfield;
    TextView text;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        button=findViewById(R.id.logout);
        text=findViewById(R.id.note);
        demotextfield=findViewById(R.id.demotextfield);
        set=findViewById(R.id.set);
        button.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),choose.class);
            startActivity(intent);
            finish();
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String collectionPath, documentId,fieldName,value;
                collectionPath="demo";
                documentId="demo";
                fieldName="text";
                value=demotextfield.getText().toString();
                setValueInField(collectionPath,documentId,fieldName,value);
            }
            private void setValueInField(String collectionPath, String documentId, String fieldName, String value) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentRef = db.collection(collectionPath).document(documentId);

                documentRef.update(fieldName, value)
                        .addOnSuccessListener(aVoid -> {
                            // Value successfully set in the field
                            Toast.makeText(admin_panel.this, "Value updated",
                                    Toast.LENGTH_SHORT).show();

                        })
                        .addOnFailureListener(error -> {
                            // Error occurred while setting the value
                            Toast.makeText(admin_panel.this, "Value updation failed",
                                    Toast.LENGTH_SHORT).show();

                        });
            }
        });
    }
}