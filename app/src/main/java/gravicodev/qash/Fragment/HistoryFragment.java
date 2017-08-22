package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Adapter.ListHistoryAdapter;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QHistory;
import gravicodev.qash.R;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    private ListView listView;
    private ListHistoryAdapter listHistoryAdapter;

    private List<String> mListHistoryKey;

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Change Title of each fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("History");

        mListHistoryKey = new ArrayList<>();
        List<QHistory> emptyList = new ArrayList<>();

        listView = (ListView) rootView.findViewById(R.id.listQashHistory);
        listHistoryAdapter = new ListHistoryAdapter(getActivity(),emptyList);
        listView.setAdapter(listHistoryAdapter);
        listView.setDivider(null);
        
        queryHistory();
        return rootView;
    }

    private void queryHistory() {
        ChildEventListener historyListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if(dataSnapshot.exists()){

                    if(!mListHistoryKey.contains(key)){
                        QHistory qhistory = dataSnapshot.getValue(QHistory.class);
                        mListHistoryKey.add(key);
                        listHistoryAdapter.refill(qhistory);

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                QHistory qhistory = dataSnapshot.getValue(QHistory.class);
                if(mListHistoryKey.contains(key)){
                    int index = mListHistoryKey.indexOf(key);
                    listHistoryAdapter.changeCondition(index,qhistory);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
