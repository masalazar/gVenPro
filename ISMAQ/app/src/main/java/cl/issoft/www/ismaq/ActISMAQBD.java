package cl.issoft.www.ismaq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MarceloUsuario on 07-14-2017.
 */

public class ActISMAQBD extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE Notas (IdNota INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador TEXT, IdLocal NUMBER, Descripcion TEXT,Subido TEXT)";
    String sqlCreate1 = "CREATE TABLE Local(IdLocal NUMBER,Identificador NUMBER, IdEmpresa NUMBER, Codigo TEXT," +
            "Nombre TEXT, Subido TEXT,IdRuta NUMBER,Encargado TEXT,Direccion TEXT, Estimado NUMBER)";
    String sqlCreate2 = "CREATE  TABLE LocalTemp(Identificador INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,IdLocal NUMBER, IdEmpresa NUMBER, Codigo TEXT," +
            "Nombre TEXT,Direccion TEXT, Encargado TEXT,IdRuta NUMBER,Estimado NUMBER)";
    String sqlCreate3 = "CREATE TABLE Recaudacion(IdRecaudacion INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,IdLocal NUMBER,Identificador NUMBER, Fecha NUMBER, Monto NUMBER,Subido TEXT,Estimado NUMBER,Lote NUMBER)";
    String sqlCreate4 = "CREATE TABLE Ruta(IdRuta NUMBER,Nombre TEXT)";

    public ActISMAQBD(Context contexto, String nombre,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreate3);
        db.execSQL(sqlCreate4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Notas");
        db.execSQL("DROP TABLE IF EXISTS Local");
        db.execSQL("DROP TABLE IF EXISTS LocalTem");
        db.execSQL("DROP TABLE IF EXISTS Recaudacion");
        db.execSQL("DROP TABLE IF EXISTS Ruta");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreate3);
        db.execSQL(sqlCreate4);
    }

}