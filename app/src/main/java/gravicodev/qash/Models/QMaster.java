package gravicodev.qash.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by supermonster on 7/29/2017.
 */

public class QMaster {
    public Integer balance;
    public String status = "enabled";
    public String title;
    public String SourceAccountNumber;
    public Long expired_at;
    public Long created_at;

    private String key;


    public QMaster(){

    }
    public QMaster(Integer balance, Long created_at,Long expired_at, String title, String SourceAccountNumber){
        this.balance = balance;
        this.created_at = created_at;
        this.expired_at = expired_at;
        this.title = title;
        this.SourceAccountNumber = SourceAccountNumber;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("balance",balance);
        result.put("status", status);
        result.put("expired_at",expired_at);
        result.put("title",title);
        result.put("SourceAccountNumber",SourceAccountNumber);

        return result;
    }


}
