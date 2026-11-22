import java.io.PrintStream;

/**
 * APE 4 - Fase 2: representación dinámica y operaciones sobre personas/relaciones.
 * Asignatura: Estructura de Datos - Universidad Técnica de Ambato.
 *
 * Red social "Conexión Ambato" implementada como multilista (lista de adyacencia
 * con nodos propios). No se usan HashMap, HashSet, ArrayList ni LinkedList.
 *
 * Ejecución (JDK 11 o superior):  java ConexionAmbato.java
 */

/** Programa principal: demostración y pruebas automatizadas. */
public class ConexionAmbato {

    private static int total = 0;
    private static int aprobadas = 0;

    /** Verifica una condición e imprime el resultado de la prueba. */
    private static void verificar(String descripcion, boolean condicion) {
        total++;
        if (condicion) aprobadas++;
        System.out.println((condicion ? "  [OK]    " : "  [FALLO] ") + descripcion);
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    /** Carga la red inicial de la Fase 1 (6 usuarios, 5 amistades). */
    private static RedSocial redInicial() {
        RedSocial red = new RedSocial();
        red.registrarPersona("A01", "Sofía");
        red.registrarPersona("A02", "Mateo");
        red.registrarPersona("A03", "Valeria");
        red.registrarPersona("A04", "Andrés");
        red.registrarPersona("A05", "Camila");
        red.registrarPersona("A06", "Diego");
        red.agregarAmistad("A01", "A02");
        red.agregarAmistad("A02", "A03");
        red.agregarAmistad("A03", "A04");
        red.agregarAmistad("A04", "A01");
        red.agregarAmistad("A04", "A05");
        return red;
    }

    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.out.println("CONEXIÓN AMBATO - APE 4 Fase 2 (multilista)");

        // ---------------- Estado inicial (Fase 1) ----------------
        titulo("Paso 1: registrar A01-A06 y las cinco amistades");
        RedSocial red = redInicial();
        System.out.print(red.mostrarRed());
        System.out.println("Personas: " + red.contarPersonas()
                + " | Aristas internas: " + red.contarAristas());
        verificar("6 usuarios registrados", red.contarPersonas() == 6);
        verificar("10 aristas internas (5 amistades x 2)", red.contarAristas() == 10);
        verificar("Grados 2,2,2,3,1,0",
                red.contarAmigos("A01") == 2 && red.contarAmigos("A02") == 2
                && red.contarAmigos("A03") == 2 && red.contarAmigos("A04") == 3
                && red.contarAmigos("A05") == 1 && red.contarAmigos("A06") == 0);

        // ---------------- Seguimientos ----------------
        titulo("Pasos 2 y 3: seguimientos dirigidos");
        verificar("seguir(A02, A01) = true", red.seguir("A02", "A01"));
        System.out.println("  Seguidores de Sofía: " + red.listarSeguidores("A01"));
        verificar("Mateo aparece entre los seguidores de Sofía",
                red.listarSeguidores("A01").contains("Mateo"));
        verificar("seguir(A06, A01) = true (Diego no tiene amigos)", red.seguir("A06", "A01"));
        System.out.println("  Seguidores de Sofía: " + red.listarSeguidores("A01"));
        verificar("Seguidores de Sofía = Diego y Mateo",
                red.listarSeguidores("A01").equals("Diego, Mateo"));
        verificar("Seguir no es recíproco: Sofía no sigue a Mateo",
                red.listarSeguidos("A01").equals("(ninguno)"));
        verificar("Seguir duplicado se rechaza", !red.seguir("A02", "A01"));
        System.out.println("  Seguidos por Mateo: " + red.listarSeguidos("A02"));

        // ---------------- Consultas ----------------
        titulo("Paso 4: listarAmigos(A04)");
        System.out.println("  Amigos de Andrés: " + red.listarAmigos("A04"));
        verificar("Andrés tiene 3 amigos (Valeria, Sofía, Camila)",
                red.contarAmigos("A04") == 3
                && red.listarAmigos("A04").contains("Valeria")
                && red.listarAmigos("A04").contains("Sofía")
                && red.listarAmigos("A04").contains("Camila"));
        verificar("Orden de lista = inverso a la inserción (inserción al inicio)",
                red.listarAmigos("A04").equals("Camila, Sofía, Valeria"));
        verificar("listarAmigos de usuario inexistente = null", red.listarAmigos("zz") == null);
        verificar("contarAmigos de usuario inexistente = -1", red.contarAmigos("zz") == -1);
        verificar("Búsqueda insensible a mayúsculas (a01 = A01)", red.contarAmigos("a01") == 2);

        // ---------------- Eliminar amistad ----------------
        titulo("Paso 5: eliminarAmistad(A04, A05)");
        verificar("eliminarAmistad(A04, A05) = true", red.eliminarAmistad("A04", "A05"));
        System.out.println("  Amigos de Andrés: " + red.listarAmigos("A04"));
        System.out.println("  Amigos de Camila: " + red.listarAmigos("A05"));
        verificar("Andrés pierde a Camila", !red.listarAmigos("A04").contains("Camila"));
        verificar("Camila pierde a Andrés (ambas aristas)", red.contarAmigos("A05") == 0);
        verificar("Eliminar de nuevo la misma amistad = false", !red.eliminarAmistad("A04", "A05"));

        // ---------------- Dejar de seguir ----------------
        titulo("Extra: dejarDeSeguir");
        verificar("dejarDeSeguir(A06, A01) = true", red.dejarDeSeguir("A06", "A01"));
        verificar("Seguidores de Sofía = Mateo", red.listarSeguidores("A01").equals("Mateo"));
        verificar("dejarDeSeguir sin relación = false", !red.dejarDeSeguir("A06", "A01"));
        red.seguir("A06", "A01"); // se restablece para el paso siguiente

        // ---------------- Eliminar persona ----------------
        titulo("Paso 6: eliminarPersona(A01)");
        int aristasAntes = red.contarAristas();
        verificar("eliminarPersona(A01) = true", red.eliminarPersona("A01"));
        System.out.print(red.mostrarRed());
        System.out.println("Aristas antes: " + aristasAntes + " | después: " + red.contarAristas());
        verificar("Quedan 5 personas", red.contarPersonas() == 5);
        verificar("Sofía ya no existe (listarAmigos = null)", red.listarAmigos("A01") == null);
        verificar("Ninguna arista apunta a Sofía",
                !red.mostrarRed().contains("Sofía"));
        verificar("Mateo y Andrés perdieron una amistad cada uno",
                red.contarAmigos("A02") == 1 && red.contarAmigos("A04") == 1);
        verificar("eliminarPersona de inexistente = false", !red.eliminarPersona("A01"));

        // ---------------- Validaciones ----------------
        titulo("Pasos 7 y 8: validaciones");
        verificar("registrarPersona(A02, otro nombre) = false (duplicado)",
                !red.registrarPersona("A02", "Otro nombre"));
        verificar("agregarAmistad(A03, A03) = false (lazo)", !red.agregarAmistad("A03", "A03"));
        verificar("seguir(A03, A03) = false (lazo)", !red.seguir("A03", "A03"));
        verificar("agregarAmistad duplicada = false", !red.agregarAmistad("A02", "A03"));
        verificar("agregarAmistad con inexistente = false", !red.agregarAmistad("A02", "A99"));
        verificar("registrarPersona con usuario vacío = false", !red.registrarPersona("  ", "X"));
        verificar("registrarPersona con espacio en el usuario = false",
                !red.registrarPersona("A 07", "X"));
        verificar("registrarPersona con coma en el usuario = false",
                !red.registrarPersona("A,07", "X"));
        verificar("registrarPersona con nulos = false", !red.registrarPersona(null, null));

        // ---------------- Resumen ----------------
        titulo("Resumen de pruebas automatizadas");
        System.out.println("Pruebas aprobadas: " + aprobadas + " de " + total);
        System.out.println(aprobadas == total ? "RESULTADO: TODAS LAS PRUEBAS PASARON"
                                              : "RESULTADO: HAY PRUEBAS FALLIDAS");
    }
}

