package gravicodev.qash.Activity;

import android.content.Intent;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import gravicodev.qash.Helper.ApiHelper;
import gravicodev.qash.R;
import gravicodev.qash.Volley.VolleyHelper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private AppCompatEditText emailLogin, passwordLogin;
    private Button btnLogin, btnSignUpNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = (AppCompatEditText) findViewById(R.id.emailLogin);
        passwordLogin = (AppCompatEditText) findViewById(R.id.passwordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUpNow = (Button) findViewById(R.id.btnSignUpNow);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

        btnSignUpNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Sign Up", Toast.LENGTH_SHORT).show();
            }
        });

        VolleyHelper vh = new VolleyHelper();
        vh.getToken();

    }

    private void loginAccount(){
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        if (!validateForm(email, password)){
            return;
        }

        startActivity(new Intent(getApplication(), MainActivity.class));
        finish();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateForm(String email, String password){
        boolean valid = true;

        if (TextUtils.isEmpty(email) | !isValidEmail(email)){
            emailLogin.setError(getString(R.string.err_msg_email));
            valid = false;
        }
        else {
            emailLogin.setError(null);
        }

        if (TextUtils.isEmpty(password)){
            passwordLogin.setError(getString(R.string.err_msg_password));
            valid = false;
        }
        else if (password.length() < 6){
            passwordLogin.setError(getString(R.string.err_msg_min_password));
            valid = false;
        }
        else {
            passwordLogin.setError(null);
        }

        return valid;
    }
}
