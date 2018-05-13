package cl.issoft.www.ismaq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 07-14-2017.
 */

public class ActEditNota extends AppCompatActivity {
    private ListView Lista;
    private String IdNota = "0",IdLocal = "0";
    private EditText txtDescripcion;
    private EditText txtLocal;
    private Context Contexto = this;
    MenuItem fav;
    ejeWS oEjeWS;
    private ProgressDialog pd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layeditnota);

        txtDescripcion = (EditText)findViewById(R.id.txtDescripcion);
        txtLocal = (EditText)findViewById(R.id.txtLocal);
        Lista = (ListView)findViewById(R.id.lisDatos);

        Bundle bundle = getIntent().getExtras();
        IdNota = bundle.getString("IdNota");

        if (!IdNota.equals("0")){
            txtDescripcion.setText(bundle.getString("Descripcion"));
            txtLocal.setText(bundle.getString("Local"));
            IdLocal = bundle.getString("IdLocal");
            txtDescripcion.requestFocus();
            txtDescripcion.setSelectAllOnFocus(true);
        }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();
        fav = menu.add(0,0,0,"Guardar");
        fav = menu.add(0,1,0,"Volver");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
            fnGuardar();
        }
        if (id == 1) {

        }
        return super.onOptionsItemSelected(item);
    }
    private void fnGuardar(){
        ActNotasBD oNotas = new ActNotasBD();
        Funciones oFunciones = new Funciones();

        if(IdLocal.equals("0")){
            oFunciones.MostrarAlerta(Contexto,"Debe especificar Local","Guardar");
            return;
        }

        if(txtDescripcion.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(Contexto,"Debe especificar Descripción","Guardar");
            return;
        }

        if (IdNota.equals("0")){
            oNotas.AgregarNota(IdLocal,txtDescripcion.getText().toString().trim(),Contexto,"0");
        }else{
            oNotas.ModificarNota(IdNota,txtDescripcion.getText().toString().trim(),Contexto);
        }

        Intent Resultado = new Intent();
        Resultado.putExtra("Guardado","SI");
        setResult(RESULT_OK, Resultado);
        finish();
    }
    private void BuscarLocal(String Texto){
        IdLocal = "0";
        if (Texto.equals("")){
            Lista.setVisibility(View.INVISIBLE);
        }else{
            Lista.setVisibility(View.VISIBLE);
        }

        ActLocalBD oLocal = new ActLocalBD();
        Cursor c =  oLocal.BuscarLocalNombre(Contexto,Texto);
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre= c.getString(c.getColumnIndex("Nombre"));
                String IdLocal= c.getString(c.getColumnIndex("IdLocal"));

                Datos.add(new tVarios(Nombre,"",IdLocal,"","","","","","","","","","","",""));

            } while(c.moveToNext());

            adapLis adapter = new adapLis(this, Datos,true);

            Lista.setAdapter(adapter);

            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    txtLocal.setText(oVarios.getValor1());
                    IdLocal = oVarios.getValor3();
                    Lista.setVisibility(View.INVISIBLE);
                    txtDescripcion.requestFocus();
                }
            });
        }
    }
}