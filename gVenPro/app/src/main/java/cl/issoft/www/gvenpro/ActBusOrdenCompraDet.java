package cl.issoft.www.gvenpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by MarceloUsuario on 11-12-2017.
 */

public class ActBusOrdenCompraDet extends AppCompatActivity {
    String FechaDesde, FechaHasta,FechaEstimadaCompraDesde,FechaEstimadaCompraHasta,Solicitada,Recibida,Pagada,Facturada,Anulada,IdProveedor;
    private Context Contexto = this;
    private ProgressDialog pd = null;
    private ListView Lista;
    MenuItem fav;
    String LlamadaG="",IdCabOrdenCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laydetalle);

        this.pd = ProgressDialog.show(this, "Procesando", "Espere unos segundos...", true, false);

        Bundle bundle = getIntent().getExtras();
        LlamadaG = bundle.getString("Llamada");
        FechaDesde = bundle.getString("FechaDesde");
        FechaHasta = bundle.getString("FechaHasta");
        FechaEstimadaCompraDesde = bundle.getString("FechaEstimadaCompraDesde");
        FechaEstimadaCompraHasta = bundle.getString("FechaEstimadaCompraHasta");
        Solicitada = bundle.getString("Solicitada");
        Recibida = bundle.getString("Recibida");
        Pagada = bundle.getString("Pagada");
        Facturada = bundle.getString("Facturada");
        Anulada = bundle.getString("Anulada");
        IdProveedor = bundle.getString("IdProveedor");
        Lista = (ListView) findViewById(R.id.lisDatos);
        Contexto = this;

        if (FechaDesde.equals("")){
            FechaDesde="0";
        }else{
            FechaDesde = FechaDesde.substring(6,10) + FechaDesde.substring(3,5) + FechaDesde.substring(0,2);
        }
        if (FechaHasta.equals("")){
            FechaHasta="0";
        }else{
            FechaHasta = FechaHasta.substring(6,10) + FechaHasta.substring(3,5) + FechaHasta.substring(0,2);
        }
        if (FechaEstimadaCompraDesde.equals("")){
            FechaEstimadaCompraDesde="0";
        }else  {
            FechaEstimadaCompraDesde = FechaEstimadaCompraDesde.substring(6,10) + FechaEstimadaCompraDesde.substring(3,5) + FechaEstimadaCompraDesde.substring(0,2);
        }
        if (FechaEstimadaCompraHasta.equals("")){
            FechaEstimadaCompraHasta="0";
        }else{
            FechaEstimadaCompraHasta = FechaEstimadaCompraHasta.substring(6,10) + FechaEstimadaCompraHasta.substring(3,5) + FechaEstimadaCompraHasta.substring(0,2);
        }


        bdCabOrdenCompra oCabOrdenCompra = new bdCabOrdenCompra();
        Cursor c = oCabOrdenCompra.Buscar(this,"0",IdProveedor,FechaDesde,FechaHasta,FechaEstimadaCompraDesde,FechaEstimadaCompraHasta,Pagada,Facturada,Solicitada,Anulada,Recibida);

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Fecha= c.getString(c.getColumnIndex("Fecha"));
                String NomPro = c.getString(c.getColumnIndex("NomPro"));
                String IdProveedor = c.getString(c.getColumnIndex("IdProveedor"));
                String FechaEstimadaCompra = c.getString(c.getColumnIndex("FechaEstimadaCompra"));
                String Cantidad = c.getString(c.getColumnIndex("Cantidad"));
                String ValorNeto = c.getString(c.getColumnIndex("ValorNeto"));
                String Iva = c.getString(c.getColumnIndex("Iva"));
                String Total = c.getString(c.getColumnIndex("ValorTotal"));
                String IdCabOrdenCompra = c.getString(c.getColumnIndex("IdCabOrdenCompra"));
                String Solicitada = c.getString(c.getColumnIndex("Solicitada"));
                String Recibida = c.getString(c.getColumnIndex("Recibida"));
                String Pagada = c.getString(c.getColumnIndex("Pagada"));
                String Anulada = c.getString(c.getColumnIndex("Anulada"));
                String Facturada = c.getString(c.getColumnIndex("Facturada"));

                Fecha = Fecha.substring(6,8) + '/' + Fecha.substring(4,6) + '/' + Fecha.substring(0,4);
                FechaEstimadaCompra = FechaEstimadaCompra.substring(6,8) + '/' + FechaEstimadaCompra.substring(4,6) + '/' + FechaEstimadaCompra.substring(0,4);

                if (Solicitada.equals("S")){Solicitada = "Si";}else{Solicitada = "No";}
                if (Recibida.equals("S")){Recibida = "Si";}else{Recibida = "No";}
                if (Pagada.equals("S")){Pagada = "Si";}else{Pagada = "No";}
                if (Anulada.equals("S")){Anulada = "Si";}else{Anulada = "No";}
                if (Facturada.equals("S")){Facturada = "Si";}else{Facturada = "No";}
                Locale locale = new Locale("es","CL");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                ValorNeto = nf.format(Long.parseLong(ValorNeto));
                Iva = nf.format(Long.parseLong(Iva));
                Total = nf.format(Long.parseLong(Total));

                String Variable = "F. E. C: " + FechaEstimadaCompra + " - S:" + Solicitada + " - R: " + Recibida + " - P: " + Pagada + " - F: " + Facturada + " - A: " + Anulada + " - VN: " + ValorNeto + " - I: " + Iva + " - T: " + Total;
                Datos.add(new tVarios(Fecha + " - " + NomPro,Variable,IdCabOrdenCompra,"","","","","","","","","","","",""));

            } while(c.moveToNext());

            adapLis adapter = new adapLis(this, Datos,false);

            Lista.setAdapter(adapter);
            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);
                    IdCabOrdenCompra = oVarios.getValor3();
                    Funciones oFunciones = new Funciones();

                    if (!LlamadaG.equals("BUSCAR")){
                        String Titulo="";
                        String Mensaje="";
                        if (LlamadaG.equals("SOLICITADA")){
                            Titulo = "Solicitar";
                            Mensaje = "¿Seguro de solicitar estos productos?";
                        }else if (LlamadaG.equals("RECIBIDA")){
                            Titulo = "Recibir";
                            Mensaje = "¿Seguro de recibir estos productos?";
                        }else if (LlamadaG.equals("PAGADA")){
                            Titulo = "Pagar";
                            Mensaje = "¿Seguro de Pagar estos productos?";
                        }else if (LlamadaG.equals("FACTURADA")){
                            Titulo = "Facturada";
                            Mensaje = "¿Seguro que esta orden fue facturada?";
                        }else if (LlamadaG.equals("ANULADA")){
                            Titulo = "Anulada";
                            Mensaje = "¿Seguro de anular esta orden?";
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(Contexto);
                        builder.setTitle(Titulo);
                        builder.setMessage(Mensaje);
                        builder.setCancelable(false);
// Add the buttons
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Aceptar();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Cancelar();
                            }
                        });
                        builder.show();

                    }else {
                        Intent Resultado = new Intent();
                        Resultado.putExtra("IdCabOrdenCompra", oVarios.getValor3());
                        Resultado.putExtra("Encontrado", "SI");

                        setResult(RESULT_OK, Resultado);
                        finish();
                    }
                }
            });
        }
        pd.dismiss();
    }
    private void Aceptar(){
        bdCabOrdenCompra oCabOrdenCompra = new bdCabOrdenCompra();
        if (LlamadaG.equals("SOLICITADA")){
            oCabOrdenCompra.Solicitar(IdCabOrdenCompra,Contexto);
        }else if (LlamadaG.equals("RECIBIDA")){
            oCabOrdenCompra.Recibir(IdCabOrdenCompra,Contexto);
        }else if (LlamadaG.equals("FACTURADA")){
            oCabOrdenCompra.Facturar(IdCabOrdenCompra,Contexto);
        }else if (LlamadaG.equals("PAGADA")){
            oCabOrdenCompra.Pagar(IdCabOrdenCompra,Contexto);
        }else if (LlamadaG.equals("ANULADA")){
            oCabOrdenCompra.Anular(IdCabOrdenCompra,"SIN MOTIVO",Contexto);
        }

        Funciones oFunciones = new Funciones();
        oFunciones.MostrarAlerta(Contexto,"La operación se realizo correctamente","Aceptar");
        finish();
    }
    private void Cancelar(){

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
        }
        return super.onOptionsItemSelected(item);
    }
}
