package com.financorp.serf.model.enums;

/**
 * Enum para los métodos de pago disponibles
 * Principio SOLID: Single Responsibility - Solo define métodos de pago
 */
public enum MetodoPago {
    EFECTIVO("Efectivo"),
    TARJETA_CREDITO("Tarjeta de Crédito"),
    TARJETA_DEBITO("Tarjeta de Débito"),
    TRANSFERENCIA("Transferencia Bancaria"),
    PAYPAL("PayPal"),
    CRIPTOMONEDA("Criptomoneda");
    
    private final String descripcion;
    
    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
