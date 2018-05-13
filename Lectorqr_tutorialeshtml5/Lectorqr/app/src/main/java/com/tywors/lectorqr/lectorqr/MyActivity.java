package com.tywors.lectorqr.lectorqr;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MyActivity extends ActionBarActivity {
    //declaramos nuestro boton
    private Button bt_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //Boton en el XML
        bt_scan=(Button)findViewById(R.id.bt_scan);

        //Añadimos Listener, al clickar...
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lanzamos la activity del escaner
                IntentIntegrator.initiateScan(MyActivity.this);
            }
        });
    }
    //Marcamos lo que queremos que haga una vez haya leido el código
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case IntentIntegrator.REQUEST_CODE:
            {
                if (resultCode == RESULT_CANCELED){
                }
                else
                {
                    //Recogemos los datos   que nos envio el código Qr/codigo de barras
                    IntentResult scanResult = IntentIntegrator.parseActivityResult(
                            requestCode, resultCode, data);
                    String UPCScanned = scanResult.getContents();
                    //cOMO ES SOLO UN EJEMPLO LO SACAREMOS POR PANTALLA.
                    Toast.makeText(getApplicationContext(),UPCScanned,Toast.LENGTH_LONG
                    ).show();
                }
                break;
            }
        }
    }
}

