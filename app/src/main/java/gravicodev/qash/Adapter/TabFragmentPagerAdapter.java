package gravicodev.qash.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import gravicodev.qash.Fragment.GenerateQRCodeFragment;
import gravicodev.qash.Fragment.HistoryFragment;
import gravicodev.qash.Fragment.HomeFragment;
import gravicodev.qash.Fragment.ScanQRCodeFragment;
import gravicodev.qash.Fragment.MoreFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "TabFragmentPagerAdapter";

    private String[] title = new String[]{
            "Home", "Generate QR-Code", "Scan QR-Code", "Transaction", "History"
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
            case 3:
                fragment = new HistoryFragment();
                break;
            case 4:
                fragment = new MoreFragment();
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
