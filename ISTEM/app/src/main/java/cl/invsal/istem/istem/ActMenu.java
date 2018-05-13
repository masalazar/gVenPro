package cl.invsal.istem.istem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ejeWS oEjeWS;
    private String Accion="";
    private String vIdSucursal="0";
    private ProgressDialog pd = null;
    private String vNumero;
    private String vEmpresa;
    private String vIdEmpresa;
    private String vIdCategoria;
    private String [] Datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laymenu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menNuevoNumero) {
            //Lanzamos la activity del escaner
            IntentIntegrator.initiateScan(this);

        } else if (id == R.id.menTodosNumeros) {
            ActBusNumeroLocal.Llamada="MOSTODO";
            Intent i = new Intent(this, ActBusNumeroLocal.class);
            startActivity(i);

        }else if (id == R.id.menNumeroEmpresa) {
            ActBusNumeroLocal.Llamada="MOSEMPRESA";
            Intent i = new Intent(this, ActBusNumeroLocal.class);
            startActivity(i);

        }else if (id == R.id.menNumeroSucursal) {
            ActBusNumeroLocal.Llamada="MOSEMPRESASUC";
            Intent i = new Intent(this, ActBusNumeroLocal.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE: {
                if (resultCode == RESULT_CANCELED) {
                } else {
                    //Recogemos los datos   que nos envio el código Qr/codigo de barras
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(
                            requestCode, resultCode, data);
                    String UPCScanned = scanResult.getContents();
                    //cOMO ES SOLO UN EJEMPLO LO SACAREMOS POR PANTALLA.
                    ValidarNumero(UPCScanned);
                }
                break;
            }
        }
    }
    private void ValidarNumero(String Numero){
        Datos = Numero.split("/");
        Funciones oFunciones = new Funciones();

        if (Datos.length != 10){
            oFunciones.MostrarAlerta(ActMenu.this,"Imagen QR no valida","Número");
            return;
        }

        this.pd = ProgressDialog.show(this, "Procesando", "Espere unos segundos...", true, false);

        Accion = "OBTFECHA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("BUSCARPUBLICIDAD")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSPubSucursal.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdPubSucursal", "0");
                oEjeWS.AddParametro("IdSucursal", vIdSucursal);
                oEjeWS.AddParametro("IdPublicidad", "0");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }

            if (Accion.equals("OBTFECHA")) {
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

                    oFunciones.MostrarAlerta(ActMenu.this, oEjeWS.getDesError(), "E R R O R");
                    pd.dismiss();
                    return;
                }

                if (Accion.equals("BUSCARPUBLICIDAD")) {
                    ResultadoBuscarPublicidad();
                }
                if (Accion.equals("OBTFECHA")) {
                    GuardarNumero();
                }
            }
        }
    }
    private void GuardarNumero(){
        Funciones oFunciones = new Funciones();
        String Fecha = oEjeWS.getValorString(0,"Fecha");

        oFunciones.EliminarNumero(Datos[0],Datos[2],Datos[4],Datos[6],Datos[7],Datos[8],ActMenu.this);
        oFunciones.AgregarNumero(Datos[0],Datos[1],Datos[2],Datos[3],Datos[4],Datos[5],Datos[6],Datos[7],Datos[8],Fecha,Datos[9],ActMenu.this);
        vNumero = Datos[6] + " - " + Datos[8];
        vEmpresa = Datos[1] + " - Suc. " + Datos[3] + " - Cat. " + Datos[5];
        vIdSucursal = Datos[2];
        vIdCategoria = Datos[4];
        vIdEmpresa = Datos[0];


        Accion = "BUSCARPUBLICIDAD";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }

    private void ResultadoBuscarPublicidad(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            ActVisNumero.vNumero = vNumero;
            ActVisNumero.vEmpresa = vEmpresa;
            ActVisNumero.vIdEmpresa = vIdEmpresa;
            ActVisNumero.vIdCategoria = vIdCategoria;

            Intent i = new Intent(this, ActVisNumero.class);
            startActivity(i);

            this.pd.dismiss();
            return;
        }

        double numero = Math.random() * oEjeWS.getCount();
        int Fila = (int)numero;
        if (oEjeWS.getValorString(Fila,"TipPub").equals("VIDEO")){
            VideoViewActivity.vNumero = vNumero;
            VideoViewActivity.vEmpresa = vEmpresa;
            VideoViewActivity.vIdCategoria = vIdCategoria;
            VideoViewActivity.vIdEmpresa = vIdEmpresa;
            VideoViewActivity.VideoURL = oEjeWS.getValorString(Fila,"URLPub");
            Intent myIntent = new Intent(ActMenu.this,
                    VideoViewActivity.class);
            startActivity(myIntent);
        }else{
            ActPublicidad.vNumero = vNumero;
            ActPublicidad.vEmpresa = vEmpresa;
            ActPublicidad.vIdCategoria = vIdCategoria;
            ActPublicidad.vIdEmpresa = vIdEmpresa;
            ActPublicidad.URL = oEjeWS.getValorString(Fila,"URLPub");
            Intent i = new Intent(this, ActPublicidad.class);
            startActivity(i);
        }
        this.pd.dismiss();
    }
}
