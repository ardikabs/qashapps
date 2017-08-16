package gravicodev.qash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

public class ListCurrentBalanceAdapter extends BaseAdapter {
    private String[] name, balance;
    private LayoutInflater inflater;

    public ListCurrentBalanceAdapter(Context context, String[] name, String[] balance) {
        inflater = LayoutInflater.from(context);
        this.name = name;
        this.balance = balance;
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
        TextView name, balance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_current_balance, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.curQrName);
            holder.balance = (TextView) convertView.findViewById(R.id.curQrBalance);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(name[position]);
        holder.balance.setText(balance[position]);

        return convertView;
    }
}
