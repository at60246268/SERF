package com.financorp.serf.model.observer;

import com.financorp.serf.model.proxy.RolUsuario;

/**
 * PATRÓN OBSERVER - Observador concreto: Notificador para Gerente
 *
 * RF5: Cuando el stock cae del mínimo, notifica al usuario con rol GERENTE.
 */
public class NotificadorGerente implements ObservadorStock {

    private final String nombreGerente;
    private final RolUsuario rol = RolUsuario.GERENTE;

    public NotificadorGerente(String nombreGerente) {
        this.nombreGerente = nombreGerente;
    }

    @Override
    public void notificarStockBajo(String nombreProducto, int stockActual, int stockMinimo) {
        System.out.println("[ALERTA - GERENTE] " + nombreGerente +
                ": El producto '" + nombreProducto + "' tiene stock " + stockActual +
                " unidades (mínimo configurado: " + stockMinimo + "). ¡Requiere reposición urgente!");
    }

    public String getNombreGerente() {
        return nombreGerente;
    }

    public RolUsuario getRol() {
        return rol;
    }
}
