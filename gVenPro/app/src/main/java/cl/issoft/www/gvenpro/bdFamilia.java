package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 04-22-2018.
 */

public class bdFamilia {
    public long Agregar(String Nombre,String Subido,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Familia(Identificador,Nombre,Subido) " +
                "VALUES (" + Identificador + ",'" + Nombre + "','" + Subido + "')");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"Familia"},
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
    public Cursor Buscar(Context context, String IdFamilia,String Nombre){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdFamilia,Nombre " +
                "FROM Familia WHERE 1 = 1 ";

        if (!IdFamilia.equals("0")){
            varSQL = varSQL + " AND IdFamilia = " + IdFamilia;
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

        db.execSQL("DELETE FROM Familia");

        db.close();
    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdFamilia,Nombre,Identificador " +
                "FROM Familia WHERE Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void ModificarSubido(String IdFamilia,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Familia SET Subido = 'S',Identificador = " + Identificador + " WHERE IdFamilia = " + IdFamilia);

        db.close();
    }
}
