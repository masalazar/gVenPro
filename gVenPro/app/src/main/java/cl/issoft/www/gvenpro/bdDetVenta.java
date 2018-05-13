package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 04-02-2018.
 */

public class bdDetVenta {
    public long Agregar(String IdCabVenta, String IdProducto, String Cantidad,Long Valor,String Subido, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "INSERT INTO DetVenta (IdCabVenta,Identificador,IdProducto,Cantidad,Valor,Subido) " +
                "VALUES (" + IdCabVenta + ",0," + IdProducto + "," + Cantidad + "," + Valor + ",'" + Subido + "')";

        try {
            db.execSQL(varSQL);
        }catch (Exception e){
            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,e.getMessage(),"sd");
            return 0;
        }
  
        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"DetVenta"},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            index = cursor.getLong(cursor.getColumnIndex("seq"));
        }
        cursor.close();
        db.close();

        return index;
    }
    public void Eliminar(String IdDetVenta, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "DELETE FROM DetVenta WHERE IdDetVenta = " + IdDetVenta;

        try {
            db.execSQL(varSQL);
        }catch (Exception e){
            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,e.getMessage(),"sd");
        }

    }
    public void ModificarSubido(String IdDetVenta,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update DetVenta SET Subido = 'S',Identificador = " + Identificador + " WHERE IdDetVenta = " + IdDetVenta);

        db.close();
    }
    public Cursor Buscar(Context context, String IdDetVenta,String IdCabVenta,String IdProducto){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT DVE.IdDetVenta,DVE.IdProducto,DVE.IdCabVenta,DVE.Cantidad,DVE.Valor,DVE.Subido,PRO.Codigo CodPro, PRO.Nombre NomPro, CVE.IdCliente IdCliente,CVE.Identificador IdenCVE,PRO.Identificador IdenPro,FAM.IdFamilia,FAM.Nombre NomFam " +
                " FROM DetVenta DVE, CabVenta CVE,Producto PRO,Familia FAM WHERE PRO.IdProducto = DVE.IdProducto AND CVE.IdCabVenta = DVE.IdCabVenta AND FAM.IdFamilia = PRO.IdFamilia ";

        if (!IdDetVenta.equals("0")){
            varSQL = varSQL + " AND DVE.IdDetVenta = " + IdDetVenta;
        }

        if (!IdCabVenta.equals("0")){
            varSQL = varSQL + " AND DVE.IdCabVenta = " + IdCabVenta;
        }

        if (!IdProducto.equals("0")){
            varSQL = varSQL + " AND DVE.IdProducto = " + IdProducto;
        }

        varSQL = varSQL + " ORDER BY FAM.IdFamilia ";

        Cursor miCursor = db.rawQuery(varSQL,null);
        return  miCursor;

    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT DVE.Identificador,DVE.IdDetVenta,DVE.IdProducto,DVE.IdCabVenta,DVE.Cantidad,DVE.Valor,DVE.Subido,PRO.Codigo CodPro, PRO.Nombre NomPro,PRO.Identificador IdenPro, CVE.IdCliente IdCliente ,CVE.Identificador IdenCVE" +
                " FROM DetVenta DVE, CabVenta CVE,Producto PRO WHERE PRO.IdProducto = DVE.IdProducto AND CVE.IdCabVenta = DVE.IdCabVenta AND DVE.Subido = 'N'";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM DetVenta");

        db.close();
    }
}
