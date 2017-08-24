package gravicodev.qash.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by mfatihas on 8/24/2017.
 */

public class PushMessage extends FirebaseInstanceIdService {
    private static final String TAG = "PushMessage";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //Ketika ada token baru. send ke db firebase beserta nomer rekening pemilik token tersebut.

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
}
