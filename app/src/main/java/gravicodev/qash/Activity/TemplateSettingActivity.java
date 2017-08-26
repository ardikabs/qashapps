package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import gravicodev.qash.Adapter.ListTemplateAdapter;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;

public class TemplateSettingActivity extends BaseActivity {
    private static final String TAG = "TemplateSettingActivity";
    private ListView listView;
    private AppCompatEditText balanceTemplate, descTemplate;
    private Button btnCreateTemplate;
    private ListTemplateAdapter listTemplateAdapter;

    private SessionManager sessionManager;

    private ArrayList<String> mTemplateKeyList;
    private DatabaseReference db;
    private ChildEventListener childListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_template);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);
        balanceTemplate = (AppCompatEditText) findViewById(R.id.template_balance);
        descTemplate = (AppCompatEditText) findViewById(R.id.template_ket);
        btnCreateTemplate = (Button) findViewById(R.id.btnCreateTemplate);
        listView = (ListView) findViewById(R.id.listTemplate);

        List<HashMap<String,Object>> emptyList = new ArrayList<>();
        listTemplateAdapter = new ListTemplateAdapter(this, emptyList);
        listView.setAdapter(listTemplateAdapter);

        mTemplateKeyList = new ArrayList<>();

        btnCreateTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = descTemplate.getText().toString().trim();
                final String balance = balanceTemplate.getText().toString().trim();
                final String key = desc.replaceAll("\\s+","")+"_"+moneyParserToInt(balance);
                if(!validateForm(desc,balance)){
                    return;
                }

                FirebaseUtils.getBaseRef().child("settings").child(getUid()).child("templates")
                        .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            showToast("Template already available");
                        }
                        else{
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("desc",desc);
                            data.put("balance",moneyParserToInt(balance));
                            data.put("created_at", System.currentTimeMillis());
                            FirebaseUtils.getBaseRef()
                                    .child("settings")
                                    .child(getUid())
                                    .child("templates")
                                    .child(key)
                                    .setValue(data);
                            descTemplate.setText("");
                            balanceTemplate.setText("");
                            showToast("Template successfully added !");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

        firebaseHandler();
    }

    private void firebaseHandler() {
        db = FirebaseUtils.getBaseRef().child("settings").child(getUid()).child("templates");
        childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                HashMap<String,Object> data = new HashMap<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    data.put(ds.getKey(),ds.getValue());
                }
                mTemplateKeyList.add(key);
                listTemplateAdapter.refill(data);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                HashMap<String,Object> data = new HashMap<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    data.put(ds.getKey(),ds.getValue());
                }
                if(mTemplateKeyList.contains(key)){
                    listTemplateAdapter.change(key,data);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = mTemplateKeyList.indexOf(key);
                mTemplateKeyList.remove(index);
                listTemplateAdapter.remove(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        db.addChildEventListener(childListener);
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

    @Override
    protected void onStop() {
        super.onStop();
        db.removeEventListener(childListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
