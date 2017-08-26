package gravicodev.qash.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import gravicodev.qash.Models.QMaster;
import gravicodev.qash.R;

public class ListTemplateAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<HashMap<String,Object>> mTemplateList;

    public ListTemplateAdapter(Activity context, List<HashMap<String,Object>> mTemplateList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mTemplateList = mTemplateList;
    }

    @Override
    public int getCount() {
        return mTemplateList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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

        HashMap<String,Object> data = mTemplateList.get(position);
        String desc = (String) data.get("desc");
        String balance = (String) data.get("balance");

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

        holder.descTemplate.setText(desc);
        holder.balanceTemplate.setText("Rp "+moneyParserString(balance));
        return convertView;
    }

    public void refill(HashMap<String, Object> template){
        mTemplateList.add(template);
        Collections.sort(mTemplateList, new Comparator<HashMap<String,Object>>() {
            @Override
            public int compare(HashMap<String,Object> o1, HashMap<String,Object> o2) {
                Long ts1 = (Long) o1.get("created_at");
                Long ts2 = (Long) o2.get("created_at");
                return ts2.compareTo(ts1);
            }
        });
        notifyDataSetChanged();
    }

    public void change(String key, HashMap<String,Object> template){
        int index = mTemplateList.indexOf(key);
        mTemplateList.set(index,template);
        notifyDataSetChanged();
    }

    public void remove(int index){
        mTemplateList.remove(index);
        notifyDataSetChanged();
    }

    public HashMap<String,Object> getData(int index){
        return mTemplateList.get(index);
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
