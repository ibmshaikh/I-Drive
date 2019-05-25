package com.example.i_drive;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Dashboard extends AppCompatActivity  {

    private FloatingActionButton fab;
    private DatabaseReference mref;
    private String myUID;
    private RecyclerView recyclerView;
    private List<Model> list;
    private Adapter adapter;
    private Context mContext = Dashboard.this;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mref = FirebaseDatabase.getInstance().getReference();
        myUID = FirebaseAuth.getInstance().getUid();

        recyclerView = findViewById(R.id.list);
        GridLayoutManager gm = new GridLayoutManager(Dashboard.this,2);
        recyclerView.setLayoutManager(gm);
        list = new ArrayList<>();
        adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);
        mDialog = new ProgressDialog(Dashboard.this);
        mDialog.show();

        mref.child(myUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    Model model = new Model();
                    model.setName(name);
                    list.add(model);
                    adapter = new Adapter(list);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    mDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab = findViewById(R.id.fab_id);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(Dashboard.this, R.style.BottomSheet_Dialog)
                        .title("Create new")
                        .grid() // <-- important part
                        .sheet(R.menu.menu_bottom_sheet)
                        .listener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                switch (which){

                                    case R.id.upload:
                                        break;
                                    case R.id.folder:
                                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Dashboard.this);
                                        LayoutInflater inflater = Dashboard.this.getLayoutInflater();
                                        View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
                                        dialogBuilder.setView(dialogView);

                                        final EditText editText = (EditText) dialogView.findViewById(R.id.foldername);
                                        TextView save = dialogView.findViewById(R.id.create);
                                        final TextView cancel = dialogView.findViewById(R.id.cancel);
                                        final AlertDialog alertDialog = dialogBuilder.create();

                                        save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                String folder_name = editText.getText().toString().trim();
                                                if (!folder_name.isEmpty()){
                                                    //Toast.makeText(Dashboard.this,"created",Toast.LENGTH_LONG).show();
                                                    Date currentTime = Calendar.getInstance().getTime();
                                                    HashMap h = new HashMap();
                                                    h.put("type","Folder");
                                                    h.put("time_created",currentTime.toString());

                                                    mref.child(myUID).child(folder_name).child("info").setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(Dashboard.this,"Folder created successfully",Toast.LENGTH_LONG).show();
                                                                alertDialog.dismiss();
                                                            }else if (!task.isSuccessful()){
                                                                Toast.makeText(Dashboard.this,"Something went wrong while creating a Folder",Toast.LENGTH_LONG).show();
                                                                alertDialog.dismiss();
                                                            }
                                                        }
                                                    });

                                                    //Previous version
                                                   // mref.child(myUID).child(folder_name).setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   //     @Override
                                                   //     public void onComplete(@NonNull Task<Void> task) {
                                                   //         if (task.isSuccessful()){
                                                   //             Toast.makeText(Dashboard.this,"Folder created successfully",Toast.LENGTH_LONG).show();
                                                   //             alertDialog.dismiss();
                                                   //         }
//
                                                   //     }
                                                   // });

                                                }

                                            }
                                        });
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();
                                            }
                                        });

                                        alertDialog.show();
                                        break;

                                }

                            }
                        }).show()
                ;
            }
        });
    }

}
