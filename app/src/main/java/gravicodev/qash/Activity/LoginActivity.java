package gravicodev.qash.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import gravicodev.qash.Helper.ApiHelper;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.User;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;
import gravicodev.qash.Volley.VolleyHelper;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private AppCompatEditText emailLogin, pinLogin;
    private Button btnLogin, btnSignUpNow;

    private SessionManager sessionManager;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = (AppCompatEditText) findViewById(R.id.emailLogin);
        pinLogin = (AppCompatEditText) findViewById(R.id.pinLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUpNow = (Button) findViewById(R.id.btnSignUpNow);

        sessionManager = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });


        btnSignUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Register your account to internet banking first.");
            }
        });

        VolleyHelper vh = new VolleyHelper();
        vh.getToken();

    }

    private void loginAccount(){
        String email = emailLogin.getText().toString().trim();
        final String pin = pinLogin.getText().toString().trim();

        if (!validateForm(email, pin)){
            return;
        }
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email,pin).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            // Unsuccessfull login
                            hideProgressDialog();
                            showToast(task.getException().getMessage());
                            emailLogin.setText("");
                            pinLogin.setText("");
                        }
                        else{
                            // Successfull login
                            onAuthSuccess(task.getResult().getUser());
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        FirebaseUtils.getBaseRef().child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setBalance(dataSnapshot.child("balance").getValue(Integer.class));
                user.setUserid(dataSnapshot.getKey());
                sessionManager.logIn(user);
                startActivity(new Intent (LoginActivity.this,MainActivity.class));
                finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateForm(String email, String pin){
        boolean valid = true;

        if (TextUtils.isEmpty(email) | !isValidEmail(email)){
            emailLogin.setError(getString(R.string.err_msg_email));
            valid = false;
        }
        else {
            emailLogin.setError(null);
        }

        if (TextUtils.isEmpty(pin)){
            pinLogin.setError(getString(R.string.err_msg_pin));
            valid = false;
        }
        else if (pin.length() < 6){
            pinLogin.setError(getString(R.string.err_msg_min_pin));
            valid = false;
        }
        else {
            pinLogin.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
