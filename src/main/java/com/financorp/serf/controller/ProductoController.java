package com.financorp.serf.controller;

import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.entities.Proveedor;
import com.financorp.serf.model.enums.Categoria;
import com.financorp.serf.model.enums.Moneda;
import com.financorp.serf.model.iterator.CatalogoProductos;
import com.financorp.serf.model.iterator.IteradorProductosPaginado;
import com.financorp.serf.model.strategy.CalculadoraPrecio;
import com.financorp.serf.model.strategy.PrecioConDescuento;
import com.financorp.serf.model.strategy.PrecioDinamico;
import com.financorp.serf.model.strategy.PrecioEstandar;
import com.financorp.serf.repository.ProveedorRepository;
import com.financorp.serf.service.ProductoService;
import com.financorp.serf.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private final NotificacionService notificacionService;
    private final CalculadoraPrecio calculadoraPrecio;
    private CatalogoProductos catalogoProductos;
    
    @Autowired
    public ProductoController(ProductoService productoService, 
                            ProveedorRepository proveedorRepository,
                            NotificacionService notificacionService) {
        this.productoService = productoService;
        this.proveedorRepository = proveedorRepository;
        this.notificacionService = notificacionService;
        this.calculadoraPrecio = new CalculadoraPrecio();
        // Por defecto usa precio estándar
        this.calculadoraPrecio.setEstrategia(new PrecioEstandar());
    }
    
    /**
     * Lista todos los productos usando Iterator Pattern con paginación
     */
    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina,
                        @RequestParam(defaultValue = "10") int tamanoPagina,
                        @RequestParam(required = false) String filtro,
                        Model model) {
        
        List<Producto> todosLosProductos = productoService.listarTodos();
        
        // Crear catálogo de productos (Iterator Pattern)
        catalogoProductos = new CatalogoProductos(todosLosProductos);
        
        // Crear iterador paginado
        IteradorProductosPaginado iterador = (IteradorProductosPaginado) catalogoProductos.crearIterador(tamanoPagina);
        
        // Obtener páginas hasta la actual
        List<Producto> productosPagina = List.of();
        for (int i = 0; i <= pagina && iterador.tieneSiguiente(); i++) {
            productosPagina = iterador.siguientePagina();
        }
        
        // Filtrar si se especificó filtro
        if (filtro != null && !filtro.trim().isEmpty()) {
            String filtroLower = filtro.toLowerCase();
            productosPagina = productosPagina.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtroLower) ||
                           p.getCodigo().toLowerCase().contains(filtroLower))
                .collect(java.util.stream.Collectors.toList());
        }
        
        model.addAttribute("productos", productosPagina);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", iterador.getTotalPaginas());
        model.addAttribute("filtro", filtro);
        model.addAttribute("totalProductos", iterador.getTotalProductos());
        
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
     * Usa OBSERVER para notificar stock bajo
     */
    @PostMapping
    public String guardar(@ModelAttribute Producto producto, RedirectAttributes redirect) {
        try {
            Producto guardado = productoService.guardarProducto(producto);
            
            // Verificar stock y notificar si es necesario (Observer Pattern)
            notificacionService.verificarStock(guardado);
            
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
    
    /**
     * Configuración de políticas de precios (Strategy Pattern)
     */
    @GetMapping("/configuracion-precios")
    public String configuracionPrecios(Model model) {
        model.addAttribute("estrategiaActual", calculadoraPrecio.getEstrategiaActiva());
        return "productos/configuracion-precios";
    }
    
    /**
     * Cambia la estrategia de precios
     */
    @PostMapping("/cambiar-estrategia")
    public String cambiarEstrategia(@RequestParam String estrategia,
                                   @RequestParam(required = false) Double parametro,
                                   RedirectAttributes redirect) {
        try {
            switch (estrategia) {
                case "ESTANDAR":
                    calculadoraPrecio.setEstrategia(new PrecioEstandar());
                    break;
                case "DESCUENTO":
                    if (parametro == null) {
                        throw new IllegalArgumentException("Debe especificar el porcentaje de descuento");
                    }
                    calculadoraPrecio.setEstrategia(new PrecioConDescuento(parametro));
                    break;
                case "DINAMICO":
                    // PrecioDinamico usa configuración predeterminada
                    calculadoraPrecio.setEstrategia(new PrecioDinamico());
                    break;
                default:
                    throw new IllegalArgumentException("Estrategia no válida");
            }
            redirect.addFlashAttribute("success", "Estrategia de precios cambiada a: " + estrategia);
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al cambiar estrategia: " + e.getMessage());
        }
        return "redirect:/productos/configuracion-precios";
    }
    
    /**
     * Calcula precio con estrategia actual
     */
    @GetMapping("/{id}/precio")
    @ResponseBody
    public BigDecimal calcularPrecioConEstrategia(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id).orElse(null);
        if (producto == null) {
            return BigDecimal.ZERO;
        }
        double precioCalculado = calculadoraPrecio.calcularPrecio(producto.getPrecioVenta());
        return BigDecimal.valueOf(precioCalculado);
    }
    
    /**
     * Configuración de stock mínimo
     */
    @PostMapping("/{id}/stock-minimo")
    public String configurarStockMinimo(@PathVariable Long id,
                                       @RequestParam Integer stockMinimo,
                                       RedirectAttributes redirect) {
        try {
            Producto producto = productoService.buscarPorId(id).orElse(null);
            if (producto == null) {
                redirect.addFlashAttribute("error", "Producto no encontrado");
                return "redirect:/productos";
            }
            
            producto.setStockMinimo(stockMinimo);
            productoService.guardarProducto(producto);
            
            // Verificar si el stock actual está por debajo del nuevo mínimo
            notificacionService.verificarStock(producto);
            
            redirect.addFlashAttribute("success", 
                "Stock mínimo configurado en " + stockMinimo + " unidades");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al configurar stock mínimo: " + e.getMessage());
        }
        return "redirect:/productos/" + id;
    }
}

