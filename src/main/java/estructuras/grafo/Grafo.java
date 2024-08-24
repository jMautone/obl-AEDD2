package estructuras.grafo;

import estructuras.Tupla;
import estructuras.listaG.ListaGenerica;
import estructuras.listaYCola.Pila;
import interfaz.EstadoCamino;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.LinkedList;
import java.util.Queue;

public class Grafo {

    private Vertice[] vertices;
    private Arista[][] aristas;
    private int maxVertices;
    private int largo;

    public Grafo(int maxVertices) {
        this.maxVertices = maxVertices;
        this.vertices = new Vertice[maxVertices];
        this.aristas = new Arista[maxVertices][maxVertices];
        for (int i = 0; i < maxVertices; i++) {
            for (int j = 0; j < maxVertices; j++) {
                aristas[i][j] = new Arista();
            }
        }
    }

    public Vertice[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertice[] vertices) {
        this.vertices = vertices;
    }

    public Arista[][] getAristas() {
        return aristas;
    }

    public void setAristas(Arista[][] aristas) {
        this.aristas = aristas;
    }

    public int getMaxVertices() {
        return maxVertices;
    }

    public void setMaxVertices(int maxVertices) {
        this.maxVertices = maxVertices;
    }

    public int getLargo() {
        return largo;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }


    public void agregarVertice(String codigo, String nombre) {
        vertices[largo] = new Vertice(codigo, nombre);
        largo++;
    }

    // cehquear
    public Boolean existeVertice(String codigo) {
        Boolean retorno = false;
        for (int i = 0; i < largo && !retorno; i++) {
            if (vertices[i].getCodigo().equals(codigo)) {
                retorno = true;
            }
        }
        return retorno;
    }

    private int buscarVertice(String codigo) {
        int retorno = -1;
        for (int i = 0; i < largo; i++) {
            if (codigo.equals(vertices[i].getCodigo())) return i;
        }
        return retorno;

    }

    public Boolean existeCamino(String codigoOrigen, String codigoDestino) {
        int indiceOrigen = buscarVertice(codigoOrigen);
        int indiceDestino = buscarVertice(codigoDestino);
        if (indiceOrigen == -1 | indiceDestino == -1) {
            return false;
        }
        if (aristas[indiceOrigen][indiceDestino].isExiste()) return true;
        else return false;
    }

    public void agregarArista(String codigoOrigen, String codigoDestino, double costo, double tiempo, double kilometros, EstadoCamino estadoDelCamino) {
        int indiceOrigen = buscarVertice(codigoOrigen);
        int indiceDestino = buscarVertice(codigoDestino);
        Arista caminoNuevo = new Arista();
        caminoNuevo = this.aristas[indiceOrigen][indiceDestino];
        caminoNuevo.setExiste(true);
        caminoNuevo.setCosto(costo);
        caminoNuevo.setTiempo(tiempo);
        caminoNuevo.setKilometros(kilometros);
        caminoNuevo.setEstado(estadoDelCamino);
    }

    public void modificarArista(String codigoOrigen, String codigoDestino, double costo, double tiempo, double kilometros, EstadoCamino estadoDelCamino) {
        int indiceOrigen = buscarVertice(codigoOrigen);
        int indiceDestino = buscarVertice(codigoDestino);
        Arista aristaAModificar = aristas[indiceOrigen][indiceDestino];
        aristaAModificar.setCosto(costo);
        aristaAModificar.setTiempo(tiempo);
        aristaAModificar.setKilometros(kilometros);
        aristaAModificar.setEstado(estadoDelCamino);

    }


    public ListaGenerica<Vertice> cantidadFronteras(int cantSaltos, String stringOrigen) {
        ListaGenerica<Vertice> centros = new ListaGenerica<>();

        Pila<Integer> frontera = new Pila<>(maxVertices);
        boolean[] visitados = new boolean[maxVertices];
        int vOrigen = buscarVertice(stringOrigen);
        frontera.apilar(vOrigen);

        int cantidadNivActual = 1;
        int cantidadNivSiguiente = 0;
        int cantidadSaltosHastaAhora = 0;

        while (!frontera.esVacia()) {//es vacia
            int vExplorar = frontera.cima();
            cantidadNivActual -= 1;
            if (!visitados[vExplorar]) {
                centros.agregarordenado(vertices[vExplorar]);
                visitados[vExplorar] = true;

                if (cantidadSaltosHastaAhora == 0)
                    frontera.desapilar();

                if (cantSaltos > cantidadSaltosHastaAhora) {
                    for (int vAdy = 0; vAdy < maxVertices; vAdy++) {
                        if (esAdyacente(vExplorar, vAdy)) {
                            frontera.apilar(vAdy);
                            cantidadNivSiguiente += 1;
                        }
                    }
                }
                if (cantidadNivActual == 0) {
                    cantidadSaltosHastaAhora++;
                    cantidadNivActual = cantidadNivSiguiente;
                    cantidadNivSiguiente = 0;
                }
            } else {
                frontera.desapilar();
            }
        }
        return centros;
    }

