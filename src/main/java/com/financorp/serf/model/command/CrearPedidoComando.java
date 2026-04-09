package com.financorp.serf.model.command;

/**
 * PATRÓN COMMAND - Comando: Crear pedido
 *
 * RF7: Encapsula la creación de un pedido como objeto registrable en el historial.
 */
public class CrearPedidoComando implements ComandoPedido {

    private final Pedido pedido;

    public CrearPedidoComando(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void ejecutar() {
        pedido.setEstado(Pedido.EstadoPedido.CREADO);
        System.out.println("[Comando] Pedido creado: " + pedido);
    }

    @Override
    public void deshacer() {
        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        System.out.println("[Comando] Creación de pedido deshecha: " + pedido.getNumero());
    }

    @Override
    public String getDescripcion() {
        return "Crear pedido #" + pedido.getNumero() + " - Producto: " + pedido.getProducto();
    }
}
