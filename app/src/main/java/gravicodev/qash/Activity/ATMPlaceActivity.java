package gravicodev.qash.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import gravicodev.qash.Adapter.ListATMAdapter;
import gravicodev.qash.R;

public class ATMPlaceActivity extends AppCompatActivity {
    private static final String TAG = "ATMPlaceActivity";
    private ListView listView;
    private ListATMAdapter listATMAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmplace);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_atm);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listATM);

        // Dummy data title
        String[] title = new String[]{
                "ATM 1", "ATM 2", "ATM 3", "ATM 4", "ATM 5"
        };

        // Dummy data address
        String[] address = new String[]{
                "Jl. Mana", "Jl. Mini", "Jl. Munu", "Jl. Mene", "Jl. Mono"
        };

        // Dummy data distance
        String[] distance = new String[]{
                "1 KM", "2 KM", "3 KM", "4 KM", "5 KM"
        };

        listATMAdapter = new ListATMAdapter(this, title, address, distance);
        listView.setAdapter(listATMAdapter);
    }
}