    private Tupla<ListaGenerica<Vertice>, Integer> dijkstraKilometros(int vOrigen, int vDestino) {
        double[] distancias = new double[maxVertices];
        boolean[] visitados = new boolean[maxVertices];
        int[] padres = new int[maxVertices];

        for (int i = 0; i < maxVertices; i++) {
            distancias[i] = Double.MAX_VALUE;
            padres[i] = -1;
        }

        padres[vOrigen] = vOrigen;
        distancias[vOrigen] = 0;
        //estaTodoVisitado es un control para saber si el padre es positivo, porque si no hay padre es que
        //todavia no encontre un camino hacia el vertice
        while (!estaTodoVisitado(visitados, padres)) {
            int vExplorar = dameElVerticeConMenorDistanciaNoVisitado(visitados, distancias);
            for (int vAdyacente = 0; vAdyacente < maxVertices; vAdyacente++) {
                if (esAdyacente(vExplorar, vAdyacente)) {
                    double distanciaPorVAExplorar = distancias[vExplorar] +
                            aristas[vExplorar][vAdyacente].getKilometros();

                    if (distanciaPorVAExplorar < distancias[vAdyacente] &&
                            !aristas[vExplorar][vAdyacente].getEstado().equals(EstadoCamino.MALO)) {
                        distancias[vAdyacente] = distanciaPorVAExplorar;
                        padres[vAdyacente] = vExplorar;
                    }
                }
            }
            visitados[vExplorar] = true;
        }
        Tupla<ListaGenerica<Vertice>, Integer> lista = reconstruirCamino(padres, vOrigen, vDestino, "km");
        return lista;
    }

    private Tupla<ListaGenerica<Vertice>, Integer> dijkstraCostoMonedas(int vOrigen, int vDestino) {
        double[] distancias = new double[maxVertices];
        boolean[] visitados = new boolean[maxVertices];
        int[] padres = new int[maxVertices];
        for (int i = 0; i < maxVertices; i++) {
            distancias[i] = Double.MAX_VALUE;
            padres[i] = -1;
        }
        padres[vOrigen] = vOrigen;
        distancias[vOrigen] = 0;
        //estaTodoVisitado es un control para saber si el padre es positivo, porque si no hay padre es que
        //todavia no encontre un camino hacia el vertice
        while (!estaTodoVisitado(visitados, padres)) {
            int vExplorar = dameElVerticeConMenorDistanciaNoVisitado(visitados, distancias);
            for (int vAdyacente = 0; vAdyacente < maxVertices; vAdyacente++) {
                if (esAdyacente(vExplorar, vAdyacente)) {
                    double distanciaPorVAExplorar = distancias[vExplorar] +
                            aristas[vExplorar][vAdyacente].getCosto();

                    if (distanciaPorVAExplorar < distancias[vAdyacente] &&
                            !aristas[vExplorar][vAdyacente].getEstado().equals(EstadoCamino.MALO)) {
                        distancias[vAdyacente] = distanciaPorVAExplorar;
                        padres[vAdyacente] = vExplorar;
                    }
                }
            }
            visitados[vExplorar] = true;
        }
        Tupla<ListaGenerica<Vertice>, Integer> lista = reconstruirCamino(padres, vOrigen, vDestino, "monedas");
        return lista;
    }

    public Tupla<ListaGenerica<Vertice>, Integer> dijkstraKilometros(String origen, String destino) {
        return this.dijkstraKilometros(buscarVertice(origen), buscarVertice(destino));
    }

    public Tupla<ListaGenerica<Vertice>, Integer> dijkstraCostoMonedas(String origen, String destino) {
        return this.dijkstraCostoMonedas(buscarVertice(origen), buscarVertice(destino));
    }

