package com.financorp.serf.service;

import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.observer.GestorInventario;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestión de notificaciones de inventario
 * Utiliza el patrón Observer
 */
@Service
public class NotificacionService {
    
    private final GestorInventario gestorInventario;
    
    public NotificacionService() {
        // GestorInventario ya registra los observadores por defecto en su constructor
        this.gestorInventario = new GestorInventario();
    }
    
    /**
     * Verifica el stock de un producto y notifica si es necesario (Observer Pattern)
     */
    public void verificarStock(Producto producto) {
        gestorInventario.actualizarStock(
            producto.getNombre(), 
            producto.getStockActual(), 
            producto.getStockMinimo()
        );
    }
    
    /**
     * Obtiene el gestor de inventario
     */
    public GestorInventario getGestorInventario() {
        return gestorInventario;
    }
}
