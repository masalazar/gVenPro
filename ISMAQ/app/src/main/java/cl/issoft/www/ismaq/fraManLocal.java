package cl.issoft.www.ismaq;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by MarceloUsuario on 08-11-2017.
 */

public class fraManLocal extends Fragment {
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="",Identificador="0",IdLocal="0";
    private EditText txtCodigo;
    private EditText txtNombre;
    private EditText txtEncargado;
    private EditText txtDireccion;
    private EditText txtEstimado;
    private Spinner cboRuta;

    public static fraManLocal newInstance(Bundle arguments){
        fraManLocal f = new fraManLocal();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laymanlocal, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        LinkedList Rutas = new LinkedList();
        ActRutaBD oRuta = new ActRutaBD();
        Cursor cRuta = oRuta.BuscarRuta(getContext(),"0");

        cboRuta = (Spinner) rootView.findViewById(R.id.cboRuta);

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

        txtCodigo = (EditText)rootView.findViewById(R.id.txtCodigo);
        txtNombre = (EditText)rootView.findViewById(R.id.txtNombre);
        txtEncargado = (EditText)rootView.findViewById(R.id.txtEncargado);
        txtDireccion = (EditText)rootView.findViewById(R.id.txtDireccion);
        txtEstimado = (EditText)rootView.findViewById(R.id.txtEstimado);

        IdLocal = getArguments().getString("IdLocal");
        Identificador = getArguments().getString("Identificador");

        if (!IdLocal.equals("0") || !Identificador.equals("0")){
            txtEncargado.setText(getArguments().getString("Encargado"));;
            txtDireccion.setText(getArguments().getString("Direccion"));;
            txtCodigo.setEnabled(false);
            txtCodigo.setText(getArguments().getString("Codigo"));
            txtNombre.setText(getArguments().getString("Nombre"));
            txtEstimado.setText(getArguments().getString("Estimado"));
            Funciones oFunciones = new Funciones();
            oFunciones.SeleccionarItemConIDSpinner(cboRuta,getArguments().getString("IdRuta"));
        }

        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnGuardar(){
        ActLocalBD oLocal = new ActLocalBD();
        Funciones oFunciones = new Funciones();

        if(txtCodigo.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Código","Guardar");
            return;
        }

        if(txtNombre.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Nombre","Guardar");
            return;
        }

        if(txtEncargado.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Encargado","Guardar");
            return;
        }

        if(txtDireccion.getText().toString().trim().equals("")) {
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Dirección","Guardar");
            return;
        }

        if(txtEstimado.getText().toString().trim().equals("0")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Estimado","Guardar");
            return;
        }


        Integer IdRuta = ((ObjNomyID) cboRuta.getItemAtPosition(cboRuta.getSelectedItemPosition())).getId();

        if (IdLocal.equals("0") && Identificador.equals("0")){
            long vLong = oLocal.AgregarLocalTemp(txtCodigo.getText().toString().trim(),txtNombre.getText().toString().trim(),txtDireccion.getText().toString().trim(),txtEncargado.getText().toString().trim(),String.valueOf(IdRuta),txtEstimado.getText().toString().trim(),getContext());
            Identificador = String.valueOf(vLong);
            if (Identificador.equals("0")){
                return;
            }
        }else{
            if (!IdLocal.equals("0")){
                oLocal.ModificarLocal(IdLocal,txtNombre.getText().toString().trim(), String.valueOf(IdRuta),txtEncargado.getText().toString(),txtDireccion.getText().toString().trim(),txtEstimado.getText().toString().trim(),getContext());
            }else{
                oLocal.ModificarLocalTem(Identificador,txtNombre.getText().toString().trim(),String.valueOf(IdRuta),txtEncargado.getText().toString(),txtDireccion.getText().toString().trim(),txtEstimado.getText().toString().trim(), getContext());
            }
        }

        oFunciones.MostrarAlerta(getContext(),"La operación se realizo correctamente","Guardar");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        fav = menu.add(0,0,0,"Guardar");
        fav = menu.add(0,1,0,"Volver");

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
            fnGuardar();

        }else if(id == 1){

        }

        return super.onOptionsItemSelected(item);
    }
}
