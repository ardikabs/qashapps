package gravicodev.qash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import gravicodev.qash.R;

public class ListInformationAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private String[] listname;
    private Integer[] listdrawable;

    public ListInformationAdapter(Context context, String[] listname, Integer[] listdrawable){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listname = listname;
        this.listdrawable = listdrawable;
    }

    @Override
    public int getCount() {
        return listname.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        TextView name;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_info, null);
            holder = new ListInformationAdapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.textListInfo);
            holder.img = (ImageView) convertView.findViewById(R.id.iconListInfo);
            convertView.setTag(holder);
        }
        else {
            holder = (ListInformationAdapter.ViewHolder) convertView.getTag();
        }

        holder.name.setText(listname[position]);
        holder.img.setImageResource(listdrawable[position]);

        return convertView;
    }
}
