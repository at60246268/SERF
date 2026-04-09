package com.financorp.serf.model.adapter;

/**
 * PATRÓN ADAPTER - Adaptador para Plin
 *
 * Traduce la interfaz de PlinApi a la interfaz PasarelaPago. Cumple RF1 y RF2.
 */
public class PlinAdapter implements PasarelaPago {

    private final PlinApi plinApi;
    private boolean habilitada;

    public PlinAdapter(boolean habilitada) {
        this.plinApi = new PlinApi();
        this.habilitada = habilitada;
    }

    @Override
    public boolean procesarPago(double monto, String detalle) {
        if (!habilitada) {
            System.out.println("[PlinAdapter] Pasarela deshabilitada.");
            return false;
        }
        String referencia = plinApi.generarCobranzaPlin(monto, detalle);
        return plinApi.validarPagoPlin(referencia);
    }

    @Override
    public boolean estaHabilitada() {
        return habilitada;
    }

    @Override
    public String getNombre() {
        return "Plin";
    }

    /** RF2: El administrador puede habilitar o deshabilitar la pasarela. */
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
}
