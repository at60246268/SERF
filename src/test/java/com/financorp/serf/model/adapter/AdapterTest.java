package com.financorp.serf.model.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Patrón Adapter (RF1, RF2)
 *
 * Verifica que cada adaptador de pasarela de pago cumpla la interfaz
 * PasarelaPago y que el gestor permita habilitar/deshabilitar pasarelas.
 */
@DisplayName("Patrón Adapter - Pasarelas de Pago")
class AdapterTest {

    private GestorPasarelasPago gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorPasarelasPago();
    }

    // ─── RF1: Integración de múltiples pasarelas ───────────────────────────

    @Test
    @DisplayName("RF1 - PayPal procesa pago correctamente")
    void payPalAdapterProcesaPago() {
        PayPalAdapter payPal = new PayPalAdapter(true);
        assertTrue(payPal.procesarPago(100.0, "Compra de laptop"),
                "PayPal debe aprobar el pago cuando está habilitado");
    }

    @Test
    @DisplayName("RF1 - Yape procesa pago correctamente")
    void yapeAdapterProcesaPago() {
        YapeAdapter yape = new YapeAdapter("999000111", true);
        assertTrue(yape.procesarPago(50.0, "Compra de mouse"),
                "Yape debe aprobar el pago cuando está habilitado");
    }

    @Test
    @DisplayName("RF1 - Plin procesa pago correctamente")
    void plinAdapterProcesaPago() {
        PlinAdapter plin = new PlinAdapter(true);
        assertTrue(plin.procesarPago(75.0, "Compra de teclado"),
                "Plin debe aprobar el pago cuando está habilitado");
    }

    @Test
    @DisplayName("RF1 - Tres pasarelas registradas por defecto")
    void gestorTieneTresPasarelasRegistradas() {
        assertEquals(3, gestor.getPasarelas().size(),
                "El gestor debe registrar PayPal, Yape y Plin");
    }

    // ─── RF2: Habilitar / deshabilitar pasarelas ──────────────────────────

    @Test
    @DisplayName("RF2 - Pasarela deshabilitada rechaza el pago")
    void pasarelaDeshabilitadaRechazaPago() {
        PayPalAdapter payPal = new PayPalAdapter(false);
        assertFalse(payPal.procesarPago(100.0, "Intento de pago"),
                "Pasarela deshabilitada debe devolver false");
        assertFalse(payPal.estaHabilitada());
    }

    @Test
    @DisplayName("RF2 - Admin deshabilita Yape desde el gestor")
    void adminDeshabilitaYapeDesdeGestor() {
        gestor.configurarPasarela("Yape", false);
        List<PasarelaPago> habilitadas = gestor.getPasarelasHabilitadas();
        assertTrue(habilitadas.stream().noneMatch(p -> p.getNombre().equals("Yape")),
                "Yape no debe aparecer en las pasarelas habilitadas");
    }

    @Test
    @DisplayName("RF2 - Admin habilita pasarela previamente deshabilitada")
    void adminHabilitaPasarela() {
        gestor.configurarPasarela("Plin", false);
        gestor.configurarPasarela("Plin", true);
        long count = gestor.getPasarelasHabilitadas().stream()
                .filter(p -> p.getNombre().equals("Plin")).count();
        assertEquals(1, count, "Plin debe estar habilitado nuevamente");
    }

    @Test
    @DisplayName("RF2 - Procesar pago a través del gestor con pasarela activa")
    void gestorProcesaPagoConPasarelaActiva() {
        assertTrue(gestor.procesarPago("PayPal", 200.0, "Orden #1001"),
                "El gestor debe delegar el pago a PayPal correctamente");
    }
}
