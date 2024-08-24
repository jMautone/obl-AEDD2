package estructuras.arbol;

import estructuras.ObjetoComparable;
import estructuras.Tupla;
import estructuras.listaG.ListaGenerica;
import excepciones.DuplicadoException;
import predicado.Predicado;
import visitor.Visitable;
import visitor.Visitor;

import java.util.Comparator;

public class ArbolGenerico<T extends ObjetoComparable<T>> implements Visitable<T> {

    private class NodoGenerico {

        private T dato;
        private NodoGenerico izq;
        private NodoGenerico der;

        /**
         * constructores
         **/
        public NodoGenerico(T dato, NodoGenerico izq, NodoGenerico der) {
            this.dato = dato;
            this.izq = izq;
            this.der = der;
        }

        public NodoGenerico(T dato) {
            this.dato = dato;
        }

    }

    private NodoGenerico raiz;

    private final Comparator<T> comparador;
    //constructores

    public ArbolGenerico(Comparator<T> comparador) {
        this.comparador = comparador;
    }

    public ArbolGenerico(NodoGenerico raiz, Comparator<T> comparador) {
        this.raiz = raiz;
        this.comparador = comparador;
    }

    @Override
    public void visitarOrdenado(Visitor<T> visitor) {
        inOrder(visitor);
    }

    @Override
    public void visitarDesordenado(Visitor<T> visitor) {
        orderDesc(visitor);
    }

    //****************** METODOS ******************//

    public void insertar(T dato) throws DuplicadoException {
        this.raiz = insertar(raiz, dato);
    }

    private NodoGenerico insertar(NodoGenerico nodo, T dato) throws DuplicadoException {
        if (nodo == null) {
            return new NodoGenerico(dato);
        } else if (dato.esMayor(nodo.dato)) {
            nodo.der = insertar(nodo.der, dato);
            return nodo;
        } else if (dato.esMenor(nodo.dato)) {
            nodo.izq = insertar(nodo.izq, dato);
            return nodo;
        } else {
            throw new DuplicadoException("Duplicado");
        }
    }

    public Tupla<T, Integer> buscar(T dato) {
        return buscar(raiz, dato, 0);
    }

    private Tupla<T, Integer> buscar(NodoGenerico nodo, T buscado, int Altura) {
        if (nodo == null) return null;
        else if (buscado.esMayor(nodo.dato)) return buscar(nodo.der, buscado, Altura + 1);
        else if (buscado.esMenor(nodo.dato)) return buscar(nodo.izq, buscado, Altura + 1);
        else {
            Tupla<T, Integer> miTupla = new Tupla<>();
            miTupla.setUno(nodo.dato);
            miTupla.setDos(Altura);
            return miTupla;
        }
    }

    public ArbolGenerico<T> buscarPredicado(Predicado<T> predicado) {
        ArbolGenerico<T> resultado = new ArbolGenerico<>(comparador);
        buscarPredicado(raiz, predicado, resultado);
        return resultado;
    }

    private void buscarPredicado(NodoGenerico nodo, Predicado<T> predicado, ArbolGenerico<T> resultado) {
        if (nodo == null) {
        } else {
            if (predicado.pasaONo(nodo.dato)) {
                try {
                    resultado.insertar(nodo.dato);
                } catch (DuplicadoException e) {
                    throw new RuntimeException(e);
                }
            }
            buscarPredicado(nodo.izq, predicado, resultado);
            buscarPredicado(nodo.der, predicado, resultado);
        }

    }

    public boolean pertenece(T dato) {
        return pertenece(raiz, dato);
    }

    private boolean pertenece(NodoGenerico nodo, T dato) {
        /** ***** ABB ***** **/
        if (nodo == null) {
            return false;
        } else {
            if (comparador.compare(dato, nodo.dato) > 0) {
                return pertenece(nodo.izq, dato);
            } else if (nodo.dato == dato) {
                return true;
            } else {
                return pertenece(nodo.der, dato);
            }
        }
    }

    /*
     * Altura del arbol
     * */
    public int alturaAb() {
        return alturaAb(raiz);
    }

    private int alturaAb(NodoGenerico n) {
        if (n == null) {
            return -1;
        } else if (esHoja(n)) {
            return 0;
        } else {
            return 1 + Math.max(alturaAb(n.der), alturaAb(n.izq));
        }
    }

    /*
     * Determinar si un nodo es hoja
     * */
    private boolean esHoja(NodoGenerico n) {
        return esVacio(n.izq) && esVacio(n.der);
    }

    /*
     * Determinar si un nodo es null
     * */
    private boolean esVacio(NodoGenerico n) {
        return n == null;
    }

    /*
     * Contar nodos de un ab
     * */
    public int cantNodos() {
        return cantNodos(raiz);
    }

    private int cantNodos(NodoGenerico n) {
        if (n == null) {
            return 0;
        } else {
            System.out.println("llamada");
            return 1 + cantNodos(n.der) + cantNodos(n.izq);
        }
    }

    /*
     * Contar hojas de un arbol
     * */
    public int cantHojas() {
        return cantHojas(raiz);
    }

    private int cantHojas(NodoGenerico n) {
        if (n == null) {
            return 0;
        } else if (n.der == null && n.izq == null) {
            return 1;
        } else {
            return cantHojas(n.der) + cantHojas(n.izq);
        }
    }

    public void inOrder(Visitor<T> visitor) {
        /** ***** ABB ***** **/
        inOrder(raiz, visitor);
    }

    private void inOrder(NodoGenerico nodo, Visitor<T> visitor) {
        /** ***** ABB ***** **/
        if (nodo != null) {
            inOrder(nodo.izq, visitor);
            //System.out.print(nodo.dato + " ");
            visitor.visitar(nodo.dato);
            inOrder(nodo.der, visitor);
        }
    }

    public void orderDesc(Visitor<T> visitor) {
        orderDesc(raiz, visitor);
    }

    private void orderDesc(NodoGenerico nodo, Visitor<T> visitor) {
        /** ***** ABB ***** **/
        if (nodo != null) {
            orderDesc(nodo.der, visitor);
            visitor.visitar(nodo.dato);
            orderDesc(nodo.izq, visitor);
        }
    }

    public void preOrder(Visitor<T> visitor) {
        /** ***** ABB ***** **/
        preOrder(raiz, visitor);
    }

    private void preOrder(NodoGenerico nodo, Visitor<T> visitor) {
        /** ***** ABB ***** **/
        if (nodo != null) {
            visitor.visitar(nodo.dato);
            preOrder(nodo.izq, visitor);
            preOrder(nodo.der, visitor);
        }
    }


    public ListaGenerica<T> filtrar(Predicado<T> pasaONo) {
        ListaGenerica<T> miLista = new ListaGenerica<>();
        filtrar(pasaONo, raiz, miLista);
        return miLista;
    }


    private void filtrar(Predicado<T> pasaONo, NodoGenerico n, ListaGenerica<T> lista) {
        if (n != null) {
            if (pasaONo.pasaONo(n.dato)) {
                lista.agregardesordenado(n.dato);
            }
            filtrar(pasaONo, n.izq, lista);
            filtrar(pasaONo, n.der, lista);
        }
    }

}


