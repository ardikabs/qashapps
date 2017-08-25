package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

public class ListTemplateAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] balance, description;

    public ListTemplateAdapter(Activity context, String[] balance, String[] description){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.balance = balance;
        this.description = description;
    }

    @Override
    public int getCount() {
        return balance.length;
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
        TextView balanceTemplate, descTemplate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_template, null);
            holder = new ViewHolder();
            holder.balanceTemplate = (TextView) convertView.findViewById(R.id.balanceTemplate);
            holder.descTemplate = (TextView) convertView.findViewById(R.id.descTemplate);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.balanceTemplate.setText(balance[position]);
        holder.descTemplate.setText(description[position]);
        return convertView;
    }
}
