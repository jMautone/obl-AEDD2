package estructuras;

public interface ObjetoComparable <T> {

    boolean esMayor(T dato);
    boolean esMenor(T dato);
    boolean elTipoCoincide(T dato);
}
