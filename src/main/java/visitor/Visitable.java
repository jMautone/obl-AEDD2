package visitor;

public interface Visitable <T>{
    //sabe la estructura y cómo recorrerla, no sé qué querés hacer cuando la recorra
    //lo que sé es que te voy a ir pasando los datos que voy recorriendo
    //y vos con esos datos hacés lo que quieras
    //quien se encarga de pasarle esas cosas en orden es el visitable
    void visitarOrdenado(Visitor<T> visitor);

    void visitarDesordenado(Visitor<T> visitor);
}
