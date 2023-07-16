package com.example.madlabproject;

import android.annotation.SuppressLint;
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

public class cart_adapter extends RecyclerView.Adapter<cart_adapter.myviewholder> {
    ArrayList<productmodel> productmodels1;
    ArrayList<productmodel> copyproductmodels;
    Context context;
    String search;
    int holderpos;
    private selectlistener listener;
    public cart_adapter(Context context, ArrayList<productmodel> productmodels,String search,ArrayList<productmodel>copyproductmodels,selectlistener listener){
        this.search=search;
        this.context=context;
        this.productmodels1=productmodels;
        this.copyproductmodels=copyproductmodels;
        this.listener=listener;
    }
    @NonNull
    @Override
    public cart_adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator=LayoutInflater.from(context);
        View view=inflator.inflate(R.layout.adminitem_layout,parent,false);
        return new cart_adapter.myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cart_adapter.myviewholder holder, @SuppressLint("RecyclerView") int position) {


        holder.textView2.setText(productmodels1.get(position).getTitle());
        holder.textView3.setText(productmodels1.get(position).getDescription());
        holder.textView4.setText(productmodels1.get(position).getCost());
        holder.textView5.setText(productmodels1.get(position).getCount());
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
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderpos=position;listener.onItemClicked(productmodels1.get(position));
                int position = holder.getLayoutPosition();
                if (position!= RecyclerView.NO_POSITION && position>=0) {
                    // Remove the item from the list
                    productmodels1.remove(position);
                    notifyItemRemoved(position);
                }
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
        public ImageView imageview;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView11);
            textView2=itemView.findViewById(R.id.textView12);
            textView3=itemView.findViewById(R.id.textView13);
            textView4=itemView.findViewById(R.id.textView14);
            textView5=itemView.findViewById(R.id.textView15);
            imageview=itemView.findViewById(R.id.imageView16);
        }
    }
    public void setItems(ArrayList<productmodel>items) {
        productmodels1.clear();
        productmodels1.addAll(items);
        notifyDataSetChanged();
    }
    public void remove(ArrayList<productmodel>items){
        productmodels1.clear();
        productmodels1.addAll(items);
        notifyItemRemoved(holderpos);
    }
    public void retrieveItems(ArrayList<productmodel>items){
        productmodels1.addAll(new ArrayList<>(new HashSet<>(items)));
    }
    public void clean(ArrayList<productmodel>items){
        if (items != null) {
            productmodels1.remove(items);
            notifyDataSetChanged();
        }
    }
    public int fetch(){
        return holderpos;
    }
}
