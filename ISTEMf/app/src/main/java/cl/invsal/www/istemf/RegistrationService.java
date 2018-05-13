package cl.invsal.www.istemf;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by MarceloUsuario on 03-05-2017.
 */

public class RegistrationService extends IntentService {
    public RegistrationService() {
        super("RegistrationService");
    }
    private ejeWS oEjeWS;
    public static String vIdSucursal,vIdCategoria,vLetra,vNumero,vCiclo,vIdEmpresa;
    public static Context Contexto;
    private String Accion,vClave;

    @Override
    protected void onHandleIntent(Intent intent) {


        try {
            InstanceID myID = InstanceID.getInstance(this);

            String registrationToken = myID.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null


            );

            GuardarToken(registrationToken);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void GuardarToken(String Token){
        vClave = Token;
        Accion = "GUARDARTOKEN";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("GUARDARTOKEN")) {
                oEjeWS.setMetodo("Agregar");

                oEjeWS.setWS("WSNotificacion.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdSucursal", vIdSucursal);
                oEjeWS.AddParametro("IdCategoria", vIdCategoria);
                oEjeWS.AddParametro("Ciclo", vCiclo);
                oEjeWS.AddParametro("Numero", vNumero);
                oEjeWS.AddParametro("Letra", vLetra);
                oEjeWS.AddParametro("Clave", vClave);
                oEjeWS.AddParametro("IdEmpresa", vIdEmpresa);
                oEjeWS.AddParametro("IdUsuCreacion", "0");
                oEjeWS.AddParametro("IdUsuModificacion", "0");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (oEjeWS.getNumError() > 0) {
                    Funciones oFunciones = new Funciones();

                    oFunciones.MostrarAlerta(Contexto, oEjeWS.getDesError(), "E R R O R");
                    return;
                }

                if (Accion.equals("GUARDARTOKEN")) {

                }
            }
        }
    }
}