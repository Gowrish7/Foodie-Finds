package com.example.madlabproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button,refresh;
    String desc,cost,title;
    TextView demotext;
    private ListenerRegistration fieldListenerRegistration;
    EditText demotextfield,demotextfielddescription,demotextfieldcost,search;
    ImageView imageview;
    TextView text;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    CollectionReference collectionRef = firestore.collection("products");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.logout);
        text=findViewById(R.id.note);
        refresh=findViewById(R.id.refresh);
        demotextfield=findViewById(R.id.demotextfield);
        demotextfielddescription=findViewById(R.id.demotextfielddescription);
        demotextfieldcost=findViewById(R.id.demotextfieldcost);
        imageview=findViewById(R.id.imageview);
        search=findViewById(R.id.search);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent=new Intent(getApplicationContext(),login.class);
            startActivity(intent);
            finish();
        }
        refresh.setOnClickListener(v -> {
            String fieldToFilter = "title";
            String filterValue = search.getText().toString();

            // Create a query to filter documents based on the field value
            Query query = collectionRef.whereEqualTo(fieldToFilter, filterValue);

            query.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();

                                if (querySnapshot != null) {
                                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                                    for (DocumentSnapshot document : documents) {
                                        // Access the field values for each document
                                        title = document.getString("title");
                                        desc = document.getString("description");
                                        cost = document.getString("cost");

                                        // Access other fields as needed
                                        // ...

                                        // Use the retrieved field values as needed
                                        // ...
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Task Failed",
                                        Toast.LENGTH_SHORT).show();
                                // Handle any errors that occur while retrieving the data
                            }
                            demotextfield.setText(title);
                            demotextfielddescription.setText(desc);
                            demotextfieldcost.setText(cost);

                            // Retrieve and display the image
                            String imageName = demotextfield.getText().toString() + demotextfieldcost.getText().toString() + ".jpg";

                            StorageReference imageRef = storageRef.child("productimages/"+demotextfield.getText().toString() + demotextfieldcost.getText().toString() );

                            final long ONE_MEGABYTE = 1024 * 1024;
                            imageRef.getBytes(ONE_MEGABYTE)
                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            // Convert the byte array to a Bitmap or use any other image processing mechanism
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                            // Set the bitmap to the ImageView
                                            imageview.setImageBitmap(bitmap);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle any errors that occur while retrieving the image
                                            Toast.makeText(MainActivity.this, "Image retrieval failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
        });

        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(),choose.class);
            startActivity(intent);
            finish();
        });
    }
}