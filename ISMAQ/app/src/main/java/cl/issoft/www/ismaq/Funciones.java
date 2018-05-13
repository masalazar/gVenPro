package cl.issoft.www.ismaq;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Spinner;

/**
 * Created by MarceloUsuario on 07-14-2017.
 */

public class Funciones {
    public  void MostrarAlerta(final Context Contexto, String Mensaje, String Titulo  ){
        AlertDialog alertDialog = new AlertDialog.Builder(Contexto).create();
        alertDialog.setTitle(Titulo);
        alertDialog.setMessage(Mensaje);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }
    public String CambiarFormatoFecha(String Fecha){
        return Fecha.substring(6,4) + "/" + Fecha.substring(6,4) + "/" + Fecha.substring(0,2);
    }
    public void SeleccionarItemConIDSpinner(Spinner spiSpinner, String Id){
        for(int i=0; i<spiSpinner.getCount(); i++) {
            if (((ObjNomyID) spiSpinner.getItemAtPosition(i)).getId() == Integer.parseInt((Id))) {
                spiSpinner.setSelection(i);
            }
        }
    }
}
