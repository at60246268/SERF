package com.financorp.serf.model.strategy;

import java.time.LocalTime;

/**
 * PATRÓN STRATEGY - Estrategia: Precio dinámico (por demanda / temporada)
 *
 * RF9: Ajusta el precio según la hora del día y nivel de demanda simulado.
 * En horas pico (9-12h y 18-21h) el precio sube; en horas bajas baja.
 */
public class PrecioDinamico implements EstrategiaPrecio {

    private final double factorDemandaAlta;   // multiplica precio en horas pico
    private final double factorDemandaBaja;   // multiplica precio en horas valle

    public PrecioDinamico(double factorDemandaAlta, double factorDemandaBaja) {
        this.factorDemandaAlta = factorDemandaAlta;
        this.factorDemandaBaja = factorDemandaBaja;
    }

    /** Constructor con factores predeterminados: +20% pico, -10% valle. */
    public PrecioDinamico() {
        this(1.20, 0.90);
    }

    @Override
    public double calcularPrecio(double precioBase) {
        double factor = esPicoDemanda() ? factorDemandaAlta : factorDemandaBaja;
        double precioFinal = precioBase * factor;
        System.out.println("[PrecioDinamico] Base: S/. " + precioBase +
                " | Factor: " + factor + " | Final: S/. " +
                String.format("%.2f", precioFinal));
        return precioFinal;
    }

    @Override
    public String getNombre() {
        return "Precio Dinámico (demanda/temporada)";
    }

    private boolean esPicoDemanda() {
        LocalTime ahora = LocalTime.now();
        return (ahora.isAfter(LocalTime.of(9, 0)) && ahora.isBefore(LocalTime.of(12, 0))) ||
               (ahora.isAfter(LocalTime.of(18, 0)) && ahora.isBefore(LocalTime.of(21, 0)));
    }
}
