package cl.issoft.www.ismaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by MarceloUsuario on 08-11-2017.
 */

public class fraBusRecaudacion extends Fragment {
    private ListView Lista;
    private ProgressDialog pd = null;
    MenuItem fav;
    TextView lblFechaDesde;
    TextView lblFechaHasta;
    String Llamada ="",Accion="";
    int ContadorFila = 0;
    private Button btnEscFechaDesde;
    private Button btnEscFechaHasta;
    private Spinner cboRuta;


    ArrayList<tVarios> DatosEliminar;

    public static fraBusRecaudacion newInstance(Bundle arguments){
        fraBusRecaudacion f = new fraBusRecaudacion();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laybusrecaudacion, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        getActivity().setTitle("LOTE: " + fraManRecaudacion.Lote);
        Lista = (ListView) rootView.findViewById(R.id.lisDatos);
        lblFechaDesde = (TextView) rootView.findViewById(R.id.lblFechaDesde);
        lblFechaHasta = (TextView) rootView.findViewById(R.id.lblFechaHasta);
        btnEscFechaDesde = (Button)rootView.findViewById(R.id.btnEscFechaDesde);
        btnEscFechaHasta = (Button)rootView.findViewById(R.id.btnEscFechaHasta);
        btnEscFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscFechaHasta();
            }
        });
        btnEscFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscFechaDesde();
            }
        });
        cboRuta = (Spinner) rootView.findViewById(R.id.cboRuta);

        LinkedList Rutas = new LinkedList();
        ActRutaBD oRuta = new ActRutaBD();
        Cursor cRuta = oRuta.BuscarRuta(getContext(),"0");

        Rutas.add((new ObjNomyID(0,"<<SELECCIONE>>")));
        if (cRuta.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre = cRuta.getString(cRuta.getColumnIndex("Nombre"));
                String IdRuta = cRuta.getString(cRuta.getColumnIndex("IdRuta"));

                Rutas.add(new ObjNomyID(Integer.parseInt(IdRuta),Nombre));

            } while (cRuta.moveToNext());
        }

        ArrayAdapter spinner_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, Rutas);
        //Añadimos el layout para el menú y se lo damos al spinner
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboRuta.setAdapter(spinner_adapter);

        setHasOptionsMenu(true);
        return rootView;
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
    private void fnBuscar(){
        Funciones oFunciones = new Funciones();
        if (lblFechaDesde.getText().toString().trim().equals("Fecha Desde") && lblFechaHasta.getText().toString().trim().equals("Fecha Hasta")){
            oFunciones.MostrarAlerta(getActivity(),"Debe especificar Fecha Desde o Fecha Hasta","Buscar");
            return;
        }

        String FechaDesde = "19000101";
        String FechaHasta = "29000101";
        if (!lblFechaDesde.getText().toString().trim().equals("")){
            FechaDesde = lblFechaDesde.getText().toString().trim();
            FechaDesde = FechaDesde.substring(6,10) + FechaDesde.substring(3,5) + FechaDesde.substring(0,2);
        }
        if (!lblFechaHasta.getText().toString().trim().equals("")){
            FechaHasta = lblFechaHasta.getText().toString().trim();
            FechaHasta = FechaHasta.substring(6,10) + FechaHasta.substring(3,5) + FechaHasta.substring(0,2);
        }

        Integer IdRuta = ((ObjNomyID) cboRuta.getItemAtPosition(cboRuta.getSelectedItemPosition())).getId();

        this.pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);
        ActRecaudacionBD oRecaudacion = new ActRecaudacionBD();
        Cursor c =  oRecaudacion.BuscarRecaudacion(getActivity(),"-TOD",FechaDesde,FechaHasta,String.valueOf(IdRuta),fraManRecaudacion.Lote);
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();
        long Suma = Long.parseLong("0");
        long SumaE = Long.parseLong("0");
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre= c.getString(c.getColumnIndex("Nombre"));
                String Codigo= c.getString(c.getColumnIndex("Codigo"));
                String Subido= c.getString(c.getColumnIndex("Subido"));
                String Monto= c.getString(c.getColumnIndex("Monto"));
                String Fecha= c.getString(c.getColumnIndex("Fecha"));
                String IdLocal= c.getString(c.getColumnIndex("IdLocal"));
                String Estimado= c.getString(c.getColumnIndex("Estimado"));
                String Identificador= c.getString(c.getColumnIndex("Identificador"));

                Fecha = Fecha.substring(6,8) + '/' + Fecha.substring(4,6) + '/' + Fecha.substring(0,4);

                Locale locale = new Locale("es","CL");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                String Valor =  nf.format(Float.parseFloat(Monto));
                String ValorE = nf.format(Float.parseFloat(Estimado));
                Datos.add(new tVarios(Nombre,Fecha + " - R " + Valor + " - E " + ValorE + " - " + Subido + " - " + IdLocal + "/" + Identificador,"","","","","","","","","","","","",""));
                Suma = Suma + Long.parseLong(Monto);
                SumaE = SumaE + Long.parseLong(Estimado);

            } while(c.moveToNext());

            Datos.add(new tVarios("TOTAL","R" + String.valueOf(Suma) + " - E " + String.valueOf(SumaE),"","","","","","","","","","","","",""));
            adapLis adapter = new adapLis(getActivity(), Datos,false);

            Lista.setAdapter(adapter);
        }
        pd.dismiss();
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

        }

        return super.onOptionsItemSelected(item);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == -1) {
            if (Llamada.equals("ESCFECHADESDE")){
                lblFechaDesde.setText(data.getStringExtra("Fecha"));
            }
            if (Llamada.equals("ESCFECHAHASTA")){
                lblFechaHasta.setText(data.getStringExtra("Fecha"));
            }
        }
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
}
