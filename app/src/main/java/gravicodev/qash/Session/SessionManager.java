package gravicodev.qash.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.common.StringUtils;

import java.io.IOException;

import gravicodev.qash.Activity.LoginActivity;
import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.User;
import gravicodev.qash.R;

/**
 * Created by supermonster on 7/28/2017.
 */

public class SessionManager {

    private final String PREF_NAME;
    private final String PREF_USER_ID;
    private final String PREF_USER_EMAIL;
    private final String PREF_USER_FULLNAME;
    private final String PREF_ACCOUNT_NUMBER;
    private final String PREF_ACCOUNT_BALANCE;
    private final String PREF_DEVICE_ID;

    private final String IS_LOGIN;

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context){
        this.context = context;

        PREF_NAME = context.getString(R.string.app_name);

        PREF_USER_ID = context.getString(R.string.id);
        PREF_USER_EMAIL = context.getString(R.string.email);
        PREF_USER_FULLNAME = context.getString(R.string.fullname);
        PREF_ACCOUNT_NUMBER = context.getString(R.string.accnum);
        PREF_ACCOUNT_BALANCE = context.getString(R.string.balanceacc);

        PREF_DEVICE_ID = context.getString(R.string.deviceid);
        IS_LOGIN = "isLoggedIn";


        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void logIn(User user){
        editor.putString(PREF_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
        editor.putString(PREF_USER_EMAIL, user.email);
        editor.putString(PREF_USER_FULLNAME, user.fullname);
        editor.putString(PREF_ACCOUNT_NUMBER, user.accountNumber);
        editor.putInt(PREF_ACCOUNT_BALANCE, user.getBalance());

        editor.putBoolean(IS_LOGIN,true);
        editor.commit();
    }

    public User getUser(){
        String fullname = preferences.getString(PREF_USER_FULLNAME,null);
        String email = preferences.getString(PREF_USER_EMAIL,null);
        String accountNum = preferences.getString(PREF_ACCOUNT_NUMBER,null);
        String userid = preferences.getString(PREF_USER_ID,null);
        int balanceacc = preferences.getInt(PREF_ACCOUNT_BALANCE, 123);

        User user = new User(fullname,accountNum,email);
        user.setBalance(balanceacc);
        user.setUserid(userid);

        return user;
    }

    public void logOut(){
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            FirebaseUtils.getBaseRef().child("userDevice")
                    .child(preferences.getString(PREF_ACCOUNT_NUMBER,null))
                    .child(token).removeValue();
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((MainActivity)context).finish();

    }

    public void renew(User user){
        editor.putString(PREF_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
        editor.putString(PREF_USER_EMAIL, user.email);
        editor.putString(PREF_USER_FULLNAME, user.fullname);
        editor.putString(PREF_ACCOUNT_NUMBER, user.accountNumber);
        editor.putInt(PREF_ACCOUNT_BALANCE, user.getBalance());

        editor.commit();
    }

    public void checkLogin(){

        if(!this.isLoggedIn()){
            this.logOut();
        }
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN,false);
    }

    public void setDeviceId(String id){
        editor.putString(PREF_DEVICE_ID,id);
        editor.commit();
    }

    public String getDeviceId(){
        return preferences.getString(PREF_DEVICE_ID,null);
    }

}
