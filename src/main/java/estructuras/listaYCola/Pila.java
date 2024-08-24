package estructuras.listaYCola;

public class Pila<T> {

    Nodo<T> primero;
    int maximo;
    int largo;

    public Pila(int maximo) {
        this.maximo = maximo;
        this.largo = 0;
        this.primero = null;
    }

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int cantelementos) {
        this.largo = cantelementos;
    }

    public Nodo getPrimero() {
        return primero;
    }

    public void setPrimero(Nodo primero) {
        this.primero = primero;
    }

    public void apilar(Object dato) {
        Nodo nuevo = new Nodo(dato);
        if (this.esVacia()) {
            this.setPrimero(nuevo);
        } else {
            nuevo.setSig(this.getPrimero());
            this.setPrimero(nuevo);
        }
        largo++;
    }

    public void desapilar() {
        if (!this.esVacia()) {
            primero = primero.getSig();
            largo--;
        }

    }

    public boolean esVacia() {
        return this.getPrimero() == null;
    }

    public boolean esllena() {
        return largo == maximo;

    }

    public T cima() {
        if (!this.esVacia()) {
            return primero.getDato();
        } else {
            return null;
        }
    }
}