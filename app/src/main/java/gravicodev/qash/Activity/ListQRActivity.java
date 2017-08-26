package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import gravicodev.qash.Preference.QMasterManager;
import gravicodev.qash.R;

public class ListQRActivity extends BaseActivity {
    private static final String TAG = "ListQRActivity";
    private ListView listView;
    private ListQRAdapter listQRAdapter;

    private List<String> mQRKeyList;
    private List<String> mQRbyUserList;
    private QMasterManager qMasterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_qr);
        qMasterManager = new QMasterManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_qashlist);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mQRKeyList = new ArrayList<>();
        List<QMaster> initList = new ArrayList<>();
        if(!qMasterManager.getData().isEmpty()){
            initList = qMasterManager.getData();
            mQRKeyList = qMasterManager.getKeyList();
        }

        listView = (ListView) findViewById(R.id.listInformationQr);
        listQRAdapter = new ListQRAdapter(this,initList);
        listView.setAdapter(listQRAdapter);

        mQRbyUserList = new ArrayList<>();

        firebaseHandler();
    }

    private void firebaseHandler() {
        final String Uid = getUid();

        final DatabaseReference dbQMaster = FirebaseUtils.getBaseRef().child("qmaster");
        DatabaseReference dbQCreator = FirebaseUtils.getBaseRef().child("qcreator").child(Uid);

        final ChildEventListener qrListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                qMaster.setKey(key);
                if(mQRbyUserList.contains(key)){
                    if(!mQRKeyList.contains(key)){
                        listQRAdapter.refill(qMaster);
                        qMasterManager.addData(qMaster);
                        qMasterManager.addKeyList(key);
                        mQRKeyList = qMasterManager.getKeyList();
                    }
                    else{
                        int index = listQRAdapter.getIndex(key);
                        listQRAdapter.changeCondition(index,qMaster);
                        qMasterManager.editData(index,qMaster);
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

                        int index = listQRAdapter.getIndex(key);
                        listQRAdapter.changeCondition(index,qMaster);
                        qMasterManager.editData(index,qMaster);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if(mQRbyUserList.contains(key)){
                    if(mQRKeyList.contains(key)){
                        int index = listQRAdapter.getIndex(key);
                        mQRbyUserList.remove(key);

                        listQRAdapter.remove(index);
                        FirebaseUtils.getBaseRef().child("qrcreator")
                                .child(Uid)
                                .child(key)
                                .removeValue();
                        qMasterManager.removeData(index);
                        qMasterManager.removeKeyData(key);
                        mQRKeyList = qMasterManager.getKeyList();
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
        ValueEventListener qrUserListener = new ValueEventListener() {
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
        };

        dbQMaster.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mQRKeyList.size() > mQRbyUserList.size()){
                    for(String key: mQRKeyList){
                        if(!mQRbyUserList.contains(key)){
                            int index = listQRAdapter.getIndex(key);
                            listQRAdapter.remove(index);
                            qMasterManager.removeData(index);
                            qMasterManager.removeKeyData(key);
                            mQRKeyList = qMasterManager.getKeyList();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbQCreator.addValueEventListener(qrUserListener);
    }
}
