package com.financorp.serf.controller;

import com.financorp.serf.model.entities.Cliente;
import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.entities.Venta;
import com.financorp.serf.model.enums.MetodoPago;
import com.financorp.serf.model.enums.Moneda;
import com.financorp.serf.repository.ClienteRepository;
import com.financorp.serf.repository.FilialRepository;
import com.financorp.serf.service.ProductoService;
import com.financorp.serf.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller para gestión de ventas
 * 
 * Principios SOLID:
 * - Single Responsibility: Solo maneja operaciones de ventas
 * - Dependency Inversion: Usa VentaService y ProductoService
 * 
 * @author FinanCorp S.A.
 */
@Controller
@RequestMapping("/ventas")
public class VentaController {
    
    private final VentaService ventaService;
    private final ProductoService productoService;
    private final ClienteRepository clienteRepository;
    private final FilialRepository filialRepository;
    
    @Autowired
    public VentaController(VentaService ventaService, 
                          ProductoService productoService,
                          ClienteRepository clienteRepository,
                          FilialRepository filialRepository) {
        this.ventaService = ventaService;
        this.productoService = productoService;
        this.clienteRepository = clienteRepository;
        this.filialRepository = filialRepository;
    }
    
    /**
     * Lista todas las ventas
     */
    @GetMapping
    public String listar(Model model) {
        List<Venta> ventas = ventaService.listarTodas();
        model.addAttribute("ventas", ventas);
        return "ventas/lista";
    }
    
    /**
     * Muestra formulario para nueva venta
     */
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setNumeroFactura(ventaService.generarNumeroFactura());
        
        model.addAttribute("venta", venta);
        model.addAttribute("productos", productoService.listarActivos());
        model.addAttribute("clientes", clienteRepository.findByActivoTrue());
        model.addAttribute("filiales", filialRepository.findByActivoTrue());
        model.addAttribute("monedas", Moneda.values());
        model.addAttribute("metodosPago", MetodoPago.values());
        
        return "ventas/formulario";
    }
    
    /**
     * Registra una nueva venta
     * Usa SINGLETON para conversión automática de moneda
     * Actualiza stock automáticamente
     */
    @PostMapping
    public String registrar(@ModelAttribute Venta venta, RedirectAttributes redirect) {
        try {
            Venta registrada = ventaService.registrarVenta(venta);
            redirect.addFlashAttribute("success",
                String.format("Venta registrada exitosamente. Monto en EUR: €%.2f - Stock actualizado", 
                registrada.getMontoEUR()));
            return "redirect:/ventas";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al registrar venta: " + e.getMessage());
            return "redirect:/ventas/nueva";
        }
    }
    
    /**
     * Muestra detalle de una venta
     */
    @GetMapping("/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Venta venta = ventaService.buscarPorId(id)
            .orElse(null);
            
        if (venta == null) {
            redirect.addFlashAttribute("error", "Venta no encontrada");
            return "redirect:/ventas";
        }
        
        model.addAttribute("venta", venta);
        return "ventas/detalle";
    }
}
