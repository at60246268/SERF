package com.financorp.serf.model.proxy;

/**
 * PATRÓN PROXY - Interfaz del servicio de reportes financieros
 *
 * Tanto el objeto real como el proxy implementan esta interfaz,
 * lo que hace la protección completamente transparente al cliente. RF3.
 */
public interface ServicioReporte {

    /**
     * Genera y retorna el contenido de un reporte financiero.
     * @param tipoReporte tipo de reporte solicitado
     * @return contenido del reporte
     */
    String generarReporte(String tipoReporte);

    /**
     * Accede a los datos financieros completos (solo roles autorizados).
     * @return resumen de datos financieros sensibles
     */
    String accederDatosFinancieros();
}
