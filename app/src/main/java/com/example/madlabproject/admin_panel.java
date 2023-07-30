package com.example.madlabproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class admin_panel extends AppCompatActivity {

    Button button,set;
    EditText demotextfield,demotextfielddescription,demotextfieldcost,demotextfieldcount;
    ImageView imageview;
    TextView text;
    String selected;
    int documentcount;
    Uri uri;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.aitem) {
            // Handle search action
            Toast.makeText(admin_panel.this, "add items clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(), admin_panel.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.ditem) {
            Toast.makeText(admin_panel.this, "delete items clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(), admin_delete.class);
            startActivity(intent);
            finish();
            // Handle settings action
            return true;
        }else if (id == R.id.logout) {
            Toast.makeText(admin_panel.this, "logout clicked", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(), choose.class);
            startActivity(intent);
            finish();
            // Handle settings action
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#22C7A9"));
        assert actionBar != null;
        actionBar.setTitle("Foodie Finds");
        actionBar.setBackgroundDrawable(colorDrawable);
        setContentView(R.layout.activity_admin_panel);
        Spinner spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        button=findViewById(R.id.logout);
        text=findViewById(R.id.note);
        demotextfield=findViewById(R.id.demotextfield);
        demotextfielddescription=findViewById(R.id.demotextfielddescription);
        demotextfieldcost=findViewById(R.id.demotextfieldcost);
        set=findViewById(R.id.set);
        imageview=findViewById(R.id.imageview);
        demotextfieldcount=findViewById(R.id.demotextfieldcount);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageview.setImageURI(result);
                            uri=result;
                            // Handle the selected image here
                            // Do something with the selected image URI
                        }
                    }
                });

        imageview = findViewById(R.id.imageview);
        button.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),choose.class);
            startActivity(intent);
            finish();
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }});
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerLauncher.launch("image/*");
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count=demotextfieldcount.getText().toString();
                if(Integer.parseInt(count)<=0){
                    Toast.makeText(admin_panel.this, "Enter valid unit count",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                Uri imageUri =uri ; // The URI of the image you want to upload

// Create a reference to the location where you want to store the image
                StorageReference imageRef = storageRef.child("productimages/"+demotextfield.getText().toString()+demotextfieldcost.getText().toString());

// Upload the image file to Firebase Storage
                UploadTask uploadTask = imageRef.putFile(imageUri);

// Optional: You can listen to the upload progress or handle success/failure
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(admin_panel.this, "Product added",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin_panel.this, "Task Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = firestore.collection("products");
                collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();

                            if (querySnapshot != null) {
                                documentcount = (querySnapshot.size()+2);
                                DocumentReference docref=firestore.collection("products").document();
                               // "product"+documentcount
                                Map<String, Object> data = new HashMap<>();
                                data.put("title", demotextfield.getText().toString());
                                data.put("description", demotextfielddescription.getText().toString());
                                data.put("cost",demotextfieldcost.getText().toString());
                                data.put("uri",uri);
                                data.put("category",selected);
                                data.put("count",count);
                                docref.set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(admin_panel.this, "Product added",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(admin_panel.this, "Product addition failed",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                imageview.setImageURI(null);
                                demotextfieldcost.setText("");
                                demotextfielddescription.setText("");
                                demotextfield.setText("");
                                demotextfieldcount.setText("");
                            }
                        }
                    }
                });}

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}