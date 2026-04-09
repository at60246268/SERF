package com.financorp.serf.model.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN OBSERVER - Sujeto observable: Gestor de inventario
 *
 * Centraliza el inventario de productos y notifica automáticamente a todos
 * los observadores suscritos cuando el stock de un producto cae por debajo
 * del mínimo configurado.
 *
 * RF5: Notifica a Gerente y Compras cuando el stock cae del mínimo.
 * RF6: El nivel mínimo de stock es configurable por producto.
 *
 * Principio SOLID:
 *   - Open/Closed: se pueden agregar nuevos observadores sin modificar esta clase
 *   - Dependency Inversion: depende de la abstracción ObservadorStock
 */
@Component
public class GestorInventario {

    private final List<ObservadorStock> observadores = new ArrayList<>();

    // RF5: Observadores predefinidos para Gerente y Compras
    public GestorInventario() {
        observadores.add(new NotificadorGerente("Gerente General"));
        observadores.add(new NotificadorCompras("Jefe de Compras"));
    }

    /** Suscribe un nuevo observador al sistema de alertas. */
    public void suscribir(ObservadorStock observador) {
        observadores.add(observador);
    }

    /** Desuscribe un observador. */
    public void desuscribir(ObservadorStock observador) {
        observadores.remove(observador);
    }

    /**
     * RF5 + RF6: Actualiza el stock de un producto.
     * Si el nuevo stock cae por debajo del mínimo configurado, notifica a todos.
     *
     * @param nombreProducto nombre del producto
     * @param nuevoStock     cantidad actual después de la operación
     * @param stockMinimo    nivel mínimo configurable por producto (RF6)
     */
    public void actualizarStock(String nombreProducto, int nuevoStock, int stockMinimo) {
        System.out.println("[GestorInventario] Producto '" + nombreProducto +
                "' | Stock actual: " + nuevoStock + " | Mínimo: " + stockMinimo);

        if (nuevoStock < stockMinimo) {
            notificarTodos(nombreProducto, nuevoStock, stockMinimo);
        }
    }

    private void notificarTodos(String nombreProducto, int stockActual, int stockMinimo) {
        for (ObservadorStock observador : observadores) {
            observador.notificarStockBajo(nombreProducto, stockActual, stockMinimo);
        }
    }

    public List<ObservadorStock> getObservadores() {
        return observadores;
    }
}
