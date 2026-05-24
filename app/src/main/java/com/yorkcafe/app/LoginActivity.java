package com.yorkcafe.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private MaterialButton btnLoginEnter, btnGoogleSignIn;
    private TextView txtGoToSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnLoginEnter = findViewById(R.id.btn_login_enter);
        btnGoogleSignIn = findViewById(R.id.btn_google_signin);
        txtGoToSignup = findViewById(R.id.txt_go_to_signup);

        // Go to Sign Up Screen
        txtGoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Mock Normal Login
        btnLoginEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "PLEASE ENTER YOUR EMAIL!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "PLEASE ENTER YOUR PASSWORD!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check registered account in SharedPreferences
                android.content.SharedPreferences prefs = getSharedPreferences("YorkCafePrefs", MODE_PRIVATE);
                String regEmail = prefs.getString("registered_email", "");
                String regPassword = prefs.getString("registered_password", "");
                String regName = prefs.getString("registered_name", "");

                if (regEmail.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "NO ACCOUNT FOUND! PLEASE SIGN UP FIRST.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!email.equalsIgnoreCase(regEmail) || !password.equals(regPassword)) {
                    Toast.makeText(LoginActivity.this, "INVALID EMAIL OR PASSWORD!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save active login session name
                prefs.edit().putString("active_user_name", regName).apply();

                Toast.makeText(LoginActivity.this, "WELCOME BACK, " + regName.toUpperCase() + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Mock Google Login
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("ENTER YOUR NAME");

                final EditText inputName = new EditText(LoginActivity.this);
                inputName.setHint("Name");
                inputName.setSingleLine(true);
                inputName.setPadding(40, 40, 40, 40);
                inputName.setTextColor(getResources().getColor(android.R.color.black));
                inputName.setHintTextColor(getResources().getColor(android.R.color.darker_gray));

                // Container layout for margins
                android.widget.FrameLayout container = new android.widget.FrameLayout(LoginActivity.this);
                android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.leftMargin = 48;
                params.rightMargin = 48;
                params.topMargin = 24;
                params.bottomMargin = 24;
                inputName.setLayoutParams(params);
                container.addView(inputName);
                builder.setView(container);

                builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getText().toString().trim();
                        if (name.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "NAME CANNOT BE EMPTY!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        android.content.SharedPreferences prefs = getSharedPreferences("YorkCafePrefs", MODE_PRIVATE);
                        prefs.edit().putString("active_user_name", name).apply();

                        Toast.makeText(LoginActivity.this, "SIGNED IN WITH GOOGLE!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                // Style dialog buttons to black
                try {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.black));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.black));
                } catch (Exception e) {
                    // ignore
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
