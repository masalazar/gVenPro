package cl.issoft.www.ismaq;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * Created by MarceloUsuario on 08-22-2017.
 */

public class Sincronizar {
    Cursor c,cLocalTemp,cRecaudacion,cLocal;
    ejeWS oEjeWS;
    String Accion = "",IdNota = "0",IdLocal = "0",Descripcion = "",Identificador = "0",IdRuta="0",Llamada="";
    String Codigo,Nombre,Encargado,Direccion,gFecha,gMonto,IdRecaudacion,Estimado,gEstimado,gLote;
    boolean bTerminar = false;
    boolean bInicioLocalesTemp = true;
    boolean isbTerminarLocalesTemp = false;
    boolean bInicioRecaudacion = true;
    boolean isbTerminarRecaudacion = false;
    boolean bInicioLocal = true;
    boolean isbTerminarLocal = false;
    Context context;
    public void Ejecutar(Context Contexto){
        bTerminar = false;
        context=Contexto;
        ActNotasBD oNotas = new ActNotasBD();
        c = oNotas.BuscarNota(Contexto,"N");

        try{
            if (c.moveToFirst()){
                Actualizar();
            }else{
                AgregarLocales();
            }
        }catch(Exception e) {
            String x = "1";
        }

    }
    private void AgregarLocales(){
        if (bInicioLocalesTemp){
            ActLocalBD oLocal = new ActLocalBD();
            cLocalTemp = oLocal.BuscarLocalTemp(context);

            if (cLocalTemp.moveToFirst()){

            }else{
                AgregarRecaudacion();
                return;
            }
            bInicioLocalesTemp=false;
        }

        if (isbTerminarLocalesTemp) {
            AgregarRecaudacion();
            return;
        }

        Identificador= cLocalTemp.getString(cLocalTemp.getColumnIndex("Identificador"));
        Codigo= cLocalTemp.getString(cLocalTemp.getColumnIndex("Codigo"));
        IdRuta= cLocalTemp.getString(cLocalTemp.getColumnIndex("IdRuta"));
        Nombre= cLocalTemp.getString(cLocalTemp.getColumnIndex("Nombre"));
        Encargado= cLocalTemp.getString(cLocalTemp.getColumnIndex("Encargado"));
        Direccion= cLocalTemp.getString(cLocalTemp.getColumnIndex("Direccion"));
        Estimado= cLocalTemp.getString(cLocalTemp.getColumnIndex("Estimado"));
        IdLocal = "0";

        Accion = "NUEVOLOCAL";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cLocalTemp.moveToNext()){
            isbTerminarLocalesTemp=true;
            cLocalTemp.close();
            return;
        }

    }
    private void ModificarLocales(){
        if (bInicioLocal){
            ActLocalBD oLocal = new ActLocalBD();
            cLocal = oLocal.BuscarLocalNoSubido(context);

            if (cLocal.moveToFirst()){

            }else{
                return;
            }
            bInicioLocal=false;
        }

        if (isbTerminarLocal) return;

        IdLocal= cLocal.getString(cLocal.getColumnIndex("IdLocal"));
        IdRuta= cLocal.getString(cLocal.getColumnIndex("IdRuta"));
        Codigo= cLocal.getString(cLocal.getColumnIndex("Codigo"));
        Nombre= cLocal.getString(cLocal.getColumnIndex("Nombre"));
        Encargado= cLocal.getString(cLocal.getColumnIndex("Encargado"));
        Direccion= cLocal.getString(cLocal.getColumnIndex("Direccion"));
        Estimado= cLocal.getString(cLocal.getColumnIndex("Estimado"));

        Accion = "MODLOCAL";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cLocal.moveToNext()){
            isbTerminarLocal=true;
            cLocal.close();
            return;
        }

    }
    private void AgregarRecaudacion(){
        if (bInicioRecaudacion){
            ActRecaudacionBD oRecaudacion = new ActRecaudacionBD();
            cRecaudacion = oRecaudacion.BuscarRecaudacion(context,"N","-TOD","-TOD","0","0");

            if (cRecaudacion.moveToFirst()){

            }else{
                ModificarLocales();
                return;
            }
            bInicioRecaudacion=false;
        }

        if (isbTerminarRecaudacion) {
            ModificarLocales();
            return;
        }

        IdRecaudacion= cRecaudacion.getString(cRecaudacion.getColumnIndex("IdRecaudacion"));
        IdLocal= cRecaudacion.getString(cRecaudacion.getColumnIndex("IdLocal"));
        gFecha= cRecaudacion.getString(cRecaudacion.getColumnIndex("Fecha"));
        gMonto= cRecaudacion.getString(cRecaudacion.getColumnIndex("Monto"));
        gLote= cRecaudacion.getString(cRecaudacion.getColumnIndex("Lote"));
        gEstimado= cRecaudacion.getString(cRecaudacion.getColumnIndex("Estimado"));

        gFecha = gFecha.substring(6,8) + "/" + gFecha.substring(4,6) + "/" + gFecha.substring(0,4);

        Accion = "RECAUDACION";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cRecaudacion.moveToNext()){
            isbTerminarRecaudacion=true;
            cRecaudacion.close();
            return;
        }

    }
    private void Actualizar(){
        if (bTerminar) {
            c.close();
            AgregarLocales();
            return;
        }

        IdNota= c.getString(c.getColumnIndex("IdNota"));
        Identificador= c.getString(c.getColumnIndex("Identificador"));
        IdLocal= c.getString(c.getColumnIndex("IdLocal"));
        Descripcion= c.getString(c.getColumnIndex("Descripcion"));

        Accion = "NOTAACT";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!c.moveToNext()){
            bTerminar=true;
            return;
        }
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("NOTAACT")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSNota.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdNota", Identificador);
                oEjeWS.AddParametro("IdLocal", IdLocal);
                oEjeWS.AddParametro("Descripcion", Descripcion);
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("NUEVOLOCAL") || Accion.equals("MODLOCAL")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSLocal.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdLocal", IdLocal);
                oEjeWS.AddParametro("Codigo", Codigo);
                oEjeWS.AddParametro("Nombre", Nombre);
                oEjeWS.AddParametro("Encargado", Encargado);
                oEjeWS.AddParametro("Direccion", Direccion);
                oEjeWS.AddParametro("Estimado", Estimado);
                oEjeWS.AddParametro("Vigencia", "S");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdRuta", IdRuta);
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("RECAUDACION")) {
                oEjeWS.setMetodo("Agregar");

                oEjeWS.setWS("WSRecaudacion.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdLocal", IdLocal);
                oEjeWS.AddParametro("Monto", gMonto);
                oEjeWS.AddParametro("Fecha", gFecha);
                oEjeWS.AddParametro("Estimado", gEstimado);
                oEjeWS.AddParametro("Lote", gLote);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (oEjeWS.getNumError() > 0) {
                    Funciones oFunciones = new Funciones();

                    if (Llamada.equals("MENU")){oFunciones.MostrarAlerta(context, oEjeWS.getDesError(), "E R R O R");}
                    return;
                }

                if (Accion.equals("NOTAACT")) {
                    ActNotasBD oNota = new ActNotasBD();
                    oNota.ModificarSubido(IdNota,"S",context);
                    Actualizar();
                }else if (Accion.equals("NUEVOLOCAL")) {
                    ActRecaudacionBD oRecaudacion = new ActRecaudacionBD();
                    IdLocal = oEjeWS.getValorString(0,"IdLocal");
                    oRecaudacion.ModificarIdLocal(IdLocal,Identificador,context);
                    ActLocalBD oLocal = new ActLocalBD();
                    oLocal.EliminarLocalTemp(Identificador,context);
                    oLocal.AgregarLocal(IdLocal,Codigo,Nombre,"S",IdRuta,Encargado,Direccion,Estimado, context);
                    Identificador = "0";
                    AgregarLocales();

                }else if (Accion.equals("RECAUDACION")) {
                    ActRecaudacionBD oRecaudacion = new ActRecaudacionBD();
                    oRecaudacion.ModificarSubido(IdRecaudacion,"S",context);
                    AgregarRecaudacion();
                }else if (Accion.equals("MODLOCAL")) {
                    ActLocalBD oLocal = new ActLocalBD();
                    oLocal.ModificarSubido(IdLocal,context);
                    ModificarLocales();
                }
            }
        }
    }
}
