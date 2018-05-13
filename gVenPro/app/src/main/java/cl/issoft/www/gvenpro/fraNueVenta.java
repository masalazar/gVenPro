package cl.issoft.www.gvenpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by MarceloUsuario on 04-30-2018.
 */

public class fraNueVenta extends Fragment {
    ClaseCerrar oClaseCerrar = new ClaseCerrar();
    private ProgressDialog pd = null;
    private Menu menuG;
    private String gPendiente = "S", gPagada = "N", EsContenedor = "N";
    private Context Contexto;
    private String ValorPagado = "0", ValorPendiente = "0";
    private TextView lblCliente, lblTotal;
    private String Llamada = "INICIO";
    public static String LlamadaG;
    private int Fila = 0;
    private String IdCliente = "0";
    private String IdDetVenta = "0";
    private Button btnEscCliente;
    private static int MenuEntregar = 0;
    private static int MenuPagar = 1;
    private static int MenuFinalizar = 2;
    private boolean bGenerar = false;
    private int Cantidad = 0;
    private float Valor = 0;
    private String IdProducto = "0", Total = "", ColEst = "", NomEst = "", TotalAPagar = "0", CantidadTotal = "0", vTotalVenta = "0";
    private String Accion;
    private String gTotal, gIva, gValor, gCantidad = "0";
    public Long IdCabVenta = Long.parseLong("0");
    public boolean bIvaIncluido = true;
    private boolean bGuardado = false;
    private ExpandableListAdapter listAdapter;
    private List<tVarios> listDataHeader;
    private HashMap<tVarios, List<tVarios>> listHash;
    private View rootView;
    ExpandableListView listView;
    boolean PermiteGuardar = true;

    public static fraNueVenta newInstance(Bundle arguments) {
        fraNueVenta f = new fraNueVenta();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.laynueventa, container, false);

