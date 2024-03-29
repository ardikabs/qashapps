package gravicodev.qash.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.R;

/**
 * Created by supermonster on 8/28/2017.
 */

public class UpdateTemplateActivity extends BaseActivity {
    private AppCompatEditText balanceTemplate, descTemplate;
    private Button btnUpdateTemplate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_template);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_update_template);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        balanceTemplate = (AppCompatEditText) findViewById(R.id.update_balance);
        descTemplate = (AppCompatEditText) findViewById(R.id.update_ket);
        btnUpdateTemplate = (Button) findViewById(R.id.btnUpdateTemplate);

        Intent intent = getIntent();
        final String balance = intent.getStringExtra("balance");
        final String desc = intent.getStringExtra("desc");
        final String key = intent.getStringExtra("key");

        balanceTemplate.setText(moneyParserString(balance));
        descTemplate.setText(desc);

        btnUpdateTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateBalance = balanceTemplate.getText().toString().trim();
                String updateDesc = descTemplate.getText().toString().trim();

                if(!validateForm(updateDesc,updateBalance)){
                    return;
                }

                HashMap<String, Object> data = new HashMap<>();
                data.put("desc",updateDesc);
                data.put("balance",moneyParserToInt(updateBalance));
                data.put("created_at", System.currentTimeMillis());

                FirebaseUtils.getBaseRef()
                        .child("settings")
                        .child(getUid())
                        .child("templates")
                        .child(key)
                        .setValue(data);
                showToast("Template successfully updated !");
                finish();
            }
        });

        balanceTemplate.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                balanceTemplate.removeTextChangedListener(this);
                String input = balanceTemplate.getText().toString();
                String inputParser = moneyParserString(input);
                balanceTemplate.setText(inputParser);
                balanceTemplate.setSelection(inputParser.length());
                balanceTemplate.addTextChangedListener(this);
            }
        });

    }

    private boolean validateForm(String description, String balance) {
        boolean valid = true;

        if (TextUtils.isEmpty(description)) {
            descTemplate.setError(getString(R.string.err_msg_desc));
            valid = false;
        } else {
            descTemplate.setError(null);
        }

        if (TextUtils.isEmpty(balance)) {
            balanceTemplate.setError(getString(R.string.err_msg_scanbalance));
            valid = false;
        }else if (Integer.parseInt(moneyParserToInt(balance)) < 10000) {
            balanceTemplate.setError(getString(R.string.err_msg_min_qrbalance));
            valid = false;
        } else {
            balanceTemplate.setError(null);
        }

        return valid;
    }


    public String moneyParserToInt(String data){
        ArrayList<String> input = new ArrayList<>();
        for(int i = data.length()-1;i>=0;i--){
            if(!".".equals(String.valueOf(data.charAt(i)))){
                input.add(String.valueOf(data.charAt(i)));
            }
        }

        int hasil = 0;
        int pengali = 1;

        for(int i = 0; i < input.size(); i++){
            hasil+= Integer.parseInt(input.get(i))*pengali;
            pengali *=10;
        }

        return String.valueOf(hasil);
    }

    public String moneyParserString(String data){
        ArrayList<String> input = new ArrayList<>();
        for(int i = data.length()-1;i>=0;i--){
            if(!".".equals(String.valueOf(data.charAt(i)))){
                input.add(String.valueOf(data.charAt(i)));
            }
        }

        String strHasil = "";
        int x = 1;
        for(int i=0; i < input.size();i++){
            if(x==3 && i != (input.size()-1)){
                strHasil = "." + input.get(i) + strHasil;
                x = 0;
            }else{
                strHasil = input.get(i) + strHasil;
            }
            x++;
        }

        return strHasil;
    }
}