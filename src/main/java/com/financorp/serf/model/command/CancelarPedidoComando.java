package com.financorp.serf.model.command;

/**
 * PATRÓN COMMAND - Comando: Cancelar pedido
 *
 * RF7: Encapsula la cancelación de un pedido.
 */
public class CancelarPedidoComando implements ComandoPedido {

    private final Pedido pedido;
    private Pedido.EstadoPedido estadoAnterior;

    public CancelarPedidoComando(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void ejecutar() {
        estadoAnterior = pedido.getEstado();
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        System.out.println("[Comando] Pedido cancelado: " + pedido.getNumero());
    }

    @Override
    public void deshacer() {
        pedido.setEstado(estadoAnterior);
        System.out.println("[Comando] Cancelación deshecha, pedido restaurado a: " + estadoAnterior);
    }

    @Override
    public String getDescripcion() {
        return "Cancelar pedido #" + pedido.getNumero();
    }
}
