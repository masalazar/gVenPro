package cl.issoft.www.ismaq;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 07-14-2017.
 */

public class ActNotasBD {
    public void AgregarNota(String IdLocal, String Descripcion, Context context, String Identificador){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Notas (IdLocal, Descripcion,Subido) " +
                "VALUES (" + IdLocal + ", '" + Descripcion +"','N')");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"Notas"},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            index = cursor.getLong(cursor.getColumnIndex("seq"));
        }
        cursor.close();

        if (Identificador.equals("0")){
            Identificador = index + "_," + ActMenu.Imei;
        }
        db.execSQL("UPDATE Notas Set Identificador = '" + Identificador + "' WHERE IdNota = " + index);

        db.close();
    }
    public void ModificarNota(String IdNota, String Descripcion, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("UPDATE Notas SET Descripcion = '" + Descripcion + "'," +
                   "Subido = 'N' WHERE IdNota = " + IdNota);

        db.close();
    }
    public void ModificarSubido(String IdNota, String Subido, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("UPDATE Notas SET Subido = '" + Subido + "' WHERE IdNota = " + IdNota);

        db.close();
    }
    public Cursor BuscarNota(Context context,String Subido){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT NOTA.IdNota,LOC.IdLocal,LOC.Nombre,LOC.Codigo,NOTA.Identificador,NOTA.Descripcion, NOTA.Subido " +
                 "FROM Notas NOTA,Local LOC WHERE LOC.IdLocal = NOTA.IdLocal";

        if (!Subido.equals("-TOD")){
            varSQL = varSQL + " AND NOTA.Subido = '" + Subido + "'";
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarNotaIdentificador(Context context,String Identificador){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT NOTA.IdNota,LOC.IdLocal,LOC.Nombre,LOC.Codigo,NOTA.Identificador,NOTA.Descripcion, NOTA.Subido " +
                "FROM Notas NOTA,Local LOC WHERE LOC.IdLocal = NOTA.IdLocal AND NOTA.Identificador = '" + Identificador + "'";

        Cursor miCursor = null;
        try {
            miCursor = db.rawQuery(varSQL,null);
        }catch(Exception e){
        // here you can catch all the exceptions
            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,e.getMessage(),"ERROR");
    } finally {

    }
        return  miCursor;
    }
    public void EliminarNota(String IdNota, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Notas WHERE IdNota = " + IdNota);

        db.close();
    }
}
