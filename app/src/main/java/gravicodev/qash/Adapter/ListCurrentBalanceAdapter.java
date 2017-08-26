package gravicodev.qash.Adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QHistory;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.R;

public class ListCurrentBalanceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<QMaster> mQMaster;

    public ListCurrentBalanceAdapter(Context context, List<QMaster> mQMaster) {
        inflater = LayoutInflater.from(context);
        this.mQMaster = mQMaster;
    }

    @Override
    public int getCount() {
        return mQMaster.size();
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
        SwitchCompat switcher;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

//        QMaster qMaster = mQMaster.get((mQMaster.size()-1)-position);
        QMaster qMaster = mQMaster.get(position);

        final String key = qMaster.getKey();
        final String status = qMaster.status;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_current_balance, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.curQrName);
            holder.balance = (TextView) convertView.findViewById(R.id.curQrBalance);
            holder.switcher = (SwitchCompat) convertView.findViewById(R.id.switchQr);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(qMaster.title);
        holder.balance.setText(moneyParserString(String.valueOf(qMaster.balance)));

        if(qMaster.status.equalsIgnoreCase("enabled")){
            holder.switcher.setChecked(true);
        }
        else{
            holder.switcher.setChecked(false);
        }

        holder.switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(status.equalsIgnoreCase("enabled")){
                        FirebaseUtils.getBaseRef().child("qmaster")
                                .child(key)
                                .child("status")
                                .setValue("disabled");
                    }
                    else{
                        FirebaseUtils.getBaseRef().child("qmaster")
                                .child(key)
                                .child("status")
                                .setValue("enabled");
                    }
                }
            }
        );

        return convertView;
    }

    public void refill(QMaster qMaster){
        mQMaster.add(qMaster);
        Collections.sort(mQMaster, new Comparator<QMaster>() {
            @Override
            public int compare(QMaster o1, QMaster o2) {
                Long ts1 = o1.created_at;
                Long ts2 = o2.created_at;
                return ts2.compareTo(ts1);
            }
        });

        notifyDataSetChanged();
    }

    public void changeCondition(int index, QMaster qMaster){
        mQMaster.set(index,qMaster);
        notifyDataSetChanged();
    }
    public void remove(int index){
        mQMaster.remove(index);
        notifyDataSetChanged();
    }

    public int getIndex(String key){
        int i = 0;
        for (QMaster qMaster: mQMaster){
            if(qMaster.getKey().equalsIgnoreCase(key)){
                return i;
            }
            i++;
        }
        return 0;
    }

    public QMaster getInfo(int position){
        QMaster qMaster = mQMaster.get(position);
        return qMaster;
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
