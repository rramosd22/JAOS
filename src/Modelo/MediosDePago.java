package Modelo;

/**
 *
 * @author dolf
 */
public class MediosDePago {

    private String medio;
    private boolean seleccionado;

    public MediosDePago(String medio, boolean seleccionado) {
        this.medio = medio;
        this.seleccionado = seleccionado;
    }

    public String getMedio() {
        return medio;
    }

    public boolean estaSeleccionado() {
        return seleccionado;
    }

    @Override
    public String toString() {
        return "'" + medio + "'";
    }
    
    
}
