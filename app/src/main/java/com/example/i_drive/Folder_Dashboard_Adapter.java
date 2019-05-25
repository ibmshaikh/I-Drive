package com.example.i_drive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Folder_Dashboard_Adapter extends RecyclerView.Adapter<Folder_Dashboard_Adapter.Viewholder> {

    private List<Model> list;
    private DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
    private String myuid = FirebaseAuth.getInstance().getUid();
    private Context context;
    private String Folder_Name;

    public Folder_Dashboard_Adapter(List<Model> list,String folder_name) {
        this.list = list;
        this.Folder_Name = folder_name;
    }

    @NonNull
    @Override
    public Folder_Dashboard_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_layout,viewGroup,false);
        context = view.getContext();
        return new Folder_Dashboard_Adapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, int i) {


            mref.child(myuid).child(Folder_Name).child(list.get(i).getName()).child("Value").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String image = dataSnapshot.child("image").getValue().toString();
                    Glide.with(context).load(image).into(viewholder.imageView);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.file_name);
        }
    }
}
