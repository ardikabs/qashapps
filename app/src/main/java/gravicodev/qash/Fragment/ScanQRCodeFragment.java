package gravicodev.qash.Fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import gravicodev.qash.Activity.SliderActivity;
import gravicodev.qash.Barcode.BarcodeCaptureActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QHistory;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.Models.QTransactions;
import gravicodev.qash.Models.User;
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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
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
                       showToast("QR Code tidak dikenali");
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

                                            Calendar today = Calendar.getInstance();
                                            Calendar cal = Calendar.getInstance();
                                            Date expireDate = new Date(qMaster.expired_at);
                                            cal.setTime(expireDate);
                                            boolean expired = cal.before(today);

                                            if(expired){
                                                showToast("Qash sudah tidak berlaku");
                                            }

                                            else{
                                                if(qMaster.status.equalsIgnoreCase("enabled")){
                                                    proses(qMaster.balance,dataSnapshot);
                                                }
                                                else{
                                                    showToast("Qash sudah tidak berlaku");
                                                }
                                            }
                                        }
                                        else{
                                            showToast("QASH tidak dikenali");
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

    private void proses(Integer balance, DataSnapshot dataSnapshot) {

        final Integer balanceUsed = Integer.parseInt(((MainActivity)getActivity()).moneyParserToInt(scanBalance.getText().toString().trim()));

        if(balanceUsed > balance){
            showToast("Balance Qash tidak mencukupi");
        }
        else{
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



            QMaster qMaster = dataSnapshot.getValue(QMaster.class);
            qMaster.balance -= balanceUsed;

            String msg = scanKet.getText().toString().trim();

            QHistory qHistory = new QHistory(balanceUsed,qMaster.title,msg);

            QTransactions trx = new QTransactions(balanceUsed,qHistory.used_at,msg,((MainActivity)getActivity()).getUser().accountNumber, qMaster.SourceAccountNumber);

/*          Transaksi terjadi pertama kali
*           Jika transaksi telah berhasil, menambahkan uang pada penerima
*           dan mengurangi uang pada pemilik
*           Sehingga QMaster berkurang valuenya
*           Dan menambahkan data pada QHistory*
*/
            dbTrx.setValue(trx);

            qHistory.setStatus("negative");
            dbHistoryOut.child(dbHistoryOut.push().getKey())
                    .setValue(qHistory);

            qHistory.setStatus("positive");
            dbHistoryIn.child(dbHistoryIn.push().getKey())
                    .setValue(qHistory);

            dbMaster.setValue(qMaster);

            ((MainActivity)getActivity()).showSuccess(balanceUsed);

            // User Info
            FirebaseUtils.getBaseRef().child("users").orderByChild("accountNumber").equalTo(BeneficieryAccountNumber)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                User user = ds.getValue(User.class);
                                user.setBalance(user.getBalance() + balanceUsed);
                                FirebaseUtils.getBaseRef().child("users").child(ds.getKey())
                                        .child("balance").setValue(user.getBalance());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            // Recipient Info
            FirebaseUtils.getBaseRef().child("users").orderByChild("accountNumber").equalTo(SourceAccountNumber)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                User user = ds.getValue(User.class);
                                user.setBalance(user.getBalance() - balanceUsed);
                                FirebaseUtils.getBaseRef().child("users").child(ds.getKey())
                                        .child("balance").setValue(user.getBalance());
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
/*            Push Notification Function
            VolleyHelper vh = new VolleyHelper();
            try {
                String title = "QASH "+qMaster.title + " telah digunakan !";
                String nominal = "Rp. "+((MainActivity)getActivity()).moneyParserString(String.valueOf(balanceUsed));
                vh.sendNotification(qMaster.SourceAccountNumber,title,nominal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
*/
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

}
