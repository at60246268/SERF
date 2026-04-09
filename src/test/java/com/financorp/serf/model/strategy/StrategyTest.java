package com.financorp.serf.model.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Patrón Strategy (RF9, RF10)
 *
 * Verifica que las tres estrategias de precio calculen correctamente
 * y que el administrador pueda cambiarlas en tiempo de ejecución.
 */
@DisplayName("Patrón Strategy - Políticas de Precios")
class StrategyTest {

    private CalculadoraPrecio calculadora;
    private static final double PRECIO_BASE = 1000.0;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraPrecio();
    }

    // ─── RF9: Precio estándar ─────────────────────────────────────────────

    @Test
    @DisplayName("RF9 - Precio estándar devuelve el precio base sin cambios")
    void precioEstandarDevuelvePrecioBase() {
        calculadora.setEstrategia(new PrecioEstandar());
        assertEquals(PRECIO_BASE, calculadora.calcularPrecio(PRECIO_BASE), 0.01);
    }

    @Test
    @DisplayName("RF9 - Nombre de estrategia estándar es correcto")
    void nombreEstrategiaEstandar() {
        PrecioEstandar estrategia = new PrecioEstandar();
        assertEquals("Precio Estándar", estrategia.getNombre());
    }

    // ─── RF9: Precio con descuento porcentual ─────────────────────────────

    @Test
    @DisplayName("RF9 - Descuento 20% calcula precio correcto")
    void descuentoVeinteYCinco() {
        calculadora.setEstrategia(new PrecioConDescuento(20.0));
        assertEquals(800.0, calculadora.calcularPrecio(PRECIO_BASE), 0.01);
    }

    @Test
    @DisplayName("RF9 - Descuento 0% equivale al precio base")
    void descuentoCeroEquivalePrecioBase() {
        calculadora.setEstrategia(new PrecioConDescuento(0.0));
        assertEquals(PRECIO_BASE, calculadora.calcularPrecio(PRECIO_BASE), 0.01);
    }

    @Test
    @DisplayName("RF9 - Descuento 100% resulta en precio cero")
    void descuentoTotalResultaPrecioCero() {
        calculadora.setEstrategia(new PrecioConDescuento(100.0));
        assertEquals(0.0, calculadora.calcularPrecio(PRECIO_BASE), 0.01);
    }

    @Test
    @DisplayName("RF9 - Porcentaje de descuento negativo lanza excepción")
    void descuentoNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new PrecioConDescuento(-5.0));
    }

    @Test
    @DisplayName("RF9 - Porcentaje de descuento mayor a 100 lanza excepción")
    void descuentoMayorACienLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new PrecioConDescuento(101.0));
    }

    // ─── RF9: Precio dinámico ─────────────────────────────────────────────

    @Test
    @DisplayName("RF9 - Precio dinámico ajusta según factor configurado")
    void precioDinamicoAjustaSegunFactor() {
        // Factor demanda alta = 1.20, factor baja = 0.90
        PrecioDinamico dinamico = new PrecioDinamico(1.20, 0.90);
        double resultado = dinamico.calcularPrecio(PRECIO_BASE);
        // El resultado debe ser o 1200 (pico) o 900 (valle)
        assertTrue(resultado == 1200.0 || resultado == 900.0,
                "El precio dinámico debe ser 1200 (pico) o 900 (valle)");
    }

    // ─── RF10: Cambio de estrategia en caliente ───────────────────────────

    @Test
    @DisplayName("RF10 - Administrador cambia estrategia estándar → descuento")
    void administradorCambiaEstrategia() {
        calculadora.setEstrategia(new PrecioEstandar());
        assertEquals(PRECIO_BASE, calculadora.calcularPrecio(PRECIO_BASE), 0.01);

        calculadora.setEstrategia(new PrecioConDescuento(10.0));
        assertEquals(900.0, calculadora.calcularPrecio(PRECIO_BASE), 0.01);
    }

    @Test
    @DisplayName("RF10 - Nombre de estrategia activa se actualiza correctamente")
    void nombreEstrategiaActivaSeActualiza() {
        calculadora.setEstrategia(new PrecioConDescuento(15.0));
        assertTrue(calculadora.getEstrategiaActiva().contains("15.0"),
                "El nombre debe reflejar el porcentaje configurado");
    }

    @Test
    @DisplayName("RF10 - Estrategia por defecto es Precio Estándar")
    void estrategiaDefaultEsEstandar() {
        assertEquals("Precio Estándar", calculadora.getEstrategiaActiva());
    }
}
