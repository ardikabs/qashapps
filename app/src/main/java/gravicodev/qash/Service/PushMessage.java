package gravicodev.qash.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Session.SessionManager;

/**
 * Created by mfatihas on 8/24/2017.
 */

public class PushMessage extends FirebaseInstanceIdService {
    private static final String TAG = "PushMessage";
    @Override
    public void onTokenRefresh() {
        FirebaseUtils.getBaseRef().child("userDevice")
                .child(new SessionManager(this).getUser().accountNumber)
                .child(new SessionManager(this).getDeviceId()).removeValue();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseUtils.getBaseRef().child("userDevice")
                .child(new SessionManager(this).getUser().accountNumber)
                .child(refreshedToken).setValue(true);
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //Ketika ada token baru. send ke db firebase beserta nomer rekening pemilik token tersebut.

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
}
