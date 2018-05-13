package cl.invsal.www.istemf;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MarceloUsuario on 03-05-2017.
 */

public class adapLis extends ArrayAdapter {
    ArrayList<tVarios> datos = new ArrayList<tVarios>();
    Context context;
    boolean Simple;
    public adapLis(Context context, ArrayList<tVarios> datos, boolean Simple) {
        super(context, R.layout.laydatosmenu, datos);
        this.context = context;
        this.datos = datos;
        this.Simple = Simple;
    }
    @Override
    public boolean isEnabled(int position) {
        if (datos.get(position).getValor13().equals("P")){
            return false;
        }else{
            return true;
        }

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.laydatoslis, null);

        TextView Titulo = (TextView) item.findViewById(R.id.Titulo);
        TextView SubTitulo = (TextView) item.findViewById(R.id.SubTitulo);

        if (datos.get(position).getValor15().equals("COLOR")){
            String string = datos.get(position).getValor14();
            item.setBackgroundColor(Color.parseColor(string));
        }

        Titulo.setText(datos.get(position).getValor1());
        SubTitulo.setText(datos.get(position).getValor2());


        return item;
    }


}
