package com.financorp.serf.model.command;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * PATRÓN COMMAND - Historial de pedidos (Invoker)
 *
 * RF7: Registra todas las acciones de pedido como comandos ejecutables.
 * Permite revertir la última acción o toda la pila de acciones.
 *
 * Principio SOLID:
 *   - Single Responsibility: solo gestiona el historial de comandos
 *   - Open/Closed: nuevos comandos se agregan sin modificar el historial
 */
@Component
public class HistorialPedidos {

    private final Deque<ComandoPedido> historial = new ArrayDeque<>();
    private final List<String> log = new ArrayList<>();

    /**
     * RF7: Ejecuta un comando y lo registra en el historial.
     */
    public void ejecutar(ComandoPedido comando) {
        comando.ejecutar();
        historial.push(comando);
        String entrada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                " | EJECUTADO | " + comando.getDescripcion();
        log.add(entrada);
        System.out.println("[Historial] Registrado: " + entrada);
    }

    /**
     * RF7: Deshace la última acción registrada.
     */
    public void deshacer() {
        if (historial.isEmpty()) {
            System.out.println("[Historial] No hay acciones para deshacer.");
            return;
        }
        ComandoPedido ultimo = historial.pop();
        ultimo.deshacer();
        String entrada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                " | DESHECHO   | " + ultimo.getDescripcion();
        log.add(entrada);
        System.out.println("[Historial] Deshecho: " + entrada);
    }

    /** Retorna el log completo en orden cronológico. */
    public List<String> getLog() {
        return List.copyOf(log);
    }

    /** Imprime el historial en consola. */
    public void imprimirHistorial() {
        System.out.println("=== HISTORIAL DE PEDIDOS ===");
        if (log.isEmpty()) {
            System.out.println("(Sin registros)");
        } else {
            log.forEach(System.out::println);
        }
        System.out.println("============================");
    }
}
