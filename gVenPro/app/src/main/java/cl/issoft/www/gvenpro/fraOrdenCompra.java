package cl.issoft.www.gvenpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MarceloUsuario on 11-09-2017.
 */

public class fraOrdenCompra  extends Fragment {
    private ProgressDialog pd = null;
    private Menu menuG;
    private Context Contexto;
    private ListView Lista;
    private TextView lblProveedor,lblTotal;
    private String Llamada = "INICIO";
    public static String LlamadaG;
    private  int Fila = 0;
    private String IdProveedor = "0";
    private  String IdDetOrdenCompra = "0";
    private Button btnEscProveedor;
    private static int MenuNuevo = 0;
    private static int MenuLimpiar = 1;
    private static int MenuGuardar = 2;
    private static int MenuEliminar = 3;
    private static int MenuIva = 4;
    private boolean bGenerar = false;
    private int Cantidad = 0;
    private float Valor = 0;
    private String IdProducto = "0",Total = "",ColEst ="",NomEst="",TotalAPagar = "0",CantidadTotal = "0",vTotalVenta = "0";
    private String Accion;
    private String gTotal,gIva,gValor,gCantidad = "0";
    public String IdCabOrdenCompra = "0";
    public boolean bIvaIncluido = true;

    public static fraOrdenCompra newInstance(Bundle arguments){
        fraOrdenCompra f = new fraOrdenCompra();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layordencompra, container, false);

        Contexto = getActivity();
        Lista = (ListView)rootView.findViewById(R.id.lisDatos);
        lblProveedor = (TextView)rootView.findViewById(R.id.lblProveedor);
        lblTotal = (TextView)rootView.findViewById(R.id.lblTotal);
        btnEscProveedor = (Button)rootView.findViewById(R.id.btnProveedor);
        lblTotal.setText("N: 0 - I: 0 - T: 0");
        btnEscProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscProveedor();
            }
        });
        LlamadaG = getArguments().getString("Llamada");

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

        IdCabOrdenCompra = getArguments().getString("IdCabOrdenCompra");

        if (!IdCabOrdenCompra.equals("0")){
            LlenarDatos();
        }
        setHasOptionsMenu(true);
        return rootView;


    }
    private void LlenarDatos(){
        bdCabOrdenCompra oCabOrdenCompra = new bdCabOrdenCompra();
        bdDetOrdenCompra oDetOrdenCompra = new bdDetOrdenCompra();

        this.pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);

        Cursor c =  oCabOrdenCompra.Buscar(getActivity(),IdCabOrdenCompra,"0","0","0","0","0","T","T","T","T","T");

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();
        Long Suma = Long.parseLong("0");

        if (c.moveToFirst()) {
            IdProveedor = c.getString(c.getColumnIndex("IdProveedor"));
            lblProveedor.setText(c.getString(c.getColumnIndex("NomPro")));
            lblTotal.setText(c.getString(c.getColumnIndex("ValorNeto")));

        }

        Cursor cd =  oDetOrdenCompra.Buscar(getActivity(),"0",IdCabOrdenCompra,"0");

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

        CrearMenu(menu);
    }
    private void CrearMenu(Menu menu){
        menu.clear();

        if (!LlamadaG.equals("LECTURA")) {
            menu.add(0, MenuNuevo, 0, "Nuevo Producto");
            menu.add(0, MenuLimpiar, 0, "Limpiar Orden");
            menu.add(0, MenuEliminar, 0, "Eliminar Producto");
            menu.add(0, MenuGuardar, 0, "Guardar");
            menu.add(0, MenuIva, 0, "&Iva Incluido");
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == MenuNuevo) {
            if (IdProveedor.equals("0")){
                Funciones oFunciones = new Funciones();

                oFunciones.MostrarAlerta(getActivity(),"Depe especificar Proveedor","Escoger Producto");
                return false;
            }
            Llamada="BUSPRODUCTO";
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

        if (id == MenuIva){
            if (item.getTitle().toString().substring(0,1).equals("&")){
                bIvaIncluido = false;
                item.setTitle("Iva Incluido");
            }else{
                item.setTitle("&Iva Incluido");
                bIvaIncluido = true;
            }
            fnCalcularValores();
        }

        return super.onOptionsItemSelected(item);
    }
    private void Guardar(){
        Funciones oFunciones = new Funciones();

        if (IdProveedor.equals("0") || IdProveedor.equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar proveedor","Guardar");
            return;
        }

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
        Date myDate = new Date();
        String Fecha = timeStampFormat.format(myDate);

        bdCabOrdenCompra oCabOrdenCompra = new bdCabOrdenCompra();
        Long IdCabOrdenCompra = oCabOrdenCompra.Agregar(IdProveedor,Fecha,String.valueOf(Integer.parseInt(Fecha)+3),String.valueOf(Math.round(Long.parseLong(gCantidad))),Long.parseLong(gValor),"N","N","N",Long.parseLong(gIva),Long.parseLong(gTotal),"N","0","N",getContext());

        for (int i = 0; i < Lista.getCount(); i++) {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);

            bdDetOrdenCompra oDetOrdenCompra = new bdDetOrdenCompra();

            oDetOrdenCompra.Agregar(String.valueOf(IdCabOrdenCompra), oVarios.getValor3(),oVarios.getValor6(),Long.parseLong(oVarios.getValor7()),"N",getContext());
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
            if (Llamada.equals("BUSPROVEEDOR")){
                lblProveedor.setText(data.getStringExtra("Rut") + " - " + data.getStringExtra("Nombre"));
                IdProveedor = data.getStringExtra("IdProveedor");
            }else if (Llamada.equals("BUSPRODUCTO")){
                fnAgregarProducto(data.getStringExtra("IdProducto"),data.getStringExtra("Codigo"),data.getStringExtra("Nombre"),data.getStringExtra("Cantidad"),Long.parseLong(data.getStringExtra("Valor")));
            }else if (Llamada.equals("MODIFICAR")){
                fnModificarProducto(data.getStringExtra("IdProducto"),data.getStringExtra("Codigo"),data.getStringExtra("Nombre"),data.getStringExtra("Cantidad"),Long.parseLong(data.getStringExtra("Valor")));
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

            Valor = Long.parseLong(oVarios.getValor8());
            Iva = (Valor * 19)/100;

            if (bIvaIncluido){
                Total = Valor;
                Valor = Valor - Iva;
            }else{
                Total = Valor + Iva;
            }
            gCantidad = String.valueOf(Long.parseLong(gCantidad) + Long.parseLong(oVarios.getValor6()));
        }
        gValor = String.valueOf(Valor);
        gIva = String.valueOf(Iva);
        gTotal = String.valueOf(Total);

        lblTotal.setText("N: " + String.valueOf(formatoImporte.format(Valor)) + " - I: " + String.valueOf(formatoImporte.format(Iva)) + " - T: " + String.valueOf(formatoImporte.format(Total)));
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
    public void fnEscProveedor(){
        Llamada="BUSPROVEEDOR";
        ActContenedor.Llamada = Llamada;
        Intent i = new Intent(Contexto, ActContenedor.class);
        startActivityForResult(i, 0);
    }
}
