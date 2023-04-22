package com.driving_app.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.driving_app.R;
import com.driving_app.utils.MessageUtils;
import com.driving_app.utils.ValidatorUtils;
import com.facebook.login.widget.LoginButton;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private TextInputEditText userNameText;
    private TextInputEditText passwordText;
    private Button loginButton;
    private TextView forgotPasswordText;

    private TextView signUpText;
    private static final int REQUEST_SIGN_UP_CODE = 9001;
    private static final String TAG = "Login";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    String name, email;
    String idToken;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener authStateListener;
    private SignInButton googleSignInButton;
    private LoginButton facebookSignInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(this);

//        facebookSignInButton = findViewById(R.id.facebookButton);



        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Signing in....");


        userNameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);

        signUpText = findViewById(R.id.signUpText);
        signUpText.setOnClickListener(this);

        forgotPasswordText = findViewById(R.id.forgotPassword);
        forgotPasswordText.setOnClickListener(this);

        loginButton= findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

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

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.cancel();
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user, password);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        MessageUtils.showMessage(LoginActivity.this, task.getException().getMessage());

                    }
                });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user,String password) {
        if(user.getEmail().equals("kpsir1217@gmail.com")){
            Intent mainIntent=new Intent(this, DrivingActivity.class);
            startActivity(mainIntent);
        }else{
            Intent mainIntent=new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }
        finish();
    }


    @Override
    public void onClick(View view) {
        if(view == loginButton){
            String email = userNameText.getText().toString();
            String password  = passwordText.getText().toString();

            if(validateFields(email, password)){
                signIn(email,password);
            }

        }else if(view == forgotPasswordText){
            Intent forgotPasswordIntent  = new Intent(this, PasswordResetActivity.class);
            startActivity(forgotPasswordIntent);
        }else if(view == signUpText){
            Intent signUpIntent = new Intent(this, SignUpActivity.class);
            startActivityForResult(signUpIntent,REQUEST_SIGN_UP_CODE);

        }else if(view == googleSignInButton){
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,RC_SIGN_IN);
        }else if(view == facebookSignInButton){

        }
    }



    private void startMain(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode  == RESULT_OK){
            if(requestCode == REQUEST_SIGN_UP_CODE){
                startMain();

            }

        }

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    private void startActivity(int flag){
        Intent decidingIntent;

        if(flag == 0){
            decidingIntent = new Intent(this, DrivingActivity.class);
        }else {
            decidingIntent = new Intent(this, InstructorActivity.class);
        }
       startActivity(decidingIntent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        MessageUtils.showMessage(this, "Connection Failed");
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
                        MessageUtils.showMessage(LoginActivity.this, "Login successful");
                        if(email.equals("kpsir1217@gmail.com")){
                            Intent mainIntent=new Intent(this, DrivingActivity.class);
                            startActivity(mainIntent);
                        }else{
                            startMain();
                        }
                    }else{
                        MessageUtils.showMessage(LoginActivity.this, task.getException().getMessage());
                    }

                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authStateListener != null){
            FirebaseAuth.getInstance().signOut();
        }
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
