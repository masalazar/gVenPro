package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 12-21-2017.
 */

public class bdStock{
    public long Agregar(String IdSucursal, String IdProducto, String CantidadFisica,String CantidadCritica,String ValorVenta, String Subido,String ValorCompra, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Stock (IdSucursal,Identificador,IdProducto,CantidadFisica, CantidadCritica,ValorVenta,Subido,ValorCompra) " +
                "VALUES (" + IdSucursal + ",0," + IdProducto + ", " + CantidadFisica +"," + CantidadCritica + "," + ValorVenta + ",'" + Subido + "'," + ValorCompra + ")");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"Stock"},
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
    public void ModificarCantidad(String IdProducto, String IdSucursal,String Cantidad,String ValorCompra, Context context,boolean UsaValorCompra){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        if (UsaValorCompra) {
            db.execSQL("Update Stock SET Subido = 'N', CantidadFisica = CantidadFisica + " + Cantidad + ",ValorCompra = " + ValorCompra + " WHERE IdProducto = " + IdProducto + " AND IdSucursal = " + IdSucursal);
        }else{
            db.execSQL("Update Stock SET Subido = 'N',CantidadFisica = CantidadFisica + " + Cantidad + " WHERE IdProducto = " + IdProducto + " AND IdSucursal = " + IdSucursal);
        }

        db.close();
    }
    public void ModificarValor(String IdProducto, String IdSucursal,String Valor,String ValorCompra, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Stock SET Subido = 'N',ValorVenta = " + Valor + ",ValorCompra = " + ValorCompra + " WHERE IdProducto = " + IdProducto + " AND IdSucursal = " + IdSucursal);

        db.close();
    }
    public void ModificarSubido(String IdStock,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Stock SET Subido = 'S',Identificador = " + Identificador + " WHERE IdStock = " + IdStock);

        db.close();
    }
    public Cursor Buscar(Context context, String IdStock,String IdProducto,String IdSucursal){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdStock,IdSucursal,Identificador,IdProducto,CantidadFisica, CantidadCritica,ValorVenta,Subido " +
                "FROM Stock WHERE 1 = 1 ";

        if (!IdStock.equals("0")){
            varSQL = varSQL + " AND IdStock = " + IdStock;
        }

        if (!IdProducto.equals("0")){
            varSQL = varSQL + " AND IdProducto = " + IdProducto;
        }

        if (!IdSucursal.equals("0")){
            varSQL = varSQL + " AND IdSucursal = " + IdSucursal;
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarVenta(Context context, String IdProducto,String Codigo,String Nombre,String IdFamilia,String Vigencia){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT STO.CantidadFisica Cantidad,STO.ValorVenta ValorVenta,PRO.Nombre Nombre,PRO.Codigo Codigo, PRO.IdProducto, STO.ValorCompra ValorCompra,PRO.Vigencia,PRO.IdFamilia,PRO.Imagen,FAM.Nombre NomFam " +
                 "FROM Stock STO, Producto PRO,Familia FAM WHERE STO.IdProducto = PRO.IdProducto AND FAM.IdFamilia = PRO.IdFamilia AND STO.IdSucursal = " + Funciones.gIdSucursal;

        if (!IdProducto.equals("0")){
            varSQL = varSQL + " AND PRO.IdProducto = " + IdProducto;
        }

        if (!IdFamilia.equals("0")){
            varSQL = varSQL + " AND PRO.IdFamilia = " + IdFamilia;
        }

        if (!Codigo.equals("")){
            varSQL = varSQL + " AND PRO.Codigo = '" + Codigo + "'";
        }

        if (!Vigencia.equals("")){
            varSQL = varSQL + " AND PRO.Vigencia = '" + Vigencia + "'";
        }

        if (!Nombre.equals("")){
            varSQL = varSQL + " AND PRO.Nombre LIKE '" + Nombre + "%'";
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT STO.IdStock,STO.IdSucursal,STO.Identificador,STO.IdProducto,STO.CantidadFisica, STO.CantidadCritica,STO.ValorVenta,STO.Subido,PROD.Identificador IdenPro,STO.ValorCompra " +
                "FROM Stock STO, Producto PROD WHERE PROD.IdProducto = STO.IdProducto AND STO.Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Stock");

        db.close();
    }
}
