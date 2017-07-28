package gravicodev.qash.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gravicodev.qash.R;

/**
 * Created by Rasyadh A Aziz on 28/07/2017.
 */

public class ScanQRCodeFragment extends Fragment {
    private static final String TAG = "ScanQRCodeFragment";

    public ScanQRCodeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scanqrcode, container, false);

        return rootView;
    }
}
