package com.financorp.serf.model.iterator;

import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.enums.Categoria;

import java.util.List;

/**
 * PATRÓN ITERATOR - Colección iterable: Catálogo de productos
 *
 * RF11: Actúa como la colección que crea el iterador adecuado.
 * RF12: Oculta la estructura interna de la lista de productos al cliente.
 *
 * El cliente solo pide un iterador (con o sin filtro) y lo utiliza,
 * sin conocer nunca que internamente hay una ArrayList, JPA list, etc.
 */
public class CatalogoProductos {

    private final List<Producto> productos;
    private final int tamanioPaginaPorDefecto = 10;

    public CatalogoProductos(List<Producto> productos) {
        this.productos = productos;
    }

    /**
     * RF11: Crea un iterador paginado sin filtro.
     */
    public IteradorCatalogo<Producto> crearIterador() {
        return new IteradorProductosPaginado(productos, tamanioPaginaPorDefecto);
    }

    /**
     * RF11: Crea un iterador paginado con tamaño de página personalizado.
     */
    public IteradorCatalogo<Producto> crearIterador(int tamanioPagina) {
        return new IteradorProductosPaginado(productos, tamanioPagina);
    }

    /**
     * RF11: Crea un iterador paginado filtrado por categoría.
     */
    public IteradorProductosPaginado crearIteradorConFiltro(Categoria categoria, int tamanioPagina) {
        return new IteradorProductosPaginado(productos, tamanioPagina, categoria);
    }

    /** Total de productos en el catálogo. RF12: sin exponer la lista. */
    public int getTotalProductos() {
        return productos.size();
    }
}
