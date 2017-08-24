package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gravicodev.qash.Adapter.ListQRAdapter;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.R;

public class ListQRActivity extends BaseActivity {
    private static final String TAG = "ListQRActivity";
    private ListView listView;
    private ListQRAdapter listQRAdapter;

    private List<String> mQRKeyList;
    private List<String> mQRbyUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qr);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_qashlist);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listInformationQr);
        List<QMaster> emptyList = new ArrayList<>();
        listQRAdapter = new ListQRAdapter(this,emptyList);
        listView.setAdapter(listQRAdapter);

        mQRKeyList = new ArrayList<>();
        mQRbyUserList = new ArrayList<>();

        firebaseHandler();
    }

    private void firebaseHandler() {
        final String Uid = getUid();

        final ChildEventListener qrListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(mQRbyUserList.contains(key)){
                    if(!mQRKeyList.contains(key)){
                        QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                        qMaster.setKey(key);

                        mQRKeyList.add(key);
                        listQRAdapter.refill(qMaster);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(mQRbyUserList.contains(key)){
                    if(mQRKeyList.contains(key)){
                        QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                        qMaster.setKey(key);

                        int index = mQRKeyList.indexOf(key);
                        listQRAdapter.changeCondition(index,qMaster);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if(mQRbyUserList.contains(key)){
                    if(mQRKeyList.contains(key)){
                        QMaster qMaster = dataSnapshot.getValue(QMaster.class);

                        int index = mQRKeyList.indexOf(key);
                        mQRKeyList.remove(index);

                        mQRbyUserList.remove(key);
                        listQRAdapter.remove(index);
                        FirebaseUtils.getBaseRef().child("qrcreator")
                                .child(Uid)
                                .child(key)
                                .removeValue();
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        final DatabaseReference dbQMaster = FirebaseUtils.getBaseRef().child("qmaster");


        FirebaseUtils.getBaseRef().child("qcreator").child(Uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            if(!mQRbyUserList.contains(ds.getKey())){
                                mQRbyUserList.add(ds.getKey());
                            }
                            dbQMaster.limitToLast(20).addChildEventListener(qrListener);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
