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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView tvSignUp, tvUsername, tvPassword;
    Button btnLogin;
    private SignUpDatabaseHelper dbHelper;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfinalproject-6874d-default-rtdb.firebaseio.com/");

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
    }

    private void addControl() {
        tvSignUp = findViewById(R.id.tvSignUp);
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        btnLogin = findViewById(R.id.btnLogin);
        dbHelper = new SignUpDatabaseHelper(this);
    }
}