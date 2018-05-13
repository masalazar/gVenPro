package cl.issoft.www.ismaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 08-09-2017.
 */

public class fraManRecaudacion extends Fragment {
    private ProgressDialog pd = null;
    MenuItem fav;
    String Llamada ="",Accion="",Identificador="0",IdLocal="0",Estimado="0";
    private EditText txtLocal,txtMonto,txtEstimado;
    public static String Lote = "0";
    private TextView lblFecha;
    private ListView Lista;
    private Button btnEscFecha;

    public static fraManRecaudacion newInstance(Bundle arguments){
        fraManRecaudacion f = new fraManRecaudacion();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laymanrecaudacion, container, false);

        try{
            Llamada = getArguments().getString("LLAMADA");
        }catch (Exception e){
            Llamada = "NORMAL";
        }

        getActivity().setTitle("LOTE :" + Lote);

        txtLocal = (EditText)rootView.findViewById(R.id.txtLocal);
        lblFecha = (TextView)rootView.findViewById(R.id.lblFecha);
        txtMonto = (EditText)rootView.findViewById(R.id.txtMonto);
        txtEstimado = (EditText)rootView.findViewById(R.id.txtEstimado);
        Lista = (ListView)rootView.findViewById(R.id.lisDatos);
        btnEscFecha = (Button)rootView.findViewById(R.id.btnEscFecha);
        btnEscFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnEscFecha();
            }
        });
        txtLocal.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable et) {
                BuscarLocal(et.toString());
            }
        });

        setHasOptionsMenu(true);
        return rootView;
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
    public void fnEscFecha(){
        Llamada = "ESCFECHA";
        Intent i = new Intent(getActivity(), ActEscFecha.class);
        startActivityForResult(i, 0);
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
    private void fnGuardar(){
        Funciones oFunciones = new Funciones();
        if (!txtMonto.isEnabled()){
            oFunciones.MostrarAlerta(getActivity(), "Esta recaudacion ya fue guardada","Guardar");
            return;
        }
        ActRecaudacionBD oRecaudacion = new ActRecaudacionBD();

        if(IdLocal.equals("0") && Identificador.equals("0")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Local","Guardar");
            return;
        }

        if(txtMonto.getText().toString().trim().equals("") || txtMonto.getText().toString().trim().equals("0")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Monto","Guardar");
            return;
        }

        if(lblFecha.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(getContext(),"Debe especificar Fecha","Guardar");
            return;
        }

        String Fecha = lblFecha.getText().toString().trim();
        Fecha = Fecha.substring(6,10) + Fecha.substring(3,5) + Fecha.substring(0,2);
        long Monto = Long.parseLong(txtMonto.getText().toString().trim());
        oRecaudacion.AgregarRecaudacion(IdLocal,Identificador,Monto,Fecha,"N",txtEstimado.getText().toString().trim(),Lote, getContext());

        txtLocal.setEnabled(false);
        btnEscFecha.setEnabled(false);
        txtMonto.setEnabled(false);
        oFunciones.MostrarAlerta(getActivity(), "La operacion se realizo correctamente","Guardar");
    }
    private void BuscarLocal(String Texto){
        IdLocal = "0";
        if (Texto.equals("")){
            Lista.setVisibility(View.INVISIBLE);
        }else{
            Lista.setVisibility(View.VISIBLE);
        }

        ActLocalBD oLocal = new ActLocalBD();
        Cursor c =  oLocal.BuscarLocalNombre(getContext(),Texto);
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre= c.getString(c.getColumnIndex("Nombre"));
                String IdLocal= c.getString(c.getColumnIndex("IdLocal"));
                String Estimado= c.getString(c.getColumnIndex("Estimado"));
                String Identificador= c.getString(c.getColumnIndex("Identificador"));

                Datos.add(new tVarios(Nombre,"",IdLocal,Identificador,Estimado,"","","","","","","","","",""));

            } while(c.moveToNext());

            adapLis adapter = new adapLis(getContext(), Datos,true);

            Lista.setAdapter(adapter);

            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    txtLocal.setText(oVarios.getValor1());
                    IdLocal = oVarios.getValor3();
                    Identificador = oVarios.getValor4();
                    txtEstimado.setText(oVarios.getValor5());
                    Lista.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == -1) {
            if(Llamada.equals("ESCFECHA")) {
                lblFecha.setText(data.getStringExtra("Fecha"));
                txtMonto.requestFocus();
            }
        }
    }
}
