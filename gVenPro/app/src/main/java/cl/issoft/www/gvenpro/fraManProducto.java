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
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.LinkedList;

/**
 * Created by MarceloUsuario on 04-22-2018.
 */

public class fraManProducto extends Fragment {
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="",Identificador="0",IdProducto="0";
    private EditText txtCodigo,txtImagen;
    private EditText txtNombre;
    private Spinner cboFamilia;
    private RadioButton rbtVigenciaSi;
    private RadioButton rbtVigenciaNo;


    public static fraManProducto newInstance(Bundle arguments){
        fraManProducto f = new fraManProducto();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laymanproducto, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        txtCodigo = (EditText)rootView.findViewById(R.id.txtCodigo);
        txtNombre = (EditText)rootView.findViewById(R.id.txtNombre);
        txtImagen = (EditText)rootView.findViewById(R.id.txtImagen);
        cboFamilia = (Spinner) rootView.findViewById(R.id.cboFamilia);
        rbtVigenciaSi = (RadioButton) rootView.findViewById(R.id.rbtVigenciaSI);
        rbtVigenciaNo = (RadioButton) rootView.findViewById(R.id.rbtVigenciaNo);
        getActivity().setTitle("Mantenedor Producto");

        IdProducto = getArguments().getString("IdProducto");
        Identificador = getArguments().getString("Identificador");

        Funciones oFunciones = new Funciones();
        LinkedList Familias = new LinkedList();
        Familias = oFunciones.linFamilia(false,"0","",getContext());

        ArrayAdapter spinner_adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, Familias);
        //Añadimos el layout para el menú y se lo damos al spinner
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboFamilia.setAdapter(spinner_adapter);

        if (!IdProducto.equals("0") || !Identificador.equals("0")){
            txtCodigo.setEnabled(false);
            txtCodigo.setText(getArguments().getString("Codigo"));
            txtNombre.setText(getArguments().getString("Nombre"));
            txtImagen.setText(getArguments().getString("Imagen"));

            if (getArguments().getString("Vigencia").equals("S")){
                rbtVigenciaSi.setChecked(true);
            }else{
                rbtVigenciaNo.setChecked(true);
            }
        }

        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnGuardar(){
        bdProducto oProducto = new bdProducto();
        Funciones oFunciones = new Funciones();

        if(txtCodigo.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Codigo","Guardar");
            return;
        }

        if(txtNombre.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Nombre","Guardar");
            return;
        }

        if(txtImagen.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Imagen","Guardar");
            return;
        }

        Integer IdFamilia = ((ObjNomyID) cboFamilia.getItemAtPosition(cboFamilia.getSelectedItemPosition())).getId();
        String Vigencia = "S";
        if (rbtVigenciaNo.isChecked()){
            Vigencia="N";
        }

        if (IdProducto.equals("0")){
            bdBase usdbh =
                    new bdBase(getContext(), "gComida", null, 1);

            SQLiteDatabase db = usdbh.getWritableDatabase();
            Cursor miCursor = db.rawQuery("SELECT * FROM Producto WHERE Codigo = '" + txtCodigo.getText().toString().trim() + "'",null);

            if (miCursor.moveToFirst()) {
                oFunciones.MostrarAlerta(getContext(),"Este Producto ya existe","Guardar");
                return;
            }
            miCursor.close();

            long vLong = oProducto.Agregar(txtCodigo.getText().toString().trim(),txtNombre.getText().toString().trim(),"N",String.valueOf(IdFamilia),"0",txtImagen.getText().toString(),Vigencia, getContext());
            IdProducto = String.valueOf(vLong);
            txtCodigo.setEnabled(false);
        }else{
            oProducto.Modificar(IdProducto,txtNombre.getText().toString().trim(), String.valueOf(IdFamilia),txtImagen.getText().toString(),Vigencia, getContext());
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
