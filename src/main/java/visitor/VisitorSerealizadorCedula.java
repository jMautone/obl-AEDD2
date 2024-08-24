package visitor;

import dominio.Jugador;

public class VisitorSerealizadorCedula<T> implements Visitor<T>{

    private StringBuilder sb = new StringBuilder();


    @Override
    public void visitar(T elemento) {
        Jugador j = (Jugador) elemento;
        if(sb.length()>0){
            sb.append("|"); //al terminar un objeto lo separo con esto
        }
        sb.append(j.getCedula());
    }

    public String getSerealizador(){
        return sb.toString();
    }


}
