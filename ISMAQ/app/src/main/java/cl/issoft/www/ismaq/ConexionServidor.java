package cl.issoft.www.ismaq;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MarceloUsuario on 07-16-2017.
 */

public class ConexionServidor extends Service {
    private Timer mTimer = null;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        this.mTimer = new Timer();
        this.mTimer.scheduleAtFixedRate(
                new TimerTask(){
                    @Override
                    public void run() {
                        ejecutarTarea();
                    }
                }
                , 0, 30000 * 60);
    }

    private void ejecutarTarea(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                Sincronizar oSin = new Sincronizar();
                oSin.Llamada = "SERVICIO";
                oSin.Ejecutar(getApplicationContext());
            }
        });
        t.start();
    }

}