/** Tipos de relación permitidos entre dos personas. */
enum TipoRelacion { AMISTAD, SEGUIR }

/** Datos de una persona de la red. */
class Persona {
    String usuario; // identificador normalizado (minúsculas)
    String nombre;

    Persona(String usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }
}

/** Vértice de la multilista: una persona y su lista propia de aristas. */
class Vertice {
    Persona persona;
    Arista aristas; // cabeza de la lista de aristas salientes
    Vertice sig;    // siguiente vértice de la red
}

/** Arista dirigida de un vértice hacia otro, con su tipo de relación. */
class Arista {
    Vertice destino;
    TipoRelacion tipo;
    Arista sig;
}

/** Red social dinámica con todas las operaciones de la Fase 2. */
class RedSocial {
    private Vertice cabeza; // lista de vértices (se inserta al inicio)

    // ------------------------------------------------------------------
    // Utilidades internas
    // ------------------------------------------------------------------

    /** Busca un vértice por su identificador normalizado. */
    private Vertice buscar(String usuario) {
        if (usuario == null) return null;
        String clave = usuario.trim().toLowerCase();
        for (Vertice v = cabeza; v != null; v = v.sig)
            if (v.persona.usuario.equals(clave)) return v;
        return null;
    }

    /** Inserta una arista al comienzo de la lista de origen. */
    private void insertar(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista a = new Arista();
        a.destino = destino;
        a.tipo = tipo;
        a.sig = origen.aristas;
        origen.aristas = a;
    }

