package com.financorp.serf.model.enums;

/**
 * Enum para los tipos de reportes disponibles
 * Utilizado en el patrón Prototype
 * Principio SOLID: Single Responsibility - Solo define tipos de reportes
 */
public enum TipoReporte {
    MENSUAL("Reporte Mensual", "Reporte de ingresos y gastos del mes"),
    TRIMESTRAL("Reporte Trimestral", "Reporte consolidado trimestral"),
    ANUAL("Reporte Anual", "Reporte consolidado anual"),
    PERSONALIZADO("Reporte Personalizado", "Reporte con rango de fechas personalizado");
    
    private final String titulo;
    private final String descripcion;
    
    TipoReporte(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
