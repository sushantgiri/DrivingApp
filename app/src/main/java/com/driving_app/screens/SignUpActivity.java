package com.driving_app.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;
import com.driving_app.utils.MessageUtils;
import com.driving_app.utils.ValidatorUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "SignUpActivity";
    private TextInputEditText userNameText;
    private TextInputEditText passwordText;
    private Button signUpButton;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Signing up....");

        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.cancel();
                    if (task.isSuccessful()) {

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        MessageUtils.showMessage(SignUpActivity.this, task.getException().getMessage());

                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user){
        System.out.println("User is"+ user);
        setResult(RESULT_OK);
        finish();
    }

    private void reload(){

    }

    @Override
    public void onClick(View view) {
        if(view == signUpButton){
            String email = userNameText.getText().toString();
            String password  = passwordText.getText().toString();


            if(validateFields(email, password)){
                createAccount(email,password);
            }
        }
    }

    private boolean validateFields(String username, String password){

        if(TextUtils.isEmpty(username)){
            MessageUtils.showMessage(this, "Email cannot be left empty.");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            MessageUtils.showMessage(this, "Password cannot be left empty.");
            return false;
        }

        if(!ValidatorUtils.isValidEmail(username)){
            MessageUtils.showMessage(this, "Please enter valid email");
            return false;
        }
        return true;

    }
}
