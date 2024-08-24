package estructuras.grafo;

import estructuras.ObjetoComparable;
import visitor.Visitable;
import visitor.Visitor;

public class Vertice implements Visitable<Vertice>, ObjetoComparable<Vertice> {
    private String codigo;
    private String nombre;

    public Vertice(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return codigo + ";" + nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertice vertice = (Vertice) o;
        return codigo.equals(vertice.codigo);
    }


    @Override
    public void visitarOrdenado(Visitor<Vertice> visitor) {

    }

    @Override
    public void visitarDesordenado(Visitor<Vertice> visitor) {

    }

    @Override
    public boolean esMayor(Vertice dato) {
        int codigo1 = Integer.parseInt(dato.codigo);
        int codigo2 = Integer.parseInt(this.codigo);
        return codigo1 > codigo2;
    }

    @Override
    public boolean esMenor(Vertice dato) {
        int codigo1 = Integer.parseInt(dato.codigo);
        int codigo2 = Integer.parseInt(this.codigo);
        return codigo2 > codigo1;
    }

    @Override
    public boolean elTipoCoincide(Vertice dato) {
        return false;
    }
}
