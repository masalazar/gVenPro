package cl.issoft.www.gvenpro;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by MarceloUsuario on 11-09-2017.
 */

public class ClaseCerrar {
    private Activity activity;
    InterfasCerrar mCallback;

    public interface InterfasCerrar {
        public void Cerrar(Intent Datos);
    }
    public void AsignarClase(Activity activity){
        this.activity = activity;
    }
    public void Cerrar(Intent data){
        mCallback = (InterfasCerrar) activity;
        mCallback.Cerrar(data);
    }
}
