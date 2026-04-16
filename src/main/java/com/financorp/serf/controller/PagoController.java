package com.financorp.serf.controller;

import com.financorp.serf.model.adapter.PasarelaPago;
import com.financorp.serf.service.PagoService;
import com.financorp.serf.service.VentaService;
import com.financorp.serf.model.entities.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador para gestión de pagos
 * Utiliza el patrón Adapter para integrar múltiples pasarelas
 * Principio SOLID: Single Responsibility - Solo gestiona requests de pagos
 * Principio SOLID: Dependency Inversion - Depende de abstracciones (servicios)
 */
@Controller
@RequestMapping("/pagos")
public class PagoController {
    
    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private VentaService ventaService;
    
    /**
     * Muestra el formulario de pago para una venta
     */
    @GetMapping("/procesar/{ventaId}")
    public String mostrarFormularioPago(@PathVariable Long ventaId, Model model) {
        Venta venta = ventaService.obtenerVentaPorId(ventaId);
        if (venta == null) {
            return "redirect:/ventas?error=venta-no-encontrada";
        }
        
        List<PasarelaPago> pasarelasDisponibles = pagoService.obtenerPasarelasDisponibles();
        
        model.addAttribute("venta", venta);
        model.addAttribute("pasarelas", pasarelasDisponibles);
        
        return "pagos/formulario";
    }
    
    /**
     * Procesa el pago de una venta
     */
    @PostMapping("/procesar")
    public String procesarPago(@RequestParam Long ventaId,
                              @RequestParam String tipoPasarela,
                              @RequestParam BigDecimal monto,
                              RedirectAttributes redirectAttributes) {
        
        String referencia = "REF-" + ventaId + "-" + System.currentTimeMillis();
        String detalle = "Pago venta #" + ventaId;
        
        boolean exito = pagoService.procesarPago(tipoPasarela, monto, detalle);
        
        if (exito) {
            redirectAttributes.addFlashAttribute("mensaje", 
                "Pago procesado exitosamente con " + tipoPasarela + ". Referencia: " + referencia);
            return "redirect:/ventas/" + ventaId;
        } else {
            redirectAttributes.addFlashAttribute("error", 
                "Error al procesar el pago. Intente nuevamente.");
            return "redirect:/pagos/procesar/" + ventaId;
        }
    }
    
    /**
     * Verifica el estado de un pago
     */
    @GetMapping("/estado")
    public String verificarEstado(@RequestParam String tipoPasarela,
                                  @RequestParam String referencia,
                                  Model model) {
        
        // Por ahora retornamos un mensaje simple
        String estado = "Pago procesado exitosamente. Referencia: " + referencia;
        
        model.addAttribute("tipoPasarela", tipoPasarela);
        model.addAttribute("referencia", referencia);
        model.addAttribute("estado", estado);
        
        return "pagos/estado";
    }
    
    /**
     * Panel de configuración de pasarelas (solo administradores)
     */
    @GetMapping("/configuracion")
    public String configuracionPasarelas(Model model) {
        List<PasarelaPago> pasarelas = pagoService.obtenerTodasPasarelas();
        model.addAttribute("pasarelas", pasarelas);
        return "pagos/configuracion";
    }
    
    /**
     * Habilita una pasarela de pago
     */
    @PostMapping("/habilitar/{tipo}")
    public String habilitarPasarela(@PathVariable String tipo, 
                                    RedirectAttributes redirectAttributes) {
        pagoService.habilitarPasarela(tipo);
        redirectAttributes.addFlashAttribute("mensaje", 
            "Pasarela " + tipo + " habilitada correctamente");
        return "redirect:/pagos/configuracion";
    }
    
    /**
     * Deshabilita una pasarela de pago
     */
    @PostMapping("/deshabilitar/{tipo}")
    public String deshabilitarPasarela(@PathVariable String tipo,
                                       RedirectAttributes redirectAttributes) {
        pagoService.deshabilitarPasarela(tipo);
        redirectAttributes.addFlashAttribute("mensaje", 
            "Pasarela " + tipo + " deshabilitada correctamente");
        return "redirect:/pagos/configuracion";
    }
}
