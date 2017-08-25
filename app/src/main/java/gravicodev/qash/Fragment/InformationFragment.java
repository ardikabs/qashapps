package gravicodev.qash.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import gravicodev.qash.Activity.ATMPlaceActivity;
import gravicodev.qash.Activity.ListQRActivity;
import gravicodev.qash.Activity.PromotionActivity;
import gravicodev.qash.Activity.StorePlaceActivity;
import gravicodev.qash.Activity.TemplateSettingActivity;
import gravicodev.qash.Adapter.ListInformationAdapter;
import gravicodev.qash.Adapter.ListQRAdapter;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.R;

public class InformationFragment extends Fragment {
    private static final String TAG = "InformationFragment";
    private ListView listView;
    private ListInformationAdapter listInformationAdapter;

    public InformationFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        String[] listname = new String[]{
                "Qash List", "Promotion", "ATM Place", "Store Place","Template Setting"
        };

        Integer[] listdrawable = new Integer[]{
                 R.drawable.ic_listqr, R.drawable.ic_promotion, R.drawable.ic_place,
                R.drawable.ic_store, R.drawable.ic_template
        };

        listView = (ListView) rootView.findViewById(R.id.listMoreInfo);
        listInformationAdapter = new ListInformationAdapter(getActivity(), listname, listdrawable);
        listView.setAdapter(listInformationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        startActivity(new Intent(getActivity(), ListQRActivity.class));
                        break;
                    case 1 :
                        startActivity(new Intent(getActivity(), PromotionActivity.class));
                        break;
                    case 2 :
                        startActivity(new Intent(getActivity(), ATMPlaceActivity.class));
                        break;
                    case 3 :
                        startActivity(new Intent(getActivity(), StorePlaceActivity.class));
                        break;
                    case 4 :
                        startActivity(new Intent(getActivity(), TemplateSettingActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

        return rootView;
    }
}
