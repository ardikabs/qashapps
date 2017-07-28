package gravicodev.qash.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supermonster on 7/29/2017.
 */

public class QMaster {
    public Integer balance;
    public String status;
    public Long expired_at;
    public String title;
    public String SourceAccountNumber;

    public QMaster(){

    }
    public QMaster(Integer balance, String status, Long expired_at, String title, String SourceAccountNumber){
        this.balance = balance;
        this.status = status;
        this.expired_at = expired_at;
        this.title = title;
        this.SourceAccountNumber = SourceAccountNumber;
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
