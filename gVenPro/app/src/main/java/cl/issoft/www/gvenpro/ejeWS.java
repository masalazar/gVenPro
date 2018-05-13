package cl.issoft.www.gvenpro;

import java.util.ArrayList;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
/**
 * Created by MarceloUsuario on 04-09-2018.
 */

public class ejeWS {
    SoapObject request;
    final String NAMESPACE = "http://tempuri.org/";
    //String URL="http://www.pruebas.invsal.cl/cid/ws/" ;
    String URL="http://gvenpro.issoft.cl/ws/" ;
    String METHOD_NAME;
    String SOAP_ACTION;
    int vNumError = 0;
    String vDesError;
    SoapObject resSoapFinal;
    ArrayList<tVarios> aParametros = new ArrayList<tVarios>();

    public void setMetodo (String Metodo){
        this.METHOD_NAME = Metodo;
        this.SOAP_ACTION = NAMESPACE + Metodo;
    }

    public void setWS (String WS){
        this.URL = this.URL + WS;
    }

    public  int getCount(){
        return resSoapFinal.getPropertyCount();
    }
    public String getValorString(int Fila, String Columna){
        SoapObject ic = (SoapObject)resSoapFinal.getProperty(Fila);
        return ic.getProperty(Columna).toString();
    }
    public int getValorInt(int Fila, String Columna){
        SoapObject ic = (SoapObject)resSoapFinal.getProperty(Fila);
        return Integer.parseInt(ic.getProperty(Columna).toString());
    }
    public  void LimpiarArreglo(){
        aParametros.clear();
    }
    public void AddParametro(String Nombre, String Valor){
        tVarios Parametro = new tVarios(Nombre,Valor,"","","","","","","","","","","","","");

        aParametros.add(Parametro);
    }
    public Boolean sEjeWS() {
        request = new SoapObject(NAMESPACE, METHOD_NAME);

        for(int i=0; i< aParametros.size(); i++) {
            request.addProperty(aParametros.get(i).getValor1(), aParametros.get(i).getValor2());
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap = (SoapObject) envelope.getResponse();

            resSoapFinal = resSoap;

            vNumError = 0;
            vDesError = "";
        } catch (Exception e) {
            vNumError = 1;
            vDesError = e.getMessage();
            return false;
        }
        return true;
    }
    public int getNumError(){
        try {
            if(vNumError > 0){ return  vNumError;}
            SoapObject ic = (SoapObject)resSoapFinal.getProperty(0);
            return Integer.parseInt(ic.getProperty("NumError").toString());

        } catch (Exception e) {
            return vNumError;
        }
    }
    public String getDesError(){
        try {
            if(vDesError.equals("")) {
                SoapObject ic = (SoapObject) resSoapFinal.getProperty(0);
                return ic.getProperty("DesError").toString();
            }else{return vDesError;}
        } catch (Exception e) {
            return vDesError;
        }
    }
}