package cl.issoft.www.gvenpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by MarceloUsuario on 11-09-2017.
 */

public class ActContenedor extends AppCompatActivity implements ClaseCerrar.InterfasCerrar {
    public static String Llamada,Id;
    private TextView lblTitulo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.laycontenedor);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        if (Llamada.equals("BUSPROVEEDOR")){
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "SELECCION");
            Fragment fragment = null;
            fragment = fraBusProveedor.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            lblTitulo.setText("Seleccionar Proveedor");
        }else if (Llamada.equals("BUSCLIENTE")){
            Bundle arguments = new Bundle();
            arguments.putString("LLAMADA", "SELECCION");
            Fragment fragment = null;
            fragment = fraBusCliente.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            lblTitulo.setText("Seleccionar Cliente");
        }else if (Llamada.equals("BUSPRODUCTO")){
            Bundle arguments = new Bundle();
            arguments.putString("Llamada", Llamada);
            Fragment fragment = null;
            fragment = fraBusProducto.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            lblTitulo.setText("Seleccionar Producto");

        }else if (Llamada.equals("BUSPRODUCTOVENTA")){
            Bundle arguments = new Bundle();
            arguments.putString("Llamada", Llamada);
            Fragment fragment = null;
            fragment = fraBusProducto.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            lblTitulo.setText("Seleccionar Producto");

        }else if (Llamada.equals("VENTA")){
            Bundle arguments = new Bundle();
            arguments.putString("Llamada", "LECTURA");
            arguments.putString("EsContenedor", "S");
            arguments.putString("IdCabVenta", Id);
            Fragment fragment = null;
            fragment = fraNueVenta.newInstance(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            lblTitulo.setText("Venta");

        }

    }
    @Override
    public void Cerrar(Intent Datos){
        setResult(RESULT_OK, Datos);
        finish();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == -1) {
            if (Llamada.equals("BUSPRODUCTO")){
                Llamada = "NUEVO";
                Intent i = new Intent(this, ActEditProdOrden.class);
                i.putExtra("Llamada", "Nuevo");
                i.putExtra("IdProducto", data.getStringExtra("IdProducto"));
                i.putExtra("Codigo", data.getStringExtra("Codigo"));
                i.putExtra("Nombre", data.getStringExtra("Nombre"));
                i.putExtra("Cantidad", "0");
                i.putExtra("Valor", "0");
                startActivityForResult(i, 0);
            }else if (Llamada.equals("BUSPRODUCTOVENTA")){
                Llamada = "Nuevo";
                Intent i = new Intent(this, ActEditProdOrden.class);
                i.putExtra("Llamada", "BUSPRODUCTOVENTA");
                i.putExtra("IdProducto", data.getStringExtra("IdProducto"));
                i.putExtra("Codigo", data.getStringExtra("Codigo"));
                i.putExtra("Nombre", data.getStringExtra("Nombre"));
                i.putExtra("Cantidad", "1");
                i.putExtra("Valor", data.getStringExtra("Valor"));
                i.putExtra("ValorCompra", data.getStringExtra("ValorCompra"));
                startActivityForResult(i, 0);
            }else{
                setResult(RESULT_OK, data);
                finish();
            }


        }
    }
}
