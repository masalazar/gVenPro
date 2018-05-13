package cl.issoft.www.gvenpro;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by MarceloUsuario on 04-22-2018.
 */

public class RecibirDatos {
    ejeWS oEjeWS;
    private String Accion="",FechaDesde,FechaHasta;
    private Context Contexto;
    public void Recibir(Context context){
        Contexto=context;

        Accion = "COMUNA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private class EjecutaWS extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            Funciones oFunciones = new Funciones();
            GeneraCodigo oGeneraCodigo = new GeneraCodigo();

            if (Accion.equals("COMUNA")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSComuna.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdComuna", "0");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("FAMILIA")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSFamilia.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdFamilia", "0");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("PRODUCTO")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSProducto.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdProducto", "0");
                oEjeWS.AddParametro("Codigo", "");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("Vigencia", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("PROVEEDOR")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSProveedor.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdProveedor", "0");
                oEjeWS.AddParametro("Rut", "");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CLIENTE")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSCliente.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCliente", "0");
                oEjeWS.AddParametro("IdComuna", "0");
                oEjeWS.AddParametro("Rut", "");
                oEjeWS.AddParametro("Nombre", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("STOCK")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSStock.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdStock", "0");
                oEjeWS.AddParametro("IdSucursal", "0");
                oEjeWS.AddParametro("IdProducto", "0");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CABVENTA")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSCabVenta.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCabVenta", "0");
                oEjeWS.AddParametro("IdCliente", "0");
                oEjeWS.AddParametro("FechaDesde", FechaDesde);
                oEjeWS.AddParametro("FechaHasta", FechaHasta);
                oEjeWS.AddParametro("Pagada", "");
                oEjeWS.AddParametro("Facturada", "");
                oEjeWS.AddParametro("Pendiente", "");
                oEjeWS.AddParametro("Anulada", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CABORDENCOMPRA")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSCabOrdenCompra.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCabOrdenCompra", "0");
                oEjeWS.AddParametro("IdProveedor", "0");
                oEjeWS.AddParametro("FechaDesde", FechaDesde);
                oEjeWS.AddParametro("FechaHasta", FechaHasta);
                oEjeWS.AddParametro("FechaEstimadaCompraDesde", "");
                oEjeWS.AddParametro("FechaEstimadaCompraHasta", "");
                oEjeWS.AddParametro("Pagada", "");
                oEjeWS.AddParametro("Facturada", "");
                oEjeWS.AddParametro("Solicitada", "");
                oEjeWS.AddParametro("Anulada", "");
                oEjeWS.AddParametro("Recibida", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("DETVENTA")) {
                oEjeWS.setMetodo("BuscarFecha");

                oEjeWS.setWS("WSDetVenta.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("FechaDesde", FechaDesde);
                oEjeWS.AddParametro("FechaHasta", FechaHasta);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("DETORDENCOMPRA")) {
                oEjeWS.setMetodo("BuscarFecha");

                oEjeWS.setWS("WSDetOrdenCompra.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("FechaDesde", FechaDesde);
                oEjeWS.AddParametro("FechaHasta", FechaHasta);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("CAJA")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSCaja.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdCaja", "0");
                oEjeWS.AddParametro("IdUsuario", "0");
                oEjeWS.AddParametro("FechaDesde", FechaDesde);
                oEjeWS.AddParametro("FechaHasta", FechaHasta);
                oEjeWS.AddParametro("Cerrado", "");
                oEjeWS.AddParametro("Cuadrado", "");
                oEjeWS.AddParametro("DineroEntregado", "");
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }else if (Accion.equals("PAGO")) {
                oEjeWS.setMetodo("Buscar");

                oEjeWS.setWS("WSPago.asmx");
                oEjeWS.LimpiarArreglo();
                oEjeWS.AddParametro("IdPago", "0");
                oEjeWS.AddParametro("IdCabVenta", "0");
                oEjeWS.AddParametro("FechaDesde", FechaDesde);
                oEjeWS.AddParametro("FechaHasta", FechaHasta);
                oEjeWS.AddParametro("IdEmpresa", "1");
                oEjeWS.AddParametro("Concatenado", oGeneraCodigo.obtCodigo());
                oEjeWS.sEjeWS();
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (oEjeWS.getNumError() > 0) {
                    Funciones oFunciones = new Funciones();

                    return;
                }

                if (Accion.equals("COMUNA")) {
                    ResultadoComuna();
                }else if (Accion.equals("FAMILIA")) {
                    ResultadoFamilia();
                }else if (Accion.equals("PRODUCTO")) {
                    ResultadoProducto();
                }else if (Accion.equals("PROVEEDOR")) {
                    ResultadoProveedor();
                }else if (Accion.equals("CLIENTE")) {
                    ResultadoCliente();
                }else if (Accion.equals("STOCK")) {
                    ResultadoStock();
                }else if (Accion.equals("CABVENTA")) {
                    ResultadoCabVenta();
                }else if (Accion.equals("CABORDENCOMPRA")) {
                    ResultadoCabOrdenCompra();
                }else if (Accion.equals("DETVENTA")) {
                    ResultadoDetVenta();
                }else if (Accion.equals("DETORDENCOMPRA")) {
                    ResultadoDetOrdenCompra();
                }else if (Accion.equals("CAJA")) {
                    ResultadoCaja();
                }else if (Accion.equals("PAGO")) {
                    ResultadoPago();
                }
            }
        }
    }
    private void ResultadoComuna(){
        bdComuna oComuna = new bdComuna();

        oComuna.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            oComuna.Agregar(oEjeWS.getValorString(i,"Nombre"),"S",oEjeWS.getValorString(i,"IdComuna"),Contexto);
        }

        Accion = "FAMILIA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoProveedor(){
        bdProveedor oProveedor = new bdProveedor();

        oProveedor.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            oProveedor.Agregar(oEjeWS.getValorString(i,"Rut"),oEjeWS.getValorString(i,"Nombre"),oEjeWS.getValorString(i,"Direccion"),oEjeWS.getValorString(i,"Vendedor"),"S",oEjeWS.getValorString(i,"IdProveedor"),Contexto);
        }

