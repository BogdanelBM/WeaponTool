package com.example.bogdanbm.finalweapontool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by BogdanBM on 1/8/2018.
 */

public class RegisterActivity extends AppCompatActivity {
    private EditText email, password;
    private Button btnSignUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.emailRegisterBox);
        password = (EditText) findViewById(R.id.passRegisterBox);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);

        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{

                                    Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
                                    intent.putExtra("useremail", email.getText().toString());

                                    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference users = root.child("users");

                                    User user = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), "user");
                                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                                    startActivity(intent);
                                    //finish();
                                }
                            }
                        });
            }
        });

    }

}
