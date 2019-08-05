package com.example.login2nd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    private EditText register_in_username,register_in_phoneNumber,register_in_password;
    private Button register_in_button;

    private DatabaseReference userTable;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        register_in_username=(EditText)findViewById(R.id.register_in_username);
        register_in_phoneNumber=(EditText)findViewById(R.id.register_in_phoneNumber);
        register_in_password=(EditText)findViewById(R.id.register_in_password);

        register_in_button=(Button) findViewById(R.id.register_in_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        userTable = FirebaseDatabase.getInstance().getReference("UserTable");

        register_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String username = register_in_username.getText().toString().trim();
                String phoneNumber = register_in_phoneNumber.getText().toString().trim();
                String password = register_in_password.getText().toString().trim();

                Users users = new Users(username,phoneNumber,password);

                userTable.child(phoneNumber)
                        .setValue(users)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterPage.this, "Registered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterPage.this,MainActivity.class));
                                    finish();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterPage.this, "Registration failed for...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
