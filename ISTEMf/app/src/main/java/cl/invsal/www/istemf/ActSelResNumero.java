package cl.invsal.www.istemf;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 03-09-2017.
 */

public class ActSelResNumero extends Fragment {
    public static String Llamada = "";
    private String Accion = "Buscar",vIdRubroParametro,vIdSucursalParametro,vIdEmpresaParametro;
    private String vIdCategoria;
    private String vNumero;
    private String vEmpresa;
    private String vNumSolo;
    private String vCategoria;
    private String vLetra;
    private String vCiclo;
    private static String vFecha;
    private int FilaCategoria;
    private ProgressDialog pd = null;
    private ListView Lista;
    ejeWS oEjeWS;

    public static ActSelResNumero newInstance(Bundle arguments){
        ActSelResNumero f = new ActSelResNumero();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.laydetalle, container, false);

        pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);
        Lista = (ListView)rootView.findViewById(R.id.lisDatos);

        if (Llamada.equals("RUBRO")){
            Accion = "OBTFECHA";
            getActivity().setTitle("Seleccione Rubro");
        }

        if (Llamada.equals("EMPRESA")){
            getActivity().setTitle("Seleccione Empresa");
            vIdRubroParametro = getArguments().getString("IdRubro");
            Accion = "EMPRESA";
        }

        if (Llamada.equals("SUCURSAL")){
            getActivity().setTitle("Seleccione Sucursal");
            vIdEmpresaParametro = getArguments().getString("IdEmpresa");
            Accion = "SUCURSAL";
        }

        if (Llamada.equals("CATEGORIA")){
            getActivity().setTitle("Seleccione Categoria");
            vIdEmpresaParametro = getArguments().getString("IdEmpresa");
            vIdSucursalParametro = getArguments().getString("IdSucursal");
            Accion = "CATEGORIA";
        }

        oEjeWS = new ejeWS();
        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        setHasOptionsMenu(true);
        return rootView;

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
            }else if (Accion.equals("RUBRO")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("Buscar");
                oEjeWS.setWS("WSRubro.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdRubro", "0");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("EMPRESA")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("Buscar");
                oEjeWS.setWS("WSEmpresa.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdEmpresa", "0");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("Rut", "");
                oEjeWS.AddParametro("Vigencia", "S");
                oEjeWS.AddParametro("IdRubro", vIdRubroParametro);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("SUCURSAL")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("Buscar");
                oEjeWS.setWS("WSSucursal.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdSucursal", "0");
                oEjeWS.AddParametro("Codigo", "");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("Vigencia", "S");
                oEjeWS.AddParametro("IdRegion", "0");
                oEjeWS.AddParametro("IdCiudad", "0");
                oEjeWS.AddParametro("IdComuna", "0");
                oEjeWS.AddParametro("IdPais", "0");
                oEjeWS.AddParametro("IdEmpresa", vIdEmpresaParametro);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CATEGORIA")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("Buscar");
                oEjeWS.setWS("WSCategoria.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCategoria", "0");
                oEjeWS.AddParametro("IdSucursal", vIdSucursalParametro);
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("Vigencia", "S");
                oEjeWS.AddParametro("IdEmpresa", vIdEmpresaParametro);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CALCULATIEMPO")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("BuscarTiempoEspera");
                oEjeWS.setWS("WSCategoriaModulo.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCategoria", vIdCategoria);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("OBTNUMERO")) {
                Funciones oFunciones = new Funciones();
                GeneraCodigo oGeneraCodigo = new GeneraCodigo();

                oEjeWS.setMetodo("ObtNumero");
                oEjeWS.setWS("WSCategoria.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCategoria", vIdCategoria);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("PUBLICIDAD")) {
                Funciones oFunciones = new Funciones();
                oFunciones.BuscarPublicidad(oEjeWS,vIdSucursalParametro,getActivity());
            }else if (Accion.equals("OBTFECHA")) {
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

                if (Accion.equals("RUBRO")) {
                    ResultadoRubro();
                }else if (Accion.equals("EMPRESA")) {
                    ResultadoEmpresa();
                }else if (Accion.equals("SUCURSAL")) {
                    ResultadoSucursal();
                }else if (Accion.equals("CATEGORIA")) {
                    ResultadoCategoria();
                }else if (Accion.equals("CALCULATIEMPO")) {
                    CalculaTiempo();
                }else if (Accion.equals("OBTNUMERO")) {
                    ResObtNumero();
                }else if (Accion.equals("PUBLICIDAD")) {
                    ResPublicidad();
                }else if (Accion.equals("OBTFECHA")) {
                    ResultadoFecha();
                }

            }
        }
    }
    private  void ResultadoFecha() {
        Funciones oFunciones = new Funciones();
        if (oEjeWS.getCount() == 0) {
            pd.dismiss();

            oFunciones.MostrarAlerta(getActivity(), "Error de servidor, no existe fecha configurada", "Número");
            return;
        }

        vFecha = oEjeWS.getValorString(0, "Fecha");
        oFunciones.EliminarNumerosOnlineFecha(vFecha, getActivity());

        Accion = "RUBRO";
        oEjeWS = new ejeWS();
        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResPublicidad(){
        if(oEjeWS.getCount() == 0){
            Bundle i = new Bundle();
            i.putString("Numero",vNumero);
            i.putString("Empresa",vEmpresa);
            i.putString("IdEmpresa",vIdEmpresaParametro);
            i.putString("IdCategoria",vIdCategoria);
            i.putString("Categoria",vCategoria);
            i.putString("Ciclo",vCiclo);
            i.putString("Letra",vLetra);
            i.putString("NumSolo",vNumSolo);
            Fragment fragment = null;
            fragment = new ActVisNumero();
            fragment.setArguments(i);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            pd.dismiss();
            return;
        }

        double numero = Math.random() * oEjeWS.getCount();
        int Fila = (int)numero;
        if (oEjeWS.getValorString(Fila,"TipPub").equals("VIDEO")){
            VideoViewActivity.vNumero = vNumero;
            VideoViewActivity.vEmpresa = vEmpresa;
            VideoViewActivity.vIdCategoria = vIdCategoria;
            VideoViewActivity.vIdEmpresa = vIdEmpresaParametro;
            VideoViewActivity.vNumSolo = vNumSolo;
            VideoViewActivity.vLetra = vLetra;
            VideoViewActivity.vCategoria = vCategoria;
            VideoViewActivity.vCiclo = vCiclo;
            VideoViewActivity.VideoURL = oEjeWS.getValorString(Fila,"URLPub");
            Intent myIntent = new Intent(getActivity(),VideoViewActivity.class);
            startActivityForResult(myIntent, 0);
        }else{
            ActPublicidad.vNumero = vNumero;
            ActPublicidad.vEmpresa = vEmpresa;
            ActPublicidad.vIdCategoria = vIdCategoria;
            ActPublicidad.vIdEmpresa = vIdEmpresaParametro;
            ActPublicidad.vNumSolo = vNumSolo;
            ActPublicidad.vLetra = vLetra;
            ActPublicidad.vCategoria = vCategoria;
            ActPublicidad.vCiclo = vCiclo;
            ActPublicidad.URL = oEjeWS.getValorString(Fila,"URLPub");
            Intent i = new Intent(getActivity(), ActPublicidad.class);
            startActivityForResult(i, 0);
        }
        pd.dismiss();
    }
    private void ResObtNumero(){
        Funciones oFunciones = new Funciones();

        vNumero = oEjeWS.getValorString(0,"Letra") + " - " + oEjeWS.getValorString(0,"Ciclo") + " - " + oEjeWS.getValorString(0,"Numero");
        vEmpresa = oEjeWS.getValorString(0,"NomEmp");
        vIdCategoria = oEjeWS.getValorString(0,"IdCategoria");
        vIdEmpresaParametro = oEjeWS.getValorString(0,"IdEmpresa");
        vNumSolo = oEjeWS.getValorString(0,"Numero");
        vLetra = oEjeWS.getValorString(0,"Letra");
        vCiclo = oEjeWS.getValorString(0,"Ciclo");
        vCategoria = oEjeWS.getValorString(0,"Nombre");

        oFunciones.AgregarNumero(oEjeWS.getValorString(0,"IdEmpresa"),oEjeWS.getValorString(0,"NomEmp")
                                ,oEjeWS.getValorString(0,"IdSucursal"),oEjeWS.getValorString(0,"NomSuc")
                                ,oEjeWS.getValorString(0,"IdCategoria"),oEjeWS.getValorString(0,"Nombre")
                                ,oEjeWS.getValorString(0,"Letra"),oEjeWS.getValorString(0,"Ciclo")
                                ,oEjeWS.getValorString(0,"Numero"),vFecha
                                ,oEjeWS.getValorString(0,"DirSuc"),getActivity());

        RegistrationService.Contexto = getActivity();
        RegistrationService.vIdEmpresa = vIdEmpresaParametro;
        RegistrationService.vIdSucursal = oEjeWS.getValorString(0,"IdSucursal");
        RegistrationService.vIdCategoria = vIdCategoria;
        RegistrationService.vLetra = vLetra;
        RegistrationService.vCiclo = vCiclo;
        RegistrationService.vNumero = vNumSolo;

        Intent i = new Intent(getActivity(), RegistrationService.class);
        getActivity().startService(i);


        Accion = "PUBLICIDAD";

        oEjeWS = new ejeWS();
        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private  void ResultadoRubro(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            pd.dismiss();

            oFunciones.MostrarAlerta(getActivity(),"No existen rubros configurados","Rubro");
            return;
        }

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        for(int i=0; i<oEjeWS.getCount(); i++) {
            Datos.add(new tVarios(oEjeWS.getValorString(i, "Nombre"),
                    "",
                    oEjeWS.getValorString(i, "IdRubro"),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "", "", "", ""));

                adapLis adapter = new adapLis(getActivity(), Datos,true);

                Lista.setAdapter(adapter);

                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                        tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                        ActSelResNumero.Llamada = "EMPRESA";

                        Bundle i = new Bundle();
                        i.putString("IdRubro",oVarios.getValor3());
                        Fragment fragment = new ActSelResNumero();
                        fragment.setArguments(i);

                        getFragmentManager().beginTransaction()
                                .replace(R.id.container, fragment)
                                .commit();
                    }
                });
        }
        pd.dismiss();
    }
    private  void ResultadoEmpresa(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            pd.dismiss();

            oFunciones.MostrarAlerta(getActivity(),"No existen empresas configuradas para este rubro","Rubro");
            return;
        }

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        for(int i=0; i<oEjeWS.getCount(); i++) {
            Datos.add(new tVarios(oEjeWS.getValorString(i, "Rut") + "-" + oEjeWS.getValorString(i, "DV") + " - " + oEjeWS.getValorString(i, "Nombre"),
                    oEjeWS.getValorString(i, "NomRub"),
                    oEjeWS.getValorString(i, "IdEmpresa"),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "", "", "", ""));

            adapLis adapter = new adapLis(getActivity(), Datos,true);

            Lista.setAdapter(adapter);

            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    ActSelResNumero.Llamada = "SUCURSAL";

                    Bundle i = new Bundle();
                    i.putString("IdEmpresa",oVarios.getValor3());
                    Fragment fragment = new ActSelResNumero();
                    fragment.setArguments(i);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });
        }
        pd.dismiss();
    }
    private  void ResultadoSucursal(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            pd.dismiss();

            oFunciones.MostrarAlerta(getActivity(),"No existen sucursales configuradas para esta empresa","Rubro");
            return;
        }

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        for(int i=0; i<oEjeWS.getCount(); i++) {
            Datos.add(new tVarios(oEjeWS.getValorString(i, "Nombre"),
                    oEjeWS.getValorString(i, "NomEmp"),
                    oEjeWS.getValorString(i, "IdSucursal"),
                    oEjeWS.getValorString(i, "IdEmpresa"),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "", "", "", ""));

            adapLis adapter = new adapLis(getActivity(), Datos,true);

            Lista.setAdapter(adapter);

            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    ActSelResNumero.Llamada = "CATEGORIA";

                    Bundle i = new Bundle();
                    i.putString("IdSucursal",oVarios.getValor3());
                    i.putString("IdEmpresa",oVarios.getValor4());
                    Fragment fragment = new ActSelResNumero();
                    fragment.setArguments(i);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });
        }
        pd.dismiss();
    }
    private  void ResultadoCategoria(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            pd.dismiss();

            oFunciones.MostrarAlerta(getActivity(),"No existen categorias configuradas para esta sucursal","Rubro");
            return;
        }

        ArrayList<tVarios> Datos = new ArrayList<tVarios>();

        for(int i=0; i<oEjeWS.getCount(); i++) {
            Datos.add(new tVarios(oEjeWS.getValorString(i, "Nombre"),
                    oEjeWS.getValorString(i, "NomSuc"),
                    oEjeWS.getValorString(i, "IdCategoria"),
                    oEjeWS.getValorString(i, "IdSucursal"),
                    oEjeWS.getValorString(i, "IdEmpresa"),
                    oEjeWS.getValorString(i, "Nombre"),
                    oEjeWS.getValorString(i, "NomSuc"),
                    oEjeWS.getValorString(i, "NomEmp"),
                    "",
                    "",
                    "",
                    "", "", "", ""));

            adapLis adapter = new adapLis(getActivity(), Datos,true);

            Lista.setAdapter(adapter);

            Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View view, int pos, long id) {
                    FilaCategoria = pos;
                    tVarios oVarios = (tVarios) Lista.getItemAtPosition(pos);

                    Accion = "CALCULATIEMPO";
                    vIdCategoria = oVarios.getValor3();
                    pd = ProgressDialog.show(getActivity(), "Procesando", "Espere unos segundos...", true, false);

                    oEjeWS = new ejeWS();
                    EjecutaWS oEjecutaWS = new EjecutaWS();
                    oEjecutaWS.execute();
                }
            });
        }
        pd.dismiss();
    }
    private  void CalculaTiempo(){
        Funciones oFunciones = new Funciones();
        if(oEjeWS.getCount() == 0){
            pd.dismiss();

            oFunciones.MostrarAlerta(getActivity(),"Tiempo de Espera aprox. para siguiente n° es de 0 minutos, por lo tanto no se puede tomar número","Rubro");
            return;
        }

        String MinEspera = String.valueOf((Long.parseLong(oEjeWS.getValorString(0,"Campo3")) - Long.parseLong(oEjeWS.getValorString(0,"Campo1")) * Long.parseLong(oEjeWS.getValorString(0,"Campo2"))));

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
        dialogo1.setTitle("Obtener Número");
        dialogo1.setMessage("El tiempo aproximado de espera para el siguiente número es: " + MinEspera +" minutos. ¿Desea reservar un número?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Aceptar();
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Cancelar();
            }
        });
        dialogo1.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            getActivity().setTitle("Visualizar N°");
            Bundle i = new Bundle();
            i.putString("Numero",data.getStringExtra("Numero"));
            i.putString("Empresa",data.getStringExtra("Empresa"));
            i.putString("IdEmpresa",data.getStringExtra("IdEmpresa"));
            i.putString("IdCategoria",data.getStringExtra("IdCategoria"));
            i.putString("Categoria",data.getStringExtra("Categoria"));
            i.putString("Ciclo",data.getStringExtra("Ciclo"));
            i.putString("Letra",data.getStringExtra("Letra"));
            i.putString("NumSolo",data.getStringExtra("NumSolo"));
            Fragment fragment = null;
            fragment = new ActVisNumero();
            fragment.setArguments(i);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
    private void Aceptar(){
        Funciones oFunciones = new Funciones();
        Cursor Cursor = oFunciones.BuscarNumeroOnline(getActivity(),vIdCategoria);

        if (Cursor.getCount() > 0){
            if (Integer.parseInt(Cursor.getString(Cursor.getColumnIndex("Cantidad")))>= 3){
                oFunciones.MostrarAlerta(getActivity(),"Se ha superado la cantidad maxima para reservar n° en esta categoria","Número");
                return;
            }
        }


        Accion = "OBTNUMERO";

        oEjeWS = new ejeWS();
        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void Cancelar(){
        pd.dismiss();
    }


}
