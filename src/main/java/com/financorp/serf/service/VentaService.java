package com.financorp.serf.service;

import com.financorp.serf.model.config.ConfiguracionGlobal;
import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.entities.Venta;
import com.financorp.serf.repository.ProductoRepository;
import com.financorp.serf.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service para gestión de ventas
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo gestiona lógica de ventas
 * - Dependency Inversion: Depende de abstracciones (repositories)
 * 
 * Usa PATRÓN SINGLETON: ConfiguracionGlobal para conversiones automáticas
 * 
 * @author FinanCorp S.A.
 */
@Service
@Transactional
public class VentaService {
    
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ConfiguracionGlobal config;
    
    @Autowired
    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.config = ConfiguracionGlobal.getInstance(); // PATRÓN SINGLETON
    }
    
    /**
     * Registra una venta
     * - Valida stock disponible
     * - Actualiza stock automáticamente
     * - Convierte monto a EUR usando SINGLETON
     */
    public Venta registrarVenta(Venta venta) {
        // Validar la venta
        venta.validarVenta();
        
        // Validar que no exista el número de factura
        if (ventaRepository.findByNumeroFactura(venta.getNumeroFactura()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una venta con el número de factura: " + venta.getNumeroFactura());
        }
        
        // Obtener el producto
        Producto producto = venta.getProducto();
        
        // Reducir stock (con validación interna)
        producto.reducirStock(venta.getCantidad());
        productoRepository.save(producto);
        
        // Convertir monto a moneda corporativa (EUR) usando SINGLETON
        double montoEUR = config.convertirAMonedaCorporativa(
            venta.getMontoTotalLocal(),
            venta.getMonedaLocal()
        );
        venta.setMontoEUR(montoEUR);
        
        // Guardar la venta
        return ventaRepository.save(venta);
    }
    
    /**
     * Busca una venta por ID
     */
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }
    
    /**
     * Busca una venta por número de factura
     */
    public Optional<Venta> buscarPorNumeroFactura(String numeroFactura) {
        return ventaRepository.findByNumeroFactura(numeroFactura);
    }
    
    /**
     * Lista todas las ventas
     */
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }
    
    /**
     * Obtiene ventas en un rango de fechas
     */
    public List<Venta> obtenerVentasPorPeriodo(LocalDate inicio, LocalDate fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }
    
    /**
     * Obtiene ventas de un producto específico
     */
    public List<Venta> obtenerVentasPorProducto(Long productoId) {
        return ventaRepository.findByProductoId(productoId);
    }
    
    /**
     * Obtiene ventas de un cliente específico
     */
    public List<Venta> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }
    
    /**
     * Calcula el total de ventas en EUR en un periodo
     */
    public Double calcularTotalVentas(LocalDate inicio, LocalDate fin) {
        Double total = ventaRepository.calcularTotalVentasEnPeriodo(inicio, fin);
        return total != null ? total : 0.0;
    }
    
    /**
     * Genera el siguiente número de factura
     */
    public String generarNumeroFactura() {
        long count = ventaRepository.count();
        return String.format("FACT-%06d", count + 1);
    }
    
    /**
     * Obtiene una venta por ID (método alias para controlador de pagos)
     */
    public Venta obtenerVentaPorId(Long id) {
        return buscarPorId(id).orElse(null);
    }
    
    /**
     * Lista todas las ventas (método alias para controlador de pedidos)
     */
    public List<Venta> listarTodasVentas() {
        return listarTodas();
    }
}
