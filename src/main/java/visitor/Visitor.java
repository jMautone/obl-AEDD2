package visitor;

public interface Visitor<T>{
    //sabe qué hacer con un dato o con un conjunto de datos
    // sabe imprimir, pasar a String etc etc
    void visitar(T elemento);
}
