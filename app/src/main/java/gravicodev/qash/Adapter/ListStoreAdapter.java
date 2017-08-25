package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

public class ListStoreAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] title, address, distance;

    public ListStoreAdapter(Activity context, String[] title, String[] address, String[] distance){
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
            convertView = inflater.inflate(R.layout.list_store, null);
            holder = new ListStoreAdapter.ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.titleStore);
            holder.address = (TextView) convertView.findViewById(R.id.addressStore);
            holder.distance = (TextView) convertView.findViewById(R.id.distanceStore);
            convertView.setTag(holder);
        }
        else {
            holder = (ListStoreAdapter.ViewHolder) convertView.getTag();
        }

        holder.title.setText(title[position]);
        holder.address.setText(address[position]);
        holder.distance.setText(distance[position]);
        return convertView;
    }
}
