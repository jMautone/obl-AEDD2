package estructuras.listaG;

import estructuras.ObjetoComparable;
import visitor.Visitable;
import visitor.Visitor;

public class ListaGenerica<T extends ObjetoComparable<T>> implements Visitable<T> {

    private class NodoListaGenerica<T> {

        private T dato;
        private NodoListaGenerica<T> sig;

        public NodoListaGenerica(T dato) {
            this.dato = dato;
        }

        public NodoListaGenerica(T dato, NodoListaGenerica<T> sig) {
            this.dato = dato;
            this.sig = sig;
        }

        /** getter && setter**/
        public T getDato() {
            return dato;
        }

        public void setDato(T dato) {
            this.dato = dato;
        }
        public NodoListaGenerica<T> getSig() {
            return sig;
        }
        public void setSig(NodoListaGenerica<T> sig) {
            this.sig = sig;
        }

    }
    private NodoListaGenerica<T> inicio;
    private NodoListaGenerica<T> ultimo;
    private int largo;
    public ListaGenerica() {
        this.inicio = null;
        this.ultimo = null;
        this.largo = 0;
    }

    @Override
    public void visitarOrdenado(Visitor<T> visitor) {
        inOrder(visitor);
    }

    @Override
    public void visitarDesordenado(Visitor<T> visitor) {
        inOrder(visitor);
    }

    public void inOrder(Visitor<T> visitor) {

        inOrder(inicio, visitor);
    }

    private void inOrder(ListaGenerica.NodoListaGenerica nodo, Visitor<T> visitor) {

        if (nodo != null) {
            inOrder(nodo.sig, visitor);
            visitor.visitar((T) nodo.dato);
        }
    }

    /**
     * getters && setters
     **/
    public NodoListaGenerica<T> getInicio() {
        return inicio;
    }

    public void setInicio(NodoListaGenerica<T> inicio) {
        this.inicio = inicio;
    }

    public NodoListaGenerica<T> getUltimo() {
        return ultimo;
    }

    public void setUltimo(NodoListaGenerica<T> ultimo) {
        this.ultimo = ultimo;
    }

    /** METODOS DE ILISTA CON TIPO GENERICO **/

    public void agregarAlPrincipio(T valorNuevo) {
        NodoListaGenerica<T> nuevo = new NodoListaGenerica(valorNuevo);
        if (this.estaVacia()) {
            ultimo = nuevo;
        }
        nuevo.setSig(inicio);
        inicio = nuevo;
        largo++;
    }

    public void agregarAlFinal(T elementoNuevo) {
        NodoListaGenerica<T> nuevo = new NodoListaGenerica(elementoNuevo);
        if (this.estaVacia()) {
            inicio = nuevo;
        } else {
            ultimo.setSig(nuevo);
        }
        ultimo = nuevo;
        largo++;
    }

    public int eliminarPrincipio() {
        if (inicio.getSig() == null) {
            inicio = inicio.getSig();
            ultimo = ultimo.getSig();
        } else {
            inicio = inicio.getSig();
        }
        largo--;
        return 1;
    }

    public int getLargo() {
        return largo;
    }

    public boolean estaVacia() {
        return inicio == null;
    }

    public T obtener(int indice) {
        NodoListaGenerica<T> aux = inicio;
        int i = 0;
        while (i < indice && aux != null) {
            aux = aux.getSig();
            i++;
        }
        if (aux == null) {
            return null;
        }
        return aux.getDato();
    }

    public boolean insertarEnPos(T dato, int k) {
        int i = 1;
        if (k == 1) {
            agregarAlPrincipio(dato);
        } else {
            NodoListaGenerica<T> aux = inicio;
            while (i < k && aux != null) {
                aux = aux.getSig();
                i++;
            }
            if (aux != null) {
                NodoListaGenerica<T> nuevo = new NodoListaGenerica<T>(dato, aux);
                inicio.setSig(nuevo);
                largo++;
            } else {
                agregarAlFinal(dato);
            }
        }
        return true;
    }

    public void agregarordenado(T dato) {
        if (this.estaVacia()|| dato.esMenor(this.getInicio().dato)){
            this.agregarAlPrincipio(dato);
        } else{
            if (dato.esMayor(this.getUltimo().dato)){
                this.agregarAlFinal(dato);
            }else{
                NodoListaGenerica nuevo=new NodoListaGenerica(dato);
                NodoListaGenerica aux = this.getInicio();
                while (aux.getSig()!=null && (dato.esMayor((T) aux.getSig().dato) || dato.equals(aux.sig.dato) /*  && dato>= aux.siguiente.dato*/)){
                    aux=aux.getSig();
                }
                nuevo.setSig(aux.getSig());
                aux.setSig(nuevo);
            }
        }
    }

    public void agregardesordenado(T dato) {
        if (this.estaVacia()|| dato.esMayor(this.getInicio().dato)){
            this.agregarAlPrincipio(dato);
        } else{
            if (dato.esMayor(this.getUltimo().dato)){
                this.agregarAlFinal(dato);
            }else{
                NodoListaGenerica nuevo=new NodoListaGenerica(dato);
                NodoListaGenerica aux = this.getInicio();
                while (aux.getSig()!=null && (dato.esMenor((T) aux.getSig().dato) || dato.equals(aux.sig.dato) /*  && dato>= aux.siguiente.dato*/)){
                    aux=aux.getSig();
                }
                nuevo.setSig(aux.getSig());
                aux.setSig(nuevo);
            }
        }
    }

}

