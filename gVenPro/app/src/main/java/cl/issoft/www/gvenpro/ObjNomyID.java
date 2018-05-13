package cl.issoft.www.gvenpro;

/**
 * Created by MarceloUsuario on 04-01-2018.
 */

public class ObjNomyID {
    int id;
    String nombre;
    //Constructor
    public ObjNomyID(int id, String nombre) {
        super();
        this.id = id;
        this.nombre = nombre;
    }
    @Override
    public String toString() {
        return nombre;
    }
    public int getId() {
        return id;
    }

}
