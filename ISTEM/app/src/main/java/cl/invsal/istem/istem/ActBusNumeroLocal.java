package cl.invsal.istem.istem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.NumberFormat;
import java.util.ArrayList;

/**431186206171
 * 431186206171
 * Created by MarceloUsuario on 03-04-2017.
 */

public class ActBusNumeroLocal extends AppCompatActivity {
    public static String Llamada = "";
    private String Accion = "Buscar";
    private Context Contexto = this;
    private ProgressDialog pd = null;
    private ListView Lista;
    ejeWS oEjeWS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laydetalle);

        Accion = "OBTFECHA";
            //Bundle bundle = getIntent().getExtras();
            //Rut = bundle.getString("Rut");
            //Nombre = bundle.getString("Nombre");

        this.pd = ProgressDialog.show(this, "Procesando", "Espere unos segundos...", true, false);

        oEjeWS = new ejeWS();
        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            if (Accion.equals("OBTFECHA")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("ObtenerFecha");
                oEjeWS.setWS("wsVarios.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("Formato", "yyyyMMdd");
                oEjeWS.sEjeWS();
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (oEjeWS.getNumError() > 0) {
                    Funciones oFunciones = new Funciones();

                    oFunciones.MostrarAlerta(Contexto, oEjeWS.getDesError(), "E R R O R");
                    pd.dismiss();
                    return;
                }

                if (Accion.equals("OBTFECHA")) {
                    ResultadoBuscar();
                }
            }
        }
    }
    private  void ResultadoBuscar(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            this.pd.dismiss();

            oFunciones.MostrarAlerta(Contexto,"Error de servidor, no existe fecha configurada","Número");
            return;
        }

        String Fecha = oEjeWS.getValorString(0,"Fecha");
        oFunciones.EliminarFecha(Fecha,Contexto);

        if (Llamada.equals("MOSTODO") || Llamada.equals("MOSTODOEMP") || Llamada.equals("MOSTODOEMPSUC")){
            setTitle("Números en ejecución");

            String IdEmpresaParametro = "0";
            String IdSucursalParametro = "0";

            if (Llamada.equals("MOSTODOEMP")) {
                Bundle bundle = getIntent().getExtras();
                IdEmpresaParametro = bundle.getString("IdEmpresa");
            }

            if (Llamada.equals("MOSTODOEMPSUC")) {
                Bundle bundle = getIntent().getExtras();
                IdEmpresaParametro = bundle.getString("IdEmpresa");
                IdSucursalParametro = bundle.getString("IdSucursal");
            }
            Cursor cCursor = oFunciones.BuscarNumero(Contexto,IdEmpresaParametro,IdSucursalParametro,"0","0","0","0");
            ArrayList<tVarios> Datos = new ArrayList<tVarios>();

            if (cCursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String IdEmpresa= cCursor.getString(cCursor.getColumnIndex("IdEmpresa"));
                    String IdSucursal= cCursor.getString(cCursor.getColumnIndex("IdSucursal"));
                    String IdCategoria= cCursor.getString(cCursor.getColumnIndex("IdCategoria"));
                    String Ciclo= cCursor.getString(cCursor.getColumnIndex("Ciclo"));
                    String Letra= cCursor.getString(cCursor.getColumnIndex("Letra"));
                    String Numero= cCursor.getString(cCursor.getColumnIndex("Numero"));
                    String Empresa = cCursor.getString(cCursor.getColumnIndex("NombreEmpresa"));
                    String Categoria = cCursor.getString(cCursor.getColumnIndex("NombreCategoria"));
                    String Sucursal = cCursor.getString(cCursor.getColumnIndex("NombreSucursal"));
                    String LetraNumero = cCursor.getString(cCursor.getColumnIndex("Letra")) + " - " + cCursor.getString(cCursor.getColumnIndex("Numero"));

                    Datos.add(new tVarios(LetraNumero,"Empresa: " + Empresa + " Sucursal: " + Sucursal + " Categoria: " + Categoria,
                            IdEmpresa,
                            IdSucursal,
                            IdCategoria,
                            Ciclo,
                            Letra,
                            Numero,
                            Empresa,Sucursal,Categoria,"","","",""));

                } while(cCursor.moveToNext());

                Lista = (ListView)findViewById(R.id.lisDatos);
                adapLis adapter = new adapLis(this, Datos,true);

                Lista.setAdapter(adapter);

                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                        tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                        if (Llamada.equals("MOSTODO") || Llamada.equals("MOSTODOEMP") || Llamada.equals("MOSTODOEMPSUC")){
                            ActVisNumero.vNumero = oVarios.getValor8();
                            ActVisNumero.vEmpresa = oVarios.getValor9() + " - Suc. " + oVarios.getValor10() + " - Cat. " + oVarios.getValor11();
                            ActVisNumero.vIdEmpresa = oVarios.getValor3();
                            ActVisNumero.vIdCategoria = oVarios.getValor5();
                            Intent i = new Intent(Contexto, ActVisNumero.class);
                            startActivity(i);
                            finish();
                        }
                        finish();
                    }
                });
            }else{
                oFunciones.MostrarAlerta(Contexto,"No existen números en ejecución","Números");
                this.pd.dismiss();
                return;
            }


            //Bundle bundle = getIntent().getExtras();
            //Rut = bundle.getString("Rut");
            //Nombre = bundle.getString("Nombre");
        }else if(Llamada.equals("MOSEMPRESA") || Llamada.equals("MOSEMPRESASUC")){
            setTitle("Seleccione Empresa");

            Cursor cCursor = oFunciones.BuscarSoloEmpresas(Contexto);

            ArrayList<tVarios> Datos = new ArrayList<tVarios>();

            if (cCursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String IdEmpresa= cCursor.getString(cCursor.getColumnIndex("IdEmpresa"));
                    String Empresa = cCursor.getString(cCursor.getColumnIndex("NombreEmpresa"));

                    Datos.add(new tVarios(Empresa,
                            "",
                            IdEmpresa,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "","","","","","",""));

                } while(cCursor.moveToNext());

                Lista = (ListView)findViewById(R.id.lisDatos);
                adapLis adapter = new adapLis(this, Datos,true);

                Lista.setAdapter(adapter);

                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                        tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                        if (Llamada.equals("MOSEMPRESA")){
                            ActBusNumeroLocal.Llamada = "MOSTODOEMP";
                            Intent i = new Intent(Contexto, ActBusNumeroLocal.class);
                            i.putExtra("IdEmpresa",oVarios.getValor3());
                            startActivity(i);
                        }
                        if (Llamada.equals("MOSEMPRESASUC")){
                            ActBusNumeroLocal.Llamada = "MOSSUCURSAL";
                            Intent i = new Intent(Contexto, ActBusNumeroLocal.class);
                            i.putExtra("IdEmpresa",oVarios.getValor3());
                            startActivity(i);
                        }
                        finish();
                    }
                });
            }else{
                oFunciones.MostrarAlerta(Contexto,"No existen números en ejecución","Números");
                this.pd.dismiss();
                return;
            }
        }else if(Llamada.equals("MOSSUCURSAL")){
            setTitle("Seleccione Sucursal");

            Bundle bundle = getIntent().getExtras();
            String IdEmpresaParametro = bundle.getString("IdEmpresa");

            Cursor cCursor = oFunciones.BuscarSoloSucursales(Contexto,IdEmpresaParametro);

            ArrayList<tVarios> Datos = new ArrayList<tVarios>();

            if (cCursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String IdEmpresa = cCursor.getString(cCursor.getColumnIndex("IdEmpresa"));
                    String IdSucursal = cCursor.getString(cCursor.getColumnIndex("IdSucursal"));
                    String Empresa = cCursor.getString(cCursor.getColumnIndex("NombreEmpresa"));
                    String Sucursal = cCursor.getString(cCursor.getColumnIndex("NombreSucursal"));
                    String Direccion = cCursor.getString(cCursor.getColumnIndex("Direccion"));

                    Datos.add(new tVarios(Sucursal,
                            Empresa + " - Dir.: " + Direccion,
                            IdEmpresa,
                            IdSucursal,
                            "",
                            "",
                            "",
                            "",
                            "","","","","","",""));

                } while(cCursor.moveToNext());

                Lista = (ListView)findViewById(R.id.lisDatos);
                adapLis adapter = new adapLis(this, Datos,true);

                Lista.setAdapter(adapter);

                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                        tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                        if (Llamada.equals("MOSSUCURSAL")){
                            ActBusNumeroLocal.Llamada = "MOSTODOEMPSUC";
                            Intent i = new Intent(Contexto, ActBusNumeroLocal.class);
                            i.putExtra("IdEmpresa",oVarios.getValor3());
                            i.putExtra("IdSucursal",oVarios.getValor4());
                            startActivity(i);
                        }
                        finish();
                    }
                });
            }else{
                oFunciones.MostrarAlerta(Contexto,"No existen números en ejecución","Números");
                this.pd.dismiss();
                return;
            }
        }

        this.pd.dismiss();
    }
}
