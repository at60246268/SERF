package com.financorp.serf.model.composite;

/**
 * Componente base del patrón Composite para reportes.
 */
public interface ComponenteReporte {
    
    String renderizar();
    
    String getTitulo();
}
