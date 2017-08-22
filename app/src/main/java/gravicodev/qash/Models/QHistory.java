package gravicodev.qash.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supermonster on 7/29/2017.
 */

public class QHistory {
    public Integer balance;
    public Object used_at;
    public String title;
    public String msg;
    private String key;

    public QHistory(){

    }
    public QHistory(Integer balance, String title, String msg){
        this.balance = balance;
        this.used_at = ServerValue.TIMESTAMP;
        this.title = title;
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("balance",balance);
        result.put("used_at",used_at);
        result.put("title",title);
        result.put("msg",msg);

        return result;
    }

}