    /** Indica si ya existe una arista de ese tipo de origen a destino. */
    private boolean existe(Vertice origen, Vertice destino, TipoRelacion tipo) {
        for (Arista a = origen.aristas; a != null; a = a.sig)
            if (a.destino == destino && a.tipo == tipo) return true;
        return false;
    }

    /** Quita la arista (origen -> destino, tipo). Devuelve true si existía. */
    private boolean quitar(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista anterior = null;
        for (Arista a = origen.aristas; a != null; anterior = a, a = a.sig) {
            if (a.destino == destino && a.tipo == tipo) {
                if (anterior == null) origen.aristas = a.sig;
                else anterior.sig = a.sig;
                return true;
            }
        }
        return false;
    }

    /** Retira de un vértice toda arista (de cualquier tipo) que apunte al objetivo. */
    private void quitarHacia(Vertice origen, Vertice objetivo) {
        Arista anterior = null;
        Arista a = origen.aristas;
        while (a != null) {
            if (a.destino == objetivo) {
                if (anterior == null) origen.aristas = a.sig;
                else anterior.sig = a.sig;
                a = (anterior == null) ? origen.aristas : anterior.sig;
            } else {
                anterior = a;
                a = a.sig;
            }
        }
    }

    // ------------------------------------------------------------------
    // Operaciones sobre personas
    // ------------------------------------------------------------------

    /** Registra una persona. Rechaza usuario vacío, repetido, con espacios o comas. */
    public boolean registrarPersona(String usuario, String nombre) {
        if (usuario == null || nombre == null) return false;
        String clave = usuario.trim().toLowerCase();
        if (clave.isEmpty() || nombre.trim().isEmpty()) return false;
        if (clave.indexOf(' ') >= 0 || clave.indexOf(',') >= 0) return false;
        if (buscar(clave) != null) return false;

        Vertice v = new Vertice();
        v.persona = new Persona(clave, nombre.trim());
        v.sig = cabeza; // inserción al inicio
        cabeza = v;
        return true;
    }

