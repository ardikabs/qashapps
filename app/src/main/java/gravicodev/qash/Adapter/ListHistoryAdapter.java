package gravicodev.qash.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gravicodev.qash.Models.QHistory;
import gravicodev.qash.R;

public class ListHistoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<QHistory> mQHistory;

    public ListHistoryAdapter(Context context, List<QHistory> mQHistory) {
        inflater = LayoutInflater.from(context);
        this.mQHistory = mQHistory;
    }

    @Override
    public int getCount() {
        return mQHistory.size();
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
        TextView name, balance, date, description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        QHistory qHistory = mQHistory.get((mQHistory.size()-1)-position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_history, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.histQrName);
            holder.balance = (TextView) convertView.findViewById(R.id.histQrBalance);
            holder.date = (TextView) convertView.findViewById(R.id.histQrDate);
            holder.description = (TextView) convertView.findViewById(R.id.histQrDesc);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = qHistory.title;
        String msg = qHistory.msg;
        String balance = moneyParserString(String.valueOf(qHistory.balance)) ;
        String status = qHistory.getStatus();
        String time = timestampConverter((Long) qHistory.used_at);


        holder.name.setText(name);
        holder.date.setText(time);
        holder.description.setText("Description : " + msg);

        if(status.equalsIgnoreCase("positive")){
            holder.balance.setText("+ Rp " + balance);
            holder.balance.setTextColor(Color.parseColor("#015CAA"));

        }
        else{
            holder.balance.setText("- Rp "+ balance);
            holder.balance.setTextColor(Color.parseColor("#F07B2D"));
        }

        return convertView;
    }

    public void refill(QHistory qHistory){
        mQHistory.add(qHistory);
        notifyDataSetChanged();
    }

    public void changeCondition(int index, QHistory qHistory){
        mQHistory.set(index,qHistory);
        notifyDataSetChanged();
    }

    private String timestampConverter(long timestamp) {
        String relativeTime;

        Date past = new Date(timestamp);
        Date now = new Date();

        long minute = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

        if(days > 0){
            relativeTime = days + " days ago";
        }
        else if(minute > 60){
            relativeTime = hours + " hours ago";
        }
        else{
            relativeTime = minute + " minute ago";
        }


        return relativeTime;

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
