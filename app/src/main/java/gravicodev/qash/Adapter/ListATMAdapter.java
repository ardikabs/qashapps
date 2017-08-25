package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

/**
 * Created by Rasyadh A Aziz on 25/08/2017.
 */

public class ListATMAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] title, address, distance;

    public ListATMAdapter(Activity context, String[] title, String[] address, String[] distance){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.title = title;
        this.address = address;
        this.distance = distance;
    }

    @Override
    public int getCount() {
        return title.length;
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

        holder.title.setText(title[position]);
        holder.address.setText(address[position]);
        holder.distance.setText(distance[position]);
        return convertView;
    }
}
