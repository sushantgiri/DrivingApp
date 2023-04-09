package com.driving_app.screens;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;
import com.driving_app.utils.MessageUtils;
import com.driving_app.utils.ValidatorUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;
    private TextInputEditText emailEt;
    private Button resetButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.resetEmailAddress);
        resetButton = findViewById(R.id.resetPasswordButton);
        resetButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Resetting Password....");
        progressDialog.setCancelable(false);



        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == backIcon){
            finish();
        }else if(view == resetButton){
            resetFields();
        }
    }

    private void resetFields(){
        String email = emailEt.getText().toString();
        if(TextUtils.isEmpty(email)){
            MessageUtils.showMessage(this, "Email cannot be left empty.");
            return;
        }

        if(ValidatorUtils.isValidEmail(email)){
            sendPasswordResetLink(email);
        }else{
            MessageUtils.showMessage(this, "Please enter valid valid email address");
        }
    }

    private void sendPasswordResetLink(String email){
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressDialog.cancel();
                    if(task.isSuccessful()){
                        MessageUtils.showMessage(PasswordResetActivity.this, "We have sent a password recovery link in your email.");
                    }else{
                        MessageUtils.showMessage(PasswordResetActivity.this, task.getException().getMessage());

                    }
                });
    }
}