        Accion = "CLIENTE";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoFamilia(){
        bdFamilia oFamilia = new bdFamilia();

        oFamilia.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            oFamilia.Agregar(oEjeWS.getValorString(i,"Nombre"),"S",oEjeWS.getValorString(i,"IdFamilia"),Contexto);
        }

        Accion = "PRODUCTO";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoProducto(){
        bdProducto oProducto = new bdProducto();
        Funciones oFunciones = new Funciones();

        oProducto.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdFamilia = oFunciones.obtIdTabla("Familia","Identificador",oEjeWS.getValorString(i,"IdFamilia"),"IdFamilia",Contexto)
;
            oProducto.Agregar(oEjeWS.getValorString(i,"Codigo"),oEjeWS.getValorString(i,"Nombre"),"S",IdFamilia,oEjeWS.getValorString(i,"IdProducto"),oEjeWS.getValorString(i,"Imagen"),oEjeWS.getValorString(i,"Vigencia"),Contexto);
        }


        Accion = "PROVEEDOR";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoCliente(){
        bdCliente oCliente = new bdCliente();
        Funciones oFunciones = new Funciones();

        oCliente.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdComuna = oFunciones.obtIdTabla("Comuna","Identificador",oEjeWS.getValorString(i,"IdComuna"),"IdComuna",Contexto)
                    ;
            oCliente.Agregar(oEjeWS.getValorString(i,"Rut"),oEjeWS.getValorString(i,"Nombre"),oEjeWS.getValorString(i,"Direccion"),IdComuna,"S",oEjeWS.getValorString(i,"IdCliente"),Contexto);
        }


