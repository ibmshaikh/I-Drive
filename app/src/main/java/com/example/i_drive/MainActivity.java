package com.example.i_drive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText edtemai,edtpassword;
    private Button login;
    private String email,password;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(MainActivity.this,Dashboard.class));
        }

        edtemai = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);
        login = findViewById(R.id.login);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("Please wait");
        dialog.setMessage("Please wait log you in...");
        dialog.setCanceledOnTouchOutside(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edtemai.getText().toString().trim();
                password = edtpassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()){
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                dialog.dismiss();
                                finish();
                                startActivity(new Intent(MainActivity.this,Dashboard.class));

                            }else {
                                dialog.dismiss();
                                edtemai.setError("email or password is incorrect");

                            }
                        }
                    });
                }

            }
        });

    }

    public void Register(View view){
        Intent i = new Intent(MainActivity.this,Register.class);
        startActivity(i);
    }
}
