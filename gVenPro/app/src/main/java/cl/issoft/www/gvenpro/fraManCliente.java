package cl.issoft.www.gvenpro;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class fraManCliente extends Fragment {
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="",Identificador="0",IdCliente="0";
    private EditText txtRut;
    private EditText txtNombre;
    private EditText txtDireccion;
    private Spinner cboComuna;

    public static fraManCliente newInstance(Bundle arguments){
        fraManCliente f = new fraManCliente();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laymancliente, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        txtRut = (EditText)rootView.findViewById(R.id.txtRut);
        txtNombre = (EditText)rootView.findViewById(R.id.txtNombre);
        txtDireccion = (EditText)rootView.findViewById(R.id.txtDireccion);
        cboComuna = (Spinner) rootView.findViewById(R.id.cboComuna);
        getActivity().setTitle("Mantenedor Cliente");

        IdCliente = getArguments().getString("IdCliente");
        Identificador = getArguments().getString("Identificador");

        Funciones oFunciones = new Funciones();
        LinkedList Comunas = new LinkedList();
        Comunas = oFunciones.linComuna(false,"0","",getContext());

        ArrayAdapter spinner_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, Comunas);
        //Añadimos el layout para el menú y se lo damos al spinner
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboComuna.setAdapter(spinner_adapter);

        if (!IdCliente.equals("0") || !Identificador.equals("0")){
            txtDireccion.setText(getArguments().getString("Direccion"));;
            txtRut.setEnabled(false);
            txtRut.setText(getArguments().getString("Rut"));
            txtNombre.setText(getArguments().getString("Nombre"));
        }

        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnGuardar(){
        bdCliente oCliente = new bdCliente();
        Funciones oFunciones = new Funciones();

        if(txtRut.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Rut","Guardar");
            return;
        }

        if(txtNombre.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Nombre","Guardar");
            return;
        }

        if(txtDireccion.getText().toString().trim().equals("")) {
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Dirección","Guardar");
            return;
        }

        Integer IdComuna = ((ObjNomyID) cboComuna.getItemAtPosition(cboComuna.getSelectedItemPosition())).getId();

        if (IdCliente.equals("0")){
            bdBase usdbh =
                    new bdBase(getContext(), "gComida", null, 1);

            SQLiteDatabase db = usdbh.getWritableDatabase();
            Cursor miCursor = db.rawQuery("SELECT * FROM Cliente WHERE Rut = '" + txtRut.getText().toString().trim() + "'",null);

            if (miCursor.moveToFirst()) {
                oFunciones.MostrarAlerta(getContext(),"Este Cliente ya existe","Guardar");
                return;
            }
            miCursor.close();

            long vLong = oCliente.Agregar(txtRut.getText().toString().trim(),txtNombre.getText().toString().trim(),txtDireccion.getText().toString().trim(),String.valueOf(IdComuna),"N","0",getContext());
            IdCliente = String.valueOf(vLong);
            txtRut.setEnabled(false);
        }else{
            oCliente.Modificar(IdCliente,txtNombre.getText().toString().trim(), txtDireccion.getText().toString().trim(),String.valueOf(IdComuna),getContext());
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
