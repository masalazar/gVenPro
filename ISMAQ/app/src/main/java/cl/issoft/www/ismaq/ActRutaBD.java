package cl.issoft.www.ismaq;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 08-16-2017.
 */

public class ActRutaBD {
    public void AgregarRuta(String IdRuta,String Nombre,Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Ruta (IdRuta,Nombre) " +
                "VALUES (" + IdRuta + ", '" + Nombre +"')");

        db.close();
    }
    public Cursor BuscarRuta(Context context, String IdRuta){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT IdRuta,Nombre " +
                "FROM Ruta LOC WHERE 1 = 1 ";

        if (!IdRuta.equals("0")){
            varSQL = varSQL + " AND IdRuta = " + IdRuta;
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public void EliminarRuta(Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Ruta");

        db.close();
    }
}
