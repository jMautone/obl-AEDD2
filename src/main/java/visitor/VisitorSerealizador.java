package visitor;

import dominio.Jugador;

public class VisitorSerealizador<T> implements Visitor<T>{

    private StringBuilder sb = new StringBuilder();


    @Override
    public void visitar(T elemento) {
        if(sb.length()>0){
            sb.append("|"); //al terminar un objeto lo separo con esto
        }
        sb.append(elemento.toString());
    }

    public String getSerealizador(){
        return sb.toString();
    }


}
