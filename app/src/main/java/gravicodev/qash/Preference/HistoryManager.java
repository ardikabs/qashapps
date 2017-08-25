package gravicodev.qash.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gravicodev.qash.Models.QHistory;

/**
 * Created by supermonster on 8/24/2017.
 */

public class HistoryManager {

    private final String PREF_NAME;
    private final String PREF_HISTORY_DATA;
    private final String PREF_KEY_DATA;


    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public HistoryManager(Context context){
        this.context = context;

        PREF_NAME = "HistoryManager";

        PREF_HISTORY_DATA = "DATA";
        PREF_KEY_DATA = "KEY";

        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private void saveData(List<QHistory> qrHistory){

        Gson gson = new Gson();
        String jsonData = gson.toJson(qrHistory);

        // SAVE NEW DATA
        editor.putString(PREF_HISTORY_DATA,jsonData);
        editor.commit();

    }

    // Data Manager

    public void addData(QHistory newdata){
        List<QHistory> qrHistory = getData();

        if(qrHistory == null){
            qrHistory = new ArrayList<>();
        }
        qrHistory.add(newdata);
        saveData(qrHistory);

    }

    public void editData(int index, QHistory existdata){
        List<QHistory> qrHistory = getData();
        qrHistory.set(index,existdata);

        saveData(qrHistory);
    }

    public void removeData(int index){
        List<QHistory> qrHistory = getData();
        if(qrHistory != null){
            qrHistory.remove(index);
            saveData(qrHistory);
        }

    }

    public ArrayList<QHistory> getData(){
        Gson gson = new Gson();
        List<QHistory> qrMasters = new ArrayList<>();

        String jsonData = preferences.getString(PREF_HISTORY_DATA,null);
        if(jsonData!=null){
            QHistory[] qmasterItem = gson.fromJson(jsonData,QHistory[].class);

            qrMasters = Arrays.asList(qmasterItem);
            qrMasters = new ArrayList<QHistory>(qrMasters);
        }

        return (ArrayList<QHistory>) qrMasters;
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

    public void removeKeyData(int index){
        List<String> qrkeylist = getKeyList();
        if(qrkeylist!=null){
            qrkeylist.remove(index);
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
