package edu.uef.doan;

import static edu.uef.doan.LoginActivity.user;
import static edu.uef.doan.LoginActivity.userDocument;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserActivity extends AppCompatActivity {
    private EditText fullName, phoneNumber, email;
    Button apply_btn;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        fullName = findViewById(R.id.editText_FullName);
        phoneNumber = findViewById(R.id.editText_Phone);
        email = findViewById(R.id.editText_Email);
        apply_btn = findViewById(R.id.done_btn);
        db = FirebaseFirestore.getInstance();

        if(user.getFullname() != null) fullName.setHint(user.getFullname());
        if(user.getEmail() != null) email.setHint(user.getEmail());
        if(user.getPhone() != null) phoneNumber.setHint(user.getPhone());

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullName.getText().toString();
                String phone = phoneNumber.getText().toString();
                String mail = email.getText().toString();

                if (name != "") user.setFullname(name);
                if (phone != "") user.setPhone(phone);
                if (mail != "") user.setEmail(mail);
                db.collection("users").document(userDocument.getId()).set(user);
            }
        });
    }
}