    private Tupla<ListaGenerica<Vertice>, Integer> reconstruirCamino(int[] padres, int vOrigen, int vDestino, String caso) {
        ListaGenerica<Vertice> camino = new ListaGenerica<>();
        int verticeActual = vDestino;

        //String [] losvertices = new String[padres.length];
        int[] losverticesInt = new int[padres.length];
        //para solucionar que cuando sea vertice 0 no se corte, si queda en -1 es porque no existe
        for (int i = 0; i < losverticesInt.length; i++) {
            losverticesInt[i] = -1;
        }
        int i = 0;

        //me paro en el ultimo y voy para atras haciendo un agregar incio
        //yendo a traves de sus pares
        while (verticeActual != vOrigen) {
            //si veo un menos uno es que no habia camino al nodo
            if (verticeActual == -1) {
                return null; //porque no hay un camino
            }
            camino.agregarAlFinal(vertices[verticeActual]);
            losverticesInt[i] = verticeActual;
            verticeActual = padres[verticeActual];
            i++;
        }
        camino.agregarAlFinal(vertices[vOrigen]);
        losverticesInt[i] = verticeActual;

        int suma;
        if (caso == "km")
            suma = calcularPonderacionKilometros(losverticesInt);
        else
            suma = calcularPonderacionMonedas(losverticesInt);

        Tupla<ListaGenerica<Vertice>, Integer> retorno = new Tupla<>();
        retorno.setUno(camino);
        retorno.setDos(suma);
        return retorno;
    }

    private int calcularPonderacionKilometros(int[] camino) {
        int suma = 0;
        //lo tengo q recorrer de atras en adelante por como los guarde
        for (int i = 0; i < camino.length; i++) {
            if (camino[i + 1] != -1)
                suma += aristas[camino[i + 1]][camino[i]].getKilometros();
            else
                break;
        }
        return suma;
    }

    private int calcularPonderacionMonedas(int[] camino) {
        int suma = 0;
        //lo tengo q recorrer de atras en adelante por como los guarde
        for (int i = 0; i < camino.length; i++) {
            if (camino[i + 1] != -1)
                suma += aristas[camino[i + 1]][camino[i]].getCosto();
            else
                break;
        }
        return suma;
    }

    private int dameElVerticeConMenorDistanciaNoVisitado(boolean[] visitados, double[] distancias) {
        int minIdx = -1;//la posicion del minimo
        double minDistancia = Double.MAX_VALUE; //el valor minimo
        for (int i = 0; i < visitados.length; i++) {
            if (distancias[i] < minDistancia && !visitados[i]) { //minimo que no esta visitado
                minDistancia = distancias[i]; //acutalizo valor minimo
                minIdx = i; //posicion del vertice con la menor distancia
            }
        }
        return minIdx;
    }

    private boolean estaTodoVisitado(boolean[] visitados, int[] padres) {
        for (int i = 0; i < visitados.length; i++) {
            if (!visitados[i] && padres[i] >= 0) return false;
        }
        return true;
    }

    private boolean esAdyacente(int vOrigen, int vDestino) {
        //Recibe dos vertices como condicion
        return aristas[vOrigen][vDestino].isExiste();
    }

    public void bfs(int vOrigen) {
        //Se puede hacer con cola o con pila
        Pila<Integer> frontera = new Pila<>(maxVertices);
        boolean[] visitados = new boolean[maxVertices];
        frontera.apilar(vOrigen);

        int cantidadNivActual = 1;
        int cantidadNivSiguiente = 0;
        int cantidadSaltosHastaAhora = 0;

        while (!frontera.esVacia()) {//es vacia
            int vExplorar = frontera.cima(); //obtener el tope de la pila
            cantidadNivActual -= 1;
            if (!visitados[vExplorar]) {
                System.out.println("Nivel: " + cantidadSaltosHastaAhora+":"+vertices[vExplorar]);
                visitados[vExplorar] = true;

                if (cantidadSaltosHastaAhora == 0)
                    frontera.desapilar();

                for (int vAdy = 0; vAdy < maxVertices; vAdy++) {
                    if (esAdyacente(vExplorar, vAdy)) {
                        frontera.apilar(vAdy);
                        cantidadNivSiguiente += 1;
                    }
                }

                if (cantidadNivActual == 0) {
                    cantidadSaltosHastaAhora++;
                    cantidadNivActual = cantidadNivSiguiente;
                    cantidadNivSiguiente = 0;
                }
            } else {
                frontera.desapilar();//sacar el tope de la pila
            }
        }
    }
}