        Accion = "STOCK";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoStock(){
        bdStock oStock = new bdStock();
        Funciones oFunciones = new Funciones();

        oStock.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdProducto = oFunciones.obtIdTabla("Producto","Identificador",oEjeWS.getValorString(i,"IdProducto"),"IdProducto",Contexto)
                    ;
            oStock.Agregar(Funciones.gIdSucursal,IdProducto,oEjeWS.getValorString(i,"CantidadFisica"),oEjeWS.getValorString(i,"CantidadCritica"),oEjeWS.getValorString(i,"ValorVenta"),"S",oEjeWS.getValorString(i,"ValorCompra"),Contexto);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateH = new Date();
        Date dateD = new Date();
        dateD = oFunciones.sumarDiasAFecha(dateD,-60);
        FechaHasta = dateFormat.format(dateH);
        FechaDesde = dateFormat.format(dateD);


        Accion = "CABVENTA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoCabVenta(){
        bdCabVenta oCabVenta = new bdCabVenta();
        Funciones oFunciones = new Funciones();

        oCabVenta.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdCliente = oFunciones.obtIdTabla("Cliente","Identificador",oEjeWS.getValorString(i,"IdCliente"),"IdCliente",Contexto);
            String Fecha = oEjeWS.getValorString(i,"Fecha");
            Fecha = Fecha.substring(6,10) + Fecha.substring(3,5) + Fecha.substring(0,2);


            oCabVenta.Agregar(IdCliente,Fecha,oEjeWS.getValorString(i,"Cantidad"),Long.parseLong(oEjeWS.getValorString(i,"ValorNeto")),oEjeWS.getValorString(i,"Pendiente"),oEjeWS.getValorString(i,"Pagada"),oEjeWS.getValorString(i,"Facturada"),Long.parseLong(oEjeWS.getValorString(i,"Iva")),Long.parseLong(oEjeWS.getValorString(i,"ValorTotal")),oEjeWS.getValorString(i,"IdCabVenta"),"S",Contexto);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateH = new Date();
        Date dateD = new Date();
        dateD = oFunciones.sumarDiasAFecha(dateD,-60);
        FechaHasta = dateFormat.format(dateH);
        FechaDesde = dateFormat.format(dateD);


        Accion = "CABORDENCOMPRA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoCabOrdenCompra(){
        bdCabOrdenCompra oCabOrdenCompra = new bdCabOrdenCompra();
        Funciones oFunciones = new Funciones();

        oCabOrdenCompra.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdProveedor = oFunciones.obtIdTabla("Proveedor","Identificador",oEjeWS.getValorString(i,"IdProveedor"),"IdProveedor",Contexto);
            String Fecha = oEjeWS.getValorString(i,"Fecha");
            Fecha = Fecha.substring(6,10) + Fecha.substring(3,5) + Fecha.substring(0,2);
            String FechaEstimadaCompra = oEjeWS.getValorString(i,"FechaEstimadaCompra");
            FechaEstimadaCompra = FechaEstimadaCompra.substring(6,10) + FechaEstimadaCompra.substring(3,5) + FechaEstimadaCompra.substring(0,2);

            oCabOrdenCompra.Agregar(IdProveedor,Fecha,FechaEstimadaCompra,oEjeWS.getValorString(i,"Cantidad"),Long.parseLong(oEjeWS.getValorString(i,"ValorNeto")),oEjeWS.getValorString(i,"Pagada"),oEjeWS.getValorString(i,"Facturada"),oEjeWS.getValorString(i,"Solicitada"),Long.parseLong(oEjeWS.getValorString(i,"Iva")),Long.parseLong(oEjeWS.getValorString(i,"ValorTotal")),oEjeWS.getValorString(i,"Recibida"),oEjeWS.getValorString(i,"IdCabOrdenCompra"),"S",Contexto);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateH = new Date();
        Date dateD = new Date();
        dateD = oFunciones.sumarDiasAFecha(dateD,-60);
        FechaHasta = dateFormat.format(dateH);
        FechaDesde = dateFormat.format(dateD);


        Accion = "DETVENTA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoDetVenta(){
        bdDetVenta oDetVenta = new bdDetVenta();
        Funciones oFunciones = new Funciones();

        oDetVenta.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdProducto = oFunciones.obtIdTabla("Producto","Identificador",oEjeWS.getValorString(i,"IdProducto"),"IdProducto",Contexto);
            String IdCabVenta = oFunciones.obtIdTabla("CabVenta","Identificador",oEjeWS.getValorString(i,"IdCabVenta"),"IdCabVenta",Contexto);

            oDetVenta.Agregar(IdCabVenta,IdProducto,oEjeWS.getValorString(i,"Cantidad"),Long.parseLong(oEjeWS.getValorString(i,"Valor")),"S",Contexto);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateH = new Date();
        Date dateD = new Date();
        dateD = oFunciones.sumarDiasAFecha(dateD,-60);
        FechaHasta = dateFormat.format(dateH);
        FechaDesde = dateFormat.format(dateD);


        Accion = "DETORDENCOMPRA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoDetOrdenCompra(){
        bdDetOrdenCompra oDetOrdenCompra = new bdDetOrdenCompra();
        Funciones oFunciones = new Funciones();

        oDetOrdenCompra.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdProducto = oFunciones.obtIdTabla("Producto","Identificador",oEjeWS.getValorString(i,"IdProducto"),"IdProducto",Contexto);
            String IdCabOrdenCompra = oFunciones.obtIdTabla("CabOrdenCompra","Identificador",oEjeWS.getValorString(i,"IdCabOrdenCompra"),"IdCabOrdenCompra",Contexto);

            oDetOrdenCompra.Agregar(IdCabOrdenCompra,IdProducto,oEjeWS.getValorString(i,"Cantidad"),Long.parseLong(oEjeWS.getValorString(i,"Valor")),"S",Contexto);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateH = new Date();
        Date dateD = new Date();
        dateD = oFunciones.sumarDiasAFecha(dateD,-60);
        FechaHasta = dateFormat.format(dateH);
        FechaDesde = dateFormat.format(dateD);


        Accion = "CAJA";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoCaja(){
        bdCaja oCaja = new bdCaja();
        Funciones oFunciones = new Funciones();

        oCaja.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String Fecha = oEjeWS.getValorString(i,"Fecha");
            Fecha = Fecha.substring(6,10) + Fecha.substring(3,5) + Fecha.substring(0,2);

            oCaja.Agregar(oEjeWS.getValorString(i,"IdUsuario"),oEjeWS.getValorString(i,"IdCaja"),Fecha,oEjeWS.getValorString(i,"ValorApertura"),oEjeWS.getValorString(i,"ValorCierre"),oEjeWS.getValorString(i,"Cuadrado"),oEjeWS.getValorString(i,"Cerrado"),oEjeWS.getValorString(i,"DineroEntregado"),"S",Contexto);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateH = new Date();
        Date dateD = new Date();
        dateD = oFunciones.sumarDiasAFecha(dateD,-60);
        FechaHasta = dateFormat.format(dateH);
        FechaDesde = dateFormat.format(dateD);


        Accion = "PAGO";
        oEjeWS = null;
        oEjeWS = new ejeWS();

        EjecutaWS oEjecutaWS = new EjecutaWS();
        oEjecutaWS.execute();
    }
    private void ResultadoPago(){
        bdPago oPago = new bdPago();
        Funciones oFunciones = new Funciones();

        oPago.EliminarTodo(Contexto);

        for(int i=0; i<oEjeWS.getCount(); i++){
            String IdCabVenta = oFunciones.obtIdTabla("CabVenta","Identificador",oEjeWS.getValorString(i,"IdCabVenta"),"IdCabVenta",Contexto);
            String IdCaja = oFunciones.obtIdTabla("Caja","Identificador",oEjeWS.getValorString(i,"IdCaja"),"IdCaja",Contexto);
            String Fecha = oEjeWS.getValorString(i,"Fecha");
            Fecha = Fecha.substring(6,10) + Fecha.substring(3,5) + Fecha.substring(0,2);
            
            oPago.Agregar(IdCabVenta,Fecha,oEjeWS.getValorString(i,"Valor"),"S",IdCaja,Contexto);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date dateH = new Date();
        Date dateD = new Date();
        dateD = oFunciones.sumarDiasAFecha(dateD,-60);
        FechaHasta = dateFormat.format(dateH);
        FechaDesde = dateFormat.format(dateD);

        oFunciones.MostrarAlerta(Contexto,"La operación se realizo correctamente","Bajar información");



    }
}
