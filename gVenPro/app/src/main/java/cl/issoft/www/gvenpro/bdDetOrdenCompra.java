package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 11-09-2017.
 */

public class bdDetOrdenCompra {
    public long Agregar(String IdCabOrdenCompra, String IdProducto, String Cantidad,Long Valor,String Subido, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "INSERT INTO DetOrdenCompra (IdCabOrdenCompra,Identificador,IdProducto,Cantidad,Valor,Subido) " +
                "VALUES (" + IdCabOrdenCompra + ",0," + IdProducto + "," + Cantidad + "," + Valor + ",'" + Subido + "')";

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
                new String[]{"DetOrdenCompra"},
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
    public void ModificarSubido(String IdDetOrdenCompra,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update DetOrdenCompra SET Subido = 'S',Identificador = " + Identificador + " WHERE IdDetOrdenCompra = " + IdDetOrdenCompra);

        db.close();
    }
    public Cursor Buscar(Context context, String IdDetOrdenCompra,String IdCabOrdenCompra,String IdProducto){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT DOCO.IdDetOrdenCompra,DOCO.IdProducto,DOCO.IdCabOrdenCompra,DOCO.Cantidad,DOCO.Valor,DOCO.Subido,PRO.Codigo CodPro, PRO.Nombre NomPro, COC.IdProveedor IdProveedor, COC.Identificador IdenCOC,PRO.Identificador IdenPro " +
                " FROM DetOrdenCompra DOCO, CabOrdenCompra COC,Producto PRO WHERE PRO.IdProducto = DOCO.IdProducto AND COC.IdCabOrdenCompra = DOCO.IdCabOrdenCompra ";

        if (!IdDetOrdenCompra.equals("0")){
            varSQL = varSQL + " AND DOCO.IdDetOrdenCompra = " + IdDetOrdenCompra;
        }

        if (!IdCabOrdenCompra.equals("0")){
            varSQL = varSQL + " AND DOCO.IdCabOrdenCompra = " + IdCabOrdenCompra;
        }

        if (!IdProducto.equals("0")){
            varSQL = varSQL + " AND DOCO.IdProducto = " + IdProducto;
        }

        Cursor miCursor = db.rawQuery(varSQL,null);
        return  miCursor;

    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT DOCO.Identificador,DOCO.IdDetOrdenCompra,DOCO.IdProducto,DOCO.IdCabOrdenCompra,DOCO.Cantidad,DOCO.Valor,DOCO.Subido,PRO.Codigo CodPro, PRO.Nombre NomPro, COC.Identificador IdenCOC,PRO.Identificador IdenPro " +
                "FROM DetOrdenCompra DOCO,Producto PRO,CabOrdenCompra COC WHERE COC.IdCabOrdenCompra = DOCO.IdCabOrdenCompra AND PRO.IdProducto = DOCO.IdProducto AND DOCO.Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM DetOrdenCompra");

        db.close();
    }
}
