package com.example.pnp2_inventory_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    //create object to input for email and password
    TextInputEditText editTextEmail, editTextPassword, editTextConPassword;
    Button reg, loginreg;
    //creating object to connect to firebase authorization
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //assigning objects to specific id's
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConPassword = findViewById(R.id.conpassword);
        reg = findViewById(R.id.reg_butt);
        loginreg = findViewById(R.id.loginreg);
        progressBar = findViewById(R.id.progressBar);

        //Add OnEditorActionListener to editTextPassword
        //pressing enter key to register
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    reg.performClick();
                    return true;
                }
                return false;
            }
        });

        //Add OnEditorActionListener to editTextEmail
        //pressing enter key to register
        editTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    reg.performClick();
                    return true;
                }
                return false;
            }
        });

        //Add OnEditorActionListener to editTextConPassword
        //pressing enter key to register
        editTextConPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    reg.performClick();
                    return true;
                }
                return false;
            }
        });

        //event handler for register button
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makes progress bar visible after clicking register button
                progressBar.setVisibility(View.VISIBLE);
                String email, password, conpassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                conpassword = String.valueOf(editTextConPassword.getText());
                //checks if the fields are empty
                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(RegistrationActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegistrationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(conpassword))
                {
                    Toast.makeText(RegistrationActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (!password.equals(conpassword))
                {
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    editTextPassword.setText("");
                    editTextConPassword.setText("");
                    return;
                }

                //creates the user and inputs the data to firebase
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //makes progress bar disappear after a task is complete
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Account created",
                                            Toast.LENGTH_SHORT).show();
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(getApplicationContext(), MakeProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    // If registration in fails, display a message to the user.
                                    Toast.makeText(RegistrationActivity.this, "Registration failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        //button to go back to log in page if user already has an account
        loginreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}