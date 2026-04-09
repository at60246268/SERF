package com.financorp.serf.model.adapter;

/**
 * PATRÓN ADAPTER - Clase incompatible de PayPal (API externa simulada)
 *
 * Representa la interfaz propia del SDK de PayPal que no es compatible
 * con nuestra interfaz PasarelaPago. El adaptador traduce las llamadas.
 */
public class PayPalApi {

    public String iniciarTransaccion(double amount, String currency, String description) {
        // Simula llamada al SDK real de PayPal
        System.out.println("[PayPal API] Iniciando transacción: " + amount + " " + currency);
        return "PAYPAL_TXN_" + System.currentTimeMillis();
    }

    public boolean confirmarTransaccion(String transactionId) {
        System.out.println("[PayPal API] Confirmando transacción: " + transactionId);
        return transactionId != null && !transactionId.isEmpty();
    }
}
