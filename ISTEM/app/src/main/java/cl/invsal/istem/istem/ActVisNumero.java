package cl.invsal.istem.istem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 02-28-2017.
 */

public class ActVisNumero extends AppCompatActivity {
    ImageView a;
    ProgressDialog pDialog;
    ejeWS oEjeWS;
    TextView lblTuNumero;
    TextView lblEmpresa;
    TextView lblNumero1;
    TextView lblNumero2;
    TextView lblNumero3;
    TextView lblNumero4;
    TextView lblCategoria1;
    TextView lblCategoria2;
    TextView lblCategoria3;
    TextView lblCategoria4;
    public static  String vNumero;
    public static String vEmpresa;
    public static String vIdCategoria;
    public static String vIdEmpresa;
    public static final String URL = "http://www.issoft.cl/x.jpg";
    private String Accion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layvisnumero);

        a = (ImageView)findViewById(R.id.a);
        lblTuNumero = (TextView)findViewById(R.id.lblTuNumero);
        lblEmpresa = (TextView)findViewById(R.id.lblEmpresa);
        lblNumero1 = (TextView)findViewById(R.id.lblNumero1);
        lblNumero2 = (TextView)findViewById(R.id.lblNumero2);
        lblNumero3 = (TextView)findViewById(R.id.lblNumero3);
        lblNumero4 = (TextView)findViewById(R.id.lblNumero4);
        lblCategoria1 = (TextView)findViewById(R.id.lblCategoria1);
        lblCategoria2 = (TextView)findViewById(R.id.lblCategoria2);
        lblCategoria3 = (TextView)findViewById(R.id.lblCategoria3);
        lblCategoria4 = (TextView)findViewById(R.id.lblCategoria4);

        lblTuNumero.setText(vNumero);
        lblEmpresa.setText(vEmpresa);

        CargaImagenes nuevaTarea = new CargaImagenes();
        nuevaTarea.execute(URL);

    }

    private class CargaImagenes extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ActVisNumero.this);
            pDialog.setMessage("Cargando Imagen");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground" , "Entra en doInBackground");
            String url = params[0];
            Bitmap imagen = descargarImagen(url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            a.setImageBitmap(result);
            pDialog.dismiss();

            Accion = "OBTULT4";

            oEjeWS = null;
            oEjeWS = new ejeWS();

            EjecutaWS oEjecutaWS = new EjecutaWS();
            oEjecutaWS.execute();
        }

    }
    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("OBTULT4")) {
                oEjeWS.setMetodo("BuscarUlt4");
                oEjeWS.setWS("WSCategoriaModulo.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCategoria", vIdCategoria);
                oEjeWS.AddParametro("IdEmpresa", vIdEmpresa);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (oEjeWS.getNumError() > 0) {
                    Funciones oFunciones = new Funciones();

                    oFunciones.MostrarAlerta(ActVisNumero.this, oEjeWS.getDesError(), "E R R O R");
                    pDialog.dismiss();
                    return;
                }

                if (Accion.equals("OBTULT4")) {
                    ResultadoObtUlt4();
                }
            }
        }
    }
    private void ResultadoObtUlt4(){
        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        if (oEjeWS.getCount() > 0){
            lblNumero1.setText(oEjeWS.getValorString(0, "Letra") + " - " + oEjeWS.getValorString(0, "Numero"));
            lblCategoria1.setText(oEjeWS.getValorString(0, "NomCat"));
        }

        if (oEjeWS.getCount() > 1){
            lblNumero2.setText(oEjeWS.getValorString(1, "Letra") + " - " + oEjeWS.getValorString(1, "Numero"));
            lblCategoria2.setText(oEjeWS.getValorString(1, "NomCat"));
        }

        if (oEjeWS.getCount() > 2){
            lblNumero3.setText(oEjeWS.getValorString(2, "Letra") + " - " + oEjeWS.getValorString(2, "Numero"));
            lblCategoria3.setText(oEjeWS.getValorString(2, "NomCat"));
        }

        if (oEjeWS.getCount() > 3){
            lblNumero4.setText(oEjeWS.getValorString(3, "Letra") + " - " + oEjeWS.getValorString(3, "Numero"));
            lblCategoria4.setText(oEjeWS.getValorString(3, "NomCat"));
        }


        pDialog.dismiss();
    }

}

