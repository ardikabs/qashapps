package gravicodev.qash.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,
                            "Permission denied to use your Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
