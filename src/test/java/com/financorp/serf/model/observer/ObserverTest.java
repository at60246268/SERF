package com.financorp.serf.model.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Patrón Observer (RF5, RF6)
 *
 * Verifica que el GestorInventario notifique a los observadores
 * cuando el stock cae por debajo del mínimo configurable.
 */
@DisplayName("Patrón Observer - Alertas de Stock")
class ObserverTest {

    private GestorInventario gestor;
    private List<String> alertasRecibidas;

    @BeforeEach
    void setUp() {
        gestor = new GestorInventario();
        alertasRecibidas = new ArrayList<>();

        // Observador de prueba: registra todas las notificaciones
        gestor.suscribir((producto, stockActual, stockMinimo) ->
                alertasRecibidas.add("ALERTA:" + producto + ":" + stockActual));
    }

    // ─── RF5: Notificación automática ─────────────────────────────────────

    @Test
    @DisplayName("RF5 - Alerta disparada cuando stock < mínimo")
    void alertaDisparadaConStockBajo() {
        gestor.actualizarStock("Laptop Dell", 3, 10);
        assertFalse(alertasRecibidas.isEmpty(),
                "Debe dispararse al menos una alerta cuando stock < mínimo");
        assertTrue(alertasRecibidas.stream().anyMatch(a -> a.contains("Laptop Dell")));
    }

    @Test
    @DisplayName("RF5 - Notificadores por defecto incluyen Gerente y Compras")
    void notificadoresIncluyenGerenteYCompras() {
        // GestorInventario crea por defecto NotificadorGerente y NotificadorCompras
        assertEquals(3, gestor.getObservadores().size(),
                "Debe haber 2 observadores por defecto + 1 de prueba = 3");
    }

    @Test
    @DisplayName("RF5 - Sin alerta cuando stock >= mínimo")
    void sinAlertaConStockSuficiente() {
        gestor.actualizarStock("Mouse Logitech", 20, 10);
        assertTrue(alertasRecibidas.isEmpty(),
                "No debe haber alertas cuando el stock es suficiente");
    }

    // ─── RF6: Mínimo configurable por producto ───────────────────────────

    @Test
    @DisplayName("RF6 - Mínimo de stock configurable: mínimo alto (50)")
    void minimoAltoDispararaAlerta() {
        gestor.actualizarStock("Teclado HP", 30, 50);
        assertFalse(alertasRecibidas.isEmpty(),
                "Con mínimo=50 y stock=30 debe disparar alerta");
    }

    @Test
    @DisplayName("RF6 - Mínimo de stock configurable: mínimo bajo (5)")
    void minimoBajoNoDispararaAlerta() {
        gestor.actualizarStock("Cable USB", 8, 5);
        assertTrue(alertasRecibidas.isEmpty(),
                "Con mínimo=5 y stock=8 no debe disparar alerta");
    }

    @Test
    @DisplayName("RF6 - Stock exactamente igual al mínimo no dispara alerta")
    void stockIgualAlMinimoNoDisparaAlerta() {
        gestor.actualizarStock("Monitor LG", 10, 10);
        assertTrue(alertasRecibidas.isEmpty(),
                "Stock igual al mínimo no debe disparar alerta (solo si stock < mínimo)");
    }

    @Test
    @DisplayName("RF5 - Observador desuscrito no recibe notificación")
    void observadorDesuscritoNoRecibe() {
        ObservadorStock observadorExtra = (p, sa, sm) -> alertasRecibidas.add("EXTRA:" + p);
        gestor.suscribir(observadorExtra);
        gestor.desuscribir(observadorExtra);
        gestor.actualizarStock("Disco SSD", 1, 5);
        assertTrue(alertasRecibidas.stream().noneMatch(a -> a.startsWith("EXTRA:")),
                "Observador desuscrito no debe recibir notificaciones");
    }
}
