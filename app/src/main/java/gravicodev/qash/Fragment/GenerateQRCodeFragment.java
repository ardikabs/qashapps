package gravicodev.qash.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Activity.ShowQRCodeActivity;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.Models.User;
import gravicodev.qash.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class GenerateQRCodeFragment extends Fragment {
    private static final String TAG = "GenerateQRCodeFragment";
    public final static int WIDTH = 500;
    private Button btnGenerate;
    private AppCompatEditText qrName, qrBalance;

    private DatabaseReference db;
    private ValueEventListener timestampListener;

    public GenerateQRCodeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generateqrcode, container, false);

        qrName = (AppCompatEditText) rootView.findViewById(R.id.qr_name);
        qrBalance = (AppCompatEditText) rootView.findViewById(R.id.qr_balance);
        btnGenerate = (Button) rootView.findViewById(R.id.btnGenerate);

        qrBalance.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                qrBalance.removeTextChangedListener(this);
                String input = qrBalance.getText().toString();
                String inputParser = ((MainActivity)getActivity()).moneyParserString(input);
                qrBalance.setText(inputParser);
                qrBalance.setSelection(inputParser.length());
                qrBalance.addTextChangedListener(this);
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrname = qrName.getText().toString().trim();
                final String qrbalance = qrBalance.getText().toString().trim();
                final ArrayList<String> data = new ArrayList<>();
                data.add(qrname);
                data.add(qrbalance);

                if (!validateForm(qrname, qrbalance)) {
                    return;
                }

                // status success (true) or failed (false)
                Boolean status = true; // still static status

                FirebaseUtils.getBaseRef().child("users")
                        .child(((MainActivity)getActivity()).getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int balance = dataSnapshot.child("balance").getValue(Integer.class);

                                if(Integer.parseInt(qrbalance) > balance){
                                    alertDialog("Generate Qash Failed",
                                            "Your balance is not enough to create QR-Code.", "CANCEL", data);
                                }
                                else{
                                    firebaseHandler();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });

        // Bekas setOnClickListenet untuk test push notification
//        pButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 7);
//                String key = "QRCODE_1";
//                Integer balance = 150000;
//                String status = "active";
//                Long date = cal.getTime().getTime();
//                String title = "TITLE QRCODE";
//                String san = "SourceAccountNumber (SOURCE)";
//
//                QMaster qMaster = new QMaster(balance, status, date, title, san);
//                FirebaseUtils.getBaseRef().child("qmaster").child(key).setValue(qMaster);
//
//                HashMap<String, Boolean> qrvalue = new HashMap<>();
//                qrvalue.put(key, true);
//                FirebaseUtils.getBaseRef().child("qcreator").child("user_1").setValue(qrvalue);
//            }
//        });

        return rootView;
    }

    private void firebaseHandler() {
        db = FirebaseUtils.getBaseRef().child("timestamp").child(((MainActivity)getActivity()).getUid());
        timestampListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = ((MainActivity)getActivity()).getUser();
                    Long createdTimestamp = dataSnapshot.getValue(Long.class);
                    String name = qrName.getText().toString().trim();
                    String balance = qrBalance.getText().toString().trim();
                    String accNumber = user.accountNumber;

                    Calendar cal = Calendar.getInstance();
                    Date expired = new Date(createdTimestamp);
                    cal.setTime(expired);
                    cal.add(Calendar.MONTH,1);
                    Long expired_date = cal.getTimeInMillis();

                    String key = accNumber+"_"+expired_date;

                    QMaster qrdata = new QMaster(Integer.parseInt(balance), expired_date,name,accNumber);

                    FirebaseUtils.getBaseRef().child("qmaster").child(key).setValue(qrdata);
                    FirebaseUtils.getBaseRef().child("qcreator").child(((MainActivity)getActivity()).getUid())
                            .child(key).setValue(true);
                    FirebaseUtils.getBaseRef().child("timestamp").child(((MainActivity)getActivity()).getUid()).removeValue();

                    String msg = name + " created with balance " + ((MainActivity)getActivity()).moneyParser(Integer.parseInt(balance));
                    ArrayList<String> data = new ArrayList<>();
                    data.add(name);
                    data.add(((MainActivity)getActivity()).moneyParser(Integer.parseInt(balance)));
                    data.add(key);

                    alertDialog("Generate Qash Success", msg, "OK", data);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        db.addValueEventListener(timestampListener);
        db.setValue(ServerValue.TIMESTAMP);


    }

    private String parseWaktu(String time) {
        if(time.length() == 1)
        {
            return "0"+time;
        } else {
            return time;
        }
    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }



    public void alertDialog(String title, String message, String status, final ArrayList<String>  data) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setTitle(title);

        if(status.equalsIgnoreCase("OK")){

            alertDialogBuilder.setPositiveButton(status, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), ShowQRCodeActivity.class);
                    intent.putExtra("GenerateQR", data);
                    startActivity(intent);

                    qrName.setText("");
                    qrBalance.setText("");
                }
            });

        }
        else{
            alertDialogBuilder.setNegativeButton(status, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean validateForm(String qrname, String qrbalance) {
        boolean valid = true;

        if (TextUtils.isEmpty(qrname)) {
            qrName.setError(getString(R.string.err_msg_qrname));
            valid = false;
        } else {
            qrName.setError(null);
        }

        if (TextUtils.isEmpty(qrbalance)) {
            qrBalance.setError(getString(R.string.err_msg_qrbalance));
            valid = false;
        } else if (Integer.parseInt(qrbalance) < 10000) {
            qrBalance.setError(getString(R.string.err_msg_min_qrbalance));
            valid = false;
        } else {
            qrBalance.setError(null);
        }

        return valid;
    }
}
