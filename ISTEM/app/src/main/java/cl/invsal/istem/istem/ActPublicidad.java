package cl.invsal.istem.istem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MarceloUsuario on 02-28-2017.
 */

public class ActPublicidad extends AppCompatActivity {
    private ImageView a;
    public static String URL;
    public static String vNumero;
    public static String vEmpresa;
    public static String vIdCategoria;
    static String vIdEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laypublicidad);

        a = (ImageView)findViewById(R.id.a);

        ActPublicidad.CargaImagenes nuevaTarea = new ActPublicidad.CargaImagenes();
        nuevaTarea.execute(URL);

    }
    private class CargaImagenes extends AsyncTask<String, Void, Bitmap> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ActPublicidad.this);
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
        }

    }
    private Bitmap descargarImagen (String imageHttpAddress){
        java.net.URL imageUrl = null;
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

    public void fnAbrirNumero(View view){
        Salir();

    }
    private void Salir(){
        ActVisNumero.vNumero = vNumero;
        ActVisNumero.vEmpresa = vEmpresa;
        ActVisNumero.vIdEmpresa = vIdEmpresa;
        ActVisNumero.vIdCategoria = vIdCategoria;
        Intent i = new Intent(this, ActVisNumero.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed (){
        Salir();
    }
}

