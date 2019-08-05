package com.example.login2nd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText sign_in_username,sign_in_password;
    private Button sign_in_button;

    private TextView sign_in_registerHere;

    private DatabaseReference userTable;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_in_username=(EditText)findViewById(R.id.sign_in_username);
        sign_in_password=(EditText)findViewById(R.id.sign_in_password);

        sign_in_registerHere = (TextView)findViewById(R.id.sign_in_registerHere);
        sign_in_registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                startActivity(new Intent(MainActivity.this,RegisterPage.class));
                finish();
            }
        });

        sign_in_button=(Button) findViewById(R.id.sign_in_button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


        userTable = FirebaseDatabase.getInstance().getReference("UserTable");

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String username = sign_in_username.getText().toString().trim();
                final String password = sign_in_password.getText().toString().trim();

                userTable.orderByChild("username").equalTo(username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        Users users = snapshot.getValue(Users.class);
                                        if (users.getPassword().equals(password)){
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Logged in.", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                   /* Users users = dataSnapshot.getValue(Users.class);
                                    if (users.getPassword().equals(password)){
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Logged in.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}
