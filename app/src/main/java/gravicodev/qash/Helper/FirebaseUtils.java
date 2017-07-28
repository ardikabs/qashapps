package gravicodev.qash.Helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by supermonster on 7/28/2017.
 */

public class FirebaseUtils {

    public static String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public static DatabaseReference getBaseRef(){
        return FirebaseDatabase.getInstance().getReference();
    }




}
