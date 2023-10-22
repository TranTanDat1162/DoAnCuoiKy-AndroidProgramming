package edu.uef.doan;

import static edu.uef.doan.LoginActivity.user;
import static edu.uef.doan.Preferences.*;
import static edu.uef.doan.LoginActivity.userDocument;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView app_name;
    FirebaseFirestore db;
    String TAG = "SplashAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addControl();
        addEvent();
    }

    private void addEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
////                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                boolean isUserLogged = getBooleanDefaults(getString(R.string.userlogged),SplashActivity.this);
                Intent i;
                if(isUserLogged) {
                    db = FirebaseFirestore.getInstance();
                    String id = getStringDefaults(getString(R.string.userid),SplashActivity.this);
                    Log.v(TAG,"ID"+ id);
                    DocumentReference docRef = db.collection("users").document(id);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                userDocument = task.getResult();
                                PopulateList.UpdateL(db,SplashActivity.this);
                                if (userDocument.exists()) {
                                    user = userDocument.toObject(User.class);
                                    Log.v(TAG,"ID"+ userDocument.getId());
                                    Log.d(TAG, "DocumentSnapshot data: " + userDocument.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                    Log.v("Login state","True");
                    i = new Intent(SplashActivity.this, HomeActivity.class);
                }
                else{
                    Log.v("Login state","false");
                    try {
                        File dir = new File(getApplicationInfo().dataDir + "/user");
                        FileUtils.cleanDirectory(dir);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    PopulateList.UpdateL(db,SplashActivity.this);
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair(imageView, "splash_image");
                pairs[1] = new Pair(imageView, "splash_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                startActivity(i,options.toBundle());
            }
        }, 3000);
    }

    private void addControl() {
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imageView = findViewById(R.id.imageView);
        app_name = findViewById(R.id.textView);

        imageView.setAnimation(topAnim);
        app_name.setAnimation(bottomAnim);
    }
}