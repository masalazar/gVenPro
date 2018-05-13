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
 * Created by MarceloUsuario on 04-22-2018.
 */

public class ActBusProductoDet extends AppCompatActivity {
    String Codigo, Nombre,IdFamilia,Vigencia;
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
        Codigo = bundle.getString("Codigo");
        Nombre = bundle.getString("Nombre");
        Vigencia = bundle.getString("Vigencia");
        IdFamilia = bundle.getString("IdFamilia");
        Lista = (ListView) findViewById(R.id.lisDatos);

        bdProducto oProducto = new bdProducto();
        Cursor c = oProducto.Buscar(this,"0",Codigo,Nombre,IdFamilia,Vigencia);

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String Nombre= c.getString(c.getColumnIndex("Nombre"));
                String Codigo = c.getString(c.getColumnIndex("Codigo"));
                String Familia = c.getString(c.getColumnIndex("NomFam"));
                String IdFamilia = c.getString(c.getColumnIndex("IdFamilia"));
                String IdProducto = c.getString(c.getColumnIndex("IdProducto"));
                String Vigencia = c.getString(c.getColumnIndex("Vigencia"));
                String Imagen = c.getString(c.getColumnIndex("Imagen"));
                String tVigencia = "SI";
                if (Vigencia.equals("N")) {tVigencia="NO";
                }

                Datos.add(new tVarios(Nombre,"Codigo: " + Codigo + " - Familia: " + Familia + " - Vigencia: " + tVigencia,IdProducto,Codigo,Nombre,Familia,Vigencia,IdFamilia,Imagen,"","","","","",""));

            } while(c.moveToNext());

            adapLis adapter = new adapLis(this, Datos,false);

            Lista.setAdapter(adapter);
            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    Intent Resultado = new Intent();
                    Resultado.putExtra("IdProducto", oVarios.getValor3());
                    Resultado.putExtra("Codigo", oVarios.getValor4());
                    Resultado.putExtra("Nombre", oVarios.getValor5());
                    Resultado.putExtra("Familia", oVarios.getValor6());
                    Resultado.putExtra("Vigencia", oVarios.getValor7());
                    Resultado.putExtra("IdFamilia", oVarios.getValor8());
                    Resultado.putExtra("Imagen", oVarios.getValor9());
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
