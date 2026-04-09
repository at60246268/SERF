package com.financorp.serf.model.strategy;

/**
 * PATRÓN STRATEGY - Estrategia: Precio estándar
 *
 * RF9: Devuelve el precio base sin modificación. Política por defecto del sistema.
 */
public class PrecioEstandar implements EstrategiaPrecio {

    @Override
    public double calcularPrecio(double precioBase) {
        System.out.println("[PrecioEstandar] Precio sin modificación: S/. " + precioBase);
        return precioBase;
    }

    @Override
    public String getNombre() {
        return "Precio Estándar";
    }
}
