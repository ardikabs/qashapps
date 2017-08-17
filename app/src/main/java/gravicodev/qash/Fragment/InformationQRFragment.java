package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import gravicodev.qash.Adapter.ListInformationQRAdapter;
import gravicodev.qash.R;

public class InformationQRFragment extends Fragment {
    private static final String TAG = "TranscationFragment";
    private ListView listView;
    private ListInformationQRAdapter listInformationQRAdapter;

    public InformationQRFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_informationqr, container, false);

        // Change Title of each fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Transaction");

        String[] name = new String[]{
                "Belanja", "Gajian", "Uang Saku", "Jajan", "THR", "Testing"
        };

        String[] date = new String[]{
                "15 August 2017", "10 August 2017", "20 July 2017", "15 July 2017", "10 July 2017",
                "1 July 2017"
        };

        String[] balance = new String[]{
                "100.000", "50.000", "20.000", "5.000", "15.000", "0"
        };

        String[] status = new String[]{
                "Active", "Active", "Active", "Active", "Active", "Not Active"
        };


        listView = (ListView) rootView.findViewById(R.id.listInformationQr);
        listInformationQRAdapter = new ListInformationQRAdapter(getActivity(), name, date, balance,
                status);
        listView.setAdapter(listInformationQRAdapter);
        listView.setDivider(null);

        return rootView;
    }
}
