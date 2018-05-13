package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 04-01-2018.
 */

public class bdComuna  {
    public long Agregar(String Nombre,String Subido,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Comuna(Identificador,Nombre,Subido) " +
                "VALUES (" + Identificador + ",'" + Nombre + "','" + Subido + "')");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"Comuna"},
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
    public Cursor Buscar(Context context, String IdComuna,String Nombre){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdComuna,Nombre " +
                "FROM Comuna WHERE 1 = 1 ";

        if (!IdComuna.equals("0")){
            varSQL = varSQL + " AND IdComuna = " + IdComuna;
        }
        if (!Nombre.equals("")){
            varSQL = varSQL + " AND Nombre LIKE '" + Nombre + "%'";
        }

        Cursor miCursor = db.rawQuery(varSQL,null);
        return  miCursor;
    }

    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Comuna");

        db.close();
    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdComuna,Nombre,Identificador " +
                "FROM Comuna WHERE Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void ModificarSubido(String IdComuna,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Comuna SET Subido = 'S',Identificador = " + Identificador + " WHERE IdComuna = " + IdComuna);

        db.close();
    }
}
