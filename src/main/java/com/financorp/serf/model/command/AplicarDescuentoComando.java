package com.financorp.serf.model.command;

/**
 * PATRÓN COMMAND - Comando: Aplicar descuento a pedido
 *
 * RF7: Encapsula la aplicación de un descuento porcentual.
 */
public class AplicarDescuentoComando implements ComandoPedido {

    private final Pedido pedido;
    private final double porcentajeDescuento;
    private double descuentoAnterior;
    private Pedido.EstadoPedido estadoAnterior;

    public AplicarDescuentoComando(Pedido pedido, double porcentajeDescuento) {
        this.pedido = pedido;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public void ejecutar() {
        descuentoAnterior = pedido.getDescuento();
        estadoAnterior = pedido.getEstado();
        pedido.setDescuento(porcentajeDescuento);
        pedido.setEstado(Pedido.EstadoPedido.CON_DESCUENTO);
        System.out.println("[Comando] Descuento de " + porcentajeDescuento + "% aplicado: " + pedido);
    }

    @Override
    public void deshacer() {
        pedido.setDescuento(descuentoAnterior);
        pedido.setEstado(estadoAnterior);
        System.out.println("[Comando] Descuento revertido al " + descuentoAnterior + "%");
    }

    @Override
    public String getDescripcion() {
        return "Aplicar descuento " + porcentajeDescuento + "% a pedido #" + pedido.getNumero();
    }
}
