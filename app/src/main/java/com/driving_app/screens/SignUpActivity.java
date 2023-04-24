package com.driving_app.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;
import com.driving_app.utils.MessageUtils;
import com.driving_app.utils.ValidatorUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth firebaseAuth;
    private static final String TAG = "SignUpActivity";
    private TextInputEditText userNameText;
    private TextInputEditText passwordText;
    private Button signUpButton;
    private GoogleApiClient googleApiClient;

    private ProgressDialog progressDialog;
    private SignInButton googleSignInButton;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener authStateListener;
    String idToken;
    String name, email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Signing up....");

        googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(this);

        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))//you can also use R.string.default_web_client_id
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        //this is where we start the Auth state Listener to listen for whether the user is signed in or not
        authStateListener = firebaseAuth -> {
            // Get signedIn user
            FirebaseUser user = firebaseAuth.getCurrentUser();

            //if user is signed in, we call a helper method to save the user details to Firebase
            if (user != null) {
                // User is signed in
                // you could place other firebase code
                //logic to save the user details to Firebase
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode  == RESULT_OK){
            if(requestCode==RC_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }

        }



        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            idToken = account.getIdToken();
            name = account.getDisplayName();
            email = account.getEmail();
            // you can store user data to SharedPreference
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential,email);
        }else{
            // Google Sign In failed, update UI appropriately
            Log.e(TAG, "Login Unsuccessful. "+result);
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential, String email){

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                    if(task.isSuccessful()){
                        MessageUtils.showMessage(SignUpActivity.this, "Login successful");
                        if(email.equals("kpsir1217@gmail.com")){
                            Intent mainIntent=new Intent(this, DrivingActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }else{
                            startMain();
                        }
                    }else{
                        MessageUtils.showMessage(SignUpActivity.this, task.getException().getMessage());
                    }

                });
    }

    private void startMain(){
        setResult(RESULT_OK);
        finish();

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
        }else if(view == googleSignInButton){
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,RC_SIGN_IN);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        MessageUtils.showMessage(this, "Connection Failed");
    }
}
