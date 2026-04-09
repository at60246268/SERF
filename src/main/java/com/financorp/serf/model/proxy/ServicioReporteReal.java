package com.financorp.serf.model.proxy;

/**
 * PATRÓN PROXY - Servicio real de reportes financieros
 *
 * Contiene la lógica real que genera reportes con información sensible.
 * No debe ser accedido directamente: el proxy controla quién puede usarlo.
 */
public class ServicioReporteReal implements ServicioReporte {

    @Override
    public String generarReporte(String tipoReporte) {
        return "[REPORTE " + tipoReporte.toUpperCase() + "] " +
               "Datos financieros consolidados de TechSolutions S.A. " +
               "Fecha: " + java.time.LocalDate.now();
    }

    @Override
    public String accederDatosFinancieros() {
        return "[DATOS SENSIBLES] Utilidad neta: S/. 250,000 | " +
               "Cuentas por cobrar: S/. 85,000 | " +
               "Cuentas por pagar: S/. 42,000 | " +
               "Flujo de caja: S/. 168,000";
    }
}
