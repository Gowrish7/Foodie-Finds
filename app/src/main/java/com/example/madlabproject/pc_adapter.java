package com.example.madlabproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;

public class pc_adapter extends RecyclerView.Adapter<pc_adapter.myviewholder> {
    ArrayList<productmodel> productmodels1;
    ArrayList<productmodel> copyproductmodels;
    Context context;
    String search;
    public pc_adapter(Context context, ArrayList<productmodel> productmodels,String search,ArrayList<productmodel>copyproductmodels){
        this.search=search;
        this.context=context;
        this.productmodels1=productmodels;
        this.copyproductmodels=copyproductmodels;
    }
    @NonNull
    @Override
    public pc_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator=LayoutInflater.from(context);
        View view=inflator.inflate(R.layout.item_layout,parent,false);
        return new pc_adapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pc_adapter.myviewholder holder, int position) {

    holder.textView2.setText(productmodels1.get(position).getTitle());
    holder.textView3.setText(productmodels1.get(position).getDescription());
    holder.textView4.setText(productmodels1.get(position).getCost());
    holder.textView5.setText(productmodels1.get(position).getCount());
        Object item;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("productimages").child(productmodels1.get(position).getimagename());
        final long TEN_MEGABYTES = 1024 * 1024;
        storageRef.getBytes(TEN_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Convert the byte array to a Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Set the Bitmap to the ImageView
                holder.imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle error fetching image
            }
        });
    }

    @Override
    public int getItemCount() {
        return productmodels1.size();
    }
    public static class myviewholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView2,textView3,textView4,textView5;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            textView2=itemView.findViewById(R.id.textView2);
            textView3=itemView.findViewById(R.id.textView3);
            textView4=itemView.findViewById(R.id.textView4);
            textView5=itemView.findViewById(R.id.textView5);
        }
    }
    public void setItems(ArrayList<productmodel>items) {
        productmodels1.clear();
        productmodels1.addAll(new ArrayList<>(new HashSet<>(items)));
    }
    public void retrieveItems(ArrayList<productmodel>items){
        productmodels1.addAll(new ArrayList<>(new HashSet<>(items)));
    }
    public void clean(ArrayList<productmodel>items){
        productmodels1.removeAll(items);
    }
}
