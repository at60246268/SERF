package com.financorp.serf.model.strategy;

/**
 * PATRÓN STRATEGY - Estrategia: Precio con descuento porcentual
 *
 * RF9: Aplica un descuento porcentual fijo sobre el precio base.
 * RF10: El administrador configura el porcentaje desde el sistema.
 */
public class PrecioConDescuento implements EstrategiaPrecio {

    private final double porcentajeDescuento;

    /**
     * @param porcentajeDescuento porcentaje a descontar (ej: 15.0 para 15%)
     */
    public PrecioConDescuento(double porcentajeDescuento) {
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double calcularPrecio(double precioBase) {
        double precioFinal = precioBase * (1 - porcentajeDescuento / 100.0);
        System.out.println("[PrecioConDescuento] Base: S/. " + precioBase +
                " | Descuento: " + porcentajeDescuento + "% | Final: S/. " +
                String.format("%.2f", precioFinal));
        return precioFinal;
    }

    @Override
    public String getNombre() {
        return "Descuento Porcentual (" + porcentajeDescuento + "%)";
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}
