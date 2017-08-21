package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import gravicodev.qash.Adapter.ListCurrentBalanceAdapter;
import gravicodev.qash.R;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private ListView listView;
    private ListCurrentBalanceAdapter listCurrentBalanceAdapter;
    private TextView nameNasabah, balanceNasabah, initialName;
    private SwitchCompat switchQr;

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

        // Data dummies nasabah
        String nasabah = "Rasyadh Abdul Aziz";
        String saldo = "Rp 20.000.000";

        nameNasabah.setText(nasabah);
        balanceNasabah.setText(saldo);
        initialName.setText(nasabah.substring(0, 1).toUpperCase());

        // Data dummies name
        String[] name = new String[]{
                "Gajian", "Uang Saku", "Belanja", "Jajan", "THR", "Testing"
        };

        // Data dummies date
        String[] date = new String[]{
                "100.000", "50.000", "20.000", "5.000", "15.000", "0"
        };

        listView = (ListView) rootView.findViewById(R.id.listCurrentBalance);
        listCurrentBalanceAdapter = new ListCurrentBalanceAdapter(getActivity(), name, date);
        listView.setAdapter(listCurrentBalanceAdapter);
        listView.setDivider(null);

        return rootView;
    }

}
