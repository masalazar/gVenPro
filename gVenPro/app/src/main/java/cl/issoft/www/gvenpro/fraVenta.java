package cl.issoft.www.gvenpro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MarceloUsuario on 04-01-2018.
 */

public class fraVenta extends Fragment {
    ClaseCerrar oClaseCerrar = new ClaseCerrar();
    private ProgressDialog pd = null;
    private Menu menuG;
    private String gPendiente="S",gPagada="N",EsContenedor="N";
    private Context Contexto;
    private ListView Lista;
    private String ValorPagado = "0", ValorPendiente = "0";
    private TextView lblCliente,lblTotal;
    private String Llamada = "INICIO";
    public static String LlamadaG;
    private  int Fila = 0;
    private String IdCliente = "0";
    private  String IdDetVenta = "0";
    private Button btnEscCliente;
    private static int MenuNuevo = 0;
    private static int MenuLimpiar = 1;
    private static int MenuGuardar = 2;
    private static int MenuEliminar = 3;
    private static int MenuPagar = 4;
    private static int MenuEntregar = 5;
    private boolean bGenerar = false;
    private int Cantidad = 0;
    private float Valor = 0;
    private String IdProducto = "0",Total = "",ColEst ="",NomEst="",TotalAPagar = "0",CantidadTotal = "0",vTotalVenta = "0";
    private String Accion;
    private String gTotal,gIva,gValor,gCantidad = "0";
    public String IdCabVenta = "0";
    public boolean bIvaIncluido = true;

    public static fraVenta newInstance(Bundle arguments){
        fraVenta f = new fraVenta();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layventa, container, false);

