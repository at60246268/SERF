package com.financorp.serf.controller;

import com.financorp.serf.model.command.Pedido;
import com.financorp.serf.service.PedidoService;
import com.financorp.serf.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestión de pedidos
 * Utiliza el patrón Command para ejecutar operaciones sobre pedidos
 * Principio SOLID: Single Responsibility - Solo gestiona requests de pedidos
 */
@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private VentaService ventaService;
    
    // Almacena pedidos activos en memoria (en producción usar BD)
    private Map<Long, Pedido> pedidosActivos = new HashMap<>();
    private Long siguienteId = 1L;
    
    /**
     * Lista todos los pedidos
     */
    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidosActivos.values());
        model.addAttribute("historial", pedidoService.getHistorial());
        return "pedidos/lista";
    }
    
    /**
     * Muestra formulario para crear pedido
     */
    @GetMapping("/nuevo")
    public String formularioNuevoPedido(@RequestParam(required = false) Long ventaId, Model model) {
        model.addAttribute("ventaId", ventaId);
        model.addAttribute("ventas", ventaService.listarTodasVentas());
        return "pedidos/formulario";
    }
    
    /**
     * Crea un nuevo pedido (Command Pattern)
     */
    @PostMapping("/crear")
    public String crearPedido(@RequestParam Long ventaId,
                             @RequestParam String producto,
                             @RequestParam Integer cantidad,
                             @RequestParam Double precio,
                             RedirectAttributes redirectAttributes) {
        
        String numeroPedido = "PED-" + siguienteId++;
        Pedido pedido = pedidoService.crearPedido(numeroPedido, producto, cantidad, precio);
        
        Long pedidoId = siguienteId - 1;
        pedidosActivos.put(pedidoId, pedido);
        
        redirectAttributes.addFlashAttribute("mensaje", 
            "Pedido " + numeroPedido + " creado exitosamente");
        return "redirect:/pedidos/" + pedidoId;
    }
    
    /**
     * Muestra detalle de un pedido
     */
    @GetMapping("/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        Pedido pedido = pedidosActivos.get(id);
        if (pedido == null) {
            return "redirect:/pedidos?error=no-encontrado";
        }
        
        model.addAttribute("pedido", pedido);
        return "pedidos/detalle";
    }
    
    /**
     * Procesa un pedido (Command Pattern)
     */
    @PostMapping("/{id}/procesar")
    public String procesarPedido(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        
        Pedido pedido = pedidosActivos.get(id);
        if (pedido == null) {
            return "redirect:/pedidos?error=no-encontrado";
        }
        
        pedidoService.procesarPedido(pedido);
        
        redirectAttributes.addFlashAttribute("mensaje", 
            "Pedido " + pedido.getNumero() + " procesado correctamente");
        return "redirect:/pedidos/" + id;
    }
    
    /**
     * Aplica descuento a un pedido (Command Pattern)
     */
    @PostMapping("/{id}/descuento")
    public String aplicarDescuento(@PathVariable Long id,
                                   @RequestParam Double porcentaje,
                                   RedirectAttributes redirectAttributes) {
        
        Pedido pedido = pedidosActivos.get(id);
        if (pedido == null) {
            return "redirect:/pedidos?error=no-encontrado";
        }
        
        pedidoService.aplicarDescuento(pedido, porcentaje);
        
        redirectAttributes.addFlashAttribute("mensaje", 
            "Descuento del " + porcentaje + "% aplicado al pedido " + pedido.getNumero());
        return "redirect:/pedidos/" + id;
    }
    
    /**
     * Cancela un pedido (Command Pattern)
     */
    @PostMapping("/{id}/cancelar")
    public String cancelarPedido(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        
        Pedido pedido = pedidosActivos.get(id);
        if (pedido == null) {
            return "redirect:/pedidos?error=no-encontrado";
        }
        
        pedidoService.cancelarPedido(pedido);
        
        redirectAttributes.addFlashAttribute("mensaje", 
            "Pedido " + pedido.getNumero() + " cancelado");
        return "redirect:/pedidos/" + id;
    }
    
    /**
     * Deshace la última operación (Command Pattern)
     */
    @PostMapping("/deshacer")
    public String deshacerOperacion(RedirectAttributes redirectAttributes) {
        pedidoService.deshacerUltimaOperacion();
        
        redirectAttributes.addFlashAttribute("mensaje", 
            "Última operación deshecha exitosamente");
        return "redirect:/pedidos";
    }
}
