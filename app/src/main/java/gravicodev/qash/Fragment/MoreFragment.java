package gravicodev.qash.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import gravicodev.qash.Activity.ATMPlaceActivity;
import gravicodev.qash.Activity.ListQRActivity;
import gravicodev.qash.Activity.AccStatementActivity;
import gravicodev.qash.Activity.PromotionActivity;
import gravicodev.qash.Activity.StorePlaceActivity;
import gravicodev.qash.Activity.TemplateSettingActivity;
import gravicodev.qash.Adapter.ListMoreGridAdapter;
import gravicodev.qash.R;

public class MoreFragment extends Fragment {
    private static final String TAG = "MoreFragment";
    private GridView gridView;
    private ListMoreGridAdapter listMoreGridAdapter;

    public MoreFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        String[] listname = new String[]{
                "Qash List", "Account Statement", "Promotion", "ATM Place", "Store Place","Template Setting"
        };

        Integer[] listdrawable = new Integer[]{
                 R.drawable.ic_listqr, R.drawable.ic_mutation, R.drawable.ic_promotion,
                R.drawable.ic_place, R.drawable.ic_store, R.drawable.ic_template
        };
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        gridView = (GridView) rootView.findViewById(R.id.gridMore);
        listMoreGridAdapter = new ListMoreGridAdapter(getActivity(), listname, listdrawable);
        gridView.setAdapter(listMoreGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        startActivity(new Intent(getActivity(), ListQRActivity.class));
                        break;
                    case 1 :
                        startActivity(new Intent(getActivity(), AccStatementActivity.class));
                        break;
                    case 2 :
                        startActivity(new Intent(getActivity(), PromotionActivity.class));
                        break;
                    case 3 :
                        if(isLocationEnabled(getContext())){
                            startActivity(new Intent(getActivity(), ATMPlaceActivity.class));
                        }else{
                            Toast.makeText(getActivity(),
                                    "Enabled your Location Service first !", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4 :
                        startActivity(new Intent(getActivity(), StorePlaceActivity.class));
                        break;
                    case 5 :
                        startActivity(new Intent(getActivity(), TemplateSettingActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

        return rootView;
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
                    Toast.makeText(getActivity(),
                            "Permission denied to use your Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

}
