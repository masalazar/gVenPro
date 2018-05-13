package cl.invsal.www.istemf;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 03-05-2017.
 */

public class ActBusNumeroLocal extends Fragment {
    public static String Llamada = "";
    private String Accion = "Buscar";
    private Context Contexto = getActivity();
    private ProgressDialog pd = null;
    private ListView Lista;
    ejeWS oEjeWS;

    public static ActBusNumeroLocal newInstance(Bundle arguments){
        ActBusNumeroLocal f = new ActBusNumeroLocal();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laydetalle, container, false);

        Accion = "OBTFECHA";

        pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);
        Lista = (ListView)rootView.findViewById(R.id.lisDatos);

        oEjeWS = new ejeWS();
        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        setHasOptionsMenu(true);
        return rootView;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            if (Accion.equals("OBTFECHA")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("ObtenerFecha");
                oEjeWS.setWS("wsVarios.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("Formato", "yyyyMMdd");
                oEjeWS.sEjeWS();
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (oEjeWS.getNumError() > 0) {
                    Funciones oFunciones = new Funciones();

                    oFunciones.MostrarAlerta(getActivity(), oEjeWS.getDesError(), "E R R O R");
                    pd.dismiss();
                    return;
                }

                if (Accion.equals("OBTFECHA")) {
                    ResultadoBuscar();
                }
            }
        }
    }
    private  void ResultadoBuscar(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            pd.dismiss();

            oFunciones.MostrarAlerta(getActivity(),"Error de servidor, no existe fecha configurada","Número");
            return;
        }

        String Fecha = oEjeWS.getValorString(0,"Fecha");
        oFunciones.EliminarFecha(Fecha,getActivity());

        if (Llamada.equals("MOSTODO") || Llamada.equals("MOSTODOEMP") || Llamada.equals("MOSTODOEMPSUC")){
            getActivity().setTitle("Números en ejecución");

            String IdEmpresaParametro = "0";
            String IdSucursalParametro = "0";

            if (Llamada.equals("MOSTODOEMP")) {
                IdEmpresaParametro = getArguments().getString("IdEmpresa");
            }

            if (Llamada.equals("MOSTODOEMPSUC")) {
                IdEmpresaParametro = getArguments().getString("IdEmpresa");
                IdSucursalParametro = getArguments().getString("IdSucursal");
            }
            Cursor cCursor = oFunciones.BuscarNumero(getActivity(),IdEmpresaParametro,IdSucursalParametro,"0","0","0","0");
            ArrayList<tVarios> Datos = new ArrayList<tVarios>();

            if (cCursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String IdEmpresa= cCursor.getString(cCursor.getColumnIndex("IdEmpresa"));
                    String IdSucursal= cCursor.getString(cCursor.getColumnIndex("IdSucursal"));
                    String IdCategoria= cCursor.getString(cCursor.getColumnIndex("IdCategoria"));
                    String Ciclo= cCursor.getString(cCursor.getColumnIndex("Ciclo"));
                    String Letra= cCursor.getString(cCursor.getColumnIndex("Letra"));
                    String Numero= cCursor.getString(cCursor.getColumnIndex("Numero"));
                    String Empresa = cCursor.getString(cCursor.getColumnIndex("NombreEmpresa"));
                    String Categoria = cCursor.getString(cCursor.getColumnIndex("NombreCategoria"));
                    String Sucursal = cCursor.getString(cCursor.getColumnIndex("NombreSucursal"));
                    String LetraNumero = cCursor.getString(cCursor.getColumnIndex("Letra")) + " - " + cCursor.getString(cCursor.getColumnIndex("Ciclo")) + " - " + cCursor.getString(cCursor.getColumnIndex("Numero"));

                    Datos.add(new tVarios(LetraNumero,"Empresa: " + Empresa + " Sucursal: " + Sucursal + " Categoria: " + Categoria,
                            IdEmpresa,
                            IdSucursal,
                            IdCategoria,
                            Ciclo,
                            Letra,
                            Numero,
                            Empresa,Sucursal,Categoria,"","","",""));

                } while(cCursor.moveToNext());

                adapLis adapter = new adapLis(getActivity(), Datos,true);

                Lista.setAdapter(adapter);

                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                        tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                        if (Llamada.equals("MOSTODO") || Llamada.equals("MOSTODOEMP") || Llamada.equals("MOSTODOEMPSUC")){

                            Bundle i = new Bundle();
                            i.putString("Numero",oVarios.getValor7() + " - " + oVarios.getValor6() + " - " + oVarios.getValor8());
                            i.putString("Empresa",oVarios.getValor9() + " - Suc. " + oVarios.getValor10());
                            i.putString("IdEmpresa",oVarios.getValor3());
                            i.putString("IdCategoria",oVarios.getValor5());
                            i.putString("Categoria",oVarios.getValor11());
                            i.putString("Ciclo",oVarios.getValor6());
                            i.putString("Letra",oVarios.getValor7());
                            i.putString("NumSolo",oVarios.getValor8());
                            Fragment fragment = new ActVisNumero();
                            fragment.setArguments(i);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();
                        }
                    }
                });
            }else{
                oFunciones.MostrarAlerta(getActivity(),"No existen números en ejecución","Números");
                pd.dismiss();
                return;
            }


            //Bundle bundle = getIntent().getExtras();
            //Rut = bundle.getString("Rut");
            //Nombre = bundle.getString("Nombre");
        }else if(Llamada.equals("MOSEMPRESA") || Llamada.equals("MOSEMPRESASUC")){
            getActivity().setTitle("Seleccione Empresa");

            Cursor cCursor = oFunciones.BuscarSoloEmpresas(getActivity());

            ArrayList<tVarios> Datos = new ArrayList<tVarios>();

            if (cCursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String IdEmpresa= cCursor.getString(cCursor.getColumnIndex("IdEmpresa"));
                    String Empresa = cCursor.getString(cCursor.getColumnIndex("NombreEmpresa"));

                    Datos.add(new tVarios(Empresa,
                            "",
                            IdEmpresa,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "","","","","","",""));

                } while(cCursor.moveToNext());

                adapLis adapter = new adapLis(getActivity(), Datos,true);

                Lista.setAdapter(adapter);

                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                        tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                        if (Llamada.equals("MOSEMPRESA")){
                            Bundle i = new Bundle();
                            ActBusNumeroLocal.Llamada = "MOSTODOEMP";
                            i.putString("IdEmpresa",oVarios.getValor3());
                            Fragment fragment = new ActBusNumeroLocal();
                            fragment.setArguments(i);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();

                        }
                        if (Llamada.equals("MOSEMPRESASUC")){
                            ActBusNumeroLocal.Llamada = "MOSSUCURSAL";

                            Bundle i = new Bundle();
                            i.putString("IdEmpresa",oVarios.getValor3());
                            Fragment fragment = new ActBusNumeroLocal();
                            fragment.setArguments(i);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();
                        }
                    }
                });
            }else{
                oFunciones.MostrarAlerta(getActivity(),"No existen números en ejecución","Números");
                pd.dismiss();
                return;
            }
        }else if(Llamada.equals("MOSSUCURSAL")){
            getActivity().setTitle("Seleccione Sucursal");

            String IdEmpresaParametro = getArguments().getString("IdEmpresa");

            Cursor cCursor = oFunciones.BuscarSoloSucursales(getActivity(),IdEmpresaParametro);

            ArrayList<tVarios> Datos = new ArrayList<tVarios>();

            if (cCursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String IdEmpresa = cCursor.getString(cCursor.getColumnIndex("IdEmpresa"));
                    String IdSucursal = cCursor.getString(cCursor.getColumnIndex("IdSucursal"));
                    String Empresa = cCursor.getString(cCursor.getColumnIndex("NombreEmpresa"));
                    String Sucursal = cCursor.getString(cCursor.getColumnIndex("NombreSucursal"));
                    String Direccion = cCursor.getString(cCursor.getColumnIndex("Direccion"));

                    Datos.add(new tVarios(Sucursal,
                            Empresa + " - Dir.: " + Direccion,
                            IdEmpresa,
                            IdSucursal,
                            "",
                            "",
                            "",
                            "",
                            "","","","","","",""));

                } while(cCursor.moveToNext());

                adapLis adapter = new adapLis(getActivity(), Datos,true);

                Lista.setAdapter(adapter);

                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                        tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                        if (Llamada.equals("MOSSUCURSAL")){
                            ActBusNumeroLocal.Llamada = "MOSTODOEMPSUC";

                            Bundle i = new Bundle();
                            i.putString("IdEmpresa",oVarios.getValor3());
                            i.putString("IdSucursal",oVarios.getValor4());
                            Fragment fragment = new ActBusNumeroLocal();
                            fragment.setArguments(i);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();
                        }
                    }
                });
            }else{
                oFunciones.MostrarAlerta(getActivity(),"No existen números en ejecución","Números");
                pd.dismiss();
                return;
            }
        }

        pd.dismiss();
    }
}
