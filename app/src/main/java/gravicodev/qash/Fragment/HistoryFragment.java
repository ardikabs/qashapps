package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gravicodev.qash.Adapter.ListHistoryAdapter;
import gravicodev.qash.Adapter.ListLastActivityAdapter;
import gravicodev.qash.R;

/**
 * Created by Rasyadh A Aziz on 28/07/2017.
 */

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    private ListView listView;
    private ListHistoryAdapter listHistoryAdapter;

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        String[] name = new String[]{
                "QR Code 1", "QR Code 2", "QR Code 3", "QR Code 4", "QR Code 5"
        };

        String[] balance = new String[]{
                "Rp 50000", "Rp 30000", "Rp 5000", "Rp 10000", "Rp 100000"
        };

        listView = (ListView) rootView.findViewById(R.id.listQashHistory);
        listHistoryAdapter = new ListHistoryAdapter(getActivity(), name, balance);
        listView.setAdapter(listHistoryAdapter);

        return rootView;
    }
}
