package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import gravicodev.qash.R;

public class ListPromotionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] title, desc, date;

    public ListPromotionAdapter(Activity context, String[] title, String[] desc, String[] date){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.title = title;
        this.desc = desc;
        this.date = date;
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

    private static class ViewHolder {
        TextView title, description, date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_promotion, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.titlePromotion);
            holder.description = (TextView) convertView.findViewById(R.id.descriptionPromotion);
            holder.date = (TextView) convertView.findViewById(R.id.datePromotion);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(title[position]);
        holder.description.setText(desc[position]);
        holder.date.setText(date[position]);
        return convertView;
    }
}
