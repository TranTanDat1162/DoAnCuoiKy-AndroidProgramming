package edu.uef.doan;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import static edu.uef.doan.LoginActivity.mList;
import static edu.uef.doan.LoginActivity.userDocument;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PopulateList {
   public static void UpdateL(FirebaseFirestore db, Context cxt)
   {
//       List mList = new ArrayList<AssignmentList>();  //this is my arraylist
       db.collection("users")
               .document(userDocument.getId())
               .collection("assignment")
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               mList.add(new AssignmentList(document.getId(),document.toObject(Assignment.class)));
//                               Intent intent = new Intent(cxt, HomeActivity.class);
//                               cxt.startActivity(intent);
                               Log.d(TAG, document.getId() + " => " + document.getData());
                           }
                           Log.v(TAG,mList.toString());
                       } else {
                           Log.d(TAG, "Error getting documents: ", task.getException());
                       }
                   }
               });
   }
}

