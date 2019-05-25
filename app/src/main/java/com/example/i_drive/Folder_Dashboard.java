package com.example.i_drive;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Folder_Dashboard extends AppCompatActivity {

    private FloatingActionButton fab;
    private DatabaseReference mref;
    private String myUID;
    private RecyclerView recyclerView;
    private List<Model> list;
    private Folder_Dashboard_Adapter adapter;
    private Context mContext = Folder_Dashboard.this;
    private ProgressDialog mDialog;
    private String Parent_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder__dashboard);

        Parent_folder = getIntent().getStringExtra("parent");

        mref = FirebaseDatabase.getInstance().getReference();
        myUID = FirebaseAuth.getInstance().getUid();

        recyclerView = findViewById(R.id.list);
        GridLayoutManager gm = new GridLayoutManager(mContext,2);
        recyclerView.setLayoutManager(gm);
        list = new ArrayList<>();
        adapter = new Folder_Dashboard_Adapter(list,Parent_folder);
        recyclerView.setAdapter(adapter);
        mDialog = new ProgressDialog(mContext);
        mDialog.show();

        mref.child(myUID).child(Parent_folder).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    if (!name.equals("info")){
                        Model model = new Model();
                        model.setName(name);
                        list.add(model);
                        adapter = new Folder_Dashboard_Adapter(list,Parent_folder);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        mDialog.dismiss();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
