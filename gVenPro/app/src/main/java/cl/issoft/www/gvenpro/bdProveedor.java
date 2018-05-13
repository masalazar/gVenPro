package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 11-02-2017.
 */

public class bdProveedor {
    public long Agregar(String Rut, String Nombre, String Direccion,String Vendedor, String Subido,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Proveedor (Rut,Identificador,Nombre,Direccion, Vendedor,Subido) " +
                "VALUES ('" + Rut + "'," + Identificador + ",'" + Nombre + "', '" + Direccion +"','" + Vendedor + "','" + Subido + "')");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"Proveedor"},
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
    public void Modificar(String IdProveedor, String Nombre,String Direccion, String Vendedor, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Proveedor SET Subido = 'N',Nombre = '" + Nombre + "',Vendedor = '" + Vendedor + "',Direccion = '" + Direccion + "' WHERE IdProveedor = " + IdProveedor);

        db.close();
    }
    public void ModificarSubido(String IdProveedor,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Proveedor SET Subido = 'S',Identificador = " + Identificador + " WHERE IdProveedor = " + IdProveedor);

        db.close();
    }
    public Cursor Buscar(Context context, String IdProveedor,String Rut,String Nombre){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdProveedor,Rut,Nombre,Direccion,Vendedor,Identificador,Subido " +
                "FROM Proveedor WHERE 1 = 1 ";

        if (!IdProveedor.equals("0")){
            varSQL = varSQL + " AND IdProveedor = " + IdProveedor;
        }
        if (!Rut.equals("")){
            varSQL = varSQL + " AND Rut = '" + Rut + "'";
        }

        if (!Nombre.equals("")){
            varSQL = varSQL + " AND Nombre LIKE '" + Nombre + "%'";
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdProveedor,Rut,Nombre,Direccion,Vendedor,Identificador,Subido " +
                "FROM Proveedor WHERE Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Proveedor");

        db.close();
    }
}
