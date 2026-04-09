package com.financorp.serf.controller;

import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.entities.Proveedor;
import com.financorp.serf.model.enums.Categoria;
import com.financorp.serf.model.enums.Moneda;
import com.financorp.serf.repository.ProveedorRepository;
import com.financorp.serf.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller para gestión de productos
 * 
 * Principios SOLID:
 * - Single Responsibility: Solo maneja operaciones de productos
 * - Dependency Inversion: Usa ProductoService (abstracción)
 * 
 * @author FinanCorp S.A.
 */
@Controller
@RequestMapping("/productos")
public  class ProductoController {
    
    private final ProductoService productoService;
    private final ProveedorRepository proveedorRepository;
    
    @Autowired
    public ProductoController(ProductoService productoService, ProveedorRepository proveedorRepository) {
        this.productoService = productoService;
        this.proveedorRepository = proveedorRepository;
    }
    
    /**
     * Lista todos los productos
     */
    @GetMapping
    public String listar(Model model) {
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);
        return "productos/lista";
    }
    
    /**
     * Muestra formulario para nuevo producto
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("monedas", Moneda.values());
        model.addAttribute("proveedores", proveedorRepository.findByActivoTrue());
        return "productos/formulario";
    }
    
    /**
     * Muestra formulario para editar producto
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Producto producto = productoService.buscarPorId(id)
            .orElse(null);
            
        if (producto == null) {
            redirect.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/productos";
        }
        
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("monedas", Moneda.values());
        model.addAttribute("proveedores", proveedorRepository.findByActivoTrue());
        return "productos/formulario";
    }
    
    /**
     * Guarda un producto (nuevo o editado)
     * Usa SINGLETON para conversión de moneda
     */
    @PostMapping
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes redirect) {
        try {
            Producto guardado = productoService.guardarProducto(producto);
            redirect.addFlashAttribute("success", 
                String.format("Producto guardado exitosamente. Precio en EUR: €%.2f", 
                guardado.getPrecioEUR()));
            return "redirect:/productos";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/productos/nuevo";
        }
    }
    
    /**
     * Muestra detalle de un producto
     */
    @GetMapping("/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        Producto producto = productoService.buscarPorId(id)
            .orElse(null);
            
        if (producto == null) {
            redirect.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/productos";
        }
        
        model.addAttribute("producto", producto);
        return "productos/detalle";
    }
    
    /**
     * Elimina (desactiva) un producto
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            productoService.eliminarProducto(id);
            redirect.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}
