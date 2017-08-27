package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import gravicodev.qash.R;

/**
 * Created by Rasyadh A Aziz on 25/08/2017.
 */

public class ListATMAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] title, address, distance;
    private List<HashMap<String,String>> mATMList;
    public ListATMAdapter(Activity context, String[] title, String[] address, String[] distance){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.title = title;
        this.address = address;
        this.distance = distance;
    }
    public ListATMAdapter(Activity context, List<HashMap<String,String>> mATMList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mATMList = mATMList;
    }

    @Override
    public int getCount() {
        return mATMList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder{
        TextView title, address, distance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        HashMap<String,String> data = mATMList.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_atm, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.titleATM);
            holder.address = (TextView) convertView.findViewById(R.id.addressATM);
            holder.distance = (TextView) convertView.findViewById(R.id.distanceATM);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(data.get("title"));
        holder.address.setText(data.get("address"));
        int distance = Integer.parseInt(data.get("distance").split("\\.")[0]);
        if(distance < 1000){
            holder.distance.setText(distance+" M");
        }
        else{
            float kmDistance = distance/1000f;
            holder.distance.setText(kmDistance+" KM");
        }
        return convertView;
    }

    public void refill(HashMap<String,String> data){
        mATMList.add(data);
        notifyDataSetChanged();
    }
}
