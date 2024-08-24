package estructuras.listaYCola;

public class Cola<T> {

    private Nodo<T> inicio;
    private Nodo<T> ultimo;
    private int largo;
    private int maximo;

    public Cola(int maximo) {
        this.maximo = maximo;
        this.inicio = null;
        this.ultimo = null;
        this.largo = 0;
    }

    public Nodo<T> getInicio() {
        return inicio;
    }

    public void setInicio(Nodo<T> inicio) {
        this.inicio = inicio;
    }

    public Nodo<T> getUltimo() {
        return ultimo;
    }

    public void setUltimo(Nodo<T> ultimo) {
        this.ultimo = ultimo;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }

    public boolean esVacia() {
        return this.inicio == null;
    }

    public boolean esllena() {
        return largo == maximo;
    }

    public void encolar(T dato) {
        if (!this.esllena()) {
            Nodo nuevo = new Nodo(dato);
            if (this.esVacia()) {
                setInicio(nuevo);
                setUltimo(nuevo);
            } else {
                ultimo.setSig(nuevo);
                setUltimo(nuevo);
            }
            largo++;
        }
    }

    public void desencolar() {
        if (!esVacia()) {
            if (largo == 1) {
                setInicio(null);
                setUltimo(null);
            } else {
                setInicio(inicio.getSig());
            }
            largo = largo - 1;
        }
    }

    public T frente() {
        if (!this.esVacia()) {
            return inicio.getDato();
        } else {
            return null;
        }
    }

    public T fondo() {
        if (!this.esVacia()) {
            return getUltimo().getDato();
        } else {
            return null;
        }
    }

    public int largo() {
        return largo;
    }
}
