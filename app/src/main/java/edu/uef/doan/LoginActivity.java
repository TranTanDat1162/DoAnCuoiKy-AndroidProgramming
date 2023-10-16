package edu.uef.doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    TextView tvSignUp, tvUsername, tvPassword;
    Button btnLogin;

    // Code using for Realtime Database

//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfinalproject-6874d-default-rtdb.firebaseio.com/");

    // Code using Cloud Firebase

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        // Code using for Realtime Database (Btn Login)
        /*

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tvUsername.getText().toString();
                String password = tvPassword.getText().toString();

                if (username.isEmpty())
                {
                    Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    btnLogin.setBackgroundResource(R.drawable.login_fail);
                    btnLogin.startAnimation(anim);
                    Toast.makeText(LoginActivity.this, "Please field username", Toast.LENGTH_SHORT).show();
                }

                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(username)) {
                                final String getPassword = snapshot.child(username).child("password").getValue(String.class);

                                if (getPassword.equals(password)) {
                                    Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                                    btnLogin.setBackgroundResource(R.drawable.login_success);
                                    btnLogin.startAnimation(anim);
                                    Toast.makeText(LoginActivity.this, "Login successfully!!!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                                    btnLogin.setBackgroundResource(R.drawable.login_fail);
                                    btnLogin.startAnimation(anim);
                                    Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                                btnLogin.setBackgroundResource(R.drawable.login_fail);
                                btnLogin.startAnimation(anim);
                                Toast.makeText(LoginActivity.this, "Wrong username", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi
                        }
                    });
                }


            }
        });

         */

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
                                            // Người dùng tồn tại với tên đăng nhập đã nhập.
                                            // Kiểm tra mật khẩu.
                                            DocumentSnapshot userDocument = task.getResult().getDocuments().get(0); // Lấy tài liệu đầu tiên (nếu có).

                                            String storedPassword = userDocument.getString("password");

                                            if (storedPassword != null && storedPassword.equals(password)) {
                                                // Đăng nhập thành công.
                                                AnimationForLoginSuccess();
                                                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                startActivity(intent);
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
    }
}