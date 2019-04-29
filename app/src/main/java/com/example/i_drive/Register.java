package com.example.i_drive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText edtemai,edtpassword,edtretype;
    private Button login;
    private String email,password,retype;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(Register.this,Dashboard.class));
        }

        edtemai = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);
        edtretype = findViewById(R.id.edtretype);
        login = findViewById(R.id.login);

        dialog = new ProgressDialog(Register.this);
        dialog.setTitle("Please wait");
        dialog.setMessage("Please wait Registering");
        dialog.setCanceledOnTouchOutside(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edtemai.getText().toString().trim();
                password = edtpassword.getText().toString().trim();
                retype = edtretype.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty() && password.equals(retype)){
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(Register.this,"Account created successfully",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                finish();
                                startActivity(new Intent(Register.this,Dashboard.class));

                            }else {
                                dialog.dismiss();
                                edtemai.setError("Something went wrong please try again");

                            }
                        }
                    });
                }else {
                    Toast.makeText(Register.this,"Password didn't match",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void login(View view){

        Intent i = new Intent(Register.this,MainActivity.class);
        startActivity(i);
    }

}
