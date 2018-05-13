package cl.invsal.www.istemf;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.support.v4.app.Fragment;

/**
 * Created by MarceloUsuario on 03-05-2017.
 */

public class ActVisNumero extends Fragment {
    ImageView a;
    ProgressDialog pDialog;
    ejeWS oEjeWS;
    TextView lblTuNumero, lblEmpresa, lblNumero1, lblNumero2, lblNumero3;
    TextView lblNumero4, lblModulo1, lblModulo2, lblModulo3, lblModulo4;
    TextView lblCategoria, lblTiempoEstimado;
    private String vNumero;
    private String vEmpresa;
    private String vCiclo;
    private String vNumSolo;
    private String vLetra;
    private String vIdCategoria;
    private String vCategoria;
    private String vIdEmpresa;
    public static final String URL = "http://www.issoft.cl/x.jpg";
    private String Accion;

    public static ActVisNumero newInstance(Bundle arguments){
        ActVisNumero f = new ActVisNumero();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.layvisnumero, container, false);

        vNumero = getArguments().getString("Numero");
        vCiclo = getArguments().getString("Ciclo");
        vNumSolo = getArguments().getString("NumSolo");
        vLetra = getArguments().getString("Letra");
        vEmpresa = getArguments().getString("Empresa");
        vIdCategoria = getArguments().getString("IdCategoria");
        vCategoria = getArguments().getString("Categoria");
        vIdEmpresa = getArguments().getString("IdEmpresa");

        a = (ImageView)rootView.findViewById(R.id.a);
        lblTuNumero = (TextView)rootView.findViewById(R.id.lblTuNumero);
        lblEmpresa = (TextView)rootView.findViewById(R.id.lblEmpresa);
        lblNumero1 = (TextView)rootView.findViewById(R.id.lblNumero1);
        lblNumero2 = (TextView)rootView.findViewById(R.id.lblNumero2);
        lblNumero3 = (TextView)rootView.findViewById(R.id.lblNumero3);
        lblNumero4 = (TextView)rootView.findViewById(R.id.lblNumero4);
        lblModulo1 = (TextView)rootView.findViewById(R.id.lblModulo1);
        lblModulo2 = (TextView)rootView.findViewById(R.id.lblModulo2);
        lblModulo3 = (TextView)rootView.findViewById(R.id.lblModulo3);
        lblModulo4 = (TextView)rootView.findViewById(R.id.lblModulo4);
        lblCategoria = (TextView)rootView.findViewById(R.id.lblCategoria);
        lblTiempoEstimado = (TextView)rootView.findViewById(R.id.lblTiempoEstimado);

        lblTuNumero.setText(vNumero);
        lblEmpresa.setText(vEmpresa);
        lblCategoria.setText(vCategoria);

        CargaImagenes nuevaTarea = new CargaImagenes();
        nuevaTarea.execute(URL);

        setHasOptionsMenu(true);
        return rootView;
    }

    private class CargaImagenes extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
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
            Accion = "OBTULT4";

            oEjeWS = null;
            oEjeWS = new ejeWS();

            EjecutaWS oEjecutaWS = new EjecutaWS();
            oEjecutaWS.execute();
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

                    lblTiempoEstimado.setText("No se puede calcular tiempo estimado");

                    oFunciones.MostrarAlerta(getActivity(), oEjeWS.getDesError(), "E R R O R");
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
            lblModulo1.setText(oEjeWS.getValorString(0, "NomMod"));
        }

        if (oEjeWS.getCount() > 1){
            lblNumero2.setText(oEjeWS.getValorString(1, "Letra") + " - " + oEjeWS.getValorString(1, "Numero"));
            lblModulo2.setText(oEjeWS.getValorString(1, "NomMod"));
        }

        if (oEjeWS.getCount() > 2){
            lblNumero3.setText(oEjeWS.getValorString(2, "Letra") + " - " + oEjeWS.getValorString(2, "Numero"));
            lblModulo3.setText(oEjeWS.getValorString(2, "NomMod"));
        }

        if (oEjeWS.getCount() > 3){
            lblNumero4.setText(oEjeWS.getValorString(3, "Letra") + " - " + oEjeWS.getValorString(3, "Numero"));
            lblModulo4.setText(oEjeWS.getValorString(3, "NomMod"));
        }

        String NumLocalC;
        String NumBDC;

        if (Integer.parseInt(vNumSolo) < 10){
            NumLocalC = vCiclo + "0" + vNumSolo;
        }else{
            NumLocalC = vCiclo + vNumSolo;
        }

        if (Integer.parseInt(oEjeWS.getValorString(0, "Numero")) < 10){
            NumBDC = oEjeWS.getValorString(0, "Ciclo") + "0" + oEjeWS.getValorString(0, "Numero");
        }else{
            NumBDC = oEjeWS.getValorString(0, "Ciclo") + oEjeWS.getValorString(0, "Numero");
        }

        String TiempoEspera = String.valueOf((Long.parseLong(NumLocalC) - Long.parseLong(NumBDC)) * Long.parseLong(oEjeWS.getValorString(0,"MinEspera")));

        lblTiempoEstimado.setText("Tiempo aproximado para atención: " + TiempoEspera + " minutos, el tiempo de espera es referencial y puede variar");
        pDialog.dismiss();
    }

}

