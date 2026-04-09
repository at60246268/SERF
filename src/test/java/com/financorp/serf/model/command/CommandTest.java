package com.financorp.serf.model.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Patrón Command (RF7)
 *
 * Verifica que las acciones de pedido se encapsulen como comandos,
 * se registren en el historial y puedan deshacerse.
 */
@DisplayName("Patrón Command - Historial de Pedidos")
class CommandTest {

    private Pedido pedido;
    private HistorialPedidos historial;

    @BeforeEach
    void setUp() {
        pedido = new Pedido("PED-001", "Laptop Dell", 2, 3500.0);
        historial = new HistorialPedidos();
    }

    // ─── RF7: Encapsular acciones como comandos ────────────────────────────

    @Test
    @DisplayName("RF7 - Crear pedido cambia estado a CREADO")
    void crearPedidoCambiaEstado() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        assertEquals(Pedido.EstadoPedido.CREADO, pedido.getEstado());
    }

    @Test
    @DisplayName("RF7 - Procesar pedido cambia estado a PROCESADO")
    void procesarPedidoCambiaEstado() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        historial.ejecutar(new ProcesarPedidoComando(pedido));
        assertEquals(Pedido.EstadoPedido.PROCESADO, pedido.getEstado());
    }

    @Test
    @DisplayName("RF7 - Aplicar descuento cambia estado y ajusta precio")
    void aplicarDescuentoModificaPedido() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        historial.ejecutar(new AplicarDescuentoComando(pedido, 10.0));
        assertEquals(Pedido.EstadoPedido.CON_DESCUENTO, pedido.getEstado());
        assertEquals(10.0, pedido.getDescuento());
        assertEquals(6300.0, pedido.getTotal(), 0.01);
    }

    @Test
    @DisplayName("RF7 - Cancelar pedido cambia estado a CANCELADO")
    void cancelarPedidoCambiaEstado() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        historial.ejecutar(new CancelarPedidoComando(pedido));
        assertEquals(Pedido.EstadoPedido.CANCELADO, pedido.getEstado());
    }

    @Test
    @DisplayName("RF7 - Acciones quedan registradas en el historial")
    void accionesSeRegistranEnHistorial() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        historial.ejecutar(new ProcesarPedidoComando(pedido));
        assertEquals(2, historial.getLog().size(),
                "Deben registrarse 2 entradas en el historial");
    }

    // ─── RF7: Deshacer acciones ────────────────────────────────────────────

    @Test
    @DisplayName("RF7 - Deshacer procesamiento revierte al estado anterior")
    void deshacerProcesamientoRevierteEstado() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        historial.ejecutar(new ProcesarPedidoComando(pedido));
        historial.deshacer(); // revierte PROCESADO → CREADO
        assertEquals(Pedido.EstadoPedido.CREADO, pedido.getEstado());
    }

    @Test
    @DisplayName("RF7 - Deshacer descuento restaura porcentaje anterior")
    void deshacerDescuentoRestauraPorcentaje() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        historial.ejecutar(new AplicarDescuentoComando(pedido, 15.0));
        historial.deshacer();
        assertEquals(0.0, pedido.getDescuento(), 0.01,
                "El descuento debe restaurarse a 0 tras deshacer");
    }

    @Test
    @DisplayName("RF7 - Deshacer sin historial no lanza excepción")
    void deshacerSinHistorialNoLanzaExcepcion() {
        assertDoesNotThrow(() -> historial.deshacer(),
                "Intentar deshacer sin historial no debe lanzar excepción");
    }

    @Test
    @DisplayName("RF7 - Deshacer registra entrada en el log")
    void deshacerRegistraEnLog() {
        historial.ejecutar(new CrearPedidoComando(pedido));
        historial.deshacer();
        assertTrue(historial.getLog().stream().anyMatch(e -> e.contains("DESHECHO")),
                "El log debe contener una entrada DESHECHO");
    }
}
