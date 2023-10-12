package edu.uef.doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    TextView tvLogin;

    EditText etUsername, etPassword, etConfirmPassword;

    Button btnSignup;

    private SignUpDatabaseHelper dbHelper;

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

                if (!password.equals(confirmpassword))
                {
                    Toast.makeText(SignupActivity.this, "Passwords do not match together", Toast.LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(SignupActivity.this, "Sign up successfully!!!", Toast.LENGTH_LONG).show();
                    User user = new User(username, password);
                    dbHelper.addUser(user);
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