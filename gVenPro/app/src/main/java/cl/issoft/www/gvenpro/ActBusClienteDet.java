package cl.issoft.www.gvenpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 04-01-2018.
 */

public class ActBusClienteDet extends AppCompatActivity {
    String Rut, Nombre,IdComuna;
    private Context Contexto = this;
    private ProgressDialog pd = null;
    private ListView Lista;
    MenuItem fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laydetalle);

        this.pd = ProgressDialog.show(this, "Procesando", "Espere unos segundos...", true, false);

        Bundle bundle = getIntent().getExtras();
        Rut = bundle.getString("Rut");
        Nombre = bundle.getString("Nombre");
        IdComuna = bundle.getString("IdComuna");
        Lista = (ListView) findViewById(R.id.lisDatos);

        bdCliente oCliente = new bdCliente();
        Cursor c = oCliente.Buscar(this,"0",Rut,Nombre,IdComuna);

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre= c.getString(c.getColumnIndex("Nombre"));
                String Rut = c.getString(c.getColumnIndex("Rut"));
                String Comuna = c.getString(c.getColumnIndex("NomCom"));
                String IdComuna = c.getString(c.getColumnIndex("IdComuna"));
                String Direccion = c.getString(c.getColumnIndex("Direccion"));
                String IdCliente = c.getString(c.getColumnIndex("IdCliente"));

                Datos.add(new tVarios(Nombre,"Rut: " + Rut + " - Comuna: " + Comuna,IdCliente,Rut,Nombre,Comuna,Direccion,IdComuna,"","","","","","",""));

            } while(c.moveToNext());

            adapLis adapter = new adapLis(this, Datos,false);

            Lista.setAdapter(adapter);
            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    Intent Resultado = new Intent();
                    Resultado.putExtra("IdCliente", oVarios.getValor3());
                    Resultado.putExtra("Rut", oVarios.getValor4());
                    Resultado.putExtra("Nombre", oVarios.getValor5());
                    Resultado.putExtra("Comuna", oVarios.getValor6());
                    Resultado.putExtra("Direccion", oVarios.getValor7());
                    Resultado.putExtra("IdComuna", oVarios.getValor8());
                    Resultado.putExtra("Encontrado", "SI");

                    setResult(RESULT_OK, Resultado);
                    finish();
                }
            });
        }
        pd.dismiss();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 0) {
        }
        return super.onOptionsItemSelected(item);
    }
}
