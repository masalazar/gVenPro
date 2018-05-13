package cl.issoft.www.gvenpro;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 11-13-2017.
 */

public class ActEjeSQL extends AppCompatActivity {
    private EditText txtSQL;
    private ListView Lista;
    MenuItem fav;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layejesql);

        txtSQL = (EditText) findViewById(R.id.txtSQL);
        Lista = (ListView) findViewById(R.id.lisDatos);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.clear();
        fav = menu.add(0,0,0,"Ejecutar SELECT");
        fav = menu.add(0,1,0,"Ejecutar OTRO");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 0) {
            fnEjecutarSQL();
        }else if(id == 1){
            fnEjecutarOtro();
        }

        return super.onOptionsItemSelected(item);
    }
    private void fnEjecutarOtro(){
        Funciones oFunciones = new Funciones();
        bdBase usdbh =
                new bdBase(this, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = txtSQL.getText().toString().trim();
        try{
            db.execSQL(varSQL);

            oFunciones.MostrarAlerta(this,"La operación se realizo correctamente","OK");
        }catch (Exception e){
            oFunciones.MostrarAlerta(this,e.getMessage(),"ERROR");
        }

        db.close();
    }
    private void fnEjecutarSQL(){
        Funciones oFunciones = new Funciones();

        bdBase usdbh =
                new bdBase(this, "gComida", null, 1);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        String varSQL = "";


        varSQL = txtSQL.getText().toString().trim().replace("&","");

        try {
            int Pos=-1;
            Cursor c = db.rawQuery(varSQL,null);
            Pos = txtSQL.getText().toString().trim().indexOf("&");
            if (Pos==-1){
                oFunciones.MostrarAlerta(this,"Las columnas deben estar entre &&","ERROR");
                return;
            }

            int Pos2=-1;
            Pos2 = txtSQL.getText().toString().trim().indexOf("&",Pos+1);
            String Columnas = txtSQL.getText().toString().trim().substring(Pos+1,Pos2);
            String Datos[] = Columnas.split(",");



            ArrayList<tVarios> Reg = new ArrayList<tVarios>();

            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String Cadena="";
                    for(int i = 0; i <= Datos.length-1; i = i + 1){
                        Cadena = Cadena + " - " + Datos[i].trim() + ": " + c.getString(i);
                    }
                    Cadena = Cadena.substring(3);
                    Reg.add(new tVarios(Cadena,"","","","","","","","","","","","","",""));
                } while(c.moveToNext());

                adapLis adapter = new adapLis(this, Reg,false);

                Lista.setAdapter(adapter);
            }
        }catch (Exception e){
            oFunciones.MostrarAlerta(this,e.getMessage(),"ERROR");
        }
    }

}
