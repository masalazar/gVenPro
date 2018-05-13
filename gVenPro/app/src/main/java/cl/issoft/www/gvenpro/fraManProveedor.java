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
 * Created by MarceloUsuario on 11-02-2017.
 */

public class fraManProveedor extends Fragment {
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="",Identificador="0",IdProveedor="0";
    private EditText txtRut;
    private EditText txtNombre;
    private EditText txtVendedor;
    private EditText txtDireccion;

    public static fraManProveedor newInstance(Bundle arguments){
        fraManProveedor f = new fraManProveedor();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laymanproveedor, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        txtRut = (EditText)rootView.findViewById(R.id.txtRut);
        txtNombre = (EditText)rootView.findViewById(R.id.txtNombre);
        txtVendedor = (EditText)rootView.findViewById(R.id.txtVendedor);
        txtDireccion = (EditText)rootView.findViewById(R.id.txtDireccion);
        getActivity().setTitle("Mantenedor Proveedor");

        IdProveedor = getArguments().getString("IdProveedor");
        Identificador = getArguments().getString("Identificador");

        if (!IdProveedor.equals("0") || !Identificador.equals("0")){
            txtVendedor.setText(getArguments().getString("Vendedor"));;
            txtDireccion.setText(getArguments().getString("Direccion"));;
            txtRut.setEnabled(false);
            txtRut.setText(getArguments().getString("Rut"));
            txtNombre.setText(getArguments().getString("Nombre"));
        }

        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnGuardar(){
        bdProveedor oProveedor = new bdProveedor();
        Funciones oFunciones = new Funciones();

        if(txtRut.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Rut","Guardar");
            return;
        }

        if(txtNombre.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Nombre","Guardar");
            return;
        }

        if(txtVendedor.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Vendedor","Guardar");
            return;
        }

        if(txtDireccion.getText().toString().trim().equals("")) {
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Dirección","Guardar");
            return;
        }

        if (IdProveedor.equals("0")){
            bdBase usdbh =
                    new bdBase(getContext(), "gComida", null, 1);

            SQLiteDatabase db = usdbh.getWritableDatabase();
            Cursor miCursor = db.rawQuery("SELECT * FROM Proveedor WHERE Rut = '" + txtRut.getText().toString().trim() + "'",null);

            if (miCursor.moveToFirst()) {
                oFunciones.MostrarAlerta(getContext(),"Este proveedor ya existe","Guardar");
                return;
            }
            miCursor.close();

            long vLong = oProveedor.Agregar(txtRut.getText().toString().trim(),txtNombre.getText().toString().trim(),txtDireccion.getText().toString().trim(),txtVendedor.getText().toString().trim(),"N","0",getContext());
            IdProveedor = String.valueOf(vLong);
            txtRut.setEnabled(false);
        }else{
            oProveedor.Modificar(IdProveedor,txtNombre.getText().toString().trim(), txtDireccion.getText().toString().trim(),txtVendedor.getText().toString(),getContext());
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
