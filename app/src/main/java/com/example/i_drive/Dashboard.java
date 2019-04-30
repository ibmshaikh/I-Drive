package com.example.i_drive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;

public class Dashboard extends AppCompatActivity  {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


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
                            public void onClick(DialogInterface dialog, int which) {
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
                                                    Toast.makeText(Dashboard.this,"created",Toast.LENGTH_LONG).show();
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
