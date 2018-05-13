package cl.issoft.www.gvenpro;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.LinkedList;

/**
 * Created by MarceloUsuario on 04-01-2018.
 */

public class fraBusCliente extends Fragment {
    ClaseCerrar oClaseCerrar = new ClaseCerrar();
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="";
    private EditText txtRut;
    private EditText txtNombre;
    private Spinner cboComuna;

    public static fraBusCliente newInstance(Bundle arguments){
        fraBusCliente f = new fraBusCliente();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.labuscliente, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        txtRut = (EditText)rootView.findViewById(R.id.txtRut);
        txtNombre = (EditText)rootView.findViewById(R.id.txtNombre);
        cboComuna = (Spinner) rootView.findViewById(R.id.cboComuna);
        getActivity().setTitle("Búsqueda Cliente");

        Funciones oFunciones = new Funciones();
        LinkedList Comunas = new LinkedList();
        Comunas = oFunciones.linComuna(true,"0","",getContext());

        ArrayAdapter spinner_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, Comunas);
        //Añadimos el layout para el menú y se lo damos al spinner
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboComuna.setAdapter(spinner_adapter);


        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnBuscar(){
        bdCliente oCliente = new bdCliente();
        Funciones oFunciones = new Funciones();

        Integer IdComuna = ((ObjNomyID) cboComuna.getItemAtPosition(cboComuna.getSelectedItemPosition())).getId();

        if(txtRut.getText().toString().trim().equals("") && txtNombre.getText().toString().trim().equals("") && IdComuna == 0){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar un parametro de búsqueda","Guardar");
            return;
        }

        Intent i = new Intent(getActivity(), ActBusClienteDet.class);
        i.putExtra("Rut",txtRut.getText().toString().trim());
        i.putExtra("Nombre",txtNombre.getText().toString().trim());
        i.putExtra("IdComuna",String.valueOf(IdComuna));
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
            if (Llamada.equals("NORMAL")) {
                String IdCliente = data.getStringExtra("IdCliente");
                String Rut = data.getStringExtra("Rut");
                String Nombre = data.getStringExtra("Nombre");
                String Comuna = data.getStringExtra("Comuna");
                String IdComuna = data.getStringExtra("IdComuna");
                String Direccion = data.getStringExtra("Direccion");

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle arguments = new Bundle();

                arguments.putString("IdCliente", IdCliente);
                arguments.putString("Rut", Rut);
                arguments.putString("Nombre", Nombre);
                arguments.putString("Comuna", Comuna);
                arguments.putString("IdComuna", IdComuna);
                arguments.putString("Direccion", Direccion);
                fraManCliente fragment = fraManCliente.newInstance(arguments);
                transaction.replace(R.id.container, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);

                transaction.commit();
            }else{
                oClaseCerrar.Cerrar(data);
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
}