        Contexto = getActivity();
        lblCliente = (TextView) rootView.findViewById(R.id.lblCliente);
        lblTotal = (TextView) rootView.findViewById(R.id.lblTotal);
        btnEscCliente = (Button) rootView.findViewById(R.id.btnCliente);
        lblTotal.setText("T: 0 - PA: 0 - PE: 0");
        btnEscCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscCliente();
            }
        });
        LlamadaG = getArguments().getString("Llamada");
        EsContenedor = getArguments().getString("EsContenedor");
        listView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

        Context c = getActivity();
        LlamadaG = getArguments().getString("Llamada");
        EsContenedor = getArguments().getString("EsContenedor");

        IdCabVenta = Long.parseLong(getArguments().getString("IdCabVenta"));

        if (IdCabVenta>0){
            PermiteGuardar = false;
            LlenarDatos();

        }else{
            listView.setEnabled(false);
            getActivity().setTitle("No Entregado y No Pagado");
            bdFamilia oFamilia = new bdFamilia();
            Cursor cFamilia = oFamilia.Buscar(c, "0", "");
            listDataHeader = new ArrayList<>();
            listHash = new HashMap<>();
            int i = 0;
            if (cFamilia.moveToFirst()) {
                do {
                    listDataHeader.add(new tVarios(cFamilia.getString(cFamilia.getColumnIndex("Nombre")), "", cFamilia.getString(cFamilia.getColumnIndex("IdFamilia")), "", "", "0", "0", "0", "", "", "", "", "", "", "F"));



                    bdStock oStock = new bdStock();
                    Cursor cCursor = oStock.BuscarVenta(c, "0", "", "",cFamilia.getString(cFamilia.getColumnIndex("IdFamilia")), "S");
                    final tVarios[] ITEMS = new tVarios[cCursor.getCount()];
                    List<tVarios> Datos = new ArrayList<>();
                    if (cCursor.moveToFirst()) {
                        do {
                            String IdDetVenta = "0";
                            String IdProducto = cCursor.getString(cCursor.getColumnIndex("IdProducto"));
                            String Codigo = cCursor.getString(cCursor.getColumnIndex("Codigo"));
                            String Nombre = cCursor.getString(cCursor.getColumnIndex("Nombre"));
                            String Imagen = cCursor.getString(cCursor.getColumnIndex("Imagen"));
                            String vCantidad = "0";
                            String Valor = cCursor.getString(cCursor.getColumnIndex("ValorVenta"));

                            Locale locale = new Locale("es", "CL");
                            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                            String vValor = nf.format(Long.parseLong(Valor));
                            String vTotal = nf.format(Long.parseLong(Valor) * Long.parseLong(vCantidad));
                            String vTotalSF = String.valueOf(Long.parseLong(Valor) * Long.parseLong(vCantidad));

                            Datos.add(new tVarios(Nombre, "Cantidad: " + vCantidad + " - Valor: " + vValor + " - TotaL:" + vTotal, IdProducto, Codigo, Nombre, vCantidad, Valor, vTotalSF, Imagen, "", IdDetVenta, "", "", "", "P"));
                        } while (cCursor.moveToNext());
                    }

                    listHash.put(listDataHeader.get(i),Datos);
                    i = i + 1;

                } while (cFamilia.moveToNext());

                listAdapter = new ExpandableListAdapter(c,listDataHeader,listHash);
                listView.setAdapter(listAdapter);
            }
        }

        setHasOptionsMenu(true);
        return rootView;
    }
    private void RevisarTitulo(){
        String Titulo = "";
        if (gPendiente.equals("S")){
            Titulo = "No Entregado";
        }else{
            Titulo = "Entregado";
        }
        if (gPagada.equals("S")){
            Titulo = Titulo + " y Pagado";
        }else{
            Titulo = Titulo + " y No Pagado";
        }
        getActivity().setTitle(Titulo);
    }
    private void LlenarDatos(){
        bdCabVenta oCabVenta = new bdCabVenta();
        bdDetVenta oDetVenta = new bdDetVenta();
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        this.pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);

        Cursor c =  oCabVenta.Buscar(getActivity(),String.valueOf(IdCabVenta),"0","0","0","T","T","T","T");

        Long Suma = Long.parseLong("0");

        if (c.moveToFirst()) {
            IdCliente = c.getString(c.getColumnIndex("IdCliente"));
            lblCliente.setText(c.getString(c.getColumnIndex("NomCli")));
            lblTotal.setText(c.getString(c.getColumnIndex("ValorNeto")));

            if (c.getString(c.getColumnIndex("Pendiente")).equals("N")){
                btnEscCliente.setEnabled(false);
            }


            gPendiente=c.getString(c.getColumnIndex("Pendiente"));
            gPagada=c.getString(c.getColumnIndex("Pagada"));
            RevisarTitulo();

        }

        Cursor cd =  oDetVenta.Buscar(getActivity(),"0",String.valueOf(IdCabVenta),"0");
        String IdFamilia = "0";
        Long SumaFam = Long.parseLong("0");
        int i = 0;
        if (cd.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                ArrayList<tVarios> Datos = new ArrayList<tVarios>();
                if (IdFamilia.equals(cd.getString(cd.getColumnIndex("IdFamilia")))){

                }else{

                    listDataHeader.add(new tVarios(cd.getString(cd.getColumnIndex("NomFam")), "", cd.getString(cd.getColumnIndex("IdFamilia")), "", "", "0", "0", "0", "", "", "", "", "", "", "F"));
                    i = i + 1;


                    Suma = Long.parseLong("0");
                }

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

                listHash.put(listDataHeader.get(i-1),Datos);

            } while(cd.moveToNext());

            listAdapter = new ExpandableListAdapter(getActivity(),listDataHeader,listHash);
            listView.setAdapter(listAdapter);

            Locale locale = new Locale("es","CL");
            NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
            String vSuma = nf.format(Suma);

            lblTotal.setText(vSuma);
            ValorPagado = String.valueOf(fnValorPagado());
            fnCalcularValores();
        }
        PermiteGuardar=false;
        pd.dismiss();
    }
    public void fnEscCliente() {
        Llamada = "BUSCLIENTE";
        ActContenedor.Llamada = Llamada;
        Intent i = new Intent(Contexto, ActContenedor.class);
        startActivityForResult(i, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (gPendiente.equals("S")) {
            menu.add(0, MenuEntregar, 0, "Entregar Productos");
        }
        if (gPagada.equals("N")) {
            menu.add(0, MenuPagar, 0, "Realizar Pago");
        }
        if (PermiteGuardar) {
            menu.add(0, MenuFinalizar, 0, "Finalizar");
        }
        menuG = menu;
    }
    private void RevisarMenu(){
        if (gPendiente.equals("N")){
            menuG.removeItem(MenuEntregar);
        }

        if (gPagada.equals("S")){
            menuG.removeItem(MenuPagar);
        }

        if (PermiteGuardar){

        }else{
            menuG.removeItem(MenuFinalizar);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (Llamada.equals("BUSCLIENTE")) {
                lblCliente.setText(data.getStringExtra("Rut") + " - " + data.getStringExtra("Nombre"));
                IdCliente = data.getStringExtra("IdCliente");
                listView.setEnabled(true);
            } else if (Llamada.equals("BUSPRODUCTOVENTA")) {
                //fnAgregarProducto(data.getStringExtra("IdProducto"),data.getStringExtra("Codigo"),data.getStringExtra("Nombre"),data.getStringExtra("Cantidad"),Long.parseLong(data.getStringExtra("Valor")));
            } else if (Llamada.equals("MODIFICAR")) {
                fnModificarProducto(data.getStringExtra("IdProducto"), data.getStringExtra("Codigo"), data.getStringExtra("Nombre"), data.getStringExtra("Cantidad"), Long.parseLong(data.getStringExtra("Valor")));
            } else if (Llamada.equals("PAGAR")) {
                if (data.getStringExtra("Pagado").equals("S")) {
                    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
                    Date myDate = new Date();
                    String Fecha = timeStampFormat.format(myDate);

                    String vValorPagado = String.valueOf(fnValorPagado());
                    ValorPagado = vValorPagado;

                    if (Long.parseLong(vValorPagado) == Long.parseLong(gTotal)){
                        gPagada = "S";
                    }
                    RevisarTitulo();
                    fnCalcularValores();
                    RevisarMenu();
                }
            }
        }
    }

    private Long fnValorPagado() {
        bdPago oPago = new bdPago();

        Cursor cd = oPago.Buscar(getActivity(), "0", "0", "0", "0", String.valueOf(IdCabVenta), "0");
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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == MenuEntregar) {
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
                    Cancelar();
                }
            });
            builder.show();
            return true;
        }
        if (id == MenuPagar) {
            Pagar();
            return true;
        }

        if (id == MenuFinalizar) {
            Finalizar(true);

            RevisarMenu();
        }


        return super.onOptionsItemSelected(item);
    }
    private void Pagar(){
        if (PermiteGuardar){Finalizar(false);}
        Llamada="PAGAR";
        Intent i = new Intent(Contexto, ActPago.class);
        i.putExtra("Llamada",Llamada);
        i.putExtra("IdCabVenta",String.valueOf(IdCabVenta));
        String Deuda = String.valueOf(Long.parseLong(gTotal) - Long.parseLong(ValorPagado));
        i.putExtra("Deuda",String.valueOf(Deuda));
        startActivityForResult(i, 0);
    }
    private void Cancelar() {

    }
    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<tVarios> listDataHeader;
        private HashMap<tVarios,List<tVarios>> listHashMap;

        public ExpandableListAdapter(Context context, List<tVarios> listDataHeader, HashMap<tVarios, List<tVarios>> listHashMap) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listHashMap = listHashMap;
        }

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return listHashMap.get(listDataHeader.get(i)).size();
        }

        @Override
        public tVarios getGroup(int i) {

            return listDataHeader.get(i);
        }
        public void Limpiar() {

            listDataHeader.clear();
            listHashMap.clear();
        }
        @Override
        public tVarios getChild(int i, int i1) {
            return listHashMap.get(listDataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            tVarios headerTitle = (tVarios) getGroup(i);
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_group,null);
            }
            TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle.getValor1());

            if (headerTitle.getValor14().equals("S")){
                lblListHeader.setTextColor(Color.RED);
            }

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            final tVarios childText = (tVarios) getChild(i,i1);
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item,null);
            }

            final int PosPapa = i;
            final int PosHijo = i1;
            TextView txtListChild = (TextView)view.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getValor1() + " - " + childText.getValor2());
            txtListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fnClicItem(PosPapa,PosHijo);
                }
            });

            if (childText.getValor14().equals("S")){
                txtListChild.setTextColor(Color.RED);
            }

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
    private void fnClicItem(int i,int j){
        if(PermiteGuardar){

        }else{
            return;
        }
        tVarios oVarios = listAdapter.getChild(i,j);
        Llamada = "MODIFICAR";
        Intent Intent = new Intent(Contexto, ActEditProdOrden.class);
        Intent.putExtra("Llamada", "MODIFICAR");
        Intent.putExtra("IdProducto", oVarios.getValor3());
        Intent.putExtra("Codigo", oVarios.getValor4());
        Intent.putExtra("Nombre", oVarios.getValor5());
        Intent.putExtra("Cantidad", oVarios.getValor6());
        Intent.putExtra("Valor", oVarios.getValor7());
        startActivityForResult(Intent, 0);

        /*
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                tVarios oVarios = listAdapter.getChild(i,j);
                Funciones oFunciones = new Funciones();
                oFunciones.MostrarAlerta(getActivity(), oVarios.getValor1(), "sd");
            }
        }*/
    }
    private void fnModificarProducto(String IdProducto,String Codigo,String Nombre, String Cantidad, Long Valor){
        Funciones oFunciones = new Funciones();
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        Locale locale = new Locale("es","CL");
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(locale);

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            tVarios oCabecera = listAdapter.getGroup(i);
            List<tVarios> Datos = new ArrayList<>();

            Long CantidadC = Long.parseLong("0");
            Long TotalC = Long.parseLong("0");
            String ColorC = "N";
            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                tVarios oVarios = listAdapter.getChild(i,j);
                String Color = "N";

                if (oVarios.getValor3().equals(IdProducto)){
                    if (Long.parseLong(Cantidad) > 0){Color = "S";ColorC="S";}
                    Datos.add(new tVarios(Nombre,"Cantidad: " + Cantidad + " Valor: " + String.valueOf(formatoImporte.format(Valor)) + " - Total: " + String.valueOf(formatoImporte.format(Valor * Long.parseLong(Cantidad))),IdProducto,Codigo,Nombre,Cantidad,String.valueOf(Valor),String.valueOf(Valor * Long.parseLong(Cantidad)),"","0","","","",Color,""));
                    CantidadC = CantidadC + Long.parseLong(Cantidad);
                    TotalC = TotalC + (Valor * Long.parseLong(Cantidad));
                }else {
                    if (Long.parseLong(oVarios.getValor6()) > 0){Color = "S";ColorC="S";}
                    Datos.add(new tVarios(oVarios.getValor1(), oVarios.getValor2(), oVarios.getValor3(), oVarios.getValor4(), oVarios.getValor5(), oVarios.getValor6(), oVarios.getValor7(), oVarios.getValor8(), "", "0", "", "", "", "", ""));
                    CantidadC = CantidadC + Long.parseLong(oVarios.getValor6());
                    TotalC = TotalC + (Long.parseLong(oVarios.getValor6()) * Long.parseLong(oVarios.getValor7()));
                }
            }

            listDataHeader.add(new tVarios(oCabecera.getValor1() + " - Cantidad: " + String.valueOf(formatoImporte.format(CantidadC)) + " Total: " + String.valueOf(formatoImporte.format(TotalC)), "", oCabecera.getValor2(), "", "", String.valueOf(CantidadC), "0", String.valueOf(TotalC), "","", "", "", "", ColorC, "F"));
            listHash.put(listDataHeader.get(i),Datos);
        }
        listAdapter.Limpiar();
        
        listAdapter = new ExpandableListAdapter(getActivity(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);
        fnCalcularValores();
    }
    private void fnCalcularValores(){
        Locale locale = new Locale("es","CL");
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(locale);
        Long Iva = Long.parseLong("0");
        Long Valor = Long.parseLong("0");
        Long Total = Long.parseLong("0");
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                tVarios oVarios = listAdapter.getChild(i,j);

                Valor = Valor + Long.parseLong(oVarios.getValor8());
                gCantidad = String.valueOf(Long.parseLong(gCantidad) + Long.parseLong(oVarios.getValor6()));
            }

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
    private boolean Finalizar(boolean MuestraMensaje){
        bdCabVenta oCabVenta = new bdCabVenta();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd");
        Date myDate = new Date();
        String Fecha = timeStampFormat.format(myDate);

        if (IdCabVenta == 0){
            IdCabVenta = oCabVenta.Agregar(IdCliente,Fecha,String.valueOf(Math.round(Long.parseLong(gCantidad))),Long.parseLong(gValor), "S","N","N",Long.parseLong(gIva),Long.parseLong(gTotal),"0","N",getContext());
        }else{
            oCabVenta.Modificar(String.valueOf(IdCabVenta),String.valueOf(Math.round(Long.parseLong(gCantidad))),Long.parseLong(gValor),Long.parseLong(gIva),Long.parseLong(gTotal),getContext());
        }

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {

            for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                tVarios oVarios = listAdapter.getChild(i, j);

                bdDetVenta oDetVenta = new bdDetVenta();

                if(Long.parseLong(oVarios.getValor6()) > 0){
                    oDetVenta.Agregar(String.valueOf(IdCabVenta), oVarios.getValor3(), oVarios.getValor6(), Long.parseLong(oVarios.getValor7()),"N", getContext());
                }
            }
        }

        PermiteGuardar=false;
        if (MuestraMensaje){
            Funciones oFunciones = new Funciones();

            oFunciones.MostrarAlerta(getActivity(),"La operación se realizo correctamente","Finalizar");
        }
        return true;
    }
    private void AceptarEntregar(){
        if (PermiteGuardar){Finalizar(false);};
        bdCabVenta oCabVenta = new bdCabVenta();
        oCabVenta.Entregar(String.valueOf(IdCabVenta),getActivity());

        gPendiente="N";
        RevisarMenu();
        RevisarTitulo();
        Funciones oFunciones = new Funciones();
        oFunciones.MostrarAlerta(getActivity(),"Productos entregados","Entregar Productos");
    }
}
