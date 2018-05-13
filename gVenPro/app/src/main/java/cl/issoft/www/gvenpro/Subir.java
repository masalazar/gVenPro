package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;

/**
 * Created by MarceloUsuario on 04-09-2018.
 */

public class Subir {
    private Cursor cProducto,cProveedor,cCliente,cComuna,cCaja,cCabOrdenCompra,cCabVenta;
    private Cursor cDetOrdenCompra,cDetVenta,cPago,cStock;
    ejeWS oEjeWS;
    private Context context;
    private boolean bTerminarProducto = false,bTerminaProveedor = false,bTerminaCliente=false;
    private boolean bTerminaComuna = false,bTerminaCaja = false,bTerminaCabOrdenCompra,bTerminaCabVenta;
    private boolean bTerminaDetOrdenCompra = false,bTerminaDetVenta,bTerminaPago,bTerminaStock;
    private String IdProducto,Identificador,Codigo,Nombre,Accion,IdProveedor,Rut,Vendedor,Direccion,IdCliente,IdStock;
    private String IdComuna,IdCaja,IdUsuario,Fecha,ValorApertura,ValorCierre,Cuadrado,Cerrado,DineroEntregado;
    private String FechaEstimadaCompra,Cantidad,ValorNeto,Pagada,Recibida,Facturada,Solicitada,Anulada,Vigencia,IdFamilia,Imagen;
    private String MotivoAnulada,Iva,ValorTotal,Descuento,IdCabOrdenCompra,IdCabVenta,Pendiente,IdDetOrdenCompra;
    private String IdDetVenta,IdPago,Valor,CantidadFisica,CantidadCritica,ValorVenta,ValorCompra;
    public void Iniciar(Context Contexto){
        context = Contexto;
        SubirProducto();
    }
    private void SubirStock(){
        bdStock oStock = new bdStock();
        cStock = oStock.BuscarNoSubido(context);

        try{
            if (cStock.moveToFirst()){
                RecorrerStock();
            }else{
                //SubirProveedor();
            }

            Funciones oFunciones = new Funciones();
            oFunciones.MostrarAlerta(context,"La operación se realizo correctamente","Subir");
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirProducto(){
        bdProducto oProducto = new bdProducto();
        cProducto = oProducto.BuscarNoSubido(context);

        try{
            if (cProducto.moveToFirst()){
                RecorrerProductos();
            }else{
                SubirProveedor();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirProveedor(){
        bdProveedor oProveedor = new bdProveedor();
        cProveedor = oProveedor.BuscarNoSubido(context);

        try{
            if (cProveedor.moveToFirst()){
                RecorrerProveedor();
            }else{
                SubirComuna();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirCliente(){
        bdCliente oCliente = new bdCliente();
        cCliente = oCliente.BuscarNoSubido(context);

        try{
            if (cCliente.moveToFirst()){
                RecorrerCliente();
            }else{
                SubirCaja();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirComuna(){
        bdComuna oComuna = new bdComuna();
        cComuna = oComuna.BuscarNoSubido(context);

        try{
            if (cComuna.moveToFirst()){
                RecorrerComuna();
            }else{
                SubirCliente();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirCaja(){
        bdCaja oCaja = new bdCaja();
        cCaja = oCaja.BuscarNoSubido(context);

        try{
            if (cCaja.moveToFirst()){
                RecorrerCaja();
            }else{
                SubirCabOrdenCompra();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirCabOrdenCompra(){
        bdCabOrdenCompra oCabOrdenCompra = new bdCabOrdenCompra();
        cCabOrdenCompra = oCabOrdenCompra.BuscarNoSubido(context);

        try{
            if (cCabOrdenCompra.moveToFirst()){
                RecorrerCabOrdenCompra();
            }else{
                SubirCabVenta();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirCabVenta(){
        bdCabVenta oCabVenta = new bdCabVenta();
        cCabVenta = oCabVenta.BuscarNoSubido(context);

        try{
            if (cCabVenta.moveToFirst()){
                RecorrerCabVenta();
            }else{
                SubirDetOrdenCompra();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirDetOrdenCompra(){
        bdDetOrdenCompra oDetOrdenCompra = new bdDetOrdenCompra();
        cDetOrdenCompra = oDetOrdenCompra.BuscarNoSubido(context);

        try{
            if (cDetOrdenCompra.moveToFirst()){
                RecorrerDetOrdenCompra();
            }else{
                SubirDetVenta();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirDetVenta(){
        bdDetVenta oDetVenta = new bdDetVenta();
        cDetVenta = oDetVenta.BuscarNoSubido(context);

        try{
            if (cDetVenta.moveToFirst()){
                RecorrerDetVenta();
            }else{
                SubirPago();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void SubirPago(){
        bdPago oPago = new bdPago();
        cPago = oPago.BuscarNoSubido(context);

        try{
            if (cPago.moveToFirst()){
                RecorrerPago();
            }else{
                SubirStock();
            }
        }catch(Exception e) {
            String x = "1";
        }
    }
    private void RecorrerPago(){
        if (bTerminaPago) {
            cPago.close();
            SubirStock();
            return;
        }

        Accion = "PAGO";
        IdPago= cPago.getString(cPago.getColumnIndex("IdPago"));
        Identificador= cPago.getString(cPago.getColumnIndex("Identificador"));
        Fecha= cPago.getString(cPago.getColumnIndex("Fecha"));
        IdCaja = cPago.getString(cPago.getColumnIndex("IdenCaj"));
        IdCabVenta = cPago.getString(cPago.getColumnIndex("IdenCVE"));
        Valor= cPago.getString(cPago.getColumnIndex("Valor"));
        Fecha = Fecha.substring(6,8) + '/' + Fecha.substring(4,6) + '/' + Fecha.substring(0,4);

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cPago.moveToNext()){
            bTerminaPago=true;
            cPago.close();
            return;
        }
    }
    private void RecorrerStock(){
        if (bTerminaStock) {
            cStock.close();
            return;
        }

        Accion = "STOCK";
        IdStock= cStock.getString(cStock.getColumnIndex("IdStock"));
        Identificador= cStock.getString(cStock.getColumnIndex("Identificador"));
        IdProducto= cStock.getString(cStock.getColumnIndex("IdenPro"));
        CantidadFisica = cStock.getString(cStock.getColumnIndex("CantidadFisica"));
        CantidadCritica = cStock.getString(cStock.getColumnIndex("CantidadCritica"));
        ValorVenta= cStock.getString(cStock.getColumnIndex("ValorVenta"));
        ValorCompra= cStock.getString(cStock.getColumnIndex("ValorCompra"));

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cStock.moveToNext()){
            bTerminaStock=true;
            cStock.close();
            return;
        }
    }
    private void RecorrerDetVenta(){
        if (bTerminaDetVenta) {
            cDetVenta.close();
            SubirPago();
            return;
        }

        Accion = "DETVENTA";
        IdCabVenta= cDetVenta.getString(cDetVenta.getColumnIndex("IdenCVE"));
        IdDetVenta= cDetVenta.getString(cDetVenta.getColumnIndex("IdDetVenta"));
        Identificador= cDetVenta.getString(cDetVenta.getColumnIndex("Identificador"));
        IdProducto= cDetVenta.getString(cDetVenta.getColumnIndex("IdenPro"));
        Cantidad= cDetVenta.getString(cDetVenta.getColumnIndex("Cantidad"));
        Valor= cDetVenta.getString(cDetVenta.getColumnIndex("Valor"));

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cDetVenta.moveToNext()){
            bTerminaDetVenta=true;
            cDetVenta.close();
            return;
        }
    }
    private void RecorrerDetOrdenCompra(){
        if (bTerminaDetOrdenCompra) {
            cDetOrdenCompra.close();
            SubirDetVenta();
            return;
        }

        Accion = "DETORDENCOMPRA";
        IdCabOrdenCompra= cDetOrdenCompra.getString(cDetOrdenCompra.getColumnIndex("IdenCOC"));
        IdDetOrdenCompra= cDetOrdenCompra.getString(cDetOrdenCompra.getColumnIndex("IdDetOrdenCompra"));
        Identificador= cDetOrdenCompra.getString(cDetOrdenCompra.getColumnIndex("Identificador"));
        IdProducto= cDetOrdenCompra.getString(cDetOrdenCompra.getColumnIndex("IdenPro"));
        Cantidad= cDetOrdenCompra.getString(cDetOrdenCompra.getColumnIndex("Cantidad"));
        Valor= cDetOrdenCompra.getString(cDetOrdenCompra.getColumnIndex("Valor"));

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cDetOrdenCompra.moveToNext()){
            bTerminaDetOrdenCompra=true;
            cDetOrdenCompra.close();
            return;
        }
    }
    private void RecorrerCabVenta(){
        if (bTerminaCabVenta) {
            cCabVenta.close();
            SubirDetOrdenCompra();
            return;
        }

        Accion = "CABVENTA";
        IdCliente= cCabVenta.getString(cCabVenta.getColumnIndex("IdenCli"));
        IdCabVenta= cCabVenta.getString(cCabVenta.getColumnIndex("IdCabVenta"));
        Identificador= cCabVenta.getString(cCabVenta.getColumnIndex("Identificador"));
        Fecha= cCabVenta.getString(cCabVenta.getColumnIndex("Fecha"));
        Cantidad= cCabVenta.getString(cCabVenta.getColumnIndex("Cantidad"));
        ValorNeto= cCabVenta.getString(cCabVenta.getColumnIndex("ValorNeto"));
        Pagada= cCabVenta.getString(cCabVenta.getColumnIndex("Pagada"));
        Pendiente= cCabVenta.getString(cCabVenta.getColumnIndex("Pendiente"));
        Facturada= cCabVenta.getString(cCabVenta.getColumnIndex("Facturada"));
        Anulada= cCabVenta.getString(cCabVenta.getColumnIndex("Anulada"));
        MotivoAnulada= cCabVenta.getString(cCabVenta.getColumnIndex("MotivoAnulada"));
        Iva= cCabVenta.getString(cCabVenta.getColumnIndex("Iva"));
        ValorTotal= cCabVenta.getString(cCabVenta.getColumnIndex("ValorTotal"));
        Descuento= cCabVenta.getString(cCabVenta.getColumnIndex("Descuento"));
        Fecha = Fecha.substring(6,8) + '/' + Fecha.substring(4,6) + '/' + Fecha.substring(0,4);

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cCabVenta.moveToNext()){
            bTerminaCabVenta=true;
            cCabVenta.close();
            return;
        }
    }
    private void RecorrerCabOrdenCompra(){
        if (bTerminaCabOrdenCompra) {
            cCabOrdenCompra.close();
            SubirCabVenta();
            return;
        }

        Accion = "CABORDENCOMPRA";
        IdProveedor= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("IdenPro"));
        IdCabOrdenCompra= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("IdCabOrdenCompra"));
        Identificador= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Identificador"));
        Fecha= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Fecha"));
        FechaEstimadaCompra= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("FechaEstimadaCompra"));
        Cantidad= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Cantidad"));
        ValorNeto= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("ValorNeto"));
        Pagada= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Pagada"));
        Recibida= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Recibida"));
        Facturada= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Facturada"));
        Solicitada= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Solicitada"));
        Anulada= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Anulada"));
        MotivoAnulada= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("MotivoAnulada"));
        Iva= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Iva"));
        ValorTotal= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("ValorTotal"));
        Descuento= cCabOrdenCompra.getString(cCabOrdenCompra.getColumnIndex("Descuento"));
        Fecha = Fecha.substring(6,8) + '/' + Fecha.substring(4,6) + '/' + Fecha.substring(0,4);
        FechaEstimadaCompra = FechaEstimadaCompra.substring(6,8) + '/' + FechaEstimadaCompra.substring(4,6) + '/' + FechaEstimadaCompra.substring(0,4);

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cCabOrdenCompra.moveToNext()){
            bTerminaCabOrdenCompra=true;
            cCabOrdenCompra.close();
            return;
        }
    }
    private void RecorrerCaja(){
        if (bTerminaCaja) {
            cCaja.close();
            SubirCabOrdenCompra();
            return;
        }

        Accion = "CAJA";
        IdCaja= cCaja.getString(cCaja.getColumnIndex("IdCaja"));
        Identificador= cCaja.getString(cCaja.getColumnIndex("Identificador"));
        IdUsuario= cCaja.getString(cCaja.getColumnIndex("IdUsuario"));
        Fecha= cCaja.getString(cCaja.getColumnIndex("Fecha"));
        ValorApertura= cCaja.getString(cCaja.getColumnIndex("ValorApertura"));
        ValorCierre= cCaja.getString(cCaja.getColumnIndex("ValorCierre"));
        Cuadrado= cCaja.getString(cCaja.getColumnIndex("Cuadrado"));
        Cerrado= cCaja.getString(cCaja.getColumnIndex("Cerrado"));
        DineroEntregado= cCaja.getString(cCaja.getColumnIndex("DineroEntregado"));
        Fecha = Fecha.substring(6,8) + '/' + Fecha.substring(4,6) + '/' + Fecha.substring(0,4);

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cCaja.moveToNext()){
            bTerminaCaja=true;
            cCaja.close();
            return;
        }
    }
    private void RecorrerComuna(){
        if (bTerminaComuna) {
            cComuna.close();
            SubirCliente();
            return;
        }

        Accion = "COMUNA";
        IdComuna= cComuna.getString(cComuna.getColumnIndex("IdComuna"));
        Identificador= cComuna.getString(cComuna.getColumnIndex("Identificador"));
        Nombre= cComuna.getString(cComuna.getColumnIndex("Nombre"));

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cComuna.moveToNext()){
            bTerminaComuna=true;
            cComuna.close();
            return;
        }
    }
    private void RecorrerCliente(){
        if (bTerminaCliente) {
            cCliente.close();
            SubirCaja();
            return;
        }

        Accion = "CLIENTE";
        IdCliente= cCliente.getString(cCliente.getColumnIndex("IdCliente"));
        IdComuna= cCliente.getString(cCliente.getColumnIndex("IdeCom"));
        Identificador= cCliente.getString(cCliente.getColumnIndex("Identificador"));
        Rut= cCliente.getString(cCliente.getColumnIndex("Rut"));
        Nombre= cCliente.getString(cCliente.getColumnIndex("Nombre"));
        Direccion= cCliente.getString(cCliente.getColumnIndex("Direccion"));

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cCliente.moveToNext()){
            bTerminaCliente=true;
            cCliente.close();
            return;
        }
    }
    private void RecorrerProveedor(){
        if (bTerminaProveedor) {
            cProveedor.close();
            SubirComuna();
            return;
        }

        Accion = "PROVEEDOR";
        IdProveedor= cProveedor.getString(cProveedor.getColumnIndex("IdProveedor"));
        Identificador= cProveedor.getString(cProveedor.getColumnIndex("Identificador"));
        Rut= cProveedor.getString(cProveedor.getColumnIndex("Rut"));
        Nombre= cProveedor.getString(cProveedor.getColumnIndex("Nombre"));
        Vendedor= cProveedor.getString(cProveedor.getColumnIndex("Vendedor"));
        Direccion= cProveedor.getString(cProveedor.getColumnIndex("Direccion"));

        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cProveedor.moveToNext()){
            bTerminaProveedor=true;
            cProveedor.close();
            return;
        }
    }
    private void RecorrerProductos(){
        if (bTerminarProducto) {
            cProducto.close();
            SubirProveedor();
            return;
        }

        Accion ="PRODUCTO";
        IdProducto= cProducto.getString(cProducto.getColumnIndex("IdProducto"));
        Identificador= cProducto.getString(cProducto.getColumnIndex("Identificador"));
        Codigo= cProducto.getString(cProducto.getColumnIndex("Codigo"));
        Nombre= cProducto.getString(cProducto.getColumnIndex("Nombre"));
        IdFamilia= cProducto.getString(cProducto.getColumnIndex("IdenFam"));
        Vigencia= cProducto.getString(cProducto.getColumnIndex("Vigencia"));
        Imagen= cProducto.getString(cProducto.getColumnIndex("Imagen"));
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();

        if (!cProducto.moveToNext()){
            bTerminarProducto=true;
            cProducto.close();
            return;
        }
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("PRODUCTO")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSProducto.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdProducto", Identificador);
                oEjeWS.AddParametro("Codigo", Codigo);
                oEjeWS.AddParametro("Nombre", Nombre);
                oEjeWS.AddParametro("IdFamilia", IdFamilia);
                oEjeWS.AddParametro("Vigencia", Vigencia);
                oEjeWS.AddParametro("Imagen", Imagen);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("PROVEEDOR")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSProveedor.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdProveedor", Identificador);
                oEjeWS.AddParametro("Rut", Rut);
                oEjeWS.AddParametro("Nombre", Nombre);
                oEjeWS.AddParametro("Vendedor", Vendedor);
                oEjeWS.AddParametro("Direccion", Direccion);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CLIENTE")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSCliente.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCliente", Identificador);
                oEjeWS.AddParametro("Rut", Rut);
                oEjeWS.AddParametro("Nombre", Nombre);
                oEjeWS.AddParametro("Direccion", Direccion);
                oEjeWS.AddParametro("IdComuna", IdComuna);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("COMUNA")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSComuna.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdComuna", Identificador);
                oEjeWS.AddParametro("Nombre", Nombre);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CAJA")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSCaja.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCaja", Identificador);
                oEjeWS.AddParametro("IdUsuario", IdUsuario);
                oEjeWS.AddParametro("Fecha", Fecha);
                oEjeWS.AddParametro("ValorApertura", ValorApertura);
                oEjeWS.AddParametro("ValorCierre", ValorCierre);
                oEjeWS.AddParametro("Cuadrado", Cuadrado);
                oEjeWS.AddParametro("Cerrado", Cerrado);
                oEjeWS.AddParametro("DineroEntregado", DineroEntregado);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CABORDENCOMPRA")) {
                oEjeWS.setMetodo("Agregar");

                oEjeWS.setWS("WSCabOrdenCompra.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCabOrdenCompra", Identificador);
                oEjeWS.AddParametro("IdProveedor", IdProveedor);
                oEjeWS.AddParametro("Fecha", Fecha);
                oEjeWS.AddParametro("FechaEstimadaCompra", FechaEstimadaCompra);
                oEjeWS.AddParametro("Cantidad", Cantidad);
                oEjeWS.AddParametro("ValorNeto", ValorNeto);
                oEjeWS.AddParametro("Pagada", Pagada);
                oEjeWS.AddParametro("Recibida", Recibida);
                oEjeWS.AddParametro("Facturada", Facturada);
                oEjeWS.AddParametro("Solicitada", Solicitada);
                oEjeWS.AddParametro("Anulada", Anulada);
                oEjeWS.AddParametro("MotivoAnulada", MotivoAnulada);
                oEjeWS.AddParametro("Iva", Iva);
                oEjeWS.AddParametro("ValorTotal", ValorTotal);
                oEjeWS.AddParametro("Descuento", "0");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CABVENTA")) {
                oEjeWS.setMetodo("Agregar");

                oEjeWS.setWS("WSCabVenta.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCliente", IdCliente);
                oEjeWS.AddParametro("Fecha", Fecha);
                oEjeWS.AddParametro("Cantidad", Cantidad);
                oEjeWS.AddParametro("ValorNeto", ValorNeto);
                oEjeWS.AddParametro("Pagada", Pagada);
                oEjeWS.AddParametro("Pendiente", Pendiente);
                oEjeWS.AddParametro("Facturada", Facturada);
                oEjeWS.AddParametro("Anulada", Anulada);
                oEjeWS.AddParametro("MotivoAnulada", MotivoAnulada);
                oEjeWS.AddParametro("Iva", Iva);
                oEjeWS.AddParametro("ValorTotal", ValorTotal);
                oEjeWS.AddParametro("Descuento", "0");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("DETORDENCOMPRA")) {
                oEjeWS.setMetodo("Agregar");

                oEjeWS.setWS("WSDetOrdenCompra.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCabOrdenCompra", IdCabOrdenCompra);
                oEjeWS.AddParametro("IdProducto", IdProducto);
                oEjeWS.AddParametro("Cantidad", Cantidad);
                oEjeWS.AddParametro("Valor", Valor);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("DETVENTA")) {
                oEjeWS.setMetodo("Agregar");

                oEjeWS.setWS("WSDetVenta.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCabVenta", IdCabVenta);
                oEjeWS.AddParametro("IdProducto", IdProducto);
                oEjeWS.AddParametro("Cantidad", Cantidad);
                oEjeWS.AddParametro("Valor", Valor);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("PAGO")) {
                oEjeWS.setMetodo("Agregar");

                oEjeWS.setWS("WSPago.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCabVenta", IdCabVenta);
                oEjeWS.AddParametro("Fecha", Fecha);
                oEjeWS.AddParametro("IdCaja", IdCaja);
                oEjeWS.AddParametro("Valor", Valor);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("IdUsuCreacion", "1");
                oEjeWS.AddParametro("IdUsuModificacion", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("STOCK")) {
                oEjeWS.setMetodo("AgregarModificar");

                oEjeWS.setWS("WSStock.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdStock", Identificador);
                oEjeWS.AddParametro("IdSucursal", Funciones.gIdSucursal);
                oEjeWS.AddParametro("IdProducto", IdProducto);
                oEjeWS.AddParametro("CantidadFisica", CantidadFisica);
                oEjeWS.AddParametro("CantidadCritica", CantidadCritica);
                oEjeWS.AddParametro("ValorCompra", ValorCompra);
                oEjeWS.AddParametro("ValorVenta", ValorVenta);
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

                    oFunciones.MostrarAlerta(context, oEjeWS.getDesError(), "E R R O R");
                    return;
                }

                if (Accion.equals("PRODUCTO")) {
                    bdProducto oProducto = new bdProducto();
                    String Id = oEjeWS.getValorString(0,"IdProducto");
                    oProducto.ModificarSubido(IdProducto,Id,context);
                    RecorrerProductos();
                }else if (Accion.equals("PROVEEDOR")) {
                    bdProveedor oProveedor = new bdProveedor();
                    String Id = oEjeWS.getValorString(0,"IdProveedor");
                    oProveedor.ModificarSubido(IdProveedor,Id,context);
                    RecorrerProveedor();
                }else if (Accion.equals("CLIENTE")) {
                    bdCliente oCliente = new bdCliente();
                    String Id = oEjeWS.getValorString(0,"IdCliente");
                    oCliente.ModificarSubido(IdCliente,Id,context);
                    RecorrerCliente();
                }else if (Accion.equals("COMUNA")) {
                    bdComuna oComuna = new bdComuna();
                    String Id = oEjeWS.getValorString(0,"IdComuna");
                    oComuna.ModificarSubido(IdComuna,Id,context);
                    RecorrerComuna();
                }else if (Accion.equals("CAJA")) {
                    bdCaja oCaja = new bdCaja();
                    String Id = oEjeWS.getValorString(0,"IdCaja");
                    oCaja.ModificarSubido(IdCaja,Id,context);
                    RecorrerCaja();
                }else if (Accion.equals("CABORDENCOMPRA")) {
                    bdCabOrdenCompra oCabOrdenCompra = new bdCabOrdenCompra();
                    String Id = oEjeWS.getValorString(0,"IdCabOrdenCompra");
                    oCabOrdenCompra.ModificarSubido(IdCabOrdenCompra,Id,context);
                    RecorrerCabOrdenCompra();
                }else if (Accion.equals("CABVENTA")) {
                    bdCabVenta oCabVenta = new bdCabVenta();
                    String Id = oEjeWS.getValorString(0,"IdCabVenta");
                    oCabVenta.ModificarSubido(IdCabVenta,Id,context);
                    RecorrerCabVenta();
                }else if (Accion.equals("DETORDENCOMPRA")) {
                    bdDetOrdenCompra odDetOrdenCompra = new bdDetOrdenCompra();
                    String Id = oEjeWS.getValorString(0,"IdDetOrdenCompra");
                    odDetOrdenCompra.ModificarSubido(IdDetOrdenCompra,Id,context);
                    RecorrerDetOrdenCompra();
                }else if (Accion.equals("DETVENTA")) {
                    bdDetVenta odDetVenta = new bdDetVenta();
                    String Id = oEjeWS.getValorString(0,"IdDetVenta");
                    odDetVenta.ModificarSubido(IdDetVenta,Id,context);
                    RecorrerDetVenta();
                }else if (Accion.equals("PAGO")) {
                    bdPago odPago = new bdPago();
                    String Id = oEjeWS.getValorString(0,"IdPago");
                    odPago.ModificarSubido(IdPago,Id,context);
                    RecorrerPago();
                }else if (Accion.equals("STOCK")) {
                    bdStock oStock = new bdStock();
                    String Id = oEjeWS.getValorString(0,"IdStock");
                    oStock.ModificarSubido(IdPago,Id,context);
                    RecorrerStock();
                }
            }
        }
    }
}
