package com.financorp.serf.model.command;

/**
 * PATRÓN COMMAND - Interfaz base para todos los comandos de pedido
 *
 * Encapsula una acción de pedido como objeto, permitiendo:
 *   - Registrar el historial de operaciones (RF7)
 *   - Deshacer (undo) operaciones erróneas (RF7)
 *
 * Principio SOLID:
 *   - Single Responsibility: cada comando maneja una única acción
 *   - Open/Closed: nuevos tipos de pedido = nueva clase, sin modificar historial
 */
public interface ComandoPedido {

    /** Ejecuta la acción del pedido. */
    void ejecutar();

    /** Revierte la acción del pedido. */
    void deshacer();

    /** Descripción legible de la acción para el historial. */
    String getDescripcion();
}
