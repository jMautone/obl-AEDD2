package sistema;

import dominio.Jugador;
import estructuras.arbol.ArbolGenerico;
import estructuras.grafo.Grafo;
import estructuras.grafo.Vertice;
import estructuras.listaG.ListaGenerica;
import estructuras.Tupla;
import excepciones.DuplicadoException;
import interfaz.*;
import predicado.JugadorPasaConsulta;
import predicado.Predicado;
import visitor.VisitorSerealizador;
import visitor.VisitorSerealizadorCedula;

import java.util.regex.*;


public class ImplementacionSistema implements Sistema {

    private ArbolGenerico<Jugador> jugadores;
    private ArbolGenerico<Jugador> jugadoresAvanzado;
    private ArbolGenerico<Jugador> jugadoresMedio;
    private ArbolGenerico<Jugador> jugadoresInicial;
    private ArbolGenerico<Jugador> jugadoresMonitor;
    private Grafo centrosUrbanos;

    public ArbolGenerico<Jugador> getJugadores() {return jugadores;}

    public void setJugadores(ArbolGenerico<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void setJugadoresAvanzados(ArbolGenerico<Jugador> jugadores) {
        this.jugadoresAvanzado = jugadores;
    }

    public void setJugadoresMedios(ArbolGenerico<Jugador> jugadores) {
        this.jugadoresMedio = jugadores;
    }

    public void setJugadoresInicial(ArbolGenerico<Jugador> jugadores) {
        this.jugadoresInicial = jugadores;
    }

    public void setJugadoresMonitor(ArbolGenerico<Jugador> jugadores) {
        this.jugadoresMonitor = jugadores;
    }

    public Grafo getCentrosUrbanos() {
        return centrosUrbanos;
    }

    public void setCentrosUrbanos(Grafo centrosUrbanos) {
        this.centrosUrbanos = centrosUrbanos;
    }


    @Override
    public Retorno inicializarSistema(int maxCentros) {
        if (maxCentros <= 5) {
            return Retorno.error1("El maximo de centros urabnos tiene que ser mayor a 5");
        }
        this.setJugadores(new ArbolGenerico<Jugador>((o1, o2) -> o1.getEdad() - o2.getEdad()));
        this.setJugadoresAvanzados(new ArbolGenerico<Jugador>((o1, o2) -> o1.getEdad() - o2.getEdad()));
        this.setJugadoresMedios(new ArbolGenerico<Jugador>((o1, o2) -> o1.getEdad() - o2.getEdad()));
        this.setJugadoresInicial(new ArbolGenerico<Jugador>((o1, o2) -> o1.getEdad() - o2.getEdad()));
        this.setJugadoresMonitor(new ArbolGenerico<Jugador>((o1, o2) -> o1.getEdad() - o2.getEdad()));
        this.setCentrosUrbanos(new Grafo(maxCentros));
        return Retorno.ok("se inicializa el sistema");
    }

    @Override
    public Retorno explorarCentroUrbano(boolean[] correctas, int[] puntajes, int minimo) {
        if (correctas == null || puntajes == null) {
            return Retorno.error1("alguno de los parámetros es null");
        } else if (correctas.length < 3 || puntajes.length < 3) {
            return Retorno.error2("alguno de los arrays tiene menos de 3 elementos");
        } else if (correctas.length != puntajes.length) {
            return Retorno.error3("los arrays tienen distinta medida");
        } else if (minimo <= 0) {
            return Retorno.error4("el minimo debe ser mayor que 0");
        } else {
            int puntaje = puntajeTotalSuma(puntajes, correctas, 0, 0);
            if (puntaje >= minimo) return Retorno.ok(puntaje, "pasa");
            else return Retorno.ok(puntaje, "no pasa");
        }
    }

    private int puntajeTotalSuma(int[] puntajes, boolean[] correctas, int pos, int correctasSeguidas) {
        if (pos >= puntajes.length) {
            return 0;
        } else if (correctas[pos]) {
            correctasSeguidas++;
            if (correctasSeguidas == 3) {
                return puntajeTotalSuma(puntajes, correctas, pos + 1, correctasSeguidas) + puntajes[pos] + 3;
            } else if (correctasSeguidas == 4) {
                return puntajeTotalSuma(puntajes, correctas, pos + 1, correctasSeguidas) + puntajes[pos] + 5;
            } else if (correctasSeguidas >= 5) {
                return puntajeTotalSuma(puntajes, correctas, pos + 1, correctasSeguidas) + puntajes[pos] + 8;
            } else {
                return puntajeTotalSuma(puntajes, correctas, pos + 1, correctasSeguidas) + puntajes[pos];
            }
        } else {
            return puntajeTotalSuma(puntajes, correctas, pos + 1, 0);
        }
    }

    @Override
    public Retorno registrarJugador(String ci, String nombre, int edad, String escuela, TipoJugador tipo) {
        Jugador unJugador = new Jugador(ci, nombre, edad, escuela, tipo);
        if (!unJugador.validarDatos()) {
            return Retorno.error1("alguno de los parámetros es vacio o null");
        }

        // https://www.javatpoint.com/java-regex
        // test: https://ihateregex.io/expr/phone/

        // crea un String con la expresión regular (REGEX)
        String REGEX = "[1-9]\\d{2}\\.\\d{3}\\-\\d|[1-9]\\.\\d{3}\\.\\d{3}\\-\\d";  //expresion regular

        // Compila el REGEX para crear patrón usando método "compile()"
        Pattern pattern = Pattern.compile(REGEX);

        // obtengo un matcher a partir del patrón
        Matcher m = pattern.matcher(ci);

        // verifico si el REGEX string se encuentra o no en actualString
        boolean coincide = m.matches();

        if (!coincide) {
            return Retorno.error2("el formato de la cedula no es valido");
        }
        //verificar existencia del jugador con cedula
        if (jugadores.pertenece(unJugador)) {
            return Retorno.error3("ya existe un jugador con esa cedula");
        }

        try {
            jugadores.insertar(unJugador);
            switch (unJugador.getTipo().getIndice()) {
                case 0:
                    jugadoresAvanzado.insertar(unJugador);
                    break;
                case 1:
                    jugadoresMedio.insertar(unJugador);
                    break;
                case 2:
                    jugadoresInicial.insertar(unJugador);
                    break;
                case 3:
                    jugadoresMonitor.insertar(unJugador);
                    break;
            }
        } catch (DuplicadoException e) {
            System.out.println(e.toString());
            return Retorno.error3("ya existe un jugador con esa cedula");
        }

        return Retorno.ok("el jugador se registro correctamente");
    }

    @Override
    public Retorno filtrarJugadores(Consulta consulta) {
        VisitorSerealizadorCedula<Jugador> visitorSerealizadorJugadores = new VisitorSerealizadorCedula<>();
        Predicado<Jugador> j = new JugadorPasaConsulta(consulta);
        ListaGenerica<Jugador> losJugadores = jugadores.filtrar(j);
        losJugadores.visitarOrdenado(visitorSerealizadorJugadores);
        return Retorno.ok(visitorSerealizadorJugadores.getSerealizador());
    }

    @Override
    public Retorno buscarJugador(String ci) {
        Jugador jugadorSoloCedula = new Jugador(ci);
        // https://www.javatpoint.com/java-regex
        // test: https://ihateregex.io/expr/phone/

        // crea un String con la expresión regular (REGEX)
        String REGEX = "[1-9]\\d{2}\\.\\d{3}\\-\\d|[1-9]\\.\\d{3}\\.\\d{3}\\-\\d";  //expresion regular

        // Compila el REGEX para crear patrón usando método "compile()"
        Pattern pattern = Pattern.compile(REGEX);

        // obtengo un matcher a partir del patrón
        Matcher m = pattern.matcher(ci);

        // verifico si el REGEX string se encuentra o no en actualString
        boolean coincide = m.matches();

        if (!coincide) {
            return Retorno.error1("el formato de la cedula no es valido");
        }

        Tupla<Jugador, Integer> miTupla = jugadores.buscar(jugadorSoloCedula);
        if (miTupla == null) {
            return Retorno.error2("El jugador no existe");
        }
        Jugador encontrado = miTupla.getUno();
        int cantidadRecorridas = miTupla.getDos();
        return Retorno.ok(cantidadRecorridas, encontrado.toString());
    }


    @Override
    public Retorno listarJugadoresPorCedulaAscendente() {
        VisitorSerealizador<Jugador> visitorSerealizador = new VisitorSerealizador<>();
        jugadores.visitarOrdenado(visitorSerealizador);
        return Retorno.ok(visitorSerealizador.getSerealizador());
    }

    @Override
    public Retorno listarJugadoresPorCedulaDescendente() {
        VisitorSerealizador<Jugador> visitorSerealizador = new VisitorSerealizador<>();
        jugadores.visitarDesordenado(visitorSerealizador);
        return Retorno.ok(visitorSerealizador.getSerealizador());
    }

    @Override
    public Retorno listarJugadoresPorTipo(TipoJugador unTipo) {

        if (unTipo == null) {
            return Retorno.error1("tipo nulo");
        }
        VisitorSerealizador<Jugador> serealizadorPorTipo = new VisitorSerealizador<>();
        Jugador jugadorSoloTipo = new Jugador(unTipo);
        switch (jugadorSoloTipo.getTipo().getIndice()) {
            case 0:
                jugadoresAvanzado.visitarOrdenado(serealizadorPorTipo);
                break;
            case 1:
                jugadoresMedio.visitarOrdenado(serealizadorPorTipo);
                break;
            case 2:
                jugadoresInicial.visitarOrdenado(serealizadorPorTipo);
                break;
            case 3:
                jugadoresMonitor.visitarOrdenado(serealizadorPorTipo);
                break;

        }

        return Retorno.ok(serealizadorPorTipo.getSerealizador());
    }

    @Override
    public Retorno registrarCentroUrbano(String codigo, String nombre) {
        if (this.centrosUrbanos.getMaxVertices() == this.centrosUrbanos.getLargo()) {
            return Retorno.error1("El grafo está lleno");
        } else if (codigo == null || codigo.equals("") || nombre == null || nombre.equals("")) {
            return Retorno.error2("algún dato es vacío o nulo");
        } else if (this.centrosUrbanos.existeVertice(codigo)) {
            return Retorno.error3("Ya existe un centro con ese código");
        }
        this.centrosUrbanos.agregarVertice(codigo, nombre);
        return Retorno.ok();
    }

    @Override
    public Retorno registrarCamino(String codigoCentroOrigen, String codigoCentroDestino, double costo, double tiempo, double kilometros, EstadoCamino estadoDelCamino) {
        if (costo <= 0 | tiempo <= 0 | kilometros <= 0) {
            return Retorno.error1("alguno de los parámetros double es menor o igual a 0");
        } else if (codigoCentroOrigen == null || codigoCentroOrigen == null || codigoCentroDestino == null || estadoDelCamino == null ||
                codigoCentroOrigen.equals("") || codigoCentroDestino.equals("")) {
            return Retorno.error2("alguno de los parámetros String o enum es vacío o null");
        } else if (!this.centrosUrbanos.existeVertice(codigoCentroOrigen)) {
            return Retorno.error3("no existe el centro de origen.");
        } else if (!this.centrosUrbanos.existeVertice(codigoCentroDestino)) {
            return Retorno.error4("no existe el centro de destino.");
        } else if (this.centrosUrbanos.existeCamino(codigoCentroOrigen, codigoCentroDestino)) {
            return Retorno.error5("ya existe el camino entre el origen y el destino");
        } else if (this.centrosUrbanos.existeCamino(codigoCentroDestino, codigoCentroOrigen)) {
            return Retorno.error5("las conexiones no son navegables en ambos sentidos");
        }
        this.centrosUrbanos.agregarArista(codigoCentroOrigen, codigoCentroDestino, costo, tiempo, kilometros, estadoDelCamino);
        return Retorno.ok();

    }

    @Override
    public Retorno actualizarCamino(String codigoCentroOrigen, String codigoCentroDestino, double costo, double tiempo, double kilometros, EstadoCamino estadoDelCamino) {
        if (costo <= 0 | tiempo <= 0 | kilometros <= 0) {
            return Retorno.error1("alguno de los parámetros double es menor o igual a 0");
        } else if (codigoCentroOrigen == null || codigoCentroOrigen == null || codigoCentroDestino == null || estadoDelCamino == null ||
                codigoCentroOrigen.equals("")|| codigoCentroDestino.equals("")) {
            return Retorno.error2("alguno de los parámetros String o enum es vacío o null");
        } else if (!this.centrosUrbanos.existeVertice(codigoCentroOrigen)) {
            return Retorno.error3("no existe el centro de origen.");
        } else if (!this.centrosUrbanos.existeVertice(codigoCentroDestino)) {
            return Retorno.error4("no existe el centro de destino.");
        } else if (!this.centrosUrbanos.existeCamino(codigoCentroOrigen, codigoCentroDestino)) {
            return Retorno.error5("No existe el camino entre el origen y el destino");
        }
        this.centrosUrbanos.modificarArista(codigoCentroOrigen, codigoCentroDestino, costo, tiempo, kilometros, estadoDelCamino);
        return Retorno.ok();
    }

    @Override
    public Retorno listadoCentrosCantDeSaltos(String codigoCentroOrigen, int cantidad) {
        if (cantidad < 0) {
            return Retorno.error1("la cantidad es menor que 0");
        } else if (!this.centrosUrbanos.existeVertice(codigoCentroOrigen)) {
            return Retorno.error2("El centro no está registrado en el sistema");
        }
        VisitorSerealizador<Vertice> serealizadorCentros = new VisitorSerealizador<>();
        ListaGenerica<Vertice> centros = centrosUrbanos.cantidadFronteras(cantidad, codigoCentroOrigen);
        centros.visitarOrdenado(serealizadorCentros);
        return Retorno.ok(serealizadorCentros.getSerealizador());
    }


    @Override
    public Retorno viajeCostoMinimoKilometros(String codigoCentroOrigen, String codigoCentroDestino) {

        if(codigoCentroOrigen == null || codigoCentroDestino == null || codigoCentroOrigen.equals("") || codigoCentroDestino.equals("")){
            return Retorno.error1("Alguno de los códigos es nulo");
        }else if(!this.centrosUrbanos.existeVertice(codigoCentroOrigen)){
            return Retorno.error3("No existe el origen");
        }else if(!this.centrosUrbanos.existeVertice(codigoCentroDestino)){
            return Retorno.error4("No existe el destino");
        }

        Tupla<ListaGenerica<Vertice>, Integer> resultado = centrosUrbanos.dijkstraKilometros(codigoCentroOrigen, codigoCentroDestino);
        if(resultado == null ) {
            return Retorno.error2("No existe camino");
        }

        VisitorSerealizador<Vertice> serealizadorCentros = new VisitorSerealizador<>();
        resultado.getUno().visitarOrdenado(serealizadorCentros);

        return Retorno.ok(resultado.getDos(),serealizadorCentros.getSerealizador());
    }

    @Override
    public Retorno viajeCostoMinimoMonedas(String codigoCentroOrigen, String codigoCentroDestino) {
        if(codigoCentroOrigen == null || codigoCentroDestino == null || codigoCentroOrigen.equals("") || codigoCentroDestino.equals("")){
            return Retorno.error1("Alguno de los códigos es nulo");
        }else if(!this.centrosUrbanos.existeVertice(codigoCentroOrigen)){
            return Retorno.error3("No existe el origen");
        }else if(!this.centrosUrbanos.existeVertice(codigoCentroDestino)){
            return Retorno.error4("No existe el destino");
        }

        Tupla<ListaGenerica<Vertice>, Integer> resultado = centrosUrbanos.dijkstraCostoMonedas(codigoCentroOrigen, codigoCentroDestino);
        if(resultado == null ) {
            return Retorno.error2("No existe camino");
        }
        VisitorSerealizador<Vertice> serealizadorCentros = new VisitorSerealizador<>();
        resultado.getUno().visitarOrdenado(serealizadorCentros);

        return Retorno.ok(resultado.getDos(),serealizadorCentros.getSerealizador());
    }
}
