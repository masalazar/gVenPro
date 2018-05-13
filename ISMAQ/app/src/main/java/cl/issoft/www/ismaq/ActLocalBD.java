package cl.issoft.www.ismaq;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;

/**
 * Created by MarceloUsuario on 07-14-2017.
 */

public class ActLocalBD {
    public void AgregarLocal(String IdLocal,String Codigo, String Nombre,String Subido,String IdRuta,String Encargado, String Direccion,String Estimado, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Local (IdLocal,Identificador,Codigo, Nombre,Subido,IdRuta,Encargado,Direccion,Estimado) " +
                "VALUES (" + IdLocal + ",0,'" + Codigo + "', '" + Nombre +"','" + Subido + "'," + IdRuta + ",'" + Encargado + "','" + Direccion + "'," + Estimado + ")");

        db.close();
    }
    public void ModificarLocal(String IdLocal, String Nombre,String IdRuta, String Encargado,String Direccion,String Estimado, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Local SET Subido = 'N',Nombre = '" + Nombre + "',Encargado = '" + Encargado + "',Direccion = '" + Direccion + "',Estimado = " + Estimado + ",IdRuta = " + IdRuta + " WHERE IdLocal = " + IdLocal);

        db.close();
    }
    public void ModificarSubido(String IdLocal, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Local SET Subido = 'S' WHERE IdLocal = " + IdLocal);

        db.close();
    }
    public void ModificarLocalTem(String Identificador, String Nombre,String IdRuta,String Encargado, String Direccion,String Estimado, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update LocalTemp SET Nombre = '" + Nombre + "',IdRuta = " + IdRuta + ",Encargado = '" + Encargado + "',Direccion = '" + Direccion + "',Estimado = " + Estimado + " WHERE Identificador = " + Identificador);

        db.close();
    }
    public Long AgregarLocalTemp(String Codigo, String Nombre,String Direccion, String Encargado,String IdRuta,String Estimado, Context context){
        Funciones oFunciones=new Funciones();
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor cLocal = db.rawQuery("Select * From Local WHERE Codigo = '" + Codigo + "'",null);

        if (cLocal.getCount() > 0){
            oFunciones.MostrarAlerta(context, "Este local ya existe","Guardar");
            return Long.parseLong("0");
        }

        Cursor cLocalTemp = db.rawQuery("Select * From LocalTemp WHERE Codigo = '" + Codigo + "'",null);

        if (cLocalTemp.getCount() > 0){
            oFunciones.MostrarAlerta(context, "Este local ya existe","Guardar");
            return Long.parseLong("0");
        }

        db.execSQL("INSERT INTO LocalTemp (IdLocal,Codigo, Nombre,Direccion,Encargado,IdRuta,Estimado) " +
                "VALUES (" + "0" + ",'" + Codigo + "', '" + Nombre +"','" + Direccion + "','" + Encargado + "'," + IdRuta + ","+ Estimado + ")");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"LocalTemp"},
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
    public Cursor BuscarLocal(Context context, String IdLocal){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT LOC.IdLocal,LOC.Nombre,LOC.Codigo,LOC.IdRuta,LOC.Encargado,LOC.Direccion,LOC.Estimado " +
                "FROM Local LOC WHERE 1 = 1 ";

        if (!IdLocal.equals("0")){
            varSQL = varSQL + " AND IdLocal = " + IdLocal;
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarLocalNoSubido(Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT LOC.IdLocal,LOC.Nombre,LOC.Codigo,LOC.IdRuta,LOC.Estimado,LOC.Encargado,LOC.Direccion " +
                "FROM Local LOC WHERE Subido = 'N' ";

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarLocalTemp(Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT LOC.IdLocal,LOC.Nombre,LOC.Codigo,LOC.Encargado,LOC.Direccion,LOC.Identificador,LOC.IdRuta,LOC.Estimado " +
                "FROM LocalTemp LOC ";

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
    public Cursor BuscarLocalNombre(Context context, String Nombre){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = " SELECT LOC.IdLocal IdLocal,LOC.Nombre Nombre,LOC.Codigo Codigo, 'N' Subido, LOC.Identificador Identificador,LOC.IdRuta IdRuta,LOC.Encargado Encargado, LOC.Direccion Direccion,LOC.Estimado Estimado " +
                " FROM Local LOC WHERE 1 = 1 ";

        if (!Nombre.equals("-TOD")){
            varSQL = varSQL + " AND Nombre LIKE '" + Nombre + "%'";
        }

        varSQL = varSQL + " UNION SELECT LOC.IdLocal IdLocal,LOC.Nombre Nombre,LOC.Codigo Codigo, 'N' Subido, LOC.Identificador Identificador,LOC.IdRuta IdRuta,LOC.Encargado Encargado, LOC.Direccion Direccion,LOC.Estimado Estimado " +
                " FROM LocalTemp LOC WHERE 1 = 1 ";

        if (!Nombre.equals("-TOD")){
            varSQL = varSQL + " AND Nombre LIKE '" + Nombre + "%'";
        }

        Cursor miCursor = db.rawQuery(varSQL,null);

        return  miCursor;
    }
    public void EliminarLocal(String IdLocal, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Local WHERE IdLocal = " + IdLocal);

        db.close();
    }
    public void EliminarLocalTemp(String Identificador, Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM LocalTemp WHERE Identificador = " + Identificador);

        db.close();
    }
    public void EliminarLocalConNotas(Context context){
        ActISMAQBD usdbh =
                new ActISMAQBD(context, "ISMAQ", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Local WHERE IdLocal NOT IN (SELECT IdLocal FROM Notas)");

        db.close();
    }
}
