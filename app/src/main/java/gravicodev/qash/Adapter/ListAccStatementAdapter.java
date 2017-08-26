package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gravicodev.qash.R;

public class ListAccStatementAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] name, trailer, amount, type, date, branch;
    private List<HashMap<String,String>> mAccountStatement;

    public ListAccStatementAdapter(Activity context, String[] name, String[] trailer, String[] amount
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

    public ListAccStatementAdapter(Activity context, List<HashMap<String,String>> mAccountStatement){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mAccountStatement = mAccountStatement;
    }
    @Override
    public int getCount() {
        return mAccountStatement.size();
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
        TextView transactionName, trailer, transactionAmount, transactionType, transactionDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        HashMap<String,String> data = mAccountStatement.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_accstatement, null);
            holder = new ViewHolder();
            holder.transactionName = (TextView) convertView.findViewById(R.id.transactionName);
            holder.trailer = (TextView) convertView.findViewById(R.id.trailer);
            holder.transactionAmount = (TextView) convertView.findViewById(R.id.transactionAmount);
            holder.transactionType = (TextView) convertView.findViewById(R.id.transactionType);
            holder.transactionDate = (TextView) convertView.findViewById(R.id.transactionDate);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.transactionName.setText(data.get("name"));
        holder.trailer.setText(data.get("trailer"));
        holder.transactionAmount.setText("Rp " + moneyParserString(String.valueOf(data.get("amount"))));
        holder.transactionType.setText(data.get("type"));
        holder.transactionDate.setText(data.get("date"));

        String identifier = "Pay By Qash";
        Boolean found = data.get("trailer").contains(identifier);

        if (found){
            holder.transactionName.setTextColor(Color.parseColor("#015CAA"));
        }

        return convertView;
    }


    public void refill(HashMap<String,String> data){
        mAccountStatement.add(data);
        notifyDataSetChanged();
    }

    public String moneyParserString(String data){
        ArrayList<String> input = new ArrayList<>();
        for(int i = data.length()-1;i>=0;i--){
            if(!".".equals(String.valueOf(data.charAt(i)))){
                input.add(String.valueOf(data.charAt(i)));
            }
        }

        String strHasil = "";
        int x = 1;
        for(int i=0; i < input.size();i++){
            if(x==3 && i != (input.size()-1)){
                strHasil = "." + input.get(i) + strHasil;
                x = 0;
            }else{
                strHasil = input.get(i) + strHasil;
            }
            x++;
        }

        return strHasil;
    }
}
