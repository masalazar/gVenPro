package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;

/**
 * Created by MarceloUsuario on 11-03-2017.
 */

public class bdProducto {
    public long Agregar(String Codigo, String Nombre,String Subido,String IdFamilia,String Identificador,String Imagen,String Vigencia, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        String varSQL = "INSERT INTO Producto (Codigo,Nombre,Subido,IdFamilia,Imagen,Vigencia,Identificador) " +
                "VALUES ('" + Codigo + "','" + Nombre + "','" + Subido + "'," + IdFamilia + ",'" + Imagen + "','" + Vigencia + "'," + Identificador + ")";
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
                new String[]{"Producto"},
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
    public void Modificar(String IdProducto, String Nombre,String IdFamilia,String Imagen,String Vigencia, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Producto SET Imagen = '" + Imagen + "',Vigencia = '" + Vigencia + "',Subido = 'N',Nombre = '" + Nombre + "',IdFamilia = " + IdFamilia + " WHERE IdProducto = " + IdProducto);

        db.close();
    }
    public void ModificarSubido(String IdProducto, String Identificador, Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("Update Producto SET Subido = 'S',Identificador = " + Identificador + " WHERE IdProducto = " + IdProducto);

        db.close();
    }
    public Cursor Buscar(Context context, String IdProducto,String Codigo,String Nombre,String IdFamilia,String Vigencia){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";

        varSQL = "SELECT PRO.IdProducto,PRO.Codigo,PRO.Nombre,PRO.Identificador,PRO.Subido,PRO.IdFamilia,FAM.Nombre NomFam,PRO.Imagen,PRO.Vigencia " +
                "FROM Producto PRO, Familia FAM WHERE FAM.IdFamilia = PRO.IdFamilia ";

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

        varSQL = "SELECT PRO.IdProducto,PRO.Codigo,PRO.Nombre,PRO.Identificador,PRO.Subido,PRO.IdFamilia,PRO.Vigencia,PRO.Imagen,FAM.Nombre NomFam,FAM.Identificador IdenFam " +
                "FROM Producto PRO, Familia FAM WHERE FAM.IdFamilia = PRO.IdFamilia AND PRO.Subido = 'N' ";

        try {
            Cursor miCursor = db.rawQuery(varSQL,null);
            return  miCursor;
        }catch (Exception e){
            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,e.getMessage(),"sd");
        }



        return  null;
    }
    public void EliminarTodo(Context context){
        bdBase usdbh =
                new bdBase(context, "gComida", null, Funciones.gVersionBD);

        SQLiteDatabase db = usdbh.getWritableDatabase();

        db.execSQL("DELETE FROM Producto");

        db.close();
    }
}
