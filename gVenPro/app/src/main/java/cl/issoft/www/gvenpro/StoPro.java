package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 12-22-2017.
 */

public class StoPro {
    public long Agregar(String IdStock, String IdProveedor, String CantidadFisica,String ValorCompra, String Subido, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO StoPro (IdProveedor,Identificador,IdStock,CantidadFisica, ValorCompra,Subido) " +
                "VALUES (" + IdProveedor + ",0," + IdStock  + ", " + CantidadFisica +"," + ValorCompra + ",'" + Subido + "')");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"StoPro"},
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
    public String obtIdStock(Context context,String IdProducto, String IdSucursal){
        bdBase usdbh =
                new bdBase(context, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        bdStock oStock = new bdStock();
        Cursor c = oStock.Buscar(context,"0",IdProducto,IdSucursal);

        String IdStock = "0";

        if (c.moveToFirst()) {
            IdStock = c.getString(c.getColumnIndex("Nombre"));
        }

        return IdStock;
    }
    public void ModificarCantidad(String IdProveedor,String IdProducto, String IdSucursal,String Cantidad, Context context){
        String IdStock = obtIdStock(context,IdProducto,IdSucursal);

        bdBase usdbh =
                new bdBase(context, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        db.execSQL("Update StoPro SET Cantidad = Cantidad + " + Cantidad + "' WHERE IdStock = " + IdStock + " AND IdProveedor = " + IdProveedor);

        db.close();
    }
    public void ModificarSubido(String IdStoPro, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update StoPro SET Subido = 'S' WHERE IdStoPro = " + IdStoPro);

        db.close();
    }
    public Cursor Buscar(Context context, String IdStoPro,String IdProveedor, String IdProducto,String IdSucursal){
        bdBase usdbh =
                new bdBase(context, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT PRO.Codigo CodPro, PRO.Nombre NomPro, STO.IdProducto, STO.IdSucursal, SPR.IdStoPro,SPR.IdProveedor,SPR.Identificador,SPR.IdStock,SPR.CantidadFisica, SPR.ValorCompra,SPR.Subido " +
                "FROM StoPro SPR, Stock STO, Producto PRO WHERE SPR.IdStock = STO.IdStock AND PRO.IdProducto = STO.IdProducto";

        if (!IdStoPro.equals("0")){
            varSQL = varSQL + " AND SPR.IdStoPro = " + IdStoPro;
        }

        if (!IdProveedor.equals("0")){
            varSQL = varSQL + " AND SPR.IdProveedor = " + IdProveedor;
        }

        if (!IdProducto.equals("0")){
            varSQL = varSQL + " AND STO.IdProducto = " + IdProducto;
        }

        if (!IdSucursal.equals("0")){
            varSQL = varSQL + " AND STO.IdSucursal = " + IdSucursal;
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdStoPro,Rut,Nombre,Direccion,Vendedor,Identificador,Subido" +
                "FROM StoPro WHERE Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM StoPro");

        db.close();
    }
}

