package com.yorkcafe.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputName, inputEmail, inputPassword;
    private MaterialButton btnSignupEnter;
    private TextView txtGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnSignupEnter = findViewById(R.id.btn_signup_enter);
        txtGoToLogin = findViewById(R.id.txt_go_to_login);

        // Go back to login screen
        txtGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Sign Up Button Click
        btnSignupEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "PLEASE ENTER YOUR FULL NAME!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "PLEASE ENTER YOUR EMAIL!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "PLEASE ENTER YOUR PASSWORD!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save registered credentials to SharedPreferences
                android.content.SharedPreferences prefs = getSharedPreferences("YorkCafePrefs", MODE_PRIVATE);
                prefs.edit()
                    .putString("registered_name", name)
                    .putString("registered_email", email)
                    .putString("registered_password", password)
                    .apply();

                // Success mock registration
                Toast.makeText(SignUpActivity.this, "ACCOUNT CREATED! PLEASE LOG IN.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
