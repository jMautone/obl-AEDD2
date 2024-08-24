package dominio;

import estructuras.ObjetoComparable;
import interfaz.TipoJugador;
import visitor.Visitable;
import visitor.Visitor;

public class Jugador implements ObjetoComparable<Jugador>, Visitable<Jugador> {
    private String cedula;
    private String nombre;
    private TipoJugador tipoJugador;
    private int edad;
    private String escuela;

    //constructor
    public Jugador(String cedula, String nombre, int edad, String escuela, TipoJugador tipoJugador) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.edad = edad;
        this.escuela = escuela;
        this.tipoJugador = tipoJugador;
    }
    //constructores para objeto cascarón
    //esto es para crear un Jugador para buscar por cédula
    public Jugador(String ci) {
        this.cedula = ci;
    }

    //esto es para crear un Jugador para buscar por tipo
    public Jugador(TipoJugador tipo) {this.tipoJugador = tipo;}


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public TipoJugador getTipo() { return tipoJugador; }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEscuela() {
        return escuela;
    }

    public void setEscuela(String escuela) {
        this.escuela = escuela;
    }

    public boolean esMayor(Jugador dato){
        String ciSinPuntos1 = dato.getCedula().replaceAll("-", "").replaceAll("\\.", "");
        int ciNum1 = Integer.parseInt(ciSinPuntos1);
        String ciSinPuntos2 = this.getCedula().replaceAll("-", "").replaceAll("\\.", "");
        int ciNum2 = Integer.parseInt(ciSinPuntos2);

        return ciNum2 > ciNum1;
    }

    public boolean esMenor(Jugador dato){
        String ciSinPuntos1 = dato.getCedula().replaceAll("-", "").replaceAll("\\.", "");
        int ciNum1 = Integer.parseInt(ciSinPuntos1);
        String ciSinPuntos2 = this.getCedula().replaceAll("-", "").replaceAll("\\.", "");
        int ciNum2 = Integer.parseInt(ciSinPuntos2);

        return ciNum2 < ciNum1;
    }

    @Override
    public boolean elTipoCoincide(Jugador dato) {
        if(dato.tipoJugador == tipoJugador)return true;
        else return false;
    }

    @Override
    public void visitarOrdenado(Visitor<Jugador> visitor) {
        String dato = "";
        /*dato = */
    }

    @Override
    public void visitarDesordenado(Visitor<Jugador> visitor) {
        String dato = "";
        /*dato = */
    }

    public boolean validarDatos() {
        return cedula != null && cedula != "" && nombre != null && nombre != "" &&
                escuela != null && escuela != "" && edad > 0 && tipoJugador != null;
    }


    @Override
    public String toString() {
        return cedula + ";" + nombre + ";" + edad + ";" + escuela + ";" + tipoJugador.getValor();
    }
}
