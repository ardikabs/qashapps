package gravicodev.qash.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supermonster on 7/29/2017.
 */

public class QHistory {
    public Integer balance;
    public Long used_at;
    public String title;
    public String msg;
    public String BeneficieryAccountNumber;

    public QHistory(){

    }
    public QHistory(Integer balance, Long used_at, String title, String msg, String BeneficeryAccountNumber){
        this.balance = balance;
        this.used_at = used_at;
        this.title = title;
        this.msg = msg;
        this.BeneficieryAccountNumber = BeneficeryAccountNumber;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("balance",balance);
        result.put("used_at",used_at);
        result.put("title",title);
        result.put("msg",msg);
        result.put("BeneficieryAccountNumber",BeneficieryAccountNumber);

        return result;
    }
}
