package cl.issoft.www.gvenpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by MarceloUsuario on 11-09-2017.
 */

public class ActEditProdOrden extends AppCompatActivity {
    MenuItem fav;
    private String Llamada = "";
    private EditText txtCantidad;
    private EditText txtValor;
    private EditText txtValorCompra;
    private TextView lblValorCompra;
    private EditText txtCodigo;
    private EditText txtNombre;
    private String IdProducto;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layeditprodorden);

        this.setTitle("Ingresar Cantidad y Valor");
        Bundle bundle = getIntent().getExtras();
        txtCantidad = (EditText)findViewById(R.id.txtCantidad);
        txtValor = (EditText)findViewById(R.id.txtValor);
        txtValorCompra = (EditText)findViewById(R.id.txtValorCompra);
        lblValorCompra = (TextView) findViewById(R.id.lblValorCompra);
        txtCodigo = (EditText)findViewById(R.id.txtCodigo);
        txtNombre = (EditText)findViewById(R.id.txtNombre);

        Llamada = bundle.getString("Llamada");
        txtCodigo.setText(bundle.getString("Codigo"));
        txtNombre.setText(bundle.getString("Nombre"));
        IdProducto = bundle.getString("IdProducto");

        txtCantidad.setText(bundle.getString("Cantidad"));
        txtValor.setText(bundle.getString("Valor"));
        txtValorCompra.setText(bundle.getString("ValorCompra"));

        if (!Llamada.equals("BUSPRODUCTOMODVALOR") && !Llamada.equals("BUSPRODUCTOVENTA")){
            txtValorCompra.setVisibility(View.INVISIBLE);
            lblValorCompra.setVisibility(View.INVISIBLE);
        }
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
            fnGuardar();
        }

        return super.onOptionsItemSelected(item);
    }
    private void fnGuardar(){
        Funciones oFunciones = new Funciones();
        if (txtCantidad.getText().toString().equals("") || txtCantidad.getText().toString().equals("0")){
            oFunciones.MostrarAlerta(this,"Debe especificar Cantidad","Aceptar");
            return;
        }

        if (txtValor.getText().toString().equals("") || txtValor.getText().toString().equals("0")){
            oFunciones.MostrarAlerta(this,"Debe especificar Valor","Aceptar");
            return;
        }

        if (Llamada.equals("BUSPRODUCTOMODVALOR")){
            if (txtValorCompra.getText().toString().equals("") || txtValorCompra.getText().toString().equals("0")){
                oFunciones.MostrarAlerta(this,"Debe especificar Valor Compra","Aceptar");
                return;
            }
        }

        Intent Resultado = new Intent();
        Resultado.putExtra("IdProducto",IdProducto);
        Resultado.putExtra("Codigo",txtCodigo.getText().toString());
        Resultado.putExtra("Nombre",txtNombre.getText().toString());
        Resultado.putExtra("Cantidad",txtCantidad.getText().toString());
        Resultado.putExtra("Valor",txtValor.getText().toString());

        if (Llamada.equals("BUSPRODUCTOMODVALOR")){
            bdStock oStock = new bdStock();

            oStock.ModificarValor(IdProducto,Funciones.gIdSucursal,txtValor.getText().toString(),txtValorCompra.getText().toString(),this);
        }

        setResult(RESULT_OK, Resultado);
        finish();
    }
}
