package com.example.bogdanbm.finalweapontool;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btnLogin, btnRegister;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        email = (EditText) findViewById(R.id.emailBox);
        password = (EditText) findViewById(R.id.passBox);
        btnLogin = (Button) findViewById(R.id.login_button);
        btnRegister = (Button) findViewById(R.id.register_button);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user_email = email.getText().toString();
                final String user_password = password.getText().toString();

                if (TextUtils.isEmpty(user_email)) {
                    Toast.makeText(getApplicationContext(), "Empty email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(user_password)) {
                    Toast.makeText(getApplicationContext(), "Empty password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(user_email,user_password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login failed! Invalid email or password,\nPlease try agin.", Toast.LENGTH_LONG).show();
                                } else {
                                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference users = root.child("users");

                                    users.addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            //refreshes list if any change; for first initialization is taking all elements even if there is no change detected
                                            Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                                            Iterator<DataSnapshot> iterator = data.iterator();

                                            while(iterator.hasNext())
                                            {
                                                User user = iterator.next().getValue(User.class);
                                                if(Objects.equals(user.email, user_email))
                                                {
                                                    Intent intent;
                                                    if(Objects.equals(user.role, "admin"))
                                                    {
                                                        //trecem de la activitatea curenta la urmatoarea
                                                        intent = new Intent(LoginActivity.this, AdminActivity.class);
                                                        intent.putExtra("email", user_email);
                                                    }
                                                    else
                                                    {
                                                        intent = new Intent(LoginActivity.this, UserActivity.class);
                                                        intent.putExtra("email", user_email);
                                                    }

                                                    startActivity(intent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
