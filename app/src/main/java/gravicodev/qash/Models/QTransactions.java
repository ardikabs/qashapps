package gravicodev.qash.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by minimonster on 8/22/2017.
 */

public class QTransactions {

    public Integer balance;
    public Object used_at;
    public String msg;
    public String BeneficieryAccountNumber;
    public String SourceAccountNumber;

    public QTransactions(){

    }
    public QTransactions(Integer balance,Object used_at, String msg, String BeneficeryAccountNumber, String SourceAccountNumber){
        this.balance = balance;
        this.used_at = used_at;
        this.msg = msg;
        this.BeneficieryAccountNumber = BeneficeryAccountNumber;
        this.SourceAccountNumber = SourceAccountNumber;
    }




    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("balance",balance);
        result.put("used_at",used_at);
        result.put("msg",msg);
        result.put("BeneficieryAccountNumber",BeneficieryAccountNumber);

        return result;
    }

}
