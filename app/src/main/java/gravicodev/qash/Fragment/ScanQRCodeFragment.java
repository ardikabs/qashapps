package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Rasyadh A Aziz on 28/07/2017.
 */

public class ScanQRCodeFragment extends Fragment {
    private static final String TAG = "ScanQRCodeFragment";
    private String BeneficieryAccountNumber = "0001-0001-1110";
    private String msg = "MESSAGE";
    private Button pbutton;

    public ScanQRCodeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scanqrcode, container, false);

        final String key = "QRCODE_1";
        pbutton = (Button) rootView.findViewById(R.id.pushtestbutton);
        pbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils.getBaseRef().child("qmaster").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final QMaster qMaster = dataSnapshot.getValue(QMaster.class);

                        Calendar today = Calendar.getInstance();
                        Calendar cal = Calendar.getInstance();
                        Date expireDate = new Date(qMaster.expired_at);
                        cal.setTime(expireDate);
                        boolean expired = cal.before(today);

                        if(expired){
                            //expired
                            showToast("QR Code tidak berlaku");

                        }
                        else{
                            if(qMaster.status.equals("active")){
                                //proses transfer disini

                                FirebaseUtils.getBaseRef().child("qmaster").child(key).runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        QMaster qMaster1 = mutableData.getValue(QMaster.class);
                                        Integer balanceUsed = 5000;
                                        qMaster1.balance -= balanceUsed;

                                        Calendar cal = Calendar.getInstance();
                                        Long currentDate = cal.getTime().getTime();

                                        QHistory qHistory = new QHistory(balanceUsed,currentDate,qMaster1.title,msg, BeneficieryAccountNumber);
                                        FirebaseUtils.getBaseRef().child("qhistory").child("user_1").child(key).setValue(qHistory);

                                        String trxKey = FirebaseUtils.getBaseRef().child("qtransactions").push().getKey();
                                        FirebaseUtils.getBaseRef().child("qtransactions").child("user_1").child(trxKey).setValue(qHistory);

                                        mutableData.setValue(qMaster1);
                                        FirebaseUtils.getBaseRef().child("qtransactions").child("user_1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                                    ds.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                    }
                                });
                            }
                            else{
                                FirebaseUtils.getBaseRef().child("qmaster").child(dataSnapshot.getKey()).child("status").setValue("nonactive");
                                showToast("QR Code tidak berlaku");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        return rootView;
    }

    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
