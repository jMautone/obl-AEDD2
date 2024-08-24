package predicado;

import dominio.Jugador;
import interfaz.Consulta;

public class JugadorPasaConsulta implements Predicado<Jugador>{

    Consulta consulta;

    public JugadorPasaConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    private boolean jugadorPasaConsulta (Jugador jugador, Consulta.NodoConsulta nodoActual){
        if(nodoActual.getTipoNodoConsulta() == Consulta.TipoNodoConsulta.Or){
            if(jugadorPasaConsulta(jugador,nodoActual.getIzq()) || jugadorPasaConsulta(jugador,nodoActual.getDer())){
                return true;
            } else {
                return false;
            }
        }else if (nodoActual.getTipoNodoConsulta() == Consulta.TipoNodoConsulta.And){
            if(jugadorPasaConsulta(jugador,nodoActual.getIzq()) && jugadorPasaConsulta(jugador,nodoActual.getDer())){
                return true;
            } else {
                return false;
            }
        } else if(nodoActual.getTipoNodoConsulta() == Consulta.TipoNodoConsulta.EdadMayor){
            return jugador.getEdad() > nodoActual.getValorInt();
        } else if(nodoActual.getTipoNodoConsulta() == Consulta.TipoNodoConsulta.EscuelaIgual){
            return jugador.getEscuela().equals(nodoActual.getValorString());
        } else if(nodoActual.getTipoNodoConsulta() == Consulta.TipoNodoConsulta.NombreIgual) {
            return jugador.getNombre().equals(nodoActual.getValorString());
        }
        // nunca debería llegar a este return
        return false;
    }

    @Override
    public boolean pasaONo(Jugador jugador) {

        return jugadorPasaConsulta(jugador,consulta.getRaiz());
    }
}
