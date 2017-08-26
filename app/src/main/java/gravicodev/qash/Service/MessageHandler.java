package gravicodev.qash.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import gravicodev.qash.Activity.MainActivity;
import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Helper.VolleyCallback;
import gravicodev.qash.Models.User;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;
import gravicodev.qash.Volley.VolleyHelper;

public class MessageHandler extends FirebaseMessagingService {
    private static final String TAG = "MessageHandler";
    public MessageHandler() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        retrievingSaldo(remoteMessage);
        showNotification(remoteMessage);

    }

    private void retrievingSaldo(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String accnum = data.get("AccountNumber");
        VolleyHelper vh = new VolleyHelper();
        try {
            vh.getSaldo(new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    FirebaseUtils.getBaseRef().child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("balance")
                            .setValue(Integer.parseInt(result.split("\\.")[0]));

                }
            },accnum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(RemoteMessage remoteMessage){
        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();

        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("index",3);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap notifyImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_qash);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_qash)
                .setLargeIcon(notifyImage)
                .setColor(Color.parseColor("#00437D"))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
