package gravicodev.qash.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import gravicodev.qash.Adapter.ListATMAdapter;
import gravicodev.qash.Helper.VolleyCallback;
import gravicodev.qash.R;
import gravicodev.qash.Volley.VolleyHelper;

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

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double[] longitude = {location.getLongitude()};
        final double[] latitude = {location.getLatitude()};

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude[0] = location.getLongitude();
                latitude[0] = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        Log.d(TAG,""+longitude[0]);
        Log.d(TAG,""+latitude[0]);

        VolleyHelper vh = new VolleyHelper();
        try {
            vh.getATM(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.d(TAG,result);
                }
            },""+latitude[0], ""+longitude[0]);
        } catch (JSONException e) {
            e.printStackTrace();
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
