package com.financorp.serf.model.adapter;

/**
 * PATRÓN ADAPTER - Adaptador para Yape
 *
 * Traduce la interfaz de YapeApi a la interfaz PasarelaPago. Cumple RF1 y RF2.
 */
public class YapeAdapter implements PasarelaPago {

    private final YapeApi yapeApi;
    private final String numeroCelularEmpresa;
    private boolean habilitada;

    public YapeAdapter(String numeroCelularEmpresa, boolean habilitada) {
        this.yapeApi = new YapeApi();
        this.numeroCelularEmpresa = numeroCelularEmpresa;
        this.habilitada = habilitada;
    }

    @Override
    public boolean procesarPago(double monto, String detalle) {
        if (!habilitada) {
            System.out.println("[YapeAdapter] Pasarela deshabilitada.");
            return false;
        }
        return yapeApi.realizarPagoYape(numeroCelularEmpresa, monto);
    }

    @Override
    public boolean estaHabilitada() {
        return habilitada;
    }

    @Override
    public String getNombre() {
        return "Yape";
    }

    /** RF2: El administrador puede habilitar o deshabilitar la pasarela. */
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
}
