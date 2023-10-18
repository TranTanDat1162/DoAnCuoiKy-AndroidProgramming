package edu.uef.doan;

import static edu.uef.doan.LoginActivity.user;
import static edu.uef.doan.LoginActivity.userDocument;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserActivity extends AppCompatActivity {
    private EditText fullName, phoneNumber, email;
    int SELECT_PICTURE = 200;
    Button apply_btn;
    ImageButton uploadImage_btn,return_btn;
    ImageView uploadedImage_view;
    FirebaseFirestore db;
    // Create a storage reference from our app
    StorageReference storageRef;
    FirebaseStorage storage;
    Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Initialize variables
        initializeVar();

        if (user.getFullname() != null) fullName.setHint(user.getFullname());
        if (user.getEmail() != null) email.setHint(user.getEmail());
        if (user.getPhone() != null) phoneNumber.setHint(user.getPhone());
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        uploadImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullName.getText().toString();
                String phone = phoneNumber.getText().toString();
                String mail = email.getText().toString();
                String id = userDocument.getId();

                if (!name.equals("")) user.setFullname(name);
                if (!phone.equals("")) user.setPhone(phone);
                if (!mail.equals("")) user.setEmail(mail);

                if(selectedImageUri != null){
                    StorageReference riversRef = storageRef.child(id+"/userpfp.jpg");
                    UploadTask uploadTask = riversRef.putFile(selectedImageUri);
                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(UserActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            user.setImage("Yes");
                            db.collection("users").document(id).set(user);
                            Toast.makeText(UserActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //Send the newly acquired data to the cloud
                else {
                    db.collection("users").document(id).set(user);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }
    private void initializeVar(){
        fullName = findViewById(R.id.editText_FullName);
        phoneNumber = findViewById(R.id.editText_Phone);
        email = findViewById(R.id.editText_Email);
        apply_btn = findViewById(R.id.done_btn);
        uploadImage_btn = findViewById(R.id.uploadImagebtn);
        uploadedImage_view = findViewById(R.id.uploadedImage);
        return_btn = findViewById(R.id.returnButton);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }
    // this function is triggered when
    // the Select Image Button is clicked
    public void imageChooser() {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    uploadedImage_view.setImageURI(selectedImageUri);
                }
            }
        }
    }
}