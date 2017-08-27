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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        List<HashMap<String, String>> emptyList = new ArrayList<>();
        listATMAdapter = new ListATMAdapter(this, emptyList);
        listView.setAdapter(listATMAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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

            Log.d(TAG, "" + longitude[0]);
            Log.d(TAG, "" + latitude[0]);

            VolleyHelper vh = new VolleyHelper();
            try {
                vh.getATM(new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        JSONArray arrATM = null;
                        try {
                            arrATM = new JSONArray(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < arrATM.length(); i++) {
                            HashMap<String, String> newdata = new HashMap<>();
                            try {
                                newdata.put("title", arrATM.getJSONObject(i).getString("Type"));
                                newdata.put("distance", arrATM.getJSONObject(i).getString("Distance"));
                                newdata.put("address", arrATM.getJSONObject(i).getString("Address"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            listATMAdapter.refill(newdata);
                        }
                    }
                }, "" + latitude[0], "" + longitude[0]);
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
