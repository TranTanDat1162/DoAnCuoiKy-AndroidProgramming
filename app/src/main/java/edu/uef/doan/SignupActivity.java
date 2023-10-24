package edu.uef.doan;

import static edu.uef.doan.LoginActivity.user;
import static edu.uef.doan.LoginActivity.userDocument;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextView tvLogin;

    EditText etUsername, etPassword, etConfirmPassword;

    Button btnSignup;
    DocumentReference docRef;
    static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd HH:mm");


    // Code using Cloud Firebase
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        addControl();
        addEvent();
    }

    private void addEvent() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Code using Cloud fire database
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirmpassword = etConfirmPassword.getText().toString();

                if (confirmpassword.isEmpty() || password.isEmpty() || username.isEmpty())
                {
                    AnimationForSignUpFail();
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    // Kiểm tra xem người dùng đã cung cấp đủ thông tin chưa.
                    if (password.isEmpty() || username.isEmpty()) {
                        AnimationForSignUpFail();
                        Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }

                    else if (!confirmpassword.equals(password))
                    {
                        AnimationForSignUpFail();
                        Toast.makeText(SignupActivity.this, "Passwords do not match together", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        // Trước khi thêm tài khoản, kiểm tra xem tài khoản đã tồn tại chưa.
                        db.collection("users")
                                .whereEqualTo("username", username)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (!task.getResult().isEmpty()) {
                                                // Tài khoản đã tồn tại.
                                                AnimationForSignUpFail();
                                                Toast.makeText(SignupActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Tài khoản chưa tồn tại, thêm vào Firestore.
                                                User user = new User(username, password);

                                                db.collection("users")
                                                        .add(user)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                AnimationForSignUpSuccess();
                                                                docRef = documentReference;
                                                                firstMsg();
                                                                Toast.makeText(SignupActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();

                                                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                AnimationForSignUpFail();
                                                                Toast.makeText(SignupActivity.this, "Fail to register", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            // Xử lý lỗi.
                                            Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }

            private void AnimationForSignUpSuccess() {
                Animation anim = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.shake);
                btnSignup.setBackgroundResource(R.drawable.login_success);
                btnSignup.startAnimation(anim);
            }

            private void AnimationForSignUpFail() {
                Animation anim = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.shake);
                btnSignup.setBackgroundResource(R.drawable.login_fail);
                btnSignup.startAnimation(anim);
            }
        });
    }

    private void addControl() {
        // Code using Cloud Firebase
        db = FirebaseFirestore.getInstance();

        tvLogin = findViewById(R.id.tvLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
    }
    private void firstMsg(){
        // Lấy ID của người dùng hiện tại từ Firebase Authentication
        String id = docRef.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Tạo một Object chứa dữ liệu để lưu vào Firestore
        Assignment assignmentData = new Assignment();
        assignmentData.setTopic("Welcome to Homework manager");
        assignmentData.setCreateTime(String.valueOf(timestamp.toString()));

        // Lưu dữ liệu vào Firestore trong bảng "assignments" của người dùng hiện tại
        db.collection("users").document(id).collection("assignment")
                .add(assignmentData)
                .addOnSuccessListener(documentReference -> {
                    // Xử lý khi dữ liệu được lưu thành công
                    Toast.makeText(SignupActivity.this, "Dữ liệu đã được lưu thành công vào Firestore.", Toast.LENGTH_SHORT).show();
                    // Điều hướng hoặc thực hiện các hành động cần thiết sau khi lưu dữ liệu thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi dữ liệu không thể được lưu vào Firestore
                    Toast.makeText(SignupActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}