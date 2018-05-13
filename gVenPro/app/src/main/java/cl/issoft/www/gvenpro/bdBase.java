package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MarceloUsuario on 11-02-2017.
 */

public class bdBase extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE Proveedor (IdProveedor INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER, Rut TEXT, Nombre TEXT, Direccion TEXT, Vendedor TEXT, Subido TEXT)";
    String sqlCreate1 = "CREATE TABLE Producto (IdProducto INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER, Codigo TEXT, Nombre TEXT, Subido TEXT,IdFamilia NUMBER,Imagen TEXT,Vigencia TEXT)";
    String sqlCreate2 = "CREATE  TABLE CabOrdenCompra(IdCabOrdenCompra INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdProveedor NUMBER,Fecha NUMBER, FechaEstimadaCompra NUMBER, Cantidad NUMBER," +
            "ValorNeto NUMBER,Pagada TEXT, Facturada TEXT,Solicitada TEXT,Anulada TEXT,MotivoAnulada TEXT,Iva NUMBER,ValorTotal NUMBER,Subido TEXT,Descuento NUMBER,Recibida TEXT)";
    String sqlCreate3 = "CREATE TABLE DetOrdenCompra(IdDetOrdenCompra INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdCabOrdenCompra NUMBER, IdProducto NUMBER, Cantidad NUMBER, Valor NUMBER,Subido TEXT)";
    String sqlCreate4 = "CREATE TABLE Stock(IdStock INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdSucursal NUMBER, IdProducto NUMBER, CantidadFisica NUMBER, CantidadCritica NUMBER, ValorVenta NUMBER,Subido TEXT, ValorCompra NUMBER)";
    String sqlCreate5 = "CREATE TABLE StoPro(IdStoPro INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdProveedor NUMBER, IdStock NUMBER, CantidadFisica NUMBER, ValorCompra NUMBER,Subido TEXT)";
    String sqlCreate6 = "CREATE TABLE Cliente (IdCliente INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER, Rut TEXT, Nombre TEXT, Direccion TEXT, Subido TEXT,IdComuna NUMBER)";
    String sqlCreate7 = "CREATE TABLE Comuna (IdComuna INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER, Nombre TEXT,Subido TEXT)";
    String sqlCreate8 = "CREATE  TABLE CabVenta(IdCabVenta INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdCliente NUMBER,Fecha NUMBER, Cantidad NUMBER," +
                "ValorNeto NUMBER,Pagada TEXT, Pendiente TEXT,Anulada TEXT,MotivoAnulada TEXT,Iva NUMBER,ValorTotal NUMBER,Subido TEXT,Descuento NUMBER,Facturada TEXT)";
        String sqlCreate9 = "CREATE TABLE DetVenta(IdDetVenta INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdCabVenta NUMBER, IdProducto NUMBER, Cantidad NUMBER, Valor NUMBER,Subido TEXT)";
    String sqlCreate10 = "CREATE TABLE Pago(IdPago INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdCabVenta NUMBER, Fecha NUMBER, Valor NUMBER,Subido TEXT,IdCaja NUMBER)";
    String sqlCreate11 = "CREATE TABLE Caja(IdCaja INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER,IdUsuario NUMBER, Fecha NUMBER, ValorApertura NUMBER,ValorCierre NUMBER,Cuadrado TEXT,Cerrado TEXT,DineroEntregado TEXT, Subido TEXT)";
    String sqlCreate12 = "CREATE TABLE Familia (IdFamilia INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Identificador NUMBER, Nombre TEXT,Subido TEXT)";
    
    public bdBase(Context contexto, String nombre,
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
        db.execSQL(sqlCreate5);
        db.execSQL(sqlCreate6);
        db.execSQL(sqlCreate7);
        db.execSQL(sqlCreate8);
        db.execSQL(sqlCreate9);
        db.execSQL(sqlCreate10);
        db.execSQL(sqlCreate11);
        db.execSQL(sqlCreate12);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Proveedor");
        db.execSQL("DROP TABLE IF EXISTS Producto");
        db.execSQL("DROP TABLE IF EXISTS CabOrdenCompra");
        db.execSQL("DROP TABLE IF EXISTS DetOrdenCompra");
        db.execSQL("DROP TABLE IF EXISTS Stock");
        db.execSQL("DROP TABLE IF EXISTS StoPro");
        db.execSQL("DROP TABLE IF EXISTS Cliente");
        db.execSQL("DROP TABLE IF EXISTS Comuna");
        db.execSQL("DROP TABLE IF EXISTS CabVenta");
        db.execSQL("DROP TABLE IF EXISTS DetVenta");
        db.execSQL("DROP TABLE IF EXISTS Pago");
        db.execSQL("DROP TABLE IF EXISTS Caja");
        db.execSQL("DROP TABLE IF EXISTS Familia");

        //db.execSQL("DROP TABLE IF EXISTS Ruta");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate1);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreate3);
        db.execSQL(sqlCreate4);
        db.execSQL(sqlCreate5);
        db.execSQL(sqlCreate6);
        db.execSQL(sqlCreate7);
        db.execSQL(sqlCreate8);
        db.execSQL(sqlCreate9);
        db.execSQL(sqlCreate10);
        db.execSQL(sqlCreate11);
        db.execSQL(sqlCreate12);
    }

}