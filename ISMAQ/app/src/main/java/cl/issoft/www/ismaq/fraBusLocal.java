package cl.issoft.www.ismaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by MarceloUsuario on 08-11-2017.
 */

public class fraBusLocal extends Fragment {
    private ListView Lista;
    private ProgressDialog pd = null;
    MenuItem fav;
    EditText txtNombre;
    String Llamada ="",Accion="",Identificador="0",IdLocal="0";
    int ContadorFila = 0;
    ejeWS oEjeWS;
    ArrayList<tVarios> DatosEliminar;

    public static fraBusLocal newInstance(Bundle arguments){
        fraBusLocal f = new fraBusLocal();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laybuslocal, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        Lista = (ListView) rootView.findViewById(R.id.lisDatos);
        txtNombre = (EditText)rootView.findViewById(R.id.txtNombre);

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
        fav = menu.add(0,0,0,"Buscar");

        super.onCreateOptionsMenu(menu, inflater);
    }
    private void fnBuscar(){
        this.pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);
        ActLocalBD oLocal = new ActLocalBD();
        Cursor c =  oLocal.BuscarLocalNombre(getActivity(),txtNombre.getText().toString().trim());
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre= c.getString(c.getColumnIndex("Nombre"));
                String Codigo= c.getString(c.getColumnIndex("Codigo"));
                String Subido= c.getString(c.getColumnIndex("Subido"));
                String IdLocal= c.getString(c.getColumnIndex("IdLocal"));
                String IdRuta= c.getString(c.getColumnIndex("IdRuta"));
                String Encargado= c.getString(c.getColumnIndex("Encargado"));
                String Direccion= c.getString(c.getColumnIndex("Direccion"));
                String Estimado= c.getString(c.getColumnIndex("Estimado"));
                String Identificador= c.getString(c.getColumnIndex("Identificador"));

                Datos.add(new tVarios(Nombre,Codigo + " - " + Subido,IdLocal,Identificador,Codigo,IdRuta,Encargado,Direccion,Estimado,"","","","","",""));

            } while(c.moveToNext());

            adapLis adapter = new adapLis(getActivity(), Datos,false);

            Lista.setAdapter(adapter);

            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Bundle arguments = new Bundle();
                    arguments.putString("LLAMADA", "EDITAR");
                    arguments.putString("Codigo", oVarios.getValor5());
                    arguments.putString("Nombre", oVarios.getValor1());
                    arguments.putString("IdLocal", oVarios.getValor3());
                    arguments.putString("Identificador", oVarios.getValor4());
                    arguments.putString("IdRuta", oVarios.getValor6());
                    arguments.putString("Encargado", oVarios.getValor7());
                    arguments.putString("Direccion", oVarios.getValor8());
                    arguments.putString("Estimado", oVarios.getValor9());
                    fraManLocal fragment = fraManLocal.newInstance(arguments);
                    transaction.replace(R.id.container, fragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(null);

                    transaction.commit();

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
            fnBuscar();

        }

        return super.onOptionsItemSelected(item);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == -1) {
                fnBuscar();
        }
    }
}
