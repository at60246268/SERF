package com.financorp.serf.model.adapter;

/**
 * PATRÓN ADAPTER - Clase incompatible de Plin (API externa simulada)
 */
public class PlinApi {

    public String generarCobranzaPlin(double monto, String concepto) {
        System.out.println("[Plin API] Generando cobranza de S/. " + monto + " - " + concepto);
        return "PLIN_REF_" + System.currentTimeMillis();
    }

    public boolean validarPagoPlin(String referencia) {
        System.out.println("[Plin API] Validando pago con referencia: " + referencia);
        return referencia != null && referencia.startsWith("PLIN_REF_");
    }
}
