package estructuras.grafo;
import interfaz.EstadoCamino;

public class Arista {

    private double costo;
    private double tiempo;
    private double kilometros;
    private EstadoCamino estado;
    private boolean existe;

    public Arista() {
        this.existe = false;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public double getKilometros() {
        return kilometros;
    }

    public void setKilometros(double kilometros) {
        this.kilometros = kilometros;
    }

    public EstadoCamino getEstado() {
        return estado;
    }

    public void setEstado(EstadoCamino estado) {
        this.estado = estado;
    }

    public boolean isExiste() {
        return existe;
    }
    public void setExiste(boolean existe) {
        this.existe = existe;
    }
}
