package gravicodev.qash.Models;

/**
 * Created by supermonster on 7/29/2017.
 */

public class User {
    public String fullname;
    public String accountNumber;
    public String email;
    private String userid;
    private int balance;

    public User(){

    }

    public User(String fullname, String accountNumber, String email){
        this.fullname = fullname;
        this.accountNumber = accountNumber;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
