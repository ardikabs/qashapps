package gravicodev.qash.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import gravicodev.qash.Fragment.GenerateQRCodeFragment;
import gravicodev.qash.Fragment.HistoryFragment;
import gravicodev.qash.Fragment.HomeFragment;
import gravicodev.qash.Fragment.ScanQRCodeFragment;

/**
 * Created by Rasyadh A Aziz on 28/07/2017.
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "TabFragmentPagerAdapter";

    private String[] title = new String[]{
            "Home", "Generate QR-Code", "Scan QR-Code", "History"
    };

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new GenerateQRCodeFragment();
                break;
            case 2:
                fragment = new ScanQRCodeFragment();
                break;
            case 3 :
                fragment = new HistoryFragment();
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return title.length;
    }
}
