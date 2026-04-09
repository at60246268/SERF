package com.financorp.serf.model.command;

/**
 * PATRÓN COMMAND - Comando: Procesar pedido
 *
 * RF7: Encapsula el procesamiento de un pedido.
 */
public class ProcesarPedidoComando implements ComandoPedido {

    private final Pedido pedido;
    private Pedido.EstadoPedido estadoAnterior;

    public ProcesarPedidoComando(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void ejecutar() {
        estadoAnterior = pedido.getEstado();
        pedido.setEstado(Pedido.EstadoPedido.PROCESADO);
        System.out.println("[Comando] Pedido procesado: " + pedido);
    }

    @Override
    public void deshacer() {
        pedido.setEstado(estadoAnterior);
        System.out.println("[Comando] Procesamiento deshecho, pedido restaurado a: " + estadoAnterior);
    }

    @Override
    public String getDescripcion() {
        return "Procesar pedido #" + pedido.getNumero();
    }
}
