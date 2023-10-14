package edu.uef.doan;

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

public class LoginActivity extends AppCompatActivity {

    TextView tvSignUp, tvUsername, tvPassword;
    Button btnLogin;
    private SignUpDatabaseHelper dbHelper;

    User user = new User();

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
            public void onClick(View view) {
                String username = tvUsername.getText().toString();
                String password = tvPassword.getText().toString();

                if (dbHelper.getUserByUsername(username) == null)
                {
                    Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                    btnLogin.setBackgroundResource(R.drawable.login_fail);
                    btnLogin.startAnimation(anim);
                    Toast.makeText(LoginActivity.this, "Wrong username", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (dbHelper.getUserByPassword(username, password) == null)
                    {
                        Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                        btnLogin.setBackgroundResource(R.drawable.login_fail);
                        btnLogin.startAnimation(anim);
                        Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Animation anim = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake);
                        btnLogin.setBackgroundResource(R.drawable.login_success);
                        btnLogin.startAnimation(anim);
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
                    }
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