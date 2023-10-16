package edu.uef.doan;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    TextView tvLogin;

    EditText etUsername, etPassword, etConfirmPassword;

    Button btnSignup;

    private SignUpDatabaseHelper dbHelper;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfinalproject-6874d-default-rtdb.firebaseio.com/");

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

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirmpassword = etConfirmPassword.getText().toString();

                if (confirmpassword.isEmpty() || password.isEmpty() || username.isEmpty())
                {
                    Animation anim = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.shake);
                    btnSignup.setBackgroundResource(R.drawable.login_fail);
                    btnSignup.startAnimation(anim);
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(username)) {
                                Animation anim = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.shake);
                                btnSignup.setBackgroundResource(R.drawable.login_fail);
                                btnSignup.startAnimation(anim);
                                Toast.makeText(SignupActivity.this, "User is already registered", Toast.LENGTH_SHORT).show();
                            }

                            else if (!confirmpassword.equals(password))
                            {
                                Animation anim = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.shake);
                                btnSignup.setBackgroundResource(R.drawable.login_fail);
                                btnSignup.startAnimation(anim);
                                Toast.makeText(SignupActivity.this, "Passwords do not match together", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Animation anim = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.shake);
                                btnSignup.setBackgroundResource(R.drawable.login_success);
                                btnSignup.startAnimation(anim);
                                databaseReference.child("users").child(username).child("password").setValue(password);

                                Toast.makeText(SignupActivity.this, "Sign up successfully!!!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void addControl() {
        tvLogin = findViewById(R.id.tvLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        dbHelper = new SignUpDatabaseHelper(this);
    }
}