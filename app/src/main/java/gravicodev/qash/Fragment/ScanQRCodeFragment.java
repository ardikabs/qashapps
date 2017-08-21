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

        Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, 1);

        final String key = "QRCODE_1";
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
            }
        });

        // Bekas test push notif
//        pbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseUtils.getBaseRef().child("qmaster").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(final DataSnapshot dataSnapshot) {
//                        final QMaster qMaster = dataSnapshot.getValue(QMaster.class);
//
//                        Calendar today = Calendar.getInstance();
//                        Calendar cal = Calendar.getInstance();
//                        Date expireDate = new Date(qMaster.expired_at);
//                        cal.setTime(expireDate);
//                        boolean expired = cal.before(today);
//
//                        if(expired){
//                            //expired
//                            showToast("QR Code tidak berlaku");
//
//                        }
//                        else{
//                            if(qMaster.status.equals("active")){
//                                //proses transfer disini
//
//                                FirebaseUtils.getBaseRef().child("qmaster").child(key).runTransaction(new Transaction.Handler() {
//                                    @Override
//                                    public Transaction.Result doTransaction(MutableData mutableData) {
//                                        QMaster qMaster1 = mutableData.getValue(QMaster.class);
//                                        Integer balanceUsed = 5000;
//                                        qMaster1.balance -= balanceUsed;
//
//                                        Calendar cal = Calendar.getInstance();
//                                        Long currentDate = cal.getTime().getTime();
//
//                                        QHistory qHistory = new QHistory(balanceUsed,currentDate,qMaster1.title,msg, BeneficieryAccountNumber);
//                                        FirebaseUtils.getBaseRef().child("qhistory").child("user_1").child(key).setValue(qHistory);
//
//                                        String trxKey = FirebaseUtils.getBaseRef().child("qtransactions").push().getKey();
//                                        FirebaseUtils.getBaseRef().child("qtransactions").child("user_1").child(trxKey).setValue(qHistory);
//
//                                        mutableData.setValue(qMaster1);
//                                        FirebaseUtils.getBaseRef().child("qtransactions").child("user_1").addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                for (DataSnapshot ds : dataSnapshot.getChildren()){
//                                                    ds.getRef().removeValue();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                        return Transaction.success(mutableData);
//                                    }
//
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//                                    }
//                                });
//                            }
//                            else{
//                                FirebaseUtils.getBaseRef().child("qmaster").child(dataSnapshot.getKey()).child("status").setValue("nonactive");
//                                showToast("QR Code tidak berlaku");
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
//                    homeTv.setText(barcode.displayValue);
                    Toast.makeText(getActivity(), barcode.displayValue, Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getActivity(), "salah bos", Toast.LENGTH_SHORT).show();
            } else Log.e("jancok", String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }
    
    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
