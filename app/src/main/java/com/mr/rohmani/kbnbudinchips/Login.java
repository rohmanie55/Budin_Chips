package com.mr.rohmani.kbnbudinchips;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mr.rohmani.kbnbudinchips.Models.User;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;


    private EditText email_text;
    private EditText password_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        //Get database Refrence
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email_text = (EditText) findViewById(R.id.email);
        password_text = (EditText) findViewById(R.id.password);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        Button btn_masuk = (Button) findViewById(R.id.btn_masuk);

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    //vaidasi form login
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email_text.getText().toString())) {
            email_text.setError("Required");
            result = false;
        } else {
            email_text.setError(null);
        }

        if (TextUtils.isEmpty(password_text.getText().toString())) {
            password_text.setError("Required");
            result = false;
        } else {
            password_text.setError(null);
        }

        return result;
    }

    //nntuk mengambil username dari email
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    //fngsi yang menjalankan login
    private void signIn() {
        String email = email_text.getText().toString();
        String password = password_text.getText().toString();

        if (!validateForm()) {
            return;
        }

        //menampilkn progres dialog
        showProgressDialog();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Login java", "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(Login.this, "Login In Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //on oath succes write user into firebase param:firebase user id
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        String email = user.getEmail();
        //deklarasi user model yang menampung sema variable
        User userModel = new User(username, email);

        mDatabase.child("users").child(user.getUid()).setValue(userModel);

        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