    /** Elimina el vértice y todas las aristas entrantes. */
    public boolean eliminarPersona(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) return false;
        for (Vertice w = cabeza; w != null; w = w.sig)
            quitarHacia(w, objetivo); // Retira toda arista hacia objetivo.
        Vertice anterior = null, actual = cabeza;
        while (actual != objetivo) {
            anterior = actual;
            actual = actual.sig;
        }
        if (anterior == null) cabeza = objetivo.sig;
        else anterior.sig = objetivo.sig;
        return true;
    }

    // ------------------------------------------------------------------
    // Operaciones sobre relaciones
    // ------------------------------------------------------------------

    /** La amistad es recíproca: se crean dos aristas. */
    public boolean agregarAmistad(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb
                || existe(va, vb, TipoRelacion.AMISTAD)) return false;
        insertar(va, vb, TipoRelacion.AMISTAD);
        insertar(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    /** Seguir es dirigido: se crea una sola arista. */
    public boolean seguir(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb
                || existe(va, vb, TipoRelacion.SEGUIR)) return false;
        insertar(va, vb, TipoRelacion.SEGUIR);
        return true;
    }

    /** Quita ambas aristas de AMISTAD. */
    public boolean eliminarAmistad(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb) return false;
        if (!existe(va, vb, TipoRelacion.AMISTAD)) return false;
        quitar(va, vb, TipoRelacion.AMISTAD);
        quitar(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    /** Quita únicamente la arista SEGUIR de a hacia b. */
    public boolean dejarDeSeguir(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb) return false;
        return quitar(va, vb, TipoRelacion.SEGUIR);
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------

    /** Nombres de los destinos de un tipo de arista, en el orden de la lista. */
    private String nombresSalientes(Vertice v, TipoRelacion tipo) {
        StringBuilder sb = new StringBuilder();
        for (Arista a = v.aristas; a != null; a = a.sig) {
            if (a.tipo != tipo) continue;
            if (sb.length() > 0) sb.append(", ");
            sb.append(a.destino.persona.nombre);
        }
        return sb.length() == 0 ? "(ninguno)" : sb.toString();
    }

    /** Lista de amigos, o null si el usuario no existe. */
    public String listarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : nombresSalientes(v, TipoRelacion.AMISTAD);
    }

    /** Lista de seguidos, o null si el usuario no existe. */
    public String listarSeguidos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : nombresSalientes(v, TipoRelacion.SEGUIR);
    }

    /** Lista de seguidores (requiere revisar toda la red), o null si no existe. */
    public String listarSeguidores(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) return null;
        StringBuilder sb = new StringBuilder();
        for (Vertice w = cabeza; w != null; w = w.sig) {
            if (existe(w, objetivo, TipoRelacion.SEGUIR)) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(w.persona.nombre);
            }
        }
        return sb.length() == 0 ? "(ninguno)" : sb.toString();
    }

    /** Cantidad de amigos, o -1 si el usuario no existe. */
    public int contarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) return -1;
        int n = 0;
        for (Arista a = v.aristas; a != null; a = a.sig)
            if (a.tipo == TipoRelacion.AMISTAD) n++;
        return n;
    }

    /** Número de personas registradas. */
    public int contarPersonas() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.sig) n++;
        return n;
    }

    /** Número de aristas almacenadas (cada amistad aporta dos). */
    public int contarAristas() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.sig)
            for (Arista a = v.aristas; a != null; a = a.sig) n++;
        return n;
    }

    /** Texto con toda la red: cada vértice y sus aristas. */
    public String mostrarRed() {
        StringBuilder sb = new StringBuilder();
        for (Vertice v = cabeza; v != null; v = v.sig) {
            sb.append(v.persona.usuario.toUpperCase()).append(" ")
              .append(v.persona.nombre).append(" -> ");
            if (v.aristas == null) sb.append("(sin relaciones)");
            for (Arista a = v.aristas; a != null; a = a.sig) {
                sb.append("[").append(a.tipo).append(":")
                  .append(a.destino.persona.nombre).append("]");
                if (a.sig != null) sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
