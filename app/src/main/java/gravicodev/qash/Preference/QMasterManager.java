package gravicodev.qash.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gravicodev.qash.Models.QMaster;

/**
 * Created by supermonster on 8/24/2017.
 */

public class QMasterManager {

    private final String PREF_NAME;
    private final String PREF_HOME_DATA;
    private final String PREF_KEY_DATA;


    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public QMasterManager(Context context){
        this.context = context;

        PREF_NAME = "QMasterManager";

        PREF_HOME_DATA = "DATA";
        PREF_KEY_DATA = "KEY";

        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void saveData(List<QMaster> qrMasters){

        Gson gson = new Gson();
        String jsonData = gson.toJson(qrMasters);

        // SAVE NEW DATA
        editor.putString(PREF_HOME_DATA,jsonData);
        editor.commit();

    }

    // Data Manager

    public void addData(QMaster newdata){
        List<QMaster> qrMasters = getData();

        if(qrMasters == null){
            qrMasters = new ArrayList<>();
        }
        qrMasters.add(newdata);
        Collections.sort(qrMasters, new Comparator<QMaster>() {
            @Override
            public int compare(QMaster o1, QMaster o2) {
                Long ts1 = o1.created_at;
                Long ts2 = o2.created_at;
                return ts2.compareTo(ts1);
            }
        });
        saveData(qrMasters);

    }

    public void editData(int index, QMaster existdata){
        List<QMaster> qrMasters = getData();
        qrMasters.set(index,existdata);

        saveData(qrMasters);
    }

    public void removeData(int index){
        List<QMaster> qMasters = getData();
        if(qMasters != null){
            qMasters.remove(index);
            saveData(qMasters);
        }

    }

    public ArrayList<QMaster> getData(){
        Gson gson = new Gson();
        List<QMaster> qrMasters = new ArrayList<>();

        String jsonData = preferences.getString(PREF_HOME_DATA,null);
        if(jsonData!=null){
            QMaster[] qmasterItem = gson.fromJson(jsonData,QMaster[].class);

            qrMasters = Arrays.asList(qmasterItem);
            qrMasters = new ArrayList<QMaster>(qrMasters);
        }

        return (ArrayList<QMaster>) qrMasters;
    }


    // Key List Manager

    public void addKeyList(String key){
        List<String> qrkeylist = getKeyList();

        if(qrkeylist== null){
            qrkeylist = new ArrayList<>();
        }
        qrkeylist.add(key);
        saveKeyData(qrkeylist);
    }

    private void saveKeyData(List<String> qrkeylist) {
        Gson gson = new Gson();

        String jsondata = gson.toJson(qrkeylist);

        editor.putString(PREF_KEY_DATA,jsondata);
        editor.commit();
    }

    public void removeKeyData(String key){
        List<String> qrkeylist = getKeyList();
        if(qrkeylist!=null){
            qrkeylist.remove(key);
            saveKeyData(qrkeylist);
        }
    }

    public ArrayList<String> getKeyList(){
        Gson gson = new Gson();
        List<String> qrkeylist = new ArrayList<>();

        String data = preferences.getString(PREF_KEY_DATA,null);

        if(data != null ){
            String[] arrayData = gson.fromJson(data,String[].class);

            qrkeylist = Arrays.asList(arrayData);
            qrkeylist = new ArrayList<>(qrkeylist);
        }


        return (ArrayList<String>) qrkeylist;
    }

    public void delete(){
        editor.clear();
        editor.commit();
    }


}
