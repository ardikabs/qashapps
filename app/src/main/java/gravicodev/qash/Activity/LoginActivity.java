package gravicodev.qash.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Helper.VolleyCallback;
import gravicodev.qash.Models.User;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;
import gravicodev.qash.Volley.VolleyHelper;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private AppCompatEditText useridLogin, pinLogin;
    private Button btnLogin, btnSignUpNow;

    private SessionManager sessionManager;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        useridLogin = (AppCompatEditText) findViewById(R.id.useridLogin);
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

    }

    private void loginAccount(){
        String userid = useridLogin.getText().toString().trim();
        String pin = pinLogin.getText().toString().trim();

        if (!validateForm(userid, pin)){
            return;
        }
        showProgressDialog();
        VolleyHelper vh = new VolleyHelper();
        try {
            vh.login(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG,result);
                    if(!result.equalsIgnoreCase("gagal")){
                        mAuth.signInWithCustomToken(result)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful()){
                                            //Successfull Login
                                            onAuthSuccess(task.getResult().getUser());
                                        }
                                        else{
                                            //Unsucessfull Login
                                            hideProgressDialog();
                                            showToast(task.getException().getMessage());
                                            useridLogin.setText("");
                                            pinLogin.setText("");
                                        }
                                    }
                                });
                    }
                    else{
                        hideProgressDialog();
                        showToast("Login Invalid");
                        useridLogin.setText("");
                        pinLogin.setText("");
                    }
                }
            },userid, pin);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onAuthSuccess(FirebaseUser user) {
        FirebaseUtils.getBaseRef().child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                user.setBalance(dataSnapshot.child("balance").getValue(Integer.class));
                user.setUserid(dataSnapshot.getKey());
                String fullname = user.fullname;
                String accountNumber = user.accountNumber;
                VolleyHelper vh = new VolleyHelper();
                try {
                    vh.validateName(new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if(result.equalsIgnoreCase("benar")){
                                hideProgressDialog();
                                sessionManager.logIn(user);
                                startActivity(new Intent (LoginActivity.this,MainActivity.class));
                                finish();
                            }
                            else{
                                hideProgressDialog();
                                showToast("Login Invalid");
                            }

                        }
                    },accountNumber,fullname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateForm(String userid, String pin){
        boolean valid = true;

        if (TextUtils.isEmpty(userid) || isValidEmail(userid)){
            useridLogin.setError(getString(R.string.err_msg_userid));
            valid = false;
        }
        else {
            useridLogin.setError(null);
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
