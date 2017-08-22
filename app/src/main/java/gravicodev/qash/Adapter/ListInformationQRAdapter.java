package gravicodev.qash.Adapter;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gravicodev.qash.Activity.ShowQRCodeActivity;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QMaster;
import gravicodev.qash.R;

import static android.R.attr.data;

public class ListInformationQRAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<QMaster> mQMaster;
    private Context context;

    public ListInformationQRAdapter(Context context, List<QMaster> qMasters) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mQMaster = qMasters;
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
        TextView name, date, balance, status;
        Button showQR, deleteQR;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        QMaster qMaster = mQMaster.get((mQMaster.size()-1)-position);
        final String key = qMaster.getKey();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_information, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameInfo);
            holder.date = (TextView) convertView.findViewById(R.id.createdValueInfo);
            holder.balance = (TextView) convertView.findViewById(R.id.balanceValueInfo);
            holder.status = (TextView) convertView.findViewById(R.id.statusValueInfo);
            holder.showQR = (Button)convertView.findViewById(R.id.showQr);
            holder.deleteQR = (Button) convertView.findViewById(R.id.deleteQr);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(qMaster.title);
        holder.date.setText(timestampTodate((Long) qMaster.created_at));
        holder.balance.setText(moneyParserString(String.valueOf(qMaster.balance)));
        holder.status.setText(qMaster.status);

        final String title = qMaster.title;
        final String balance = String.valueOf(qMaster.balance);
        final String bitmapStr = qMaster.getKey();

        holder.showQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> data = new ArrayList<>();
                data.add(title);
                data.add(moneyParserString(String.valueOf(balance)));
                data.add(bitmapStr);
                Intent intent = new Intent(context, ShowQRCodeActivity.class);
                intent.putExtra("ShowQR",data);
                context.startActivity(intent);
            }
        });

        holder.deleteQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure want to delete " + title + " ?");
                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUtils.getBaseRef()
                                .child("qmaster")
                                .child(key)
                                .removeValue();
                    }
                });

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return convertView;
    }

    public void refill(QMaster qMaster){
        mQMaster.add(qMaster);
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

    public String timestampTodate(Long timestamp){
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date myDate = new Date(timestamp);
        newDateFormat.applyPattern("EEEE, d MMM yyyy");
        return newDateFormat.format(myDate);

    }

}
