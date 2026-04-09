package com.financorp.serf.model.observer;

/**
 * PATRÓN OBSERVER - Interfaz del observador
 *
 * Todo componente que desee recibir alertas de stock bajo debe implementar
 * esta interfaz. Desacopla al emisor de la notificación del receptor.
 */
public interface ObservadorStock {

    /**
     * RF5: Recibe la notificación cuando el stock de un producto cae bajo el mínimo.
     *
     * @param nombreProducto nombre del producto con stock bajo
     * @param stockActual    cantidad actual en inventario
     * @param stockMinimo    nivel mínimo configurado (RF6)
     */
    void notificarStockBajo(String nombreProducto, int stockActual, int stockMinimo);
}
