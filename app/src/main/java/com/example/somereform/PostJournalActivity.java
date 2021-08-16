package com.example.somereform;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import io.grpc.Context;
import util.JournalApi;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private ImageView imageView;




    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
     private final FirebaseFirestore db= FirebaseFirestore.getInstance();
     private final CollectionReference collectionReference =db.collection("Journal");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);




firebaseAuth =FirebaseAuth.getInstance();
        ProgressBar progressBar = findViewById(R.id.post_progressBar);
        EditText titleEditText = findViewById(R.id.post_title_et);
        EditText thoughtsEditText = findViewById(R.id.post_description_et);
imageView =findViewById(R.id.post_imageView);
        TextView currentUserTextView = findViewById(R.id.post_username_textview);
        Button saveButton = findViewById(R.id.post_save_journal_button);
saveButton.setOnClickListener(this);
        ImageView addPhotoButton = findViewById(R.id.postCameraButton);
addPhotoButton.setOnClickListener(this);




if(JournalApi.getInstance()!=null) {
    String currentUserId = JournalApi.getInstance().getUserId();
    String currentUserName = JournalApi.getInstance().getUsername();
currentUserTextView.setText(currentUserName);











}

authStateListener =new FirebaseAuth.AuthStateListener(){

    @Override
    public void onAuthStateChanged(@NonNull  FirebaseAuth firebaseAuth) {

        user = firebaseAuth.getCurrentUser();
        if(user!=null){

        }else{

        }









    }
};









    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_save_journal_button:

                // savejournal
                break;
            case R.id.postCameraButton:
                //get image from gallery/phone
                Intent galleryIntent =new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*"); // anyrthing ng related  to image.
              startActivityForResult(galleryIntent,GALLERY_CODE);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==GALLERY_CODE && resultCode== RESULT_OK){

            if(data !=null){

                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);  // show image



            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user= firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}