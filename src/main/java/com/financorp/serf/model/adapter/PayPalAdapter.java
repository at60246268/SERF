package com.financorp.serf.model.adapter;

/**
 * PATRÓN ADAPTER - Adaptador para PayPal
 *
 * Traduce la interfaz de PayPalApi (externa/incompatible) a la interfaz
 * PasarelaPago (interna). El cliente solo conoce PasarelaPago y nunca
 * interactúa directamente con el SDK de PayPal. Cumple RF1 y RF2.
 */
public class PayPalAdapter implements PasarelaPago {

    private final PayPalApi payPalApi;
    private boolean habilitada;

    public PayPalAdapter(boolean habilitada) {
        this.payPalApi = new PayPalApi();
        this.habilitada = habilitada;
    }

    @Override
    public boolean procesarPago(double monto, String detalle) {
        if (!habilitada) {
            System.out.println("[PayPalAdapter] Pasarela deshabilitada.");
            return false;
        }
        String txId = payPalApi.iniciarTransaccion(monto, "PEN", detalle);
        return payPalApi.confirmarTransaccion(txId);
    }

    @Override
    public boolean estaHabilitada() {
        return habilitada;
    }

    @Override
    public String getNombre() {
        return "PayPal";
    }

    /** RF2: El administrador puede habilitar o deshabilitar la pasarela. */
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
}
