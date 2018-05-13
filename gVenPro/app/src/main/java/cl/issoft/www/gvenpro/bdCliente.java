package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by MarceloUsuario on 04-01-2018.
 */

public class bdCliente {
    public long Agregar(String Rut, String Nombre, String Direccion,String IdComuna, String Subido,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("INSERT INTO Cliente (Rut,Identificador,Nombre,Direccion, Subido,IdComuna) " +
                "VALUES ('" + Rut + "'," + Identificador + ",'" + Nombre + "', '" + Direccion +"','" + Subido + "'," + IdComuna + ")");

        long index = 0;
        Cursor cursor = db.query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{"Cliente"},
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
    public void Modificar(String IdCliente, String Nombre,String Direccion,String IdComuna, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Cliente SET Subido = 'N',Nombre = '" + Nombre + "',Direccion = '" + Direccion + "',IdComuna = " + IdComuna + " WHERE IdCliente = " + IdCliente);

        db.close();
    }
    public void ModificarSubido(String IdCliente,String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Cliente SET Subido = 'S',Identificador = " + Identificador + " WHERE IdCliente = " + IdCliente);

        db.close();
    }
    public Cursor Buscar(Context context, String IdCliente,String Rut,String Nombre,String IdComuna){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT CLI.IdCliente,CLI.Rut,CLI.Nombre,CLI.Direccion,CLI.Identificador,CLI.Subido,CLI.IdComuna, COMU.Nombre NomCom, COMU.Identificador IdeCom " +
                "FROM Cliente CLI, Comuna COMU WHERE COMU.IdComuna = CLI.IdComuna ";

        if (!IdCliente.equals("0")){
            varSQL = varSQL + " AND CLI.IdCliente = " + IdCliente;
        }
        if (!IdComuna.equals("0")){
            varSQL = varSQL + " AND CLI.IdComuna = " + IdComuna;
        }
        if (!Rut.equals("")){
            varSQL = varSQL + " AND CLI.Rut = '" + Rut + "'";
        }

        if (!Nombre.equals("")){
            varSQL = varSQL + " AND CLI.Nombre LIKE '" + Nombre + "%'";
        }

        Cursor miCursor = db.rawQuery(varSQL,null);



        return  miCursor;
    }
    public Cursor BuscarNoSubido(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT CLI.IdCliente,CLI.Rut,CLI.Nombre,CLI.Direccion,CLI.Identificador,CLI.Subido,CLI.IdComuna, COMU.Nombre NomCom, COMU.Identificador IdeCom " +
                "FROM Cliente CLI, Comuna COMU WHERE COMU.IdComuna = CLI.IdComuna AND CLI.Subido = 'N' ";

        Cursor miCursor = null;

        try {
            miCursor = db.rawQuery(varSQL,null);
        }catch (Exception e){
            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,e.getMessage(),"sd");
        }



        return  miCursor;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Cliente");

        db.close();
    }
}
