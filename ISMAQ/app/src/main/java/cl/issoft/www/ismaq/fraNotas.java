package cl.issoft.www.ismaq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 07-14-2017.
 */

public class fraNotas extends Fragment {
    private ListView Lista;
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="",Identificador="0",IdNota="0";
    int ContadorFila = 0;
    ejeWS oEjeWS;
    ArrayList<tVarios> DatosEliminar;

    public static fraNotas newInstance(Bundle arguments){
        fraNotas f = new fraNotas();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laydetalle, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        Lista = (ListView) rootView.findViewById(R.id.lisDatos);
        fnBuscar();
        setHasOptionsMenu(true);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        fav = menu.add(0,0,0,"Eliminar");
        fav = menu.add(0,1,0,"Nueva");

        super.onCreateOptionsMenu(menu, inflater);
    }
    private void fnBuscar(){
        this.pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);
        ActNotasBD oNotas = new ActNotasBD();
        Cursor c =  oNotas.BuscarNota(getActivity(),"-TOD");
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre= c.getString(c.getColumnIndex("Nombre"));
                String Codigo= c.getString(c.getColumnIndex("Codigo"));
                String Subido= c.getString(c.getColumnIndex("Subido"));
                String IdNota= c.getString(c.getColumnIndex("IdNota"));
                String Descripcion= c.getString(c.getColumnIndex("Descripcion"));
                String IdLocal= c.getString(c.getColumnIndex("IdLocal"));
                String Identificador= c.getString(c.getColumnIndex("Identificador"));

                Datos.add(new tVarios(Descripcion,Nombre + " - " + Subido + " - " + IdNota,IdNota,IdLocal,"",Identificador,"","","","S","","","","",""));

            } while(c.moveToNext());

            adapLis adapter = new adapLis(getActivity(), Datos,false);

            Lista.setAdapter(adapter);

            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    Llamada = "EDITNOTA";
                    Intent i = new Intent(getActivity(), ActEditNota.class);
                    i.putExtra("IdNota",oVarios.getValor3());
                    i.putExtra("Descripcion",oVarios.getValor1());
                    i.putExtra("Local",oVarios.getValor2());
                    i.putExtra("IdLocal",oVarios.getValor4());
                    startActivityForResult(i, 0);
                }
            });
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
            ContadorFila = 0;
            DatosEliminar = new ArrayList<tVarios>();
            fnEliminar();

        }else if(id == 1){
            Llamada = "EDITNOTA";
            Intent i = new Intent(getActivity(), ActEditNota.class);
            i.putExtra("IdNota","0");
            startActivityForResult(i, 0);
        }

        return super.onOptionsItemSelected(item);
    }
    private void fnEliminar(){

        for (int i = ContadorFila; i < Lista.getCount(); i++)
        {
            tVarios oVarios = (tVarios) Lista.getItemAtPosition(i);
            Identificador = oVarios.getValor6();
            IdNota = oVarios.getValor3();
            ViewGroup row = (ViewGroup) Lista.getChildAt(i);
            CheckBox tvTest = (CheckBox) row.findViewById(R.id.chkSel);

            if (tvTest.isChecked())
            {
                Accion = "ELIMINAR";
                oEjeWS = null;
                oEjeWS = new ejeWS();

                EjecutaWS oEjecutaWS = new EjecutaWS();
                oEjecutaWS.execute();
                ContadorFila = ContadorFila + 1;
                return;
            }else{
                DatosEliminar.add(new tVarios(oVarios.getValor1(),oVarios.getValor2(),oVarios.getValor3(),oVarios.getValor4(),"","","","","","S","","","","",""));
                ContadorFila = ContadorFila + 1;
            }
        }

        adapLis adapter = new adapLis(getActivity(), DatosEliminar,false);

        Lista.setAdapter(adapter);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == -1) {
            if(Llamada.equals("EDITNOTA")) {
                fnBuscar();
            }
        }
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("ELIMINAR")) {
                oEjeWS.setMetodo("Eliminar");

                oEjeWS.setWS("WSNota.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdNota", Identificador);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (oEjeWS.getNumError() > 0) {
                    Funciones oFunciones = new Funciones();

                    oFunciones.MostrarAlerta(getActivity(), oEjeWS.getDesError(), "E R R O R");
                    pd.dismiss();
                    return;
                }

                if (Accion.equals("ELIMINAR")){
                    ActNotasBD oNotas = new ActNotasBD();
                    oNotas.EliminarNota(IdNota,getActivity());
                    fnEliminar();
                }

            }
        }
    }
}
