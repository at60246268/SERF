package com.financorp.serf.model.adapter;

/**
 * PATRÓN ADAPTER - Interfaz objetivo común para todas las pasarelas de pago
 *
 * Define el contrato unificado que deben cumplir todos los adaptadores.
 * Permite integrar cualquier pasarela externa (PayPal, Yape, Plin) sin cambiar
 * el código cliente. Cumple RF1 y RF2.
 *
 * Principio SOLID Open/Closed: Agregar una nueva pasarela solo requiere
 * crear un nuevo adaptador, sin modificar el código existente.
 */
public interface PasarelaPago {

    /**
     * Procesa un pago con el monto indicado.
     * @param monto   importe a cobrar (en soles PEN)
     * @param detalle descripción de la operación
     * @return true si el pago fue aceptado
     */
    boolean procesarPago(double monto, String detalle);

    /**
     * Verifica si la pasarela está habilitada (RF2).
     */
    boolean estaHabilitada();

    /**
     * Nombre descriptivo de la pasarela (para UI y logs).
     */
    String getNombre();
}
