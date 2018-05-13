package cl.invsal.www.istemf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MarceloUsuario on 03-05-2017.
 */

public class UsuariosSQLiteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE Usuarios (Rut TEXT, Nombre TEXT)";
    String sqlCreate1 = "CREATE TABLE Numeros(IdEmpresa NUMBER, NombreEmpresa TEXT, IdSucursal NUMBER," +
            "NombreSucursal TEXT,IdCategoria NUMBER, NombreCategoria TEXT, Letra TEXT," +
            "Ciclo NUMBER, Numero NUMBER,Fecha NUMBER,Direccion TEXT)";
    String sqlCreate2 = "CREATE TABLE NumerosOnline(IdCategoria NUMBER, Cantidad NUMBER,Fecha NUMBER)";

    public UsuariosSQLiteHelper(Context contexto, String nombre,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Usuarios");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

}

