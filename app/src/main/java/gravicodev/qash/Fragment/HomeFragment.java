package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import gravicodev.qash.R;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ListView listView;
    private ListCurrentBalanceAdapter listCurrentBalanceAdapter;
    private TextView nameNasabah, balanceNasabah, initialName;
    private SwitchCompat switchQr;

    private List<String> mQRKeyList;
    private List<String> mQRbyUserList;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Change Title of each fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Qash");

        nameNasabah = (TextView) rootView.findViewById(R.id.nameNasabah);
        balanceNasabah = (TextView) rootView.findViewById(R.id.balanceNasabah);
        initialName = (TextView) rootView.findViewById(R.id.initialName);
        switchQr = (SwitchCompat) rootView.findViewById(R.id.switchQr);

        listView = (ListView) rootView.findViewById(R.id.listCurrentBalance);
        List<QMaster> emptyList = new ArrayList<>();
        listCurrentBalanceAdapter = new ListCurrentBalanceAdapter(getActivity(),emptyList );
        listView.setAdapter(listCurrentBalanceAdapter);

        mQRKeyList = new ArrayList<>();
        mQRbyUserList = new ArrayList<>();

        firebaseHandler();

        User user = ((MainActivity)getActivity()).getUser();
        nameNasabah.setText(user.fullname);
        balanceNasabah.setText(moneyParser(user.getBalance()));
        initialName.setText(user.fullname.substring(0, 1).toUpperCase());

        return rootView;
    }

    private void firebaseHandler() {
        final String Uid = ((MainActivity)getActivity()).getUid();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setBalance(dataSnapshot.child("balance").getValue(Integer.class));

                nameNasabah.setText(user.fullname);
                balanceNasabah.setText(moneyParser(user.getBalance()));
                initialName.setText(user.fullname.substring(0, 1).toUpperCase());

                ((MainActivity)getActivity()).renewUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        final ChildEventListener qrListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(mQRbyUserList.contains(key)){
                    if(!mQRKeyList.contains(key)){
                        QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                        qMaster.setKey(key);

                        mQRKeyList.add(key);
                        listCurrentBalanceAdapter.refill(qMaster);
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
                        listCurrentBalanceAdapter.changeCondition(index,qMaster);
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
                        listCurrentBalanceAdapter.remove(index);
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

        DatabaseReference dbUsers = FirebaseUtils.getBaseRef().child("users").child(Uid);
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

        // Value Listener
        dbUsers.addValueEventListener(userListener);

        // Add Listener
        ((MainActivity)getActivity()).addListener(dbUsers,userListener);
        ((MainActivity)getActivity()).addChildListener(dbQMaster,qrListener);

    }

    private void apiCaller() {
    }

    private String moneyParser(int data){
        int delimiter = 10;
        ArrayList<Integer> hasil = new ArrayList<>();
        while(true){
            if(data > 0){
                hasil.add(data % delimiter);
                data /= delimiter;
            }else{
                break;
            }
        }

        String strHasil = "";
        int x = 1;
        for(int i=0; i < hasil.size();i++){
            if(x==3){
                strHasil = "." + String.valueOf(hasil.get(i)) + strHasil;
                x = 0;
            }else{
                strHasil = String.valueOf(hasil.get(i)) + strHasil;
            }
            x++;
        }

        return "Rp " +strHasil;
    }

}
