package gravicodev.qash.Models;

/**
 * Created by supermonster on 7/29/2017.
 */

public class User {
    public String fname;
    public String lname;
    public String accountNumber;
    public String email;
    public String userid;

    public User(){

    }

    public User(String fname, String lname, String accountNumber, String email, String userid){
        this.fname = fname;
        this.lname = lname;
        this.accountNumber = accountNumber;
        this.email = email;
        this.userid = userid;
    }


}
