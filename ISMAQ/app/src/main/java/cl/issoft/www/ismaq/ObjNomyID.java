package cl.issoft.www.ismaq;

/**
 * Created by MarceloUsuario on 08-16-2017.
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
