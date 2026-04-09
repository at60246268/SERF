package com.financorp.serf.model.memento;

import com.financorp.serf.model.command.Pedido;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.ArrayList;

/**
 * PATRÓN MEMENTO - Caretaker: custodio de los mementos de pedidos
 *
 * RF8: Guarda el historial de estados de un pedido y permite restaurar
 * cualquier estado anterior mediante la operación "deshacer".
 *
 * El caretaker NO inspecciona ni modifica el contenido de los mementos;
 * solo los almacena y los entrega al pedido cuando se requiere restaurar.
 */
@Component
public class CaretakerPedido {

    private final Deque<PedidoMemento> historialEstados = new ArrayDeque<>();

    /**
     * RF8: Guarda el estado actual del pedido antes de una modificación crítica.
     */
    public void guardarEstado(Pedido pedido) {
        PedidoMemento memento = new PedidoMemento(pedido);
        historialEstados.push(memento);
        System.out.println("[CaretakerPedido] Estado guardado: " + memento);
    }

    /**
     * RF8: Restaura el pedido al último estado guardado.
     */
    public void restaurarUltimoEstado(Pedido pedido) {
        if (historialEstados.isEmpty()) {
            System.out.println("[CaretakerPedido] No hay estados previos para restaurar.");
            return;
        }
        PedidoMemento memento = historialEstados.pop();
        pedido.setPrecio(memento.getPrecio());
        pedido.setDescuento(memento.getDescuento());
        pedido.setEstado(memento.getEstado());
        System.out.println("[CaretakerPedido] Pedido restaurado al estado: " + memento);
    }

    /** Retorna la lista de todos los mementos guardados (para auditoría). */
    public List<PedidoMemento> getHistorialEstados() {
        return new ArrayList<>(historialEstados);
    }

    /** Indica si hay estados anteriores disponibles. */
    public boolean tieneEstadosPrevios() {
        return !historialEstados.isEmpty();
    }
}
