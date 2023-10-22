package edu.uef.doan;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static edu.uef.doan.Preferences.*;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    TextView tvSignUp, tvUsername, tvPassword;
    Button btnLogin;
    CheckBox rememberCheck;
    static User user = new User();
    static DocumentSnapshot userDocument;
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static List mList = new ArrayList<AssignmentList>();

    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    // Code using Cloud Firebase
    FirebaseFirestore db;

    // Save login/logout state
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        try {
            setupDir();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addControl();
        addEvent();
    }

    private void addEvent() {
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        // Code use Cloud Firebase (Btn Login)
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tvUsername.getText().toString();
                String password = tvPassword.getText().toString();

                if (username.isEmpty())
                {
                    AnimationForLoginFail();
                    Toast.makeText(LoginActivity.this, "Please field username", Toast.LENGTH_SHORT).show();
                }

                else {
                    // Truy vấn tài liệu trong Firestore để kiểm tra thông tin đăng nhập.
                    db.collection("users")
                            .whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            userDocument = task.getResult().getDocuments().get(0); // Lấy tài liệu đầu tiên (nếu có).
                                            user = userDocument.toObject(User.class);
                                            if (user != null && user.getPassword().equals(password)) {
                                                // Đăng nhập thành công.
                                                AnimationForLoginSuccess();
                                                syncCloud();
                                                if(rememberCheck.isChecked()){
                                                    // Ghi chú TT và trạng thái người dùng vào SharedPreferences
                                                    setBooleanDefaults(getString(R.string.userlogged),true,LoginActivity.this);
                                                    setStringDefaults(getString(R.string.userid),userDocument.getId(),LoginActivity.this);
                                                    Log.v("Login State","true");
                                                }
                                                else {
                                                    setBooleanDefaults(getString(R.string.userlogged),false,LoginActivity.this);
                                                    Log.v("Login State","false");
                                                }
                                                PopulateList.UpdateL(db,LoginActivity.this);
                                                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Sai mật khẩu.
                                                AnimationForLoginFail();
                                                Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            // Sai tên đăng nhập.
                                            AnimationForLoginFail();
                                            Toast.makeText(LoginActivity.this, "Wrong username", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Xử lý lỗi.
                                        Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            private void AnimationForLoginFail() {
                Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                btnLogin.setBackgroundResource(R.drawable.login_fail);
                btnLogin.startAnimation(anim);
            }

            private void AnimationForLoginSuccess() {
                Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                btnLogin.setBackgroundResource(R.drawable.login_success);
                btnLogin.startAnimation(anim);
            }
        });
    }

    private void addControl() {
        // Code using Cloud Firebase
        db = FirebaseFirestore.getInstance();

        tvSignUp = findViewById(R.id.tvSignUp);
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        btnLogin = findViewById(R.id.btnLogin);
        rememberCheck = findViewById(R.id.checkBox);
    }
    private void setupDir() throws IOException {
        Files.createDirectories(Paths.get(getApplicationInfo().dataDir + "/user/pfp"));
        Files.createDirectories(Paths.get(getApplicationInfo().dataDir + "/user/assignments"));
    }
    private void syncCloud(){
        File pfp = new File(getApplicationInfo().dataDir + "/user/pfp/userpfp.jpg");
        if(!pfp.exists()){
            downloadPfp();
        }
        else{
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
    private void downloadPfp(){
        StorageReference pfpRef = storageRef.child(userDocument.getId()+"/userpfp.jpg");
        File localFile = new File(getApplicationInfo().dataDir + "/user/pfp/userpfp.jpg");
        pfpRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            // Local temp file has been created
            PopulateList.UpdateL(db,LoginActivity.this);
            Log.v("DownloadPfp","pfp downloaded");
        }).addOnFailureListener(exception -> {
            // Handle any errors
            PopulateList.UpdateL(db,LoginActivity.this);
            Log.v("DownloadPfp","failed");
        });
    }
}