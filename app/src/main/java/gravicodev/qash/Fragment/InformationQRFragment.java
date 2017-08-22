package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Adapter.ListInformationQRAdapter;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.Models.User;
import gravicodev.qash.R;

public class InformationQRFragment extends Fragment {
    private static final String TAG = "TranscationFragment";
    private ListView listView;
    private ListInformationQRAdapter listInformationQRAdapter;

    private List<String> mQRKeyList;
    private List<String> mQRbyUserList;
    public InformationQRFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_informationqr, container, false);

        // Change Title of each fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Information");

        listView = (ListView) rootView.findViewById(R.id.listInformationQr);
        List<QMaster> emptyList = new ArrayList<>();
        listInformationQRAdapter = new ListInformationQRAdapter(getActivity(),emptyList);
        listView.setAdapter(listInformationQRAdapter);
        listView.setDivider(null);

        mQRKeyList = new ArrayList<>();
        mQRbyUserList = new ArrayList<>();

        firebaseHandler();
        return rootView;
    }

    private void firebaseHandler() {
        final String Uid = ((MainActivity)getActivity()).getUid();

        final ChildEventListener qrListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(mQRbyUserList.contains(key)){
                    if(!mQRKeyList.contains(key)){
                        QMaster qMaster = dataSnapshot.getValue(QMaster.class);
                        qMaster.setKey(key);

                        mQRKeyList.add(key);
                        listInformationQRAdapter.refill(qMaster);
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
                        listInformationQRAdapter.changeCondition(index,qMaster);
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
                        listInformationQRAdapter.remove(index);
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