        Contexto = getActivity();
        Lista = (ListView)rootView.findViewById(R.id.lisDatos);
        lblCliente = (TextView)rootView.findViewById(R.id.lblCliente);
        lblTotal = (TextView)rootView.findViewById(R.id.lblTotal);
        btnEscCliente = (Button)rootView.findViewById(R.id.btnCliente);
        lblTotal.setText("T: 0 - PA: 0 - PE: 0");
        btnEscCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscCliente();
            }
        });
        LlamadaG = getArguments().getString("Llamada");
        EsContenedor = getArguments().getString("EsContenedor");

        Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                Llamada = "MODIFICAR";
                tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                Intent i = new Intent(Contexto, ActEditProdOrden.class);
                i.putExtra("Llamada", "MODIFICAR");
                i.putExtra("IdProducto", oVarios.getValor3());
                i.putExtra("Codigo", oVarios.getValor4());
                i.putExtra("Nombre", oVarios.getValor5());
                i.putExtra("Cantidad", oVarios.getValor6());
                i.putExtra("Valor", oVarios.getValor7());
                startActivityForResult(i, 0);

            }
        });

        IdCabVenta = getArguments().getString("IdCabVenta");

        if (!IdCabVenta.equals("0")){
            LlenarDatos();
        }else{
            getActivity().setTitle("No Entregado y No Pagado");
        }

        setHasOptionsMenu(true);
        return rootView;


    }
    private void LlenarDatos(){
        bdCabVenta oCabVenta = new bdCabVenta();
        bdDetVenta oDetVenta = new bdDetVenta();

        this.pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);

        Cursor c =  oCabVenta.Buscar(getActivity(),IdCabVenta,"0","0","0","T","T","T","T");

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();
        Long Suma = Long.parseLong("0");

        if (c.moveToFirst()) {
            IdCliente = c.getString(c.getColumnIndex("IdCliente"));
            lblCliente.setText(c.getString(c.getColumnIndex("NomCli")));
            lblTotal.setText(c.getString(c.getColumnIndex("ValorNeto")));

            if (c.getString(c.getColumnIndex("Pendiente")).equals("N")){
                btnEscCliente.setEnabled(false);
            }

            String Titulo = "";
            if (c.getString(c.getColumnIndex("Pendiente")).equals("S")){
                Titulo = "No Entregado";
            }else{
                Titulo = "Entregado";
            }
            if (c.getString(c.getColumnIndex("Pagada")).equals("S")){
                Titulo = Titulo + " y Pagado";
            }else{
                Titulo = Titulo + " y No Pagado";
            }
            getActivity().setTitle(Titulo);
            gPendiente=c.getString(c.getColumnIndex("Pendiente"));
            gPagada=c.getString(c.getColumnIndex("Pagada"));

        }

        Cursor cd =  oDetVenta.Buscar(getActivity(),"0",IdCabVenta,"0");

        if (cd.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String NomPro= cd.getString(cd.getColumnIndex("NomPro"));
                String IdProducto= cd.getString(cd.getColumnIndex("IdProducto"));
                String Codigo= cd.getString(cd.getColumnIndex("CodPro"));
                String Nombre= cd.getString(cd.getColumnIndex("NomPro"));
                String vCantidad= cd.getString(cd.getColumnIndex("Cantidad"));
                String Valor= cd.getString(cd.getColumnIndex("Valor"));

                Locale locale = new Locale("es","CL");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                String vValor = nf.format(Long.parseLong(Valor));
                String vTotal = nf.format(Long.parseLong(Valor) * Long.parseLong(vCantidad));
                String vTotalSF = String.valueOf(Long.parseLong(Valor) * Long.parseLong(vCantidad));

                Datos.add(new tVarios(NomPro,"Cantidad: " + vCantidad + " - Valor: " + vValor + " - TotaL:" + vTotal,IdProducto,Codigo,Nombre,vCantidad,Valor,vTotalSF,"","","","","","",""));
                Suma = Suma + Long.parseLong(Valor);

            } while(cd.moveToNext());

            Locale locale = new Locale("es","CL");
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            String vSuma = nf.format(Suma);

            lblTotal.setText(vSuma);
            adapLis adapter = new adapLis(getActivity(), Datos,false);

            Lista.setAdapter(adapter);
            ValorPagado = String.valueOf(fnValorPagado());
            fnCalcularValores();
        }
        pd.dismiss();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menuG = menu;

        if(gPendiente.equals("S")){
            CrearMenu(menu);
        }else if(gPagada.equals("N")){
            menuG.add(0, MenuPagar, 0, "Realizar pago");
        }
    }
    private void CrearMenu(Menu menu){
        menu.clear();

        if (!LlamadaG.equals("LECTURA")) {
            menu.add(0, MenuNuevo, 0, "Nuevo Producto");
            menu.add(0, MenuLimpiar, 0, "Limpiar Venta");
            menu.add(0, MenuEliminar, 0, "Eliminar Producto");
            menu.add(0, MenuGuardar, 0, "Guardar");
        }else{
            menu.add(0, MenuEntregar, 0, "Entregar Productos");
            menu.add(0, MenuPagar, 0, "Hacer Pago");
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == MenuNuevo) {
            if (IdCliente.equals("0")){
                Funciones oFunciones = new Funciones();

                oFunciones.MostrarAlerta(getActivity(),"Depe especificar Cliente","Escoger Producto");
                return false;
            }
            Llamada="BUSPRODUCTOVENTA";
            ActContenedor.Llamada = Llamada;
            Intent i = new Intent(Contexto, ActContenedor.class);
            startActivityForResult(i, 0);
            return true;
        }
        if (id == MenuEliminar) {
            EliminarProducto();
            return true;
        }

        if (id == MenuLimpiar){
            Limpiar();
        }

        if (id == MenuGuardar){
            Guardar();
        }

        if (id == MenuPagar){
            Pagar();
        }

        if (id == MenuEntregar){
            Entregar();
        }

        return super.onOptionsItemSelected(item);
    }
    private void Entregar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Recibir Pago");
        builder.setMessage("¿Seguro de entregar estos productos?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AceptarEntregar();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                CancelarEntregar();
            }
        });
        builder.show();
    }
    private void CancelarEntregar(){

    }
    private void AceptarEntregar(){
        bdCabVenta oCabVenta = new bdCabVenta();
        oCabVenta.Entregar(IdCabVenta,getActivity());

        for (int i = 0; i < Lista.getCount(); i++) {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);

            bdStock oStock = new bdStock();
            oStock.ModificarCantidad(oVarios.getValor3(),Funciones.gIdSucursal,"-" + oVarios.getValor6(),"0",getActivity(),false);
        }

        getFragmentManager().beginTransaction().remove(this).commit();
        if (EsContenedor.equals("S")){
            oClaseCerrar.Cerrar(null);
        }
    }
    private void Pagar(){
        Llamada="PAGAR";
        Intent i = new Intent(Contexto, ActPago.class);
        i.putExtra("Llamada",Llamada);
        i.putExtra("IdCabVenta",IdCabVenta);
        String Deuda = String.valueOf(Long.parseLong(gTotal) - Long.parseLong(ValorPagado));
        i.putExtra("Deuda",String.valueOf(Deuda));
        startActivityForResult(i, 0);
    }
    private void Guardar(){
        Funciones oFunciones = new Funciones();

        if (IdCliente.equals("0") || IdCliente.equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Cliente","Guardar");
            return;
        }

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
        Date myDate = new Date();
        String Fecha = timeStampFormat.format(myDate);

        bdCabVenta oCabVenta = new bdCabVenta();
        Long IdCabVenta = oCabVenta.Agregar(IdCliente,Fecha,String.valueOf(Math.round(Long.parseLong(gCantidad))),Long.parseLong(gValor), "S","N","N",Long.parseLong(gIva),Long.parseLong(gTotal),"0","N",getContext());

        for (int i = 0; i < Lista.getCount(); i++) {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);

            bdDetVenta oDetVenta = new bdDetVenta();

            oDetVenta.Agregar(String.valueOf(IdCabVenta), oVarios.getValor3(),oVarios.getValor6(),Long.parseLong(oVarios.getValor7()),"N",getContext());
        }

        getFragmentManager().beginTransaction().remove(this).commit();
    }
    private void Limpiar(){
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        adapLis adapter = new adapLis(Contexto, Datos,false);

        Lista.setAdapter(adapter);
        lblTotal.setText("");
    }
    private  void EliminarProducto(){
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();
        Long Total = Long.parseLong("0");
        Long CantidadLocal = Long.parseLong("0");
        for (int i = 0; i < Lista.getCount(); i++) {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);
            CheckBox Sel = (CheckBox)Lista.getChildAt(i).findViewById(R.id.chkSel);

            if(Sel.isChecked() == false) {
                if (!oVarios.getValor10().equals("")) {
                    Datos.add(new tVarios(oVarios.getValor1(), oVarios.getValor2(), oVarios.getValor3(), oVarios.getValor4(), oVarios.getValor5(),
                            oVarios.getValor6(), oVarios.getValor7(), oVarios.getValor8(), oVarios.getValor9(), oVarios.getValor10(), oVarios.getValor11(), oVarios.getValor12(), oVarios.getValor13(), oVarios.getValor14(), oVarios.getValor15()));

                    Total = Total + Long.parseLong(oVarios.getValor4());
                    CantidadLocal = CantidadLocal + Integer.parseInt(oVarios.getValor5());
                }
            }
        }

        CantidadTotal = String.valueOf(CantidadLocal);
        vTotalVenta = String.valueOf(Total);

        adapLis adapter = new adapLis(Contexto, Datos,false);

        Lista.setAdapter(adapter);
        fnCalcularValores();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == -1) {
            if (Llamada.equals("BUSCLIENTE")){
                lblCliente.setText(data.getStringExtra("Rut") + " - " + data.getStringExtra("Nombre"));
                IdCliente = data.getStringExtra("IdCliente");
            }else if (Llamada.equals("BUSPRODUCTOVENTA")){
                fnAgregarProducto(data.getStringExtra("IdProducto"),data.getStringExtra("Codigo"),data.getStringExtra("Nombre"),data.getStringExtra("Cantidad"),Long.parseLong(data.getStringExtra("Valor")));
            }else if (Llamada.equals("MODIFICAR")){
                fnModificarProducto(data.getStringExtra("IdProducto"),data.getStringExtra("Codigo"),data.getStringExtra("Nombre"),data.getStringExtra("Cantidad"),Long.parseLong(data.getStringExtra("Valor")));
            }else if (Llamada.equals("PAGAR")){
                if (data.getStringExtra("Pagado").equals("S")){
                    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
                    Date myDate = new Date();
                    String Fecha = timeStampFormat.format(myDate);

                    String vValorPagado = String.valueOf(fnValorPagado());
                    ValorPagado = vValorPagado;

                    fnCalcularValores();

                    getFragmentManager().beginTransaction().remove(this).commit();
                }
            }
        }
    }
    private void fnCalcularValores(){
        Locale locale = new Locale("es","CL");
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(locale);
        Long Iva = Long.parseLong("0");
        Long Valor = Long.parseLong("0");
        Long Total = Long.parseLong("0");
        for (int i = 0; i < Lista.getCount(); i++) {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);

            Valor = Valor + Long.parseLong(oVarios.getValor8());
            gCantidad = String.valueOf(Long.parseLong(gCantidad) + Long.parseLong(oVarios.getValor6()));
        }
        Iva = (Valor * 19)/100;

        if (bIvaIncluido){
            Total = Valor;
            Valor = Valor - Iva;
        }else{
            Total = Valor + Iva;
        }

        gValor = String.valueOf(Valor);
        gIva = String.valueOf(Iva);
        gTotal = String.valueOf(Total);
        Long vValorPendiente = Total - Long.parseLong(ValorPagado);
        ValorPendiente = String.valueOf(vValorPendiente);

        lblTotal.setText("T: " + String.valueOf(formatoImporte.format(Total)) + " - PA: " + formatoImporte.format(Long.parseLong(ValorPagado))  + " - PE: " + formatoImporte.format(Long.parseLong(ValorPendiente)));
    }
    private void fnAgregarProducto(String IdProducto,String Codigo,String Nombre, String Cantidad, Long Valor){
        Funciones oFunciones = new Funciones();
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();
        Locale locale = new Locale("es","CL");
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(locale);

        for (int i = 0; i < Lista.getCount(); i++) {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);

            if (oVarios.getValor3().equals(IdProducto)){
                oFunciones.MostrarAlerta(Contexto,"Este producto ya existe en esta orden","Agregar Producto");
                return;
            }

            Datos.add(new tVarios(oVarios.getValor1(),oVarios.getValor2(),oVarios.getValor3(),oVarios.getValor4(),oVarios.getValor5(),oVarios.getValor6(),oVarios.getValor7(),oVarios.getValor8(),"","","0","","","",""));
        }

        Datos.add(new tVarios(Nombre,"Cantidad: " + Cantidad + " Valor: " + String.valueOf(formatoImporte.format(Valor)) + " - Total: " + String.valueOf(formatoImporte.format(Valor * Long.parseLong(Cantidad))),IdProducto,Codigo,Nombre,Cantidad,String.valueOf(Valor),String.valueOf(Valor * Long.parseLong(Cantidad)),"","0","","","","",""));

        adapLis adapter = new adapLis(Contexto, Datos,false);

        Lista.setAdapter(adapter);
        fnCalcularValores();
    }
    private void fnModificarProducto(String IdProducto,String Codigo,String Nombre, String Cantidad, Long Valor){
        Funciones oFunciones = new Funciones();
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();
        Locale locale = new Locale("es","CL");
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(locale);



        for (int i = 0; i < Lista.getCount(); i++) {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);

            if (oVarios.getValor3().equals(IdProducto)){
                Datos.add(new tVarios(Nombre,"Cantidad: " + Cantidad + " Valor: " + String.valueOf(formatoImporte.format(Valor)) + " - Total: " + String.valueOf(formatoImporte.format(Valor * Long.parseLong(Cantidad))),IdProducto,Codigo,Nombre,Cantidad,String.valueOf(Valor),String.valueOf(Valor * Long.parseLong(Cantidad)),"","0","","","","",""));
            }else {
                Datos.add(new tVarios(oVarios.getValor1(), oVarios.getValor2(), oVarios.getValor3(), oVarios.getValor4(), oVarios.getValor5(), oVarios.getValor6(), oVarios.getValor7(), oVarios.getValor8(), "", "0", "", "", "", "", ""));
            }
        }

        adapLis adapter = new adapLis(Contexto, Datos,false);

        Lista.setAdapter(adapter);
        fnCalcularValores();
    }
    public void fnEscCliente(){
        Llamada="BUSCLIENTE";
        ActContenedor.Llamada = Llamada;
        Intent i = new Intent(Contexto, ActContenedor.class);
        startActivityForResult(i, 0);
    }
    private Long fnValorPagado(){
        bdPago oPago = new bdPago();

        Cursor cd =  oPago.Buscar(getActivity(),"0","0","0","0",IdCabVenta,"0");
        Long Valor = Long.parseLong("0");

        if (cd.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String vValor = cd.getString(cd.getColumnIndex("Valor"));

                Valor = Valor + Long.parseLong(vValor);
            } while (cd.moveToNext());
        }

        return Valor;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Nos aseguramos de que la actividad contenedora haya implementado la
        // interfaz de retrollamada. Si no, lanzamos una excepci�n
        try {
            oClaseCerrar.AsignarClase(activity);

        } catch (ClassCastException e) {
            Funciones oFunciones = new Funciones();

            oFunciones.MostrarAlerta(getActivity(), e.getMessage(), "sdf");
            throw new ClassCastException(e.getMessage() +  activity.toString()
                    + " debe implementar OnHeadlineSelectedListener" );
        }
    }

}
