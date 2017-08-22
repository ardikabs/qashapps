package gravicodev.qash.Fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Activity.SliderActivity;
import gravicodev.qash.Barcode.BarcodeCaptureActivity;
import android.widget.Button;
import android.widget.Toast;

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
    private String BeneficieryAccountNumber = "0001-0001-1110";
    private String msg = "MESSAGE";
    private AppCompatEditText scanBalance, scanKet;
    private Button btnScan, testsuccess;
    private FrameLayout dimmer;
    private LottieAnimationView animSuccess;

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
        testsuccess = (Button) rootView.findViewById(R.id.testsucces);
        dimmer = (FrameLayout) rootView.findViewById(R.id.dimmerSuccess);
        animSuccess = (LottieAnimationView) rootView.findViewById(R.id.animation_success);

        testsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimmer.setVisibility(View.VISIBLE);
                animSuccess.playAnimation();

                int delay = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dimmer.setVisibility(View.GONE);
                    }
                }, delay * 3000);

                FirebaseUtils.getBaseRef().child("qhistory").child("3890532372_1506068812611")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                QHistory qHistory = dataSnapshot.getValue(QHistory.class);
                                showToast(String.valueOf(qHistory.used_at));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, 1);
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
                                        if(qMaster.status.equalsIgnoreCase("active")){
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
        final Integer balanceUsed = Integer.parseInt(scanBalance.getText().toString().trim());
        DatabaseReference dbHistory = FirebaseUtils.getBaseRef().child("qhistory")
                .child(((MainActivity)getActivity()).getUid());
        DatabaseReference dbMaster = FirebaseUtils.getBaseRef().child("qmaster")
                .child(dataSnapshot.getKey());
        DatabaseReference dbTrx = FirebaseUtils.getBaseRef().child("qtransactions")
                .child(dataSnapshot.getKey());

        if(balanceUsed > balance){
            showToast("Balance Qash tidak mencukupi");
        }
        else{
            showToast(dataSnapshot.getKey());

            QMaster qMaster = dataSnapshot.getValue(QMaster.class);
            qMaster.balance -= balanceUsed;

            String msg = scanKet.getText().toString().trim();

            QHistory qHistory = new QHistory(balanceUsed,qMaster.title,msg);
            qHistory.setKey(dataSnapshot.getKey());
            QTransactions trx = new QTransactions(balanceUsed,qHistory.used_at,msg,((MainActivity)getActivity()).getUser().accountNumber, qMaster.SourceAccountNumber);


            dbMaster.setValue(qMaster);

            dbHistory.child(dbHistory.push().getKey())
                    .setValue(qHistory);

            dbTrx.setValue(trx);

        }
    }

    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
