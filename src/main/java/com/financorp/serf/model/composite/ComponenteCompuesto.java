package com.financorp.serf.model.composite;

import java.util.List;

/**
 * Interfaz para componentes compuestos (con hijos).
 */
public interface ComponenteCompuesto extends ComponenteReporte {
    
    void agregar(ComponenteReporte componente);
    
    boolean eliminar(ComponenteReporte componente);
    
    List<ComponenteReporte> getHijos();
    
    default boolean esCompuesto() {
        return true;
    }
    
    default int getCantidadHijos() {
        return getHijos().size();
    }
    
    default boolean tieneHijos() {
        return !getHijos().isEmpty();
    }
    
    void limpiar();
}
