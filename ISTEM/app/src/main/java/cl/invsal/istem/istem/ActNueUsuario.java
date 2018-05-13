package cl.invsal.istem.istem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MarceloUsuario on 01-31-2017.
 */

public class ActNueUsuario extends AppCompatActivity  {
    private EditText txtRut,txtNombre;
    private Context Contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laynueusuario);

        txtRut = (EditText)findViewById(R.id.txtRut);
        txtNombre = (EditText)findViewById(R.id.txtNombre);

        Funciones oFunciones = new Funciones();
        Cursor Cursor = oFunciones.BuscarUsuario(Contexto);

        if (Cursor.getCount() > 0){
            Intent i = new Intent(Contexto, ActMenu.class);
            startActivity(i);
            finish();
        }
        setTitle("ISTEM - Nuevo Usuario");
    }
    public void fnGuardar(View view){
        Funciones oFunciones = new Funciones();
        if (!oFunciones.validarRut(txtRut.getText().toString().trim())){
            oFunciones.MostrarAlerta(Contexto,"Debe especificar un rut valido","Guardar");
            txtRut.requestFocus();
            return;
        }

        if (txtNombre.getText().toString().trim().equals("")){
            oFunciones.MostrarAlerta(Contexto,"Debe especificar Nombre","Guardar");
            txtNombre.requestFocus();
            return;
        }

        oFunciones.AgregarUsuario(txtRut.getText().toString().trim(),txtNombre.getText().toString().trim(),Contexto);


        Intent i = new Intent(Contexto, ActMenu.class);
        startActivity(i);
        finish();
    }
}

