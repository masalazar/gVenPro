package cl.invsal.istem.istem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 01-31-2017.
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
    public boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }
    public void AgregarUsuario(String Rut, String Nombre, Context context){
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Usuarios (Rut, Nombre) " +
                "VALUES (" + Rut + ", '" + Nombre +"')");

        db.close();
    }
    public Cursor BuscarUsuario(Context context){
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor miCursor = db.rawQuery("SELECT Rut,Nombre From Usuarios",null);

        return  miCursor;
    }
    public void EliminarNumero(String IdEmpresa, String IdSucursal, String IdCategoria, String Letra,String Ciclo,String Numero, Context context){
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Numeros WHERE IdEmpresa = " + IdEmpresa + " AND IdCategoria = " +
                IdCategoria + " AND Letra = '" + Letra + "' AND Ciclo + " + Ciclo + " AND Numero = " + Numero);

        db.close();
    }
    public void EliminarFecha(String Fecha, Context context){
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Numeros WHERE Fecha < " + Fecha);

        db.close();
    }
    public boolean AgregarNumero(String IdEmpresa, String NombreEmpresa,String IdSucursal, String NombreSucursal
                            , String IdCategoria,String NombreCategoria,String Letra,String Ciclo,String Numero,String Fecha,String Direccion, Context context){

        UsuariosSQLiteHelper usdbh = new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor Cursor = BuscarNumero(context,IdEmpresa,IdSucursal,IdCategoria,Letra,Ciclo,Numero);

        if (Cursor.getCount() > 0){
            MostrarAlerta(context,"Este número ya existe","Número");
            return false;
        }

        db.execSQL("INSERT INTO Numeros (IdEmpresa, NombreEmpresa, IdSucursal," +
                "            NombreSucursal,IdCategoria, NombreCategoria, Letra," +
                "            Ciclo, Numero,Fecha,Direccion) " +
                "VALUES (" + IdEmpresa + ", '" + NombreEmpresa +"'," + IdSucursal +
                      ",'" + NombreSucursal + "'," + IdCategoria + ",'" + NombreCategoria +
                      "','" + Letra + "'," + Ciclo + "," + Numero + "," + Fecha + ",'" + Direccion + "')");

        db.close();

        return true;
    }
    public Cursor BuscarNumero(Context context,String IdEmpresa,String IdSucursal,String IdCategoria,String Letra,String Ciclo,String Numero){
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "SELECT * From Numeros Where 1 = 1";

        if (!IdEmpresa.equals("0")){
            varSQL = varSQL + " AND IdEmpresa = " + IdEmpresa;
        }

        if (!IdSucursal.equals("0")){
            varSQL = varSQL + " AND IdSucursal = " + IdSucursal;
        }

        if (!IdCategoria.equals("0")){
            varSQL = varSQL + " AND IdCategoria = " + IdCategoria;
        }

        if (!Letra.equals("0")){
            varSQL = varSQL + " AND Letra = '" + Letra + "'";
        }

        if (!Ciclo.equals("0")){
            varSQL = varSQL + " AND Ciclo = " + Ciclo;
        }

        if (!Numero.equals("0")){
            varSQL = varSQL + " AND Numero = " + Numero;
        }

        Cursor miCursor = db.rawQuery(varSQL,null);

        return  miCursor;
    }
    public Cursor BuscarSoloEmpresas(Context context){
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "SELECT DISTINCT IdEmpresa, NombreEmpresa From Numeros ";

        Cursor miCursor = db.rawQuery(varSQL,null);

        return  miCursor;
    }
    public Cursor BuscarSoloSucursales(Context context,String IdEmpresa){
        UsuariosSQLiteHelper usdbh =
                new UsuariosSQLiteHelper(context, "ISTEM", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "SELECT DISTINCT IdEmpresa, NombreEmpresa, IdSucursal, NombreSucursal,Direccion From Numeros ";

        Cursor miCursor = db.rawQuery(varSQL,null);

        return  miCursor;
    }

}
