package cl.issoft.www.gvenpro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by MarceloUsuario on 11-02-2017.
 */

public class Funciones {
    public static String gIdSucursal = "1";
    public static int gVersionBD = 1;
    public static String gIdUsuario="1";
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
    public LinkedList linComuna(boolean AgregaSeleccione,String IdComuna,String Nombre,Context contexto){
        bdComuna oComuna = new bdComuna();
        Cursor cComuna = oComuna.Buscar(contexto,IdComuna,Nombre);
        LinkedList Comunas = new LinkedList();

        if (AgregaSeleccione){
            Comunas.add((new ObjNomyID(0,"<<SELECCIONE>>")));
        }

        if (cComuna.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String lNombre = cComuna.getString(cComuna.getColumnIndex("Nombre"));
                String lIdComuna = cComuna.getString(cComuna.getColumnIndex("IdComuna"));

                Comunas.add(new ObjNomyID(Integer.parseInt(lIdComuna),lNombre));

            } while (cComuna.moveToNext());
        }

        return Comunas;
    }
    public LinkedList linFamilia(boolean AgregaSeleccione,String IdFamilia,String Nombre,Context contexto){
        bdFamilia oFamilia = new bdFamilia();
        Cursor cFamilia = oFamilia.Buscar(contexto,IdFamilia,Nombre);
        LinkedList Familias = new LinkedList();

        if (AgregaSeleccione){
            Familias.add((new ObjNomyID(0,"<<SELECCIONE>>")));
        }

        if (cFamilia.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String lNombre = cFamilia.getString(cFamilia.getColumnIndex("Nombre"));
                String lIdFamilia = cFamilia.getString(cFamilia.getColumnIndex("IdFamilia"));

                Familias.add(new ObjNomyID(Integer.parseInt(lIdFamilia),lNombre));

            } while (cFamilia.moveToNext());
        }

        return Familias;
    }
    public String obtIdTabla(String Tabla,String CampoConsultar,String Valor,String CampoDevuelto, Context contexto){
        bdBase usdbh =
                new bdBase(contexto, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "SELECT " + CampoDevuelto + " FROM " + Tabla + " WHERE " + CampoConsultar + " = " + Valor;
        Cursor  c = db.rawQuery(varSQL,null);

        if (c.moveToFirst()) {
                return c.getString(c.getColumnIndex(CampoDevuelto));
        }

        return "0";
    }
    public static Date sumarDiasAFecha(Date fecha, int dias){
        if (dias==0) return fecha;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

}
