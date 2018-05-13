package cl.issoft.www.ismaq;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ejeWS oEjeWS;
    private String Accion="";
    private ProgressDialog pd = null;
    private Context Contexto = this;
    public static String Imei = "";
    private String FechaDesde,FechaHasta;

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

        Imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Intent service = new Intent(this, ConexionServidor.class);
        startService(service);



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
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.menSinLocal) {
            this.pd = ProgressDialog.show(this, "Procesando", "Espere unos segundos...", true, false);

            Accion = "LOCAL";
            oEjeWS = null;
            oEjeWS = new ejeWS();

            EjecutaWS oEjecutaWS = new EjecutaWS();
            oEjecutaWS.execute();
        }else if (id == R.id.menNota) {
            fragment = new fraNotas();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBusLocal) {
            fragment = new fraBusLocal();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menNueLocal) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "EDITAR");
            arguments.putString("IdLocal", "0");
            arguments.putString("Identificador", "0");
            fragment = fraManLocal.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menNueRecaudacion) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "NUEVO");
            fragment = fraManRecaudacion.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBusRecaudacion) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "BUSCAR");
            fragment = fraBusRecaudacion.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menSetLote) {
            Bundle arguments = new Bundle();
            fragment = fraSetLote.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menSubir) {
            Funciones oFunciones = new Funciones();
            Sincronizar oSin = new Sincronizar();
            oSin.Llamada = "MENU";
            oSin.Ejecutar(this);
            oFunciones.MostrarAlerta(this,"La operación se realizo correctamente","Subir");
        }else if (id == R.id.menSQL) {
            Intent i = new Intent(this, ActEjeSQL.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("LOCAL")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSLocal.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdLocal", "0");
                oEjeWS.AddParametro("Codigo", "");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("Vigencia", "S");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            } else if (Accion.equals("RECNOTAS")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSNota.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdNota", "0");
                oEjeWS.AddParametro("IdLocal", "0");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("RECAUDACION")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSRecaudacion.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdRecaudacion", "0");
                oEjeWS.AddParametro("IdLocal", "0");
                oEjeWS.AddParametro("FechaDesde", FechaDesde);
                oEjeWS.AddParametro("FechaHasta", FechaHasta);
                oEjeWS.AddParametro("NomConcatenado", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("RUTA")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSRuta.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdRuta", "0");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
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

                if (Accion.equals("LOCAL")) {
                    ResultadoLocal();
                }else if (Accion.equals("RECNOTAS")) {
                    LlenarNotas();
                }else if (Accion.equals("RECAUDACION")) {
                    LlenarRecaudacion();
                }else if (Accion.equals("RUTA")) {
                    LlenarRuta();
                }
            }
        }
    }
    private void LlenarNotas(){
        ActNotasBD oNotas = new ActNotasBD();

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdLocal = oEjeWS.getValorString(i,"IdLocal");
            String Descripcion = oEjeWS.getValorString(i,"Descripcion");
            String Identificador = oEjeWS.getValorString(i,"IdNota");

            Cursor cu = oNotas.BuscarNotaIdentificador(getApplicationContext(), Identificador);

            if (cu.getCount() == 0) {
                oNotas.AgregarNota(IdLocal,Descripcion,getApplicationContext(),Identificador);
            }
        }

        Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy
        calendar.add(Calendar.DATE, -365);
        Date date = calendar.getTime();
        Calendar calendar1 =Calendar.getInstance(); //obtiene la fecha de hoy
        calendar1.add(Calendar.DATE, 3);
        Date date1 = calendar1.getTime();
        FechaDesde = new SimpleDateFormat("dd/MM/yyyy").format(date);
        FechaHasta = new SimpleDateFormat("dd/MM/yyyy").format(date1);


        Accion = "RECAUDACION";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

    }
    private void LlenarRecaudacion(){
        ActRecaudacionBD oRecaudacion = new ActRecaudacionBD();
        oRecaudacion.EliminarTodo(this);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdLocal = oEjeWS.getValorString(i,"IdLocal");
            String Lote = oEjeWS.getValorString(i,"Lote");
            long Monto = Long.parseLong(oEjeWS.getValorString(i,"Monto"));
            String Fecha = oEjeWS.getValorString(i,"Fecha");
            String Estimado = oEjeWS.getValorString(i,"Estimado");
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("MM/dd/yyyy");
            Date dFecha = null;

            try {
                dFecha = formatoDelTexto.parse(Fecha);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            Fecha = new SimpleDateFormat("yyyyMMdd").format(dFecha);

            oRecaudacion.AgregarRecaudacion(IdLocal,"0",Monto,Fecha,"S",Estimado,Lote,this);
        }

        Accion = "RUTA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void LlenarRuta(){
        ActRutaBD oRuta = new ActRutaBD();
        oRuta.EliminarRuta(this);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdRuta = oEjeWS.getValorString(i,"IdRuta");
            String Nombre = oEjeWS.getValorString(i,"Nombre");

            oRuta.AgregarRuta(IdRuta,Nombre,this);
        }

        Funciones oFunciones = new Funciones();

        oFunciones.MostrarAlerta(Contexto,"Las sincronización fue realizado correctamente","Sincronización");
        pd.dismiss();
    }
    private void ResultadoLocal(){
        ActLocalBD obtLocal = new ActLocalBD();

        obtLocal.EliminarLocalConNotas(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            Cursor c;
            String IdLocal = oEjeWS.getValorString(i,"IdLocal");
            c = obtLocal.BuscarLocal(Contexto,IdLocal);

            if (c.getCount() == 0){
                obtLocal.AgregarLocal(IdLocal,oEjeWS.getValorString(i,"Codigo"),oEjeWS.getValorString(i,"Nombre"),"S",oEjeWS.getValorString(i,"IdRuta"),oEjeWS.getValorString(i,"Encargado"),oEjeWS.getValorString(i,"Direccion"),oEjeWS.getValorString(i,"Estimado"),Contexto);
            }
        }

        Accion = "RECNOTAS";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
}
