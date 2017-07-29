package gravicodev.qash.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import gravicodev.qash.Barcode.BarcodeCaptureActivity;
import java.util.ArrayList;

import gravicodev.qash.Adapter.ListLastActivityAdapter;
import gravicodev.qash.R;

/**
 * Created by Rasyadh A Aziz on 28/07/2017.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ListView listView;
    private ListLastActivityAdapter listLastActivityAdapter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        String[] name = new String[]{
                "QR Code 1", "QR Code 2", "QR Code 3", "QR Code 4", "QR Code 5"
        };

        String[] date = new String[]{
                "10-07-2017", "15-07-2017", "21-07-2017", "26-07-2017", "29-07-2017"
        };

        listView = (ListView) rootView.findViewById(R.id.listLastActivity);
        listLastActivityAdapter = new ListLastActivityAdapter(getActivity(), name, date);
        listView.setAdapter(listLastActivityAdapter);

        return rootView;
    }


}
