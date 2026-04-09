package com.financorp.serf.controller;

import com.financorp.serf.service.ProductoService;
import com.financorp.serf.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

/**
 * Controller principal - Home
 * 
 * Principio SOLID: Single Responsibility - Solo maneja la página principal
 * Principio SOLID: Dependency Inversion - Depende de abstracciones (services)
 * 
 * @author FinanCorp S.A.
 */
@Controller
public class HomeController {
    
    private final ProductoService productoService;
    private final VentaService ventaService;
    
    @Autowired
    public HomeController(ProductoService productoService, VentaService ventaService) {
        this.productoService = productoService;
        this.ventaService = ventaService;
    }
    
    /**
     * Página principal con dashboard
     */
    @GetMapping("/")
    public String home(Model model) {
        // Estadísticas para el dashboard
        long totalProductos = productoService.listarActivos().size();
        long totalVentas = ventaService.listarTodas().size();
        
        // Ventas del mes actual
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now();
        Double ventasMes = ventaService.calcularTotalVentas(inicioMes, finMes);
        
        // Productos con stock bajo
        long productosStockBajo = productoService.obtenerProductosConStockBajo().size();
        
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("ventasMes", ventasMes);
        model.addAttribute("productosStockBajo", productosStockBajo);
        
        return "index";
    }
}
