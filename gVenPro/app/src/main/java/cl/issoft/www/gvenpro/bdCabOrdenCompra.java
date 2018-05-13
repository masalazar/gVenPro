package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 11-09-2017.
 */

public class bdCabOrdenCompra {
    public long Agregar(String IdProveedor, String Fecha,String FechaEstimadaCompra,String Cantidad,Long ValorNeto,String Pagada,String Facturada,String Solicitada,Long Iva,Long ValorTotal,String Recibida,String Identificador,String Subido, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "INSERT INTO CabOrdenCompra (Identificador,IdProveedor,Fecha,FechaEstimadaCompra,Cantidad,ValorNeto,Pagada,Facturada,Solicitada,Anulada,MotivoAnulada,Iva,ValorTotal,Subido,Recibida) " +
                "VALUES (" + Identificador + "," + IdProveedor + "," + Fecha + "," + FechaEstimadaCompra + "," + Cantidad + "," + ValorNeto + ",'" + Pagada + "','" + Facturada + "','" + Solicitada + "','N','Sin Anulacion'," + Iva + "," + ValorTotal + ",'" + Subido + "','" + Recibida + "')";

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
                new String[]{"CabOrdenCompra"},
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

    public void Anular(String IdCabOrdenCompra, String MotivoAnulada, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabOrdenCompra SET Subido = 'N',Anulada = 'S', MotivoAnulada = '" + MotivoAnulada + "' WHERE IdCabOrdenCompra = " + IdCabOrdenCompra);

        db.close();
    }
    public void Pagar(String IdCabOrdenCompra, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabOrdenCompra SET Subido = 'N',Pagada = 'S' WHERE IdCabOrdenCompra = " + IdCabOrdenCompra);

        db.close();
    }
    public void Recibir(String IdCabOrdenCompra, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null,Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabOrdenCompra SET Subido = 'N',Recibida = 'S' WHERE IdCabOrdenCompra = " + IdCabOrdenCompra);

        RecibirDetalle(IdCabOrdenCompra,context);
        db.close();
    }
    private void RecibirDetalle(String IdCabOrdenCompra,Context context){
        bdDetOrdenCompra oDetOrdenCompra = new bdDetOrdenCompra();

        Cursor c = oDetOrdenCompra.Buscar(context,"0",IdCabOrdenCompra,"0");

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String IdProducto = c.getString(c.getColumnIndex("IdProducto"));
                String IdProveedor = c.getString(c.getColumnIndex("IdProveedor"));
                String Cantidad = c.getString(c.getColumnIndex("Cantidad"));
                String Valor = c.getString(c.getColumnIndex("Valor"));

                bdStoPro oStoPro = new bdStoPro();

                String IdStock = oStoPro.obtIdStock(context,IdProducto,Funciones.gIdSucursal,Valor);
                oStoPro.Agregar(IdStock,IdProveedor,Cantidad,String.valueOf(Valor),"N",context);

                bdStock oStock = new bdStock();

                oStock.ModificarCantidad(IdProducto,Funciones.gIdSucursal,Cantidad,Valor,context,true);
            } while(c.moveToNext());
        }



    }
    public void Solicitar(String IdCabOrdenCompra, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabOrdenCompra SET Subido = 'N',Solicitada = 'S' WHERE IdCabOrdenCompra = " + IdCabOrdenCompra);

        db.close();
    }
    public void Facturar(String IdCabOrdenCompra, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabOrdenCompra SET Subido = 'N',Facturada = 'S' WHERE IdCabOrdenCompra = " + IdCabOrdenCompra);

        db.close();
    }
    public void ModificarSubido(String IdCabOrdenCompra,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update CabOrdenCompra SET Subido = 'S',Identificador = " + Identificador + " WHERE IdCabOrdenCompra = " + IdCabOrdenCompra);

        db.close();
    }
            public Cursor Buscar(Context context, String IdCabOrdenCompra,String IdProveedor,String FechaDesde,String FechaHasta,String FechaEstimadaCompraDesde,String FechaEstimadaCompraHasta,String Pagada, String Facturada,String Solicitada, String Anulada,String Recibida){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT OCO.IdCabOrdenCompra,OCO.IdProveedor,OCO.Fecha,OCO.FechaEstimadaCompra,OCO.Subido,OCO.Cantidad,OCO.ValorNeto,OCO.Pagada,OCO.Facturada,OCO.Solicitada,OCO.Recibida,OCO.Anulada,OCO.MotivoAnulada,OCO.Iva,OCO.ValorTotal,OCO.Subido,PRO.Rut RutPro, PRO.Nombre NomPro,PRO.Identificador IdenPro " +
                "FROM CabOrdenCompra OCO, Proveedor PRO WHERE PRO.IdProveedor = OCO.IdProveedor ";

        if (!IdCabOrdenCompra.equals("0")){
            varSQL = varSQL + " AND OCO.IdCabOrdenCompra = " + IdCabOrdenCompra;
        }

        if (!IdProveedor.equals("0")){
            varSQL = varSQL + " AND OCO.IdProveedor = " + IdProveedor;
        }

        if (!FechaDesde.equals("0")){
            varSQL = varSQL + " AND OCO.Fecha >= " + FechaDesde;
        }

        if (!FechaHasta.equals("0")){
            varSQL = varSQL + " AND OCO.Fecha <= " + FechaHasta;
        }

        if (!FechaEstimadaCompraDesde.equals("0")){
            varSQL = varSQL + " AND OCO.FechaEstimadaCompra >= " + FechaEstimadaCompraDesde;
        }

        if (!FechaEstimadaCompraHasta.equals("0")){
            varSQL = varSQL + " AND OCO.FechaEstimadaCompra <= " + FechaEstimadaCompraHasta;
        }

        if (!Pagada.equals("T")){
            varSQL = varSQL + " AND OCO.Pagada = '" + Pagada + "'";
        }

        if (!Facturada.equals("T")){
            varSQL = varSQL + " AND OCO.Facturada = '" + Facturada + "'";
        }

        if (!Recibida.equals("T")){
            varSQL = varSQL + " AND OCO.Recibida = '" + Recibida + "'";
        }

        if (!Solicitada.equals("T")){
            varSQL = varSQL + " AND OCO.Solicitada = '" + Solicitada + "'";
        }

        if (!Anulada.equals("T")){
            varSQL = varSQL + " AND OCO.Anulada = '" + Anulada + "'";
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

        varSQL = "SELECT OCO.Identificador, OCO.IdCabOrdenCompra,OCO.IdProveedor,OCO.Fecha,OCO.FechaEstimadaCompra,OCO.Subido,OCO.Cantidad,OCO.ValorNeto,OCO.Pagada,OCO.Facturada,OCO.Solicitada,OCO.Anulada,OCO.MotivoAnulada,OCO.Iva,OCO.ValorTotal,OCO.Subido,PRO.Rut RutPro,OCO.Recibida,OCO.Descuento, PRO.Nombre NomPro, PRO.Identificador IdenPro " +
                "FROM CabOrdenCompra OCO,Proveedor PRO WHERE PRO.IdProveedor = OCO.IdProveedor AND OCO.Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM CabOrdenCompra");

        db.close();
    }
}
