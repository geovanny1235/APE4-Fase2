# Conexión Ambato: APE 4 · Fase 2

Red social dinámica implementada en Java con una **multilista** (lista de adyacencia con nodos propios). Práctica de **Estructura de Datos**, Universidad Técnica de Ambato (ciclo julio–diciembre 2026).

## Contenido

| Archivo | Descripción |
|---|---|
| `ConexionAmbato.java` | Código completo: modelo, operaciones, demostración y pruebas automatizadas |

> El informe final en PDF con el formato APE de la universidad se entrega directamente en la plataforma educativa.

## Diseño

- `Vertice`: una persona y su lista de aristas.
- `Arista`: destino, `TipoRelacion` (`AMISTAD` o `SEGUIR`) y siguiente arista.
- `AMISTAD` se guarda en ambos sentidos; `SEGUIR` solo de quien sigue hacia el seguido.
- No se usan `HashMap`, `HashSet`, `ArrayList` ni `LinkedList`.

## Operaciones

`registrarPersona`, `eliminarPersona`, `agregarAmistad`, `seguir`, `eliminarAmistad`, `dejarDeSeguir`, `listarAmigos`, `listarSeguidos`, `listarSeguidores`, `contarAmigos` y `mostrarRed`.

## Ejecución

Requiere JDK 11 o superior (probado con OpenJDK 21):

```bash
java ConexionAmbato.java
