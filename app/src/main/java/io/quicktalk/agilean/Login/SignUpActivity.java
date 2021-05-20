package io.quicktalk.agilean.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import io.quicktalk.agilean.Model.User;
import io.quicktalk.agilean.R;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etFirstName, etPassword, etLastName, etConfirmPassword;
    private String emailstring, firstNamestring, lastnamestring, password, confirmPassword;
    private Spinner Businesstype;
    private ImageView ivProfile;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
Context context;
    private StorageReference fileStorage;
    private Uri localFileUri, serverFileUri;
    private View progressBar;

    //OTP


    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;


    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etEmail = findViewById(R.id.emailText);
       // emailstring = etEmail.getText().toString().trim();
        etPassword = findViewById(R.id.passwordText);
       // password = etPassword.getText().toString().trim();
        etFirstName = findViewById(R.id.firstname);
       // firstNamestring = etFirstName.getText().toString().trim();
        etLastName = findViewById(R.id.lastname);
       // lastnamestring = etLastName.getText().toString().trim();
        etConfirmPassword = findViewById(R.id.confirmpasswordText);
       // confirmPassword = etConfirmPassword.getText().toString().trim();


    }


    private boolean validateForm(String emailstring, String firstnamestring, String LastNameNamestring, String Password, String confirmPassword) {
        if (TextUtils.isEmpty(emailstring)) {
            etEmail.setError(getString(R.string.required));
            //mEmailField.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(firstnamestring)) {
            etFirstName.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(lastnamestring)) {
            etLastName.setError(getString(R.string.required));

            return false;
        } else {
            etEmail.setError(null);
            etFirstName.setError(null);
            etLastName.setError(null);

            return true;
        }
    }

    public void btnSignupClick(View v) {

        etEmail = findViewById(R.id.emailText);
        emailstring = etEmail.getText().toString().trim();
        etPassword = findViewById(R.id.passwordText);
        password = etPassword.getText().toString().trim();
        etFirstName = findViewById(R.id.firstname);
        firstNamestring = etFirstName.getText().toString().trim();
        etLastName = findViewById(R.id.lastname);
        lastnamestring = etLastName.getText().toString().trim();
        etConfirmPassword = findViewById(R.id.confirmpasswordText);
        confirmPassword = etConfirmPassword.getText().toString().trim();
       // name = etName.getText().toString().trim();


        if (emailstring.equals("")) {
            etEmail.setError(getString(R.string.enter_email));
        } else if (firstNamestring.equals("")) {
            etFirstName.setError(getString(R.string.enter_firstname));

        }else if (lastnamestring.equals("")) {
           etLastName.setError(getString(R.string.enter_Lastname));
        } else if (etPassword.equals("")) {
            etPassword.setError(getString(R.string.enter_password));
        } else if (confirmPassword.equals("")) {
            etConfirmPassword.setError(getString(R.string.enter_password));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailstring).matches()) {
            etEmail.setError(getString(R.string.enter_correct_email));
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(getString(R.string.password_mismatch));
        } else {

         //   progressBar.setVisibility(View.VISIBLE);
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(emailstring, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @SuppressLint("StringFormatInvalid")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                String userID = firebaseUser.getUid();
                                SharedPreferences preferences =
                                        PreferenceManager.getDefaultSharedPreferences(context);
                                // The SharedPreferences editor - must use commit() to submit changes
                                SharedPreferences.Editor editor = preferences.edit();

                                // Edit the saved preferences
                                editor.putString("getUid",userID);

                                editor.commit();



                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                User user = new User(emailstring,
                                        firstNamestring,lastnamestring ,firebaseUser.getUid()
                                      );
                               // progressBar.setVisibility(View.VISIBLE);
                                databaseReference.child("Users").child(userID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                       // progressBar.setVisibility(View.GONE);
                                        Toast.makeText(SignUpActivity.this, R.string.user_created_successfully, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    }
                                });




                            } else {
                                Toast.makeText(SignUpActivity.this,
                                        getString(R.string.signup_failed, task.getException()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void SignIn(View view) {

    }
}