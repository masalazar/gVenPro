package cl.issoft.www.ismaq;

import android.app.ProgressDialog;
import android.database.Cursor;
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
 * Created by MarceloUsuario on 08-22-2017.
 */

public class fraSetLote  extends Fragment {
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="";
    private EditText txtLote;
    
    public static fraSetLote newInstance(Bundle arguments){
        fraSetLote f = new fraSetLote();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laysetlote, container, false);

        txtLote = (EditText)rootView.findViewById(R.id.txtLote);
        txtLote.setText(fraManRecaudacion.Lote);
        setHasOptionsMenu(true);
        return rootView;
    }
    private void fnGuardar(){
        fraManRecaudacion.Lote=txtLote.getText().toString().trim();
        Funciones oFunciones = new Funciones();
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
        }

        return super.onOptionsItemSelected(item);
    }
}
