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
import android.widget.FrameLayout;
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

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QHistory;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.Models.QTransactions;
import gravicodev.qash.R;

public class ScanQRCodeFragment extends Fragment {
    private static final String TAG = "ScanQRCodeFragment";
    private AppCompatEditText scanBalance, scanKet;
    private Button btnScan;
    private FrameLayout dimmer;
    private LottieAnimationView animSuccess;
    private TextView ammountAccepted;

    public ScanQRCodeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scanqrcode, container, false);

        // Change Title of each fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Scan");

        btnScan = (Button) rootView.findViewById(R.id.btnScan);
        scanBalance = (AppCompatEditText) rootView.findViewById(R.id.scan_balance);
        scanKet = (AppCompatEditText) rootView.findViewById(R.id.scan_ket);
        dimmer = (FrameLayout) rootView.findViewById(R.id.dimmerSuccess);
        ammountAccepted = (TextView) rootView.findViewById(R.id.ammountAccepted);
        animSuccess = (LottieAnimationView) rootView.findViewById(R.id.animation_success);

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


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;

                    FirebaseUtils.getBaseRef()
                            .child("qmaster")
                            .child(barcode.displayValue)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
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

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
                else
                    showToast("DATA NULL");
            }
            else
                Log.e("QRCODE_ERROR:", String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    private void proses(Integer balance, DataSnapshot dataSnapshot) {
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
            showToast("Balance Qash tidak mencukupi");
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

}
