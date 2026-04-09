package com.financorp.serf.model.command;

/**
 * PATRÓN COMMAND - Modelo de pedido que es modificado por los comandos
 *
 * Representa un pedido en el sistema. Los comandos operan sobre este objeto
 * encapsulando el estado antes y después de cada operación.
 */
public class Pedido {

    public enum EstadoPedido {
        CREADO, PROCESADO, CON_DESCUENTO, CANCELADO
    }

    private String numero;
    private String producto;
    private int cantidad;
    private double precio;
    private double descuento;
    private EstadoPedido estado;

    public Pedido(String numero, String producto, int cantidad, double precio) {
        this.numero = numero;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = 0.0;
        this.estado = EstadoPedido.CREADO;
    }

    public String getNumero() { return numero; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
    public double getDescuento() { return descuento; }
    public EstadoPedido getEstado() { return estado; }

    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public void setDescuento(double descuento) { this.descuento = descuento; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getTotal() {
        return (precio * cantidad) * (1 - descuento / 100.0);
    }

    @Override
    public String toString() {
        return "Pedido{numero='" + numero + "', producto='" + producto +
               "', cantidad=" + cantidad + ", precio=" + precio +
               ", descuento=" + descuento + "%, estado=" + estado +
               ", total=S/. " + String.format("%.2f", getTotal()) + "}";
    }
}
