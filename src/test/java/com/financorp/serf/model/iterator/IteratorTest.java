package com.financorp.serf.model.iterator;

import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.enums.Categoria;
import com.financorp.serf.model.enums.Moneda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Patrón Iterator (RF11, RF12)
 *
 * Verifica que el iterador recorra el catálogo con paginación y filtros
 * sin exponer la estructura interna de la colección.
 */
@DisplayName("Patrón Iterator - Catálogo de Productos")
class IteratorTest {

    private List<Producto> productos;
    private CatalogoProductos catalogo;

    @BeforeEach
    void setUp() {
        productos = new ArrayList<>();
        // 5 laptops + 3 accesorios = 8 productos en total
        for (int i = 1; i <= 5; i++) {
            Producto p = new Producto();
            p.setNombre("Laptop " + i);
            p.setCategoria(Categoria.LAPTOP);
            p.setCodigo("LAP-00" + i);
            p.setCostoImportacion(1000.0);
            p.setMonedaImportacion(Moneda.USD);
            p.setPrecioVenta(1500.0);
            p.setMonedaVenta(Moneda.PEN);
            p.setFechaImportacion(LocalDate.now());
            productos.add(p);
        }
        for (int i = 1; i <= 3; i++) {
            Producto p = new Producto();
            p.setNombre("Accesorio " + i);
            p.setCategoria(Categoria.ACCESORIO);
            p.setCodigo("ACC-00" + i);
            p.setCostoImportacion(50.0);
            p.setMonedaImportacion(Moneda.USD);
            p.setPrecioVenta(80.0);
            p.setMonedaVenta(Moneda.PEN);
            p.setFechaImportacion(LocalDate.now());
            productos.add(p);
        }
        catalogo = new CatalogoProductos(productos);
    }

    // ─── RF11: Paginación ────────────────────────────────────────────────

    @Test
    @DisplayName("RF11 - Primera página devuelve 3 productos con tamPagina=3")
    void primeraPaginaDevuelveTresProductos() {
        IteradorProductosPaginado it = new IteradorProductosPaginado(productos, 3);
        List<Producto> pagina = it.siguientePagina();
        assertEquals(3, pagina.size());
    }

    @Test
    @DisplayName("RF11 - Total de páginas calculado correctamente")
    void totalPaginasCalculadoCorrecto() {
        IteradorProductosPaginado it = new IteradorProductosPaginado(productos, 3);
        // 8 productos / 3 por página = 3 páginas (ceil)
        assertEquals(3, it.getTotalPaginas());
    }

    @Test
    @DisplayName("RF11 - Última página puede tener menos elementos")
    void ultimaPaginaPuedeTenerMenosElementos() {
        IteradorProductosPaginado it = new IteradorProductosPaginado(productos, 3);
        it.siguientePagina(); // pág 1
        it.siguientePagina(); // pág 2
        List<Producto> ultimaPagina = it.siguientePagina(); // pág 3
        assertEquals(2, ultimaPagina.size(), "La última página tiene 2 elementos (8 mod 3)");
    }

    // ─── RF11: Filtros de búsqueda ────────────────────────────────────────

    @Test
    @DisplayName("RF11 - Filtro por categoría LAPTOP devuelve solo laptops")
    void filtroLaptopDevuelveSoloLaptops() {
        IteradorProductosPaginado it = new IteradorProductosPaginado(productos, 10, Categoria.LAPTOP);
        List<Producto> pagina = it.siguientePagina();
        assertEquals(5, pagina.size());
        assertTrue(pagina.stream().allMatch(p -> p.getCategoria() == Categoria.LAPTOP));
    }

    @Test
    @DisplayName("RF11 - Filtro por categoría ACCESORIO devuelve solo accesorios")
    void filtroAccesorioDevuelveSoloAccesorios() {
        IteradorProductosPaginado it = new IteradorProductosPaginado(productos, 10, Categoria.ACCESORIO);
        assertEquals(3, it.getTotalProductos());
    }

    // ─── RF12: Sin exposición de estructura interna ───────────────────────

    @Test
    @DisplayName("RF12 - CatalogoProductos crea iterador sin exponer la lista")
    void catalogoCreaIteradorSinExponerLista() {
        IteradorCatalogo<Producto> it = catalogo.crearIterador(5);
        assertNotNull(it, "El catalogo debe crear un iterador");
        assertTrue(it.tieneSiguiente());
    }

    @Test
    @DisplayName("RF12 - Reiniciar iterador permite recorrer desde el inicio")
    void reiniciarIteradorRecorreDesdeInicio() {
        IteradorProductosPaginado it = new IteradorProductosPaginado(productos, 5);
        it.siguientePagina();
        it.reiniciar();
        assertEquals(1, it.getPaginaActual(), "Tras reiniciar debe estar en página 1");
    }

    @Test
    @DisplayName("RF12 - siguiente() lanza excepción cuando no hay más elementos")
    void siguienteSinElementosLanzaExcepcion() {
        IteradorProductosPaginado it = new IteradorProductosPaginado(productos, 10);
        it.siguientePagina(); // consume todos
        assertThrows(NoSuchElementException.class, it::siguiente);
    }

    @Test
    @DisplayName("RF11 - getTotalProductos refleja el catálogo completo")
    void totalProductosCorrecto() {
        assertEquals(8, catalogo.getTotalProductos());
    }
}
