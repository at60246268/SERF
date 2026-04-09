package com.financorp.serf.model.strategy;

/**
 * PATRÓN STRATEGY - Interfaz de estrategia de precios
 *
 * RF9: Define el contrato para las distintas políticas de cálculo de precio.
 * El sistema puede cambiar la estrategia en tiempo de ejecución (RF10)
 * sin modificar el código que calcula el precio.
 *
 * Principio SOLID:
 *   - Open/Closed: nuevas estrategias = nueva clase, sin modificar clientes
 *   - Dependency Inversion: el cliente depende de esta abstracción
 */
public interface EstrategiaPrecio {

    /**
     * Calcula el precio final aplicando la política de la estrategia.
     *
     * @param precioBase precio base del producto en soles
     * @return precio final calculado
     */
    double calcularPrecio(double precioBase);

    /** Nombre descriptivo de la estrategia (para UI). */
    String getNombre();
}
