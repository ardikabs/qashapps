package gravicodev.qash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

public class ListInformationQRAdapter extends BaseAdapter {
    private String[] name, date;
    private LayoutInflater inflater;

    public ListInformationQRAdapter(Context context, String[] name, String[] date) {
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
            convertView = inflater.inflate(R.layout.list_information, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.listInformationName);
            holder.date = (TextView) convertView.findViewById(R.id.listInformationDate);
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
