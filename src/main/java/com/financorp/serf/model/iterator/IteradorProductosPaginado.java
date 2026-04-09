package com.financorp.serf.model.iterator;

import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.enums.Categoria;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PATRÓN ITERATOR - Iterador concreto con paginación y filtros
 *
 * RF11: Recorre el catálogo de productos con soporte de paginación y filtros.
 * RF12: Muestra los productos de forma ordenada sin exponer la colección interna.
 *
 * Principio SOLID:
 *   - Single Responsibility: solo gestiona el recorrido paginado del catálogo
 *   - Open/Closed: nuevos criterios de filtro se agregan sin modificar la interfaz
 */
public class IteradorProductosPaginado implements IteradorCatalogo<Producto> {

    private final List<Producto> productos;
    private final int tamanioPagina;
    private int indiceActual;

    /**
     * Constructor sin filtro: itera sobre todos los productos.
     *
     * @param productos    lista completa del catálogo
     * @param tamanioPagina cantidad de productos por página (RF11)
     */
    public IteradorProductosPaginado(List<Producto> productos, int tamanioPagina) {
        this.productos = List.copyOf(productos);   // RF12: copia defensiva
        this.tamanioPagina = tamanioPagina;
        this.indiceActual = 0;
    }

    /**
     * Constructor con filtro por categoría (RF11: filtros de búsqueda).
     *
     * @param productos     lista completa del catálogo
     * @param tamanioPagina cantidad de productos por página
     * @param categoria     categoría por la que filtrar
     */
    public IteradorProductosPaginado(List<Producto> productos, int tamanioPagina, Categoria categoria) {
        this.productos = productos.stream()
                .filter(p -> p.getCategoria() == categoria)
                .collect(Collectors.toUnmodifiableList());  // RF12: colección interna protegida
        this.tamanioPagina = tamanioPagina;
        this.indiceActual = 0;
    }

    @Override
    public boolean tieneSiguiente() {
        return indiceActual < productos.size();
    }

    @Override
    public Producto siguiente() {
        if (!tieneSiguiente()) {
            throw new java.util.NoSuchElementException("No hay más productos en el catálogo.");
        }
        return productos.get(indiceActual++);
    }

    @Override
    public void reiniciar() {
        this.indiceActual = 0;
    }

    /**
     * RF11: Retorna una "página" completa de productos.
     *
     * @return lista de productos de la página actual; vacía si no hay más
     */
    public List<Producto> siguientePagina() {
        if (!tieneSiguiente()) return List.of();

        int fin = Math.min(indiceActual + tamanioPagina, productos.size());
        List<Producto> pagina = productos.subList(indiceActual, fin);
        indiceActual = fin;
        return List.copyOf(pagina);   // RF12: no se expone la lista interna
    }

    /** Página actual (base 1). */
    public int getPaginaActual() {
        return (indiceActual / tamanioPagina) + 1;
    }

    /** Total de páginas disponibles. */
    public int getTotalPaginas() {
        return (int) Math.ceil((double) productos.size() / tamanioPagina);
    }

    /** Total de productos (puede ser menor que el catálogo completo si hay filtro). */
    public int getTotalProductos() {
        return productos.size();
    }
}
