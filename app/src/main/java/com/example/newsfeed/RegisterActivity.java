package com.example.newsfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private TextView allreadyHaveAccount;
    private Button signUp,logIn,register;
    private TextInputEditText nameEdt,emailEdt,contactEdt,passwordEdt;
    private CheckBox mCheckBox;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        initViews();

        signUp.setEnabled(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox.isChecked()){
                    performAuthentication();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please agree term and conditions..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        allreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }

    private void performAuthentication() {
        String name = nameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String contact = contactEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        if (!isValidEmail(email)){
            emailEdt.setError("Enter correct email..");
            emailEdt.requestFocus();
        }
        else if (name.isEmpty()){
            nameEdt.setError("Required Field");
        }
        else if (name.length() < 3){
            nameEdt.setError("Should have atleast 3 characters..");
        }
        else if (password.isEmpty() || password.length() < 6){
            passwordEdt.setError("Length of password should not be less than 6 characters");
            return;
        }
        else if (!isValidMobileNumber(contact)){
            contactEdt.setError("Enter valid mobile number..");
        }
        else{
            progressDialog.setMessage("Please wait while Registration..");
            progressDialog.setTitle("Registration..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration successful...", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Email Already exists...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void initViews() {
        mCheckBox = findViewById(R.id.checkbox);
        nameEdt = findViewById(R.id.idName);
        emailEdt = findViewById(R.id.idEmail);
        contactEdt = findViewById(R.id.idContactNo);
        passwordEdt = findViewById(R.id.idPassword);
        allreadyHaveAccount = findViewById(R.id.idAlreadyHaveAccount);
        signUp = findViewById(R.id.idSignUpBtn);
        logIn = findViewById(R.id.idLogInBtn);
        register = findViewById(R.id.idRegisterButton);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isValidMobileNumber(String contact) {
        return Patterns.PHONE.matcher(contact).matches() && !TextUtils.isEmpty(contact) && contact.length() == 10;
    }
}