package com.financorp.serf.model.decorator;

import com.financorp.serf.model.builder.Reporte;

/**
 * PATRÓN DECORATOR - Clase base abstracta para decoradores de reportes
 * 
 * Permite añadir funcionalidades dinámicamente a los reportes sin modificar su estructura.
 * Los decoradores pueden apilarse para agregar múltiples características.
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo decora reportes
 * - Open/Closed: Extiende funcionalidad sin modificar el original
 * - Liskov Substitution: Puede sustituir a Reporte en cualquier contexto
 * 
 * @author FinanCorp S.A.
 * @version 1.0.0
 */
public abstract class ReporteDecorator {
    
    protected Reporte reporteBase;
    
    /**
     * Constructor que recibe el reporte a decorar
     * 
     * @param reporte Reporte base
     */
    public ReporteDecorator(Reporte reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo");
        }
        this.reporteBase = reporte;
    }
    
    /**
     * Método para renderizar el reporte decorado
     * Las subclases deben implementar este método para añadir su decoración
     * 
     * @return String HTML del reporte decorado
     */
    public abstract String renderizar();
    
    /**
     * Aplica la decoración al reporte
     * Modifica las propiedades del reporte según la decoración
     */
    public abstract void aplicar();
    
    /**
     * Obtiene el reporte base decorado
     * 
     * @return Reporte base
     */
    public Reporte getReporteBase() {
        return reporteBase;
    }
}
