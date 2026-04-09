package com.financorp.serf.model.memento;

import com.financorp.serf.model.command.Pedido;

/**
 * PATRÓN MEMENTO - Instantánea del estado de un pedido
 *
 * RF8: Guarda una copia inmutable del estado de un pedido en un momento dado,
 * permitiendo restaurarlo posteriormente. El exterior no puede modificar
 * el estado guardado (encapsulación preservada).
 */
public class PedidoMemento {

    private final String numero;
    private final double precio;
    private final double descuento;
    private final Pedido.EstadoPedido estado;
    private final String fechaGuardado;

    public PedidoMemento(Pedido pedido) {
        this.numero    = pedido.getNumero();
        this.precio    = pedido.getPrecio();
        this.descuento = pedido.getDescuento();
        this.estado    = pedido.getEstado();
        this.fechaGuardado = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public String getNumero()    { return numero; }
    public double getPrecio()    { return precio; }
    public double getDescuento() { return descuento; }
    public Pedido.EstadoPedido getEstado() { return estado; }
    public String getFechaGuardado()       { return fechaGuardado; }

    @Override
    public String toString() {
        return "Memento[" + fechaGuardado + "] Pedido #" + numero +
               " | Estado: " + estado + " | Precio: " + precio +
               " | Descuento: " + descuento + "%";
    }
}
