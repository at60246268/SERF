package com.financorp.serf.model.strategy;

import org.springframework.stereotype.Component;

/**
 * PATRÓN STRATEGY - Contexto: Calculadora de precios
 *
 * RF10: El administrador selecciona o cambia la estrategia de precios
 * desde la configuración del sistema. El contexto aplica la estrategia
 * activa sin conocer su implementación interna.
 *
 * Principio SOLID:
 *   - Single Responsibility: solo delega el cálculo a la estrategia activa
 *   - Open/Closed: cambiar la política no modifica esta clase
 */
@Component
public class CalculadoraPrecio {

    private EstrategiaPrecio estrategiaActiva;

    public CalculadoraPrecio() {
        // RF10: Estrategia por defecto
        this.estrategiaActiva = new PrecioEstandar();
    }

    /**
     * RF10: El administrador cambia la estrategia desde configuración.
     */
    public void setEstrategia(EstrategiaPrecio estrategia) {
        this.estrategiaActiva = estrategia;
        System.out.println("[CalculadoraPrecio] Estrategia cambiada a: " + estrategia.getNombre());
    }

    /**
     * RF9: Calcula el precio usando la estrategia activa.
     */
    public double calcularPrecio(double precioBase) {
        return estrategiaActiva.calcularPrecio(precioBase);
    }

    public String getEstrategiaActiva() {
        return estrategiaActiva.getNombre();
    }
}
