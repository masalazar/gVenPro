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
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.LinkedList;

/**
 * Created by MarceloUsuario on 04-22-2018.
 */

public class fraBusProducto extends Fragment {
    ClaseCerrar oClaseCerrar = new ClaseCerrar();
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="";
    private EditText txtCodigo;
    private EditText txtNombre;
    private Spinner cboFamilia;
    private RadioButton rbtVigenciaSi;
    private RadioButton rbtVigenciaNo;
    private RadioButton rbtVigenciaAmbos;

    public static fraBusProducto newInstance(Bundle arguments){
        fraBusProducto f = new fraBusProducto();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laybusproducto, container, false);

        try{
            Llamada = getArguments().getString("Llamada");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        txtCodigo = (EditText)rootView.findViewById(R.id.txtCodigo);
        txtNombre = (EditText)rootView.findViewById(R.id.txtNombre);
        cboFamilia = (Spinner) rootView.findViewById(R.id.cboFamilia);
        rbtVigenciaSi = (RadioButton) rootView.findViewById(R.id.rbtVigenciaSI);
        rbtVigenciaNo = (RadioButton) rootView.findViewById(R.id.rbtVigenciaNo);
        rbtVigenciaAmbos = (RadioButton) rootView.findViewById(R.id.rbtVigenciaAmbos);
        getActivity().setTitle("Búsqueda Producto");

        if (!Llamada.equals("BUSCAR")) {
            rbtVigenciaAmbos.setEnabled(false);
            rbtVigenciaSi.setEnabled(false);
            rbtVigenciaNo.setEnabled(false);
            rbtVigenciaSi.setChecked(true);
        }



        Funciones oFunciones = new Funciones();
        LinkedList Familias = new LinkedList();
        Familias = oFunciones.linFamilia(true,"0","",getContext());

        ArrayAdapter spinner_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, Familias);
        //Añadimos el layout para el menú y se lo damos al spinner
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboFamilia.setAdapter(spinner_adapter);


        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnBuscar(){
        bdProducto oProducto = new bdProducto();
        Funciones oFunciones = new Funciones();

        Integer IdFamilia = ((ObjNomyID) cboFamilia.getItemAtPosition(cboFamilia.getSelectedItemPosition())).getId();

        if(txtCodigo.getText().toString().trim().equals("") && txtNombre.getText().toString().trim().equals("") && IdFamilia == 0){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar un parametro de búsqueda","Guardar");
            return;
        }

        String Vigencia = "";
        if (rbtVigenciaSi.isChecked()){Vigencia="S";}
        if (rbtVigenciaNo.isChecked()){Vigencia="N";}

        Intent i = new Intent(getActivity(), ActBusProductoDet.class);
        i.putExtra("Codigo",txtCodigo.getText().toString().trim());
        i.putExtra("Nombre",txtNombre.getText().toString().trim());
        i.putExtra("Vigencia",Vigencia);
        i.putExtra("IdFamilia",String.valueOf(IdFamilia));
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
                String IdProducto = data.getStringExtra("IdProducto");
                String Codigo = data.getStringExtra("Codigo");
                String Nombre = data.getStringExtra("Nombre");
                String Familia = data.getStringExtra("Familia");
                String IdFamilia = data.getStringExtra("IdFamilia");
                String Direccion = data.getStringExtra("Direccion");

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle arguments = new Bundle();

                arguments.putString("IdProducto", IdProducto);
                arguments.putString("Codigo", Codigo);
                arguments.putString("Nombre", Nombre);
                arguments.putString("Familia", Familia);
                arguments.putString("IdFamilia", IdFamilia);
                arguments.putString("Direccion", Direccion);
                fraManProducto fragment = fraManProducto.newInstance(arguments);
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
