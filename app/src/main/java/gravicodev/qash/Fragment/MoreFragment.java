package gravicodev.qash.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import gravicodev.qash.Activity.ATMPlaceActivity;
import gravicodev.qash.Activity.ListQRActivity;
import gravicodev.qash.Activity.MutationActivity;
import gravicodev.qash.Activity.PromotionActivity;
import gravicodev.qash.Activity.StorePlaceActivity;
import gravicodev.qash.Activity.TemplateSettingActivity;
import gravicodev.qash.Adapter.ListMoreAdapter;
import gravicodev.qash.R;

public class MoreFragment extends Fragment {
    private static final String TAG = "MoreFragment";
    private ListView listView;
    private ListMoreAdapter listMoreAdapter;

    public MoreFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_more, container, false);

        String[] listname = new String[]{
                "Qash List", "Mutation", "Promotion", "ATM Place", "Store Place","Template Setting"
        };

        Integer[] listdrawable = new Integer[]{
                 R.drawable.ic_listqr, R.drawable.ic_mutation, R.drawable.ic_promotion,
                R.drawable.ic_place, R.drawable.ic_store, R.drawable.ic_template
        };

        listView = (ListView) rootView.findViewById(R.id.listMoreInfo);
        listMoreAdapter = new ListMoreAdapter(getActivity(), listname, listdrawable);
        listView.setAdapter(listMoreAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        startActivity(new Intent(getActivity(), ListQRActivity.class));
                        break;
                    case 1 :
                        startActivity(new Intent(getActivity(), MutationActivity.class));
                        break;
                    case 2 :
                        startActivity(new Intent(getActivity(), PromotionActivity.class));
                        break;
                    case 3 :
                        startActivity(new Intent(getActivity(), ATMPlaceActivity.class));
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
}
