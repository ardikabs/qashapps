package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

/**
 * Created by Rasyadh A Aziz on 29/07/2017.
 */

public class ListLastActivityAdapter extends BaseAdapter {
    private String[] name, date;
    private LayoutInflater inflater;

    public ListLastActivityAdapter(Context context, String[] name, String[] date) {
        inflater = LayoutInflater.from(context);
        this.name = name;
        this.date = date;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        TextView name, date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_last_activity, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.lastQrName);
            holder.date = (TextView) convertView.findViewById(R.id.lastQrDate);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(name[position]);
        holder.date.setText(date[position]);

        return convertView;
    }
}
