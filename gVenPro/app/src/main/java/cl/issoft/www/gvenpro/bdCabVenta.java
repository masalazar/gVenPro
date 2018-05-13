package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 04-02-2018.
 */

public class bdCabVenta {
    public long Agregar(String IdCliente, String Fecha,String Cantidad,Long ValorNeto,String Pendiente,String Pagada,String Facturada,Long Iva,Long ValorTotal,String Identificador,String Subido,Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "INSERT INTO CabVenta (Identificador,IdCliente,Fecha,Cantidad,ValorNeto,Pendiente,Pagada,Facturada,Anulada,MotivoAnulada,Iva,ValorTotal,Subido) " +
                "VALUES (" + Identificador + "," + IdCliente + "," + Fecha + "," + Cantidad + "," + ValorNeto + ",'" + Pendiente + "','" + Pagada + "','" + Facturada + "','N','Sin Anulacion'," + Iva + "," + ValorTotal + ",'" + Subido + "')";

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
                new String[]{"CabVenta"},
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
    public void Modificar(String IdCabVenta, String Cantidad,Long ValorNeto,Long Iva,Long ValorTotal,Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "UPDATE CabVenta SET Subido = 'N',Cantidad = " + Cantidad + ",ValorNeto = " + ValorNeto + ",Iva = " + Iva + ",ValorTotal = " + ValorTotal + " WHERE IdCabVenta = " + IdCabVenta;

        try {
            db.execSQL(varSQL);
        }catch (Exception e){
            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,e.getMessage(),"sd");
        }

    }
    public void Anular(String IdCabVenta, String MotivoAnulada, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabVenta SET Subido = 'N',Anulada = 'S', MotivoAnulada = '" + MotivoAnulada + "' WHERE IdCabVenta = " + IdCabVenta);

        db.close();
    }
    public void Pagar(String IdCabVenta, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabVenta SET Subido = 'N',Pagada = 'S' WHERE IdCabVenta = " + IdCabVenta);

        db.close();
    }
    public void Facturar(String IdCabVenta, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabVenta SET Subido = 'N',Facturada = 'S' WHERE IdCabVenta = " + IdCabVenta);

        db.close();
    }
    public void Entregar(String IdCabVenta, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabVenta SET Subido = 'N',Pendiente = 'N' WHERE IdCabVenta = " + IdCabVenta);

        db.close();
    }
    public void ModificarSubido(String IdCabVenta,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabVenta SET Subido = 'S',Identificador = " + Identificador + " WHERE IdCabVenta = " + IdCabVenta);

        db.close();
    }
    public Cursor Buscar(Context context, String IdCabVenta,String IdCliente,String FechaDesde,String FechaHasta,String Pendiente,String Pagada, String Facturada,String Anulada){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT CVE.IdCabVenta,CVE.IdCliente,CVE.Fecha,CVE.Subido,CVE.Cantidad,CVE.Pendiente,CVE.ValorNeto,CVE.Pagada,CVE.Facturada,CVE.Anulada,CVE.MotivoAnulada,CVE.Iva,CVE.ValorTotal,CVE.Subido,CLI.Rut RutCli, CLI.Nombre NomCli,CLI.Identificador IdenCli " +
                "FROM CabVenta CVE, Cliente CLI WHERE CLI.IdCliente = CVE.IdCliente ";

        if (!IdCabVenta.equals("0")){
            varSQL = varSQL + " AND CVE.IdCabVenta = " + IdCabVenta;
        }

        if (!IdCliente.equals("0")){
            varSQL = varSQL + " AND CVE.IdCliente = " + IdCliente;
        }

        if (!FechaDesde.equals("0")){
            varSQL = varSQL + " AND CVE.Fecha >= " + FechaDesde;
        }

        if (!FechaHasta.equals("0")){
            varSQL = varSQL + " AND CVE.Fecha <= " + FechaHasta;
        }

        if (!Pendiente.equals("T")){
            varSQL = varSQL + " AND CVE.Pendiente = '" + Pendiente + "'";
        }

        if (!Pagada.equals("T")){
            varSQL = varSQL + " AND CVE.Pagada = '" + Pagada + "'";
        }

        if (!Facturada.equals("T")){
            varSQL = varSQL + " AND CVE.Facturada = '" + Facturada + "'";
        }

        if (!Anulada.equals("T")){
            varSQL = varSQL + " AND CVE.Anulada = '" + Anulada + "'";
        }

        try {
            Cursor miCursor = db.rawQuery(varSQL,null);
            return  miCursor;
        }catch (Exception e){
            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,e.getMessage(),"sd");
        }
        return  null;
    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT CVE.Identificador,CVE.IdCabVenta,CVE.IdCliente,CVE.Fecha,CVE.Subido,CVE.Cantidad,CVE.ValorNeto,CVE.Pendiente,CVE.Pagada,CVE.Facturada,CVE.Anulada,CVE.MotivoAnulada,CVE.Iva,CVE.ValorTotal,CVE.Subido,CLI.Rut RutCli, CLI.Nombre NomCli,CLI.Identificador IdenCli,CVE.Descuento " +
                "FROM CabVenta CVE, Cliente CLI WHERE CLI.IdCliente = CVE.IdCliente AND CVE.Subido = 'N'";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarUltimo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT CVE.IdCabVenta,CVE.IdCliente,CVE.Fecha,CVE.Subido,CVE.Cantidad,CVE.ValorNeto,CVE.Pendiente,CVE.Pagada,CVE.Facturada,CVE.Anulada,CVE.MotivoAnulada,CVE.Iva,CVE.ValorTotal,CVE.Subido,CLI.Rut RutCli, CLI.Nombre NomCli " +
                "FROM CabVenta CVE, Cliente CLI WHERE CLI.IdCliente = CVE.IdCliente ORDER BY IdCabVenta DESC LIMIT 1";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM CabVenta");

        db.close();
    }
}
