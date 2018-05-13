package cl.issoft.www.ismaq;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 08-11-2017.
 */

public class ActRecaudacionBD {
    public void AgregarRecaudacion(String IdLocal, String Identificador, Long Monto,String Fecha,String Subido,String Estimado,String Lote, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Recaudacion (IdLocal,Identificador, Monto,Fecha,Subido,Estimado,Lote) " +
                "VALUES (" + IdLocal + ", " + Identificador +"," + Monto + "," + Fecha + ",'" + Subido + "'," + Estimado + "," + Lote + ")");

        db.close();
    }
    public void EliminarTodo(Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Recaudacion");

        db.close();
    }
    public void ModificarIdLocal(String IdLocal, String Identificador, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("UPDATE Recaudacion SET IdLocal = " + IdLocal + ",Identificador = 0 WHERE Identificador = " + Identificador);

        db.close();
    }
    public void ModificarSubido(String IdRecaudacion, String Subido, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("UPDATE Recaudacion SET Subido = '" + Subido + "' WHERE IdRecaudacion = " + IdRecaudacion);

        db.close();
    }
    public Cursor BuscarRecaudacion(Context context,String Subido,String FechaDesde, String FechaHasta,String IdRuta,String Lote){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT REC.IdRecaudacion IdRecaudacion, LOC.IdLocal IdLocal, LOC.Identificador Identificador,LOC.Codigo Codigo, LOC.Nombre Nombre, " +
                "REC.Fecha Fecha, REC.Monto Monto, REC.Subido Subido,REC.Estimado Estimado,REC.Lote Lote " +
                " FROM Recaudacion REC,(SELECT IdLocal, Identificador, Codigo, Nombre, IdRuta FROM Local UNION SELECT IdLocal, Identificador, Codigo, Nombre, IdRuta FROM LocalTemp) LOC" +
                " WHERE LOC.IdLocal = REC.IdLocal AND LOC.Identificador = REC.Identificador";

        if (!Subido.equals("-TOD")){
            varSQL = varSQL + " AND REC.Subido = '" + Subido + "'";
        }

        if (!IdRuta.equals("0")){
            varSQL = varSQL + " AND LOC.IdRuta = " + IdRuta;
        }

        if (!Lote.equals("0")){
            varSQL = varSQL + " AND REC.Lote = " + Lote;
        }

        if (!FechaDesde.equals("-TOD")){
            varSQL = varSQL + " AND REC.Fecha >= " + FechaDesde;
        }

        if (!FechaHasta.equals("-TOD")){
            varSQL = varSQL + " AND REC.Fecha <= " + FechaHasta;
        }

        varSQL = varSQL + " ORDER BY REC.Fecha, LOC.Nombre";
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
}
