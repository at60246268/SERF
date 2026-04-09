package com.financorp.serf.model.memento;

import com.financorp.serf.model.command.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Patrón Memento (RF8)
 *
 * Verifica que el caretaker guarde y restaure correctamente
 * el estado anterior de un pedido.
 */
@DisplayName("Patrón Memento - Restauración de Pedidos")
class MementoTest {

    private Pedido pedido;
    private CaretakerPedido caretaker;

    @BeforeEach
    void setUp() {
        pedido = new Pedido("PED-002", "Monitor LG", 1, 1200.0);
        caretaker = new CaretakerPedido();
    }

    // ─── RF8: Guardar y restaurar estado ─────────────────────────────────

    @Test
    @DisplayName("RF8 - Guardar estado crea un memento correctamente")
    void guardarEstadoCreaMemento() {
        caretaker.guardarEstado(pedido);
        assertTrue(caretaker.tieneEstadosPrevios(),
                "Debe haber al menos un estado guardado");
    }

    @Test
    @DisplayName("RF8 - Restaurar estado revierte el precio modificado")
    void restaurarEstadoReviertePrecios() {
        caretaker.guardarEstado(pedido); // guarda precio=1200
        pedido.setPrecio(950.0);         // modifica precio
        caretaker.restaurarUltimoEstado(pedido);
        assertEquals(1200.0, pedido.getPrecio(), 0.01,
                "El precio debe restaurarse al valor guardado");
    }

    @Test
    @DisplayName("RF8 - Restaurar estado revierte el descuento aplicado")
    void restaurarEstadoRevierteDescuento() {
        caretaker.guardarEstado(pedido); // descuento=0
        pedido.setDescuento(20.0);
        caretaker.restaurarUltimoEstado(pedido);
        assertEquals(0.0, pedido.getDescuento(), 0.01);
    }

    @Test
    @DisplayName("RF8 - Restaurar estado revierte el cambio de estado del pedido")
    void restaurarEstadoRevierteEstadoPedido() {
        caretaker.guardarEstado(pedido); // estado=CREADO
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        caretaker.restaurarUltimoEstado(pedido);
        assertEquals(Pedido.EstadoPedido.CREADO, pedido.getEstado());
    }

    @Test
    @DisplayName("RF8 - Múltiples estados guardados se restauran en orden LIFO")
    void multiplesEstadosSeRestauranLIFO() {
        caretaker.guardarEstado(pedido); // estado1: precio=1200
        pedido.setPrecio(1000.0);
        caretaker.guardarEstado(pedido); // estado2: precio=1000
        pedido.setPrecio(800.0);         // actual: precio=800

        caretaker.restaurarUltimoEstado(pedido); // restaura estado2
        assertEquals(1000.0, pedido.getPrecio(), 0.01);

        caretaker.restaurarUltimoEstado(pedido); // restaura estado1
        assertEquals(1200.0, pedido.getPrecio(), 0.01);
    }

    @Test
    @DisplayName("RF8 - Restaurar sin estado previo no lanza excepción")
    void restaurarSinEstadoPrevioNoLanzaExcepcion() {
        assertDoesNotThrow(() -> caretaker.restaurarUltimoEstado(pedido),
                "No debe lanzar excepción si no hay mementos guardados");
    }

    @Test
    @DisplayName("RF8 - El memento preserva la fecha de creación")
    void mementoPreservaFecha() {
        caretaker.guardarEstado(pedido);
        PedidoMemento memento = caretaker.getHistorialEstados().get(0);
        assertNotNull(memento.getFechaGuardado());
        assertFalse(memento.getFechaGuardado().isBlank());
    }
}
