package com.financorp.serf.model.adapter;

/**
 * PATRÓN ADAPTER - Clase incompatible de Yape (API externa simulada)
 *
 * Representa la interfaz propia del SDK de Yape que no es compatible
 * con nuestra interfaz PasarelaPago.
 */
public class YapeApi {

    public boolean realizarPagoYape(String numeroCelular, double importe) {
        // Simula llamada al SDK real de Yape
        System.out.println("[Yape API] Pago de S/. " + importe + " al número: " + numeroCelular);
        return true;
    }
}
