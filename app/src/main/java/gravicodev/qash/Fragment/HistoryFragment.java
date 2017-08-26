package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Adapter.ListHistoryAdapter;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QHistory;
import gravicodev.qash.Preference.QHistoryManager;
import gravicodev.qash.R;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    private ListView listView;
    private ListHistoryAdapter listHistoryAdapter;

    private List<String> mListHistoryKey;
    private QHistoryManager qHistoryManager;

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        qHistoryManager = new QHistoryManager(getContext());
        mListHistoryKey = new ArrayList<>();
        List<QHistory> initList = new ArrayList<>();
        if(!qHistoryManager.getData().isEmpty()){
            initList = qHistoryManager.getData();
            mListHistoryKey = qHistoryManager.getKeyList();
        }
        listView = (ListView) rootView.findViewById(R.id.listQashHistory);
        listHistoryAdapter = new ListHistoryAdapter(getActivity(),initList);
        listView.setAdapter(listHistoryAdapter);

        queryHistory();
        return rootView;
    }

    private void queryHistory() {
        final ChildEventListener historyListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                QHistory qHistory = dataSnapshot.getValue(QHistory.class);
                qHistory.setKey(key);
                if(dataSnapshot.exists()){
                    if(!mListHistoryKey.contains(key)){
                        listHistoryAdapter.refill(qHistory);
                        qHistoryManager.addData(qHistory);
                        qHistoryManager.addKeyList(key);
                        mListHistoryKey = qHistoryManager.getKeyList();
                    }
                    else{
                        int index = listHistoryAdapter.getIndex(key);
                        listHistoryAdapter.changeCondition(index,qHistory);
                        qHistoryManager.editData(index,qHistory);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                QHistory qHistory = dataSnapshot.getValue(QHistory.class);
                if(mListHistoryKey.contains(key)){

                    int index = listHistoryAdapter.getIndex(key);
                    listHistoryAdapter.changeCondition(index,qHistory);
                    qHistoryManager.editData(index,qHistory);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if(mListHistoryKey.contains(key)){
                    int index = listHistoryAdapter.getIndex(key);
                    listHistoryAdapter.remove(index);
                    qHistoryManager.removeData(index);
                    qHistoryManager.removeKeyData(key);
                    mListHistoryKey = qHistoryManager.getKeyList();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference dbHistory = FirebaseUtils.getBaseRef().child("qhistory")
                .child(((MainActivity)getActivity()).getUser().accountNumber);

        dbHistory.orderByChild("used_at").limitToLast(10).addChildEventListener(historyListener);

        // Add Listener
        ((MainActivity)getActivity()).addChildListener(dbHistory,historyListener);
    }
}
