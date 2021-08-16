package com.example.somereform;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.JournalApi;

public class CreateAccountActivity extends AppCompatActivity {
    private Button loginButton;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;// to listen
    private FirebaseUser currentUser;

    //Firestore connection
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference =db.collection("Users");


    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;

    private EditText userNameEditText;
    private Calendar JournalApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        Button createAcctButton = findViewById(R.id.create_acct_button);
      progressBar=findViewById(R.id.create_acct_progress);
      emailEditText=findViewById(R.id.email_account);
      passwordEditText=findViewById(R.id.password_account);

      userNameEditText=findViewById(R.id.username_account);
      firebaseAuth=FirebaseAuth.getInstance();


        //Firestore is subset of firbase which can authincate user,create user  ,Firebase

        // to authenticate user
        authStateListener = firebaseAuth -> {

            currentUser =firebaseAuth.getCurrentUser();

            // user is already logged in
            // no user yet.


        };

        createAcctButton.setOnClickListener(v -> {
if(!TextUtils.isEmpty(emailEditText.getText().toString())&& !TextUtils.isEmpty(passwordEditText.getText().toString())&& !TextUtils.isEmpty(userNameEditText.getText().toString())){

String email=emailEditText.getText().toString().trim();
String password=passwordEditText.getText().toString().trim();
String username=userNameEditText.getText().toString().trim();


createUserEmailAccount(email,password,username);
}else{
Toast.makeText(CreateAccountActivity.this,"Empty Field are Not Allowed",Toast.LENGTH_LONG).show();
}



        });



    }
    private void createUserEmailAccount(String email,String password,String username){


        if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password) &&!TextUtils.isEmpty(username)){

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull  Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        // we take user tto add joournal activity

                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        String currentUserId = currentUser.getUid();

                        // Create a user map so we can create  User collection.
                        Map<String, String> userObj = new HashMap<>();

                        userObj.put("userId", currentUserId);
                        userObj.put("username", username);
                        // save to our firebase database.
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull  Task<DocumentSnapshot> task) {

                                        if (Objects.requireNonNull(task.getResult()).exists()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            String name = task.getResult().getString("username");

                                   JournalApi journalApi = util.JournalApi.getInstance();
                                   journalApi.setUserId(currentUserId);
                                   journalApi.setUsername(name);

                                            Intent intent = new Intent(CreateAccountActivity.this, PostJournalActivity.class);

                                            intent.putExtra("username", name);
                                            intent.putExtra("userId", currentUserId);
                                          startActivity(intent);




                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }

                                    }
                                });

                            }// elase something went wrrong.

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateAccountActivity.this,"Sorry unsuccessfull",Toast.LENGTH_LONG).show();

                            }
                        });


                    }else {
                        //something went wrong
                    }


                }




            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateAccountActivity.this,"Sorry unsuccessfull",Toast.LENGTH_LONG).show();
                }
            });

        }else {

        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        currentUser =firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);


    }
}