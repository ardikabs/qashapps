package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;

import gravicodev.qash.R;

public class ListMutationAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] name, trailer, amount, type, date, branch;

    public ListMutationAdapter(Activity context, String[] name, String[] trailer, String[] amount
            , String[] type, String[] date, String[] branch){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.name = name;
        this.trailer = trailer;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.branch = branch;
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

    private static class ViewHolder{
        TextView transactionName, trailer, transactionAmount, transactionType, transactionDate,
                branchCode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_mutation, null);
            holder = new ViewHolder();
            holder.transactionName = (TextView) convertView.findViewById(R.id.transactionName);
            holder.trailer = (TextView) convertView.findViewById(R.id.trailer);
            holder.transactionAmount = (TextView) convertView.findViewById(R.id.transactionAmount);
            holder.transactionType = (TextView) convertView.findViewById(R.id.transactionType);
            holder.transactionDate = (TextView) convertView.findViewById(R.id.transactionDate);
            holder.branchCode = (TextView) convertView.findViewById(R.id.branchCode);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.transactionName.setText(name[position]);
        holder.trailer.setText(trailer[position]);
        holder.transactionAmount.setText(amount[position]);
        holder.transactionType.setText(type[position]);
        holder.transactionDate.setText(date[position]);
        holder.branchCode.setText(branch[position]);

        String identifier = "Qash";
        Boolean found = trailer[position].contains(identifier);

        if (found){
            holder.transactionName.setTextColor(Color.parseColor("#015CAA"));
        }

        return convertView;
    }
}
