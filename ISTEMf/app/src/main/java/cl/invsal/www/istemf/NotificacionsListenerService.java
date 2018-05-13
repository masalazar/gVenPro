package cl.invsal.www.istemf;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by MarceloUsuario on 03-06-2017.
 */

public class NotificacionsListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String message = data.getString("message");
        String datos = data.getString("datos");

        //..... fetch other values similarly

        sendNotification(message,title,datos);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message,String titulo, String datos) {
        Intent i = new Intent(this, ActMenu.class);
        String[] Datos = datos.split("__");

        i.putExtra("Llamada","NOTIFICACION");
        i.putExtra("Numero",Datos[0]);
        i.putExtra("Empresa",Datos[1]);
        i.putExtra("IdEmpresa",Datos[2]);
        i.putExtra("IdCategoria",Datos[3]);
        i.putExtra("Categoria",Datos[4]);
        i.putExtra("Ciclo",Datos[5]);
        i.putExtra("Letra",Datos[6]);
        i.putExtra("NumSolo",Datos[7]);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =  (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, notificationBuilder.build());

        Log.v("notification message",message);
    }


}