package cl.issoft.www.gvenpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

/**
 * Created by MarceloUsuario on 11-13-2017.
 */

public class ActEscFecha extends AppCompatActivity {
    private DatePicker dpiFecha;
    MenuItem fav;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layescfecha);

        dpiFecha = (DatePicker)findViewById(R.id.dpiFecha);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();
        fav = menu.add(0,0,0,"Aceptar");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 0) {
            fnEscoger();
        }else if(id == 1){

        }

        return super.onOptionsItemSelected(item);
    }
    private void fnEscoger(){
        int anio = dpiFecha.getYear();
        int mes = dpiFecha.getMonth();
        mes = mes + 1;
        int dia = dpiFecha.getDayOfMonth();
        String Fecha;

        if (dia <10){
            Fecha = "0" + String.valueOf(dia) + "/";
        }else{
            Fecha = String.valueOf(dia) + "/";
        }

        if(mes < 10){
            Fecha = Fecha + "0" + String.valueOf(mes) + "/";
        }else{
            Fecha = Fecha + String.valueOf(mes) + "/";
        }

        if (String.valueOf(anio).length() ==2){
            Fecha = Fecha + "20" + String.valueOf(anio);
        }else{
            Fecha = Fecha + String.valueOf(anio);
        }

        Intent Resultado = new Intent();
        Resultado.putExtra("Fecha",Fecha);

        setResult(RESULT_OK, Resultado);
        finish();
    }

}
