package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gravicodev.qash.Adapter.ListHistoryAdapter;
import gravicodev.qash.R;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    private ListView listView;
    private ListHistoryAdapter listHistoryAdapter;

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Change Title of each fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("History");

        String[] name = new String[]{
                "Belanja", "Gajian", "Uang Saku", "Jajan", "THR", "Testing"
        };

        String[] balance = new String[]{
                "20.000", "900.000", "10.000", "5.000", "85.000", "20.000"
        };

        String[] date = new String[]{
                "1 hour ago", "5 hour ago", "yesterday at 10.00", "2 August 2017", "13 July 2017",
                "1 July 2017"
        };

        listView = (ListView) rootView.findViewById(R.id.listQashHistory);
        listHistoryAdapter = new ListHistoryAdapter(getActivity(), name, balance, date);
        listView.setAdapter(listHistoryAdapter);
        listView.setDivider(null);

        return rootView;
    }
}
