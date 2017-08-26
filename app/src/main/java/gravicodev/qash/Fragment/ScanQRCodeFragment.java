package gravicodev.qash.Fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Barcode.BarcodeCaptureActivity;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QHistory;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.Models.QTransactions;
import gravicodev.qash.R;
import gravicodev.qash.Volley.VolleyHelper;

public class ScanQRCodeFragment extends Fragment {
    private static final String TAG = "ScanQRCodeFragment";
    private AppCompatEditText scanBalance, scanKet;
    private Button btnScan;
    private FrameLayout dimmer;
    private LottieAnimationView animSuccess;
    private TextView ammountAccepted;
    private Spinner spinnerTemplate;
    private List<String> templateList;
    private ArrayList<HashMap<String,String>> templateData;
    private int check = 1;
    public ScanQRCodeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scanqrcode, container, false);
        btnScan = (Button) rootView.findViewById(R.id.btnScan);
        scanBalance = (AppCompatEditText) rootView.findViewById(R.id.scan_balance);
        scanKet = (AppCompatEditText) rootView.findViewById(R.id.scan_ket);
        dimmer = (FrameLayout) rootView.findViewById(R.id.dimmerSuccess);
        ammountAccepted = (TextView) rootView.findViewById(R.id.ammountAccepted);
        animSuccess = (LottieAnimationView) rootView.findViewById(R.id.animation_success);
        spinnerTemplate = (Spinner) rootView.findViewById(R.id.spinnerTemplate);

        templateList = new ArrayList<>();
        templateData = new ArrayList<>();
        HashMap<String,String> initData = new HashMap<>();
        templateList.add("Choose Your Template");
        templateData.add(initData);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, templateList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemplate.setAdapter(dataAdapter);
        spinnerTemplate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    String item = parent.getItemAtPosition(position).toString();
                    int index = templateList.indexOf(item);
                    HashMap<String,String> data = templateData.get(index);
                    String balance = data.get("balance");
                    String desc = data.get("desc");
                    scanBalance.setText(((MainActivity)getActivity()).moneyParserString(balance));
                    scanKet.setText(desc);
                }
                else{
                    scanBalance.setText("");
                    scanKet.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = scanKet.getText().toString().trim();
                String balance = scanBalance.getText().toString().trim();

                if(!validateForm(description,balance)){
                     return;
                }

                Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
                intent.putExtra("balance",scanBalance.getText().toString().trim());
                intent.putExtra("description",scanKet.getText().toString().trim());
                startActivityForResult(intent, 1);
            }
        });

        scanBalance.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                scanBalance.removeTextChangedListener(this);
                String input = scanBalance.getText().toString();
                String inputParser = ((MainActivity)getActivity()).moneyParserString(input);
                scanBalance.setText(inputParser);
                scanBalance.setSelection(inputParser.length());
                scanBalance.addTextChangedListener(this);
            }
        });

        firebaseHandler();
        return rootView;
    }

    private void firebaseHandler() {
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ValueEventListener valueTemplateListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                templateData.clear();
                templateList.clear();
                HashMap<String,String> initData = new HashMap<>();
                templateList.add("Choose Your Template");
                templateData.add(initData);
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String balance  = ds.child("balance").getValue(String.class);
                    String desc = ds.child("desc").getValue(String.class);
                    String key = desc+" "+"(Rp "+((MainActivity)getActivity()).moneyParserString(balance)+")";
                    templateList.add(key);

                    HashMap<String,String> data = new HashMap<>();
                    data.put("desc",desc);
                    data.put("balance",balance);
                    templateData.add(data);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, templateList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTemplate.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference dbTemplateUser = FirebaseUtils.getBaseRef().child("settings")
                .child(Uid).child("templates");
        dbTemplateUser.addValueEventListener(valueTemplateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;

                    // Check barcode result
                    // If contain ".", ",", "#","[","]"
                    if(!checkChildCharacter(barcode.displayValue)){
                       showToast("QR Code not recognized !");
                    }

                    // Read barcode in firebase
                    else{
                        FirebaseUtils.getBaseRef()
                                .child("qmaster")
                                .child(barcode.displayValue)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                                            String keyValidation = "";
                                            if(dataSnapshot.getKey().contains("!")){
                                                String data[] = dataSnapshot.getKey().split("!");
                                                keyValidation = shaParser(qMaster.SourceAccountNumber+"_"+qMaster.expired_at);
                                                if(keyValidation.equalsIgnoreCase(data[1])){
                                                    // Success to Decode
                                                    preProsesToFirebase(qMaster,dataSnapshot);
                                                }
                                                else{
                                                    // Fail to Decode
                                                    showToast("Qash not recognized !");
                                                }
                                            }
                                            else{
                                                try {
                                                    keyValidation = hmacSha256(qMaster.SourceAccountNumber+"_"+qMaster.expired_at,((MainActivity)getActivity()).KEY);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                if(keyValidation.equalsIgnoreCase(dataSnapshot.getKey())){
                                                    // Success to Decode
                                                    preProsesToFirebase(qMaster,dataSnapshot);
                                                }
                                                else{
                                                    // Fail to Decode
                                                    showToast("Qash not recognized !");
                                                }
                                            }

                                        }
                                        else{
                                            showToast("Qash not recognized !");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                }
            }
            else
                Log.e("QRCODE_ERROR:", String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    private void preProsesToFirebase(QMaster qMaster, DataSnapshot dataSnapshot){
        Calendar today = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Date expireDate = new Date(qMaster.expired_at);
        cal.setTime(expireDate);
        boolean expired = cal.before(today);

        if(expired){
            showToast("Qash is no longer valid !");
        }

        else{
            if(qMaster.status.equalsIgnoreCase("enabled")){
                prosesToFirebase(qMaster.balance,dataSnapshot);
            }
            else{
                showToast("Qash is no longer active !");
            }
        }
    }

    private void prosesToFirebase(Integer balance, DataSnapshot dataSnapshot) {
        final Integer balanceUsed = Integer.parseInt(((MainActivity)getActivity()).moneyParserToInt(scanBalance.getText().toString().trim()));
        String BeneficieryAccountNumber = ((MainActivity)getActivity()).getUser().accountNumber;
        String SourceAccountNumber = dataSnapshot.child("SourceAccountNumber").getValue(String.class);
        // For Sender
        DatabaseReference dbHistoryOut = FirebaseUtils.getBaseRef().child("qhistory")
                .child(SourceAccountNumber);

        // For Receiver
        DatabaseReference dbHistoryIn = FirebaseUtils.getBaseRef().child("qhistory")
                .child(BeneficieryAccountNumber);

        DatabaseReference dbMaster = FirebaseUtils.getBaseRef().child("qmaster")
                .child(dataSnapshot.getKey());

        DatabaseReference dbTrx = FirebaseUtils.getBaseRef().child("qtransactions")
                .child(dataSnapshot.getKey());

        if(balanceUsed > balance){
            showToast("Qash Balance is not enough !");
        }
        else{

            QMaster qMaster = dataSnapshot.getValue(QMaster.class);
            qMaster.balance -= balanceUsed;

            String msg = scanKet.getText().toString().trim();

            QHistory qHistory = new QHistory(balanceUsed,qMaster.title,msg);

            QTransactions trx = new QTransactions(balanceUsed,qHistory.used_at,msg,((MainActivity)getActivity()).getUser().accountNumber, qMaster.SourceAccountNumber);


            dbMaster.setValue(qMaster);

            qHistory.setStatus("negative");

            dbHistoryOut.child(dbHistoryOut.push().getKey())
                    .setValue(qHistory);

            qHistory.setStatus("positive");
            dbHistoryIn.child(dbHistoryIn.push().getKey())
                    .setValue(qHistory);
            dbTrx.setValue(trx);

            showSuccess(balanceUsed);
            VolleyHelper vh = new VolleyHelper();
            try {
                String title = "Qash "+qMaster.title + " has been used !";
                String nominal = "Rp. "+((MainActivity)getActivity()).moneyParserString(String.valueOf(balanceUsed));
                vh.sendNotification(qMaster.SourceAccountNumber,title,nominal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            scanBalance.setText("");
            scanKet.setText("");
        }
    }

    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void showSuccess(Integer acceptedNumber){
        ammountAccepted.setText("Rp. "+ ((MainActivity)getActivity()).moneyParserString(String.valueOf(acceptedNumber)) );
        dimmer.setVisibility(View.VISIBLE);
        animSuccess.playAnimation();

        int delay = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dimmer.setVisibility(View.GONE);
            }
        }, delay * 3000);
    }

    private boolean validateForm(String description, String balance) {
        boolean valid = true;

        if (TextUtils.isEmpty(description)) {
            scanKet.setError(getString(R.string.err_msg_desc));
            valid = false;
        } else {
            scanKet.setError(null);
        }

        if (TextUtils.isEmpty(balance)) {
            scanBalance.setError(getString(R.string.err_msg_scanbalance));
            valid = false;
        } else if (Integer.parseInt(((MainActivity)getActivity()).moneyParserToInt(balance)) < 10000) {
            scanBalance.setError(getString(R.string.err_msg_min_qrbalance));
            valid = false;
        } else {
            scanBalance.setError(null);
        }

        return valid;
    }

    private boolean checkChildCharacter(String child){
        boolean valid = true;

        if(child.contains(".")){
            valid = false;
        }
        else if(child.contains(",")){
            valid = false;
        }
        else if(child.contains("#")){
            valid = false;
        }
        else if(child.contains("[")){
            valid = false;
        }
        else if(child.contains("]")){
            valid = false;
        }
        return valid;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private String shaParser(String input){
        MessageDigest digest=null;
        String hash = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(input.getBytes());

            hash = bytesToHexString(digest.digest());

        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return hash;
    }

    private static String hmacSha256(String value, String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String type = "HmacSHA256";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return bytesToHex(bytes);
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
