package cl.issoft.www.gvenpro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;

import static android.R.attr.data;

public class ActMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Fragment fragment = null;

        if (id == R.id.menNuevoProveedor) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "NUEVO");
            arguments.putString("IdProveedor", "0");
            arguments.putString("Identificador", "0");
            fragment = fraManProveedor.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menNuevoCliente) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "NUEVO");
            arguments.putString("IdCliente", "0");
            arguments.putString("Identificador", "0");
            fragment = fraManCliente.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menNuevoProducto) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "NUEVO");
            arguments.putString("IdProducto", "0");
            arguments.putString("Identificador", "0");
            fragment = fraManProducto.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarProveedor) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "NORMAL");
            fragment = fraBusProveedor.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarCliente) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "NORMAL");
            fragment = fraBusCliente.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarProducto) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "BUSCAR");
            fragment = fraBusProducto.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menModValorVenta) {
            Bundle arguments = new Bundle();
            arguments.putString("Llamada", "BUSPRODUCTOMODVALOR");
            fragment = fraBusProducto.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menNuevaOrdenCompra) {
            Bundle arguments = new Bundle();
            arguments.putString("Llamada", "NUEVA");
            arguments.putString("IdCabOrdenCompra", "0");
            fragment = fraOrdenCompra.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        else if (id == R.id.menNuevaVenta) {
            Bundle arguments = new Bundle();
            arguments.putString("Llamada", "NUEVA");
            arguments.putString("IdCabVenta", "0");
            fragment = fraNueVenta.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarOrdenCompra) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "BUSCAR");
            fragment = fraBusOrdenCompra.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarVenta) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "BUSCAR");
            fragment = fraBusVenta.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarPendiente) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "BUSCARPENDIENTE");
            fragment = fraBusVenta.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarPagada) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "BUSCARPAGADA");
            fragment = fraBusVenta.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menUltimaVenta) {
            bdCabVenta oCabVenta = new bdCabVenta();
            Cursor c = oCabVenta.BuscarUltimo(this);
            String IdCabVenta = "0";

            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    IdCabVenta = c.getString(c.getColumnIndex("IdCabVenta"));
                } while (c.moveToNext());

                Bundle arguments = new Bundle();
                arguments.putString("IdCabVenta", IdCabVenta);
                arguments.putString("Llamada", "LECTURA");

                fragment = fraNueVenta.newInstance(arguments);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();

            }
        }else if (id == R.id.menSQL) {
            Intent i = new Intent(this, ActEjeSQL.class);
            startActivity(i);
        }else if (id == R.id.menSolicitarOrdenCompra) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "SOLICITADA");
            fragment = fraBusOrdenCompra.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        else if (id == R.id.menRecibirOrdenCompra) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "RECIBIDA");
            fragment = fraBusOrdenCompra.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        else if (id == R.id.menFacturadaOrdenCompra) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "FACTURADA");
            fragment = fraBusOrdenCompra.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menPagarOrdenCompra) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "PAGADA");
            fragment = fraBusOrdenCompra.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menAnularOrdenCompra) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "ANULADA");
            fragment = fraBusOrdenCompra.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menApertura) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "APERTURA");
            fragment = fraApertura.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menCierre) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "CIERRE");
            fragment = fraCierre.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menBuscarCaja) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "BUSCAR");
            fragment = faBusCaja.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menDineroEntregado) {
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "DINEROENTREGADO");
            fragment = faBusCaja.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }else if (id == R.id.menSubir) {
            Subir oSubir = new Subir();

            oSubir.Iniciar(this);
        }
        else if (id == R.id.menBajar) {
            RecibirDatos oRecibirDatos = new RecibirDatos();

            oRecibirDatos.Recibir(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
