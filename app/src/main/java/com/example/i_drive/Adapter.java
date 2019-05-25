package com.example.i_drive;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder>{

    private List<Model> list;
    private DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
    private String myuid = FirebaseAuth.getInstance().getUid();
    private Context context;

    public Adapter(List<Model> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_layout,viewGroup,false);
        context = view.getContext();
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, final int i) {

        //if (list.get(i).getType().equals("Folder")){
        //    viewholder.imageView.setImageResource(R.drawable.folder);
        //    viewholder.textView.setText(list.get(i).getName());
        //}else if (list.get(i).getType().equals("image")){
        //    viewholder.imageView.setImageResource(R.drawable.upload);
        //    viewholder.textView.setText(list.get(i).getName());
        //}
        final String folder_name = list.get(i).getName();
        mref.child(myuid).child(list.get(i).getName()).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String type = dataSnapshot.child("type").getValue().toString();
                    if (type.equals("Folder")) {
                        viewholder.imageView.setImageResource(R.drawable.folder);
                        viewholder.textView.setText(list.get(i).getName());
                        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(context,Folder_Dashboard.class);
                                i.putExtra("parent",folder_name);
                                context.startActivity(i);
                            }
                        });
                    } else if (type.equals("image")) {
                        viewholder.imageView.setImageResource(R.drawable.upload);
                        viewholder.textView.setText(list.get(i).getName());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Old version
     //  mref.child(myuid).child(list.get(i).getName()).addValueEventListener(new ValueEventListener() {
     //      @Override
     //      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
     //          String type = dataSnapshot.child("type").getValue().toString();
     //          if (type.equals("Folder")){
     //              viewholder.imageView.setImageResource(R.drawable.folder);
     //              viewholder.textView.setText(list.get(i).getName());
     //          }else if (type.equals("image")){
     //              viewholder.imageView.setImageResource(R.drawable.upload);
     //              viewholder.textView.setText(list.get(i).getName());
     //          }

     //      }
     //      @Override
     //      public void onCancelled(@NonNull DatabaseError databaseError) {

     //      }
     //  });
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
