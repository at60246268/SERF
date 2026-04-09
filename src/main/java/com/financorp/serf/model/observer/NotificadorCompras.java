package com.financorp.serf.model.observer;

import com.financorp.serf.model.proxy.RolUsuario;

/**
 * PATRÓN OBSERVER - Observador concreto: Notificador para área de Compras
 *
 * RF5: Cuando el stock cae del mínimo, notifica al usuario con rol COMPRAS
 * para que gestione la orden de reabastecimiento.
 */
public class NotificadorCompras implements ObservadorStock {

    private final String responsableCompras;
    private final RolUsuario rol = RolUsuario.COMPRAS;

    public NotificadorCompras(String responsableCompras) {
        this.responsableCompras = responsableCompras;
    }

    @Override
    public void notificarStockBajo(String nombreProducto, int stockActual, int stockMinimo) {
        System.out.println("[ALERTA - COMPRAS] " + responsableCompras +
                ": Generar orden de compra para '" + nombreProducto + "'. " +
                "Stock actual: " + stockActual + " | Mínimo: " + stockMinimo +
                ". Favor coordinar reabastecimiento con proveedor.");
    }

    public String getResponsableCompras() {
        return responsableCompras;
    }

    public RolUsuario getRol() {
        return rol;
    }
}
