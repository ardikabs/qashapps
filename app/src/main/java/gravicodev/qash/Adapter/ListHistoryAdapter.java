package gravicodev.qash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

public class ListHistoryAdapter extends BaseAdapter {
    private String[] name, balance, date;
    private LayoutInflater inflater;

    public ListHistoryAdapter(Context context, String[] name, String[] balance, String[] date) {
        inflater = LayoutInflater.from(context);
        this.name = name;
        this.balance = balance;
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
        TextView name, balance, date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_history, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.histQrName);
            holder.balance = (TextView) convertView.findViewById(R.id.histQrBalance);
            holder.date = (TextView) convertView.findViewById(R.id.histQrDate);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(name[position]);
        holder.balance.setText(balance[position]);
        holder.date.setText(date[position]);

        return convertView;
    }
}
