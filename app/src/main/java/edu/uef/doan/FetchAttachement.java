package edu.uef.doan;

import static edu.uef.doan.LoginActivity.storage;
import static edu.uef.doan.LoginActivity.userDocument;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FetchAttachement {
//    StorageReference storageRef = storage.getReference();
//    public void Fetch(FirebaseFirestore db, Context cxt){
//        StorageReference attachmentRef = storageRef.child(userDocument.getId()+"/userpfp.jpg");
//        File localFile = new File(getApplicationInfo().dataDir + "/user/pfp/userpfp.jpg");
//        attachmentRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
//            // Local temp file has been created
//            PopulateList.UpdateL(db,LoginActivity.this);
//            Log.v("DownloadPfp","pfp downloaded");
//        }).addOnFailureListener(exception -> {
//            // Handle any errors
//            PopulateList.UpdateL(db,LoginActivity.this);
//            Log.v("DownloadPfp","failed");
//        });
//    }

}
