package cl.issoft.www.gvenpro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by MarceloUsuario on 11-11-2017.
 */

public class
fraBusOrdenCompra extends Fragment {
    private ProgressDialog pd = null;
    MenuItem fav;
    private Context Contexto;
    String Llamada ="",Accion="",IdProveedor = "0";
    private TextView lblFechaDesde;
    private TextView lblProveedor;
    private TextView lblFechaHasta;
    private TextView lblFechaEstimadaCompraDesde;
    private TextView lblFechaEstimadaCompraHasta;
    private RadioButton rbtSolicitadaSi;
    private RadioButton rbtSolicitadaNo;
    private RadioButton rbtSolicitadaAmbos;
    private RadioButton rbtRecibidaSi;
    private RadioButton rbtRecibidaNo;
    private RadioButton rbtRecibidaAmbos;
    private RadioButton rbtPagadaSi;
    private RadioButton rbtPagadaNo;
    private RadioButton rbtPagadaAmbos;
    private RadioButton rbtFacturadaSi;
    private RadioButton rbtFacturadaNo;
    private RadioButton rbtFacturadaAmbos;
    private RadioButton rbtAnuladaSi;
    private RadioButton rbtAnuladaNo;
    private RadioButton rbtAnuladaAmbos;
    private Button btnEscProveedor;
    private Button btnEscFechaDesde;
    private Button btnEscFechaHasta;
    private Button btnEscFechaEstimadaCompraDesde;
    private Button btnEscFechaEstimadaCompraHasta;
    String LlamadaG = "";

    ClaseCerrar oClaseCerrar = new ClaseCerrar();

    public static fraBusOrdenCompra newInstance(Bundle arguments){
        fraBusOrdenCompra f = new fraBusOrdenCompra();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laybusordencompra, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
            LlamadaG = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }
        Contexto = getActivity();
        lblProveedor = (TextView) rootView.findViewById(R.id.lblProveedor);
        lblFechaDesde = (TextView) rootView.findViewById(R.id.lblFechaDesde);
        lblFechaHasta = (TextView)rootView.findViewById(R.id.lblFechaHasta);
        lblFechaEstimadaCompraDesde = (TextView) rootView.findViewById(R.id.lblFechaEstimadaCompraDesde);
        lblFechaEstimadaCompraHasta = (TextView)rootView.findViewById(R.id.lblFechaEstimadaCompraHasta);
        rbtSolicitadaSi = (RadioButton) rootView.findViewById(R.id.rbtSolicitadaSI);
        rbtSolicitadaNo = (RadioButton) rootView.findViewById(R.id.rbtSolicitadaNo);
        rbtSolicitadaAmbos = (RadioButton) rootView.findViewById(R.id.rbtSolicitadaAmbos);
        rbtRecibidaSi = (RadioButton) rootView.findViewById(R.id.rbtRecibidaSI);
        rbtRecibidaNo = (RadioButton) rootView.findViewById(R.id.rbtRecibidaNo);
        rbtRecibidaAmbos = (RadioButton) rootView.findViewById(R.id.rbtRecibidaAmbos);
        rbtPagadaSi = (RadioButton) rootView.findViewById(R.id.rbtPagadaSI);
        rbtPagadaNo = (RadioButton) rootView.findViewById(R.id.rbtPagadaNo);
        rbtPagadaAmbos = (RadioButton) rootView.findViewById(R.id.rbtPagadaAmbos);
        rbtFacturadaSi = (RadioButton) rootView.findViewById(R.id.rbtFacturadaSI);
        rbtFacturadaNo = (RadioButton) rootView.findViewById(R.id.rbtFacturadaNo);
        rbtFacturadaAmbos = (RadioButton) rootView.findViewById(R.id.rbtFacturadaAmbos);
        rbtAnuladaSi = (RadioButton) rootView.findViewById(R.id.rbtAnuladaSI);
        rbtAnuladaNo = (RadioButton) rootView.findViewById(R.id.rbtAnuladaNo);
        rbtAnuladaAmbos = (RadioButton) rootView.findViewById(R.id.rbtAnuladaAmbos);
        btnEscProveedor = (Button)rootView.findViewById(R.id.btnProveedor);
        btnEscFechaDesde = (Button)rootView.findViewById(R.id.btnEscFechaDesde);
        btnEscFechaHasta = (Button)rootView.findViewById(R.id.btnEscFechaHasta);
        btnEscFechaEstimadaCompraDesde = (Button)rootView.findViewById(R.id.btnEscFechaEstimadaCompraDesde);
        btnEscFechaEstimadaCompraHasta = (Button)rootView.findViewById(R.id.btnEscFechaEstimadaCompraHasta);

        getActivity().setTitle("Búsqueda Orden Compra");

        btnEscProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscProveedor();
            }
        });
        btnEscFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscFechaDesde();
            }
        });
        btnEscFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscFechaHasta();
            }
        });
        btnEscFechaEstimadaCompraHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                fnEscFechaEstimadaCompraHasta();
            }
        });
        btnEscFechaEstimadaCompraDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                fnEscFechaEstimadaCompraDesde();
            }
        });

        if (!LlamadaG.equals("BUSCAR")) {
            rbtSolicitadaAmbos.setEnabled(false);
            rbtSolicitadaSi.setEnabled(false);
            rbtSolicitadaNo.setEnabled(false);
            rbtRecibidaAmbos.setEnabled(false);
            rbtRecibidaSi.setEnabled(false);
            rbtRecibidaNo.setEnabled(false);
            rbtPagadaAmbos.setEnabled(false);
            rbtPagadaSi.setEnabled(false);
            rbtPagadaNo.setEnabled(false);
            rbtFacturadaAmbos.setEnabled(false);
            rbtFacturadaSi.setEnabled(false);
            rbtFacturadaNo.setEnabled(false);
            rbtAnuladaAmbos.setEnabled(false);
            rbtAnuladaSi.setEnabled(false);
            rbtAnuladaNo.setEnabled(false);
        }
        if (LlamadaG.equals("SOLICITADA")){rbtSolicitadaNo.setChecked(true);}
        if (LlamadaG.equals("RECIBIDA")){rbtRecibidaNo.setChecked(true);}
        if (LlamadaG.equals("PAGADA")){rbtPagadaNo.setChecked(true);}
        if (LlamadaG.equals("FACTURADA")){rbtFacturadaNo.setChecked(true);}
        if (LlamadaG.equals("ANULADA")){rbtAnuladaNo.setChecked(true);}

        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnBuscar(){
        bdProducto oProducto = new bdProducto();
        Funciones oFunciones = new Funciones();
        Llamada = "BUSCAR";
        if(lblFechaDesde.getText().toString().trim().equals("") && lblFechaEstimadaCompraDesde.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Fecha Desde o Fecha Estimada Compra Desde","Guardar");
            return;
        }

        Intent i = new Intent(getActivity(), ActBusOrdenCompraDet.class);
        i.putExtra("Llamada",LlamadaG);
        i.putExtra("IdProveedor",IdProveedor);
        i.putExtra("FechaDesde",lblFechaDesde.getText().toString().trim());
        i.putExtra("FechaHasta",lblFechaHasta.getText().toString().trim());
        i.putExtra("FechaEstimadaCompraDesde",lblFechaEstimadaCompraDesde.getText().toString().trim());
        i.putExtra("FechaEstimadaCompraHasta",lblFechaEstimadaCompraHasta.getText().toString().trim());

        String Solicitada = "T";
        if (rbtSolicitadaSi.isChecked()){Solicitada="S";}
        if (rbtSolicitadaNo.isChecked()){Solicitada="N";}

        String Recibida = "T";
        if (rbtRecibidaSi.isChecked()){Recibida="S";}
        if (rbtRecibidaNo.isChecked()){Recibida="N";}

        String Pagada = "T";
        if (rbtPagadaSi.isChecked()){Pagada="S";}
        if (rbtPagadaNo.isChecked()){Pagada="N";}

        String Facturada = "T";
        if (rbtFacturadaSi.isChecked()){Facturada="S";}
        if (rbtFacturadaNo.isChecked()){Facturada="N";}

        String Anulada = "T";
        if (rbtAnuladaSi.isChecked()){Anulada="S";}
        if (rbtAnuladaNo.isChecked()){Anulada="N";}

        i.putExtra("Solicitada",Solicitada);
        i.putExtra("Recibida",Recibida);
        i.putExtra("Pagada",Pagada);
        i.putExtra("Facturada",Facturada);
        i.putExtra("Anulada",Anulada);

        startActivityForResult(i, 0);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        fav = menu.add(0,0,0,"Buscar");

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
            fnBuscar();

        }else if(id == 1){

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == -1) {
            if (Llamada.equals("BUSPROVEEDOR")){
                lblProveedor.setText(data.getStringExtra("Rut") + " - " + data.getStringExtra("Nombre"));
                IdProveedor = data.getStringExtra("IdProveedor");
            }else if (Llamada.equals("ESCFECHADESDE")){
                lblFechaDesde.setText(data.getStringExtra("Fecha"));
            }else if (Llamada.equals("ESCFECHAHASTA")){
                lblFechaHasta.setText(data.getStringExtra("Fecha"));
            }else if (Llamada.equals("ESCFECHAESTIMADACOMPRADESDE")){
                lblFechaEstimadaCompraDesde.setText(data.getStringExtra("Fecha"));
            }else if (Llamada.equals("ESCFECHAESTIMADACOMPRAHASTA")){
                lblFechaEstimadaCompraHasta.setText(data.getStringExtra("Fecha"));
            }else {
                String IdCabOrdenCompra = data.getStringExtra("IdCabOrdenCompra");

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle arguments = new Bundle();

                arguments.putString("IdCabOrdenCompra", IdCabOrdenCompra);
                arguments.putString("Llamada", "LECTURA");
                fraOrdenCompra fragment = fraOrdenCompra.newInstance(arguments);
                transaction.replace(R.id.container, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        }
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
    public void fnEscProveedor(){
        Llamada="BUSPROVEEDOR";
        ActContenedor.Llamada = Llamada;
        Intent i = new Intent(Contexto, ActContenedor.class);
        startActivityForResult(i, 0);
    }
    public void fnEscFechaDesde(){
        Llamada = "ESCFECHADESDE";
        Intent i = new Intent(getActivity(), ActEscFecha.class);
        startActivityForResult(i, 0);
    }
    public void fnEscFechaHasta(){
        Llamada = "ESCFECHAHASTA";
        Intent i = new Intent(getActivity(), ActEscFecha.class);
        startActivityForResult(i, 0);
    }
    public void fnEscFechaEstimadaCompraDesde(){
        Llamada = "ESCFECHAESTIMADACOMPRADESDE";
        Intent i = new Intent(getActivity(), ActEscFecha.class);
        startActivityForResult(i, 0);
    }
    public void fnEscFechaEstimadaCompraHasta(){
        Llamada = "ESCFECHAESTIMADACOMPRAHASTA";
        Intent i = new Intent(getActivity(), ActEscFecha.class);
        startActivityForResult(i, 0);
    }
}
