package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import gravicodev.qash.Activity.BaseActivity;
import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Adapter.ListCurrentBalanceAdapter;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.User;
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


        firebaseHandler();

        return rootView;
    }

    private void firebaseHandler() {
        String Uid = ((MainActivity)getActivity()).getUid();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setBalance(dataSnapshot.child("balance").getValue(Integer.class));
                nameNasabah.setText(user.fullname);
                balanceNasabah.setText(moneyParser(user.getBalance()));
                initialName.setText(user.fullname.substring(0, 1).toUpperCase());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        DatabaseReference users = FirebaseUtils.getBaseRef().child("users").child(Uid);
        users.addValueEventListener(userListener);

        // Add Listener
        ((MainActivity)getActivity()).addListener(users,userListener);
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

        return "Rp. " +strHasil;
    }

}
