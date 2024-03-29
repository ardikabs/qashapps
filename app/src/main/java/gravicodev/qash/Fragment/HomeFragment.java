package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Adapter.ListCurrentBalanceAdapter;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.Models.User;
import gravicodev.qash.Preference.QMasterManager;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ListView listView;
    private ListCurrentBalanceAdapter listCurrentBalanceAdapter;
    private TextView nameNasabah, balanceNasabah, initialName;
    private FrameLayout emptyLayout;
    private SwitchCompat switchQr;

    private List<String> mQRKeyList;
    private List<String> mQRbyUserList;
    private QMasterManager QMasterManager;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        QMasterManager = new QMasterManager(getContext());

        nameNasabah = (TextView) rootView.findViewById(R.id.nameNasabah);
        balanceNasabah = (TextView) rootView.findViewById(R.id.balanceNasabah);
        initialName = (TextView) rootView.findViewById(R.id.initialName);
        switchQr = (SwitchCompat) rootView.findViewById(R.id.switchQr);
        emptyLayout = (FrameLayout) rootView.findViewById(R.id.emptyCurrentQr);
        listView = (ListView) rootView.findViewById(R.id.listCurrentBalance);


        List<QMaster> emptyList = new ArrayList<>();
        mQRKeyList = new ArrayList<>();
        if(!QMasterManager.getData().isEmpty()){
            emptyList = QMasterManager.getData();
            mQRKeyList = QMasterManager.getKeyList();
            listView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        else{
            listView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        listCurrentBalanceAdapter = new ListCurrentBalanceAdapter(getActivity(),emptyList );
        listView.setAdapter(listCurrentBalanceAdapter);

        mQRbyUserList = new ArrayList<>();

        firebaseHandler();

        User user = ((MainActivity)getActivity()).getUser();
        nameNasabah.setText(user.fullname);
        balanceNasabah.setText("Rp "+((MainActivity)getActivity()).moneyParserString(String.valueOf(user.getBalance())));
        initialName.setText(user.fullname.substring(0, 1).toUpperCase());
        nameNasabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMasterManager.delete();
            }
        });
        return rootView;
    }

    private void firebaseHandler() {
        final String Uid = ((MainActivity)getActivity()).getUid();
        DatabaseReference dbUsers = FirebaseUtils.getBaseRef().child("users").child(Uid);
        DatabaseReference dbQCreator = FirebaseUtils.getBaseRef().child("qcreator").child(Uid);
        final DatabaseReference dbQMaster = FirebaseUtils.getBaseRef().child("qmaster");

        // Child listener for QR data
        final ChildEventListener qrListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                qMaster.setKey(key);
                if(mQRbyUserList.contains(key)){
                    if(!mQRKeyList.contains(key)){
                        listCurrentBalanceAdapter.refill(qMaster);
                        QMasterManager.addData(qMaster);
                        QMasterManager.addKeyList(key);
                        mQRKeyList = QMasterManager.getKeyList();
                    }
                    else{
                        int index = listCurrentBalanceAdapter.getIndex(key);
                        listCurrentBalanceAdapter.changeCondition(index,qMaster);
                        QMasterManager.editData(index,qMaster);
                    }
                    listView.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                qMaster.setKey(key);

                if (mQRbyUserList.contains(key)){
                    if(mQRKeyList.contains(key)){

                        int index = listCurrentBalanceAdapter.getIndex(key);
                        listCurrentBalanceAdapter.changeCondition(index,qMaster);
                        QMasterManager.editData(index,qMaster);
                    }
                }

/*              Jika balance qmaster menjadi 0, data qr code langsung di hapus
                if(qMaster.balance == 0){
                    FirebaseUtils.getBaseRef().child("qmaster")
                            .child(key)
                            .removeValue();

                    int index = listCurrentBalanceAdapter.getIndex(key);
                    mQRbyUserList.remove(key);
                    listCurrentBalanceAdapter.remove(index);
                    QMasterManager.removeData(index);
                    QMasterManager.removeKeyData(key);
                    mQRKeyList = QMasterManager.getKeyList();

                    if(mQRKeyList.size()!=0){
                        listView.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);
                    }
                    else{
                        listView.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                    }
                }
                else{}
*/


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if(mQRbyUserList.contains(key)){
                    if(mQRKeyList.contains(key)){
                        int index = listCurrentBalanceAdapter.getIndex(key);
                        mQRbyUserList.remove(key);
                        listCurrentBalanceAdapter.remove(index);
                        QMasterManager.removeData(index);
                        QMasterManager.removeKeyData(key);
                        mQRKeyList = QMasterManager.getKeyList();

                        if(mQRKeyList.size()!=0){
                            listView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                        }
                        else{
                            listView.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
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

        // User information value listener
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try{
                    nameNasabah.setText(user.fullname);
                    balanceNasabah.setText("Rp. "+((MainActivity)getActivity()).moneyParserString(String.valueOf(user.getBalance())));
                    initialName.setText(user.fullname.substring(0, 1).toUpperCase());
                    new SessionManager(getContext()).renew(user);
                }
                catch (NullPointerException ex){
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // QR owned by sser listener
        ValueEventListener qrUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(!mQRbyUserList.contains(ds.getKey())){
                        mQRbyUserList.add(ds.getKey());
                    }
                }
                // Listener to sum the total of QR owned by user
                dbQMaster.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(mQRKeyList.size() > mQRbyUserList.size()){
                            for(String key: mQRKeyList){
                                if(!mQRbyUserList.contains(key)){
                                    int index = listCurrentBalanceAdapter.getIndex(key);
                                    listCurrentBalanceAdapter.remove(index);
                                    QMasterManager.removeData(index);
                                    QMasterManager.removeKeyData(key);
                                    mQRKeyList = QMasterManager.getKeyList();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // Event Listener QR Master owned by user
                dbQMaster.limitToLast(20).addChildEventListener(qrListener);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } ;

        // Value Listener
        dbUsers.addValueEventListener(userListener);
        dbQCreator.addValueEventListener(qrUserListener);

        // Add Listener
        ((MainActivity)getActivity()).addListener(dbUsers,userListener);
        ((MainActivity)getActivity()).addListener(dbUsers,qrUserListener);
        ((MainActivity)getActivity()).addChildListener(dbQMaster,qrListener);

    }

    private void apiCaller() {
    }

}
