package com.financorp.serf.service;

import com.financorp.serf.model.command.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Servicio para gestión de pedidos
 * Utiliza el patrón Command
 */
@Service
public class PedidoService {
    
    private final HistorialPedidos historial;
    
    public PedidoService() {
        this.historial = new HistorialPedidos();
    }
    
    /**
     * Crea un nuevo pedido
     */
    public Pedido crearPedido(String numero, String producto, int cantidad, double precio) {
        Pedido pedido = new Pedido(numero, producto, cantidad, precio);
        ComandoPedido comando = new CrearPedidoComando(pedido);
        historial.ejecutar(comando);
        return pedido;
    }
    
    /**
     * Procesa un pedido existente
     */
    public void procesarPedido(Pedido pedido) {
        ComandoPedido comando = new ProcesarPedidoComando(pedido);
        historial.ejecutar(comando);
    }
    
    /**
     * Aplica un descuento a un pedido
     */
    public void aplicarDescuento(Pedido pedido, double porcentaje) {
        ComandoPedido comando = new AplicarDescuentoComando(pedido, porcentaje);
        historial.ejecutar(comando);
    }
    
    /**
     * Cancela un pedido
     */
    public void cancelarPedido(Pedido pedido) {
        ComandoPedido comando = new CancelarPedidoComando(pedido);
        historial.ejecutar(comando);
    }
    
    /**
     * Deshace la última operación
     */
    public void deshacerUltimaOperacion() {
        historial.deshacer();
    }
    
    /**
     * Obtiene el historial de comandos
     */
    public HistorialPedidos getHistorial() {
        return historial;
    }
}
