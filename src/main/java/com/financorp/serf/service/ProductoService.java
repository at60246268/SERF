package com.financorp.serf.service;

import com.financorp.serf.model.config.ConfiguracionGlobal;
import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.enums.Categoria;
import com.financorp.serf.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gestión de productos
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo gestiona lógica de productos
 * - Dependency Inversion: Depende de ProductoRepository (abstracción)
 * - Open/Closed: Extensible sin modificar
 * 
 * Usa PATRÓN SINGLETON: ConfiguracionGlobal para conversiones
 * 
 * @author FinanCorp S.A.
 */
@Service
@Transactional
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    private final ConfiguracionGlobal config;
    
    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        this.config = ConfiguracionGlobal.getInstance(); // PATRÓN SINGLETON
    }
    
    /**
     * Guarda un producto y convierte el precio a EUR usando SINGLETON
     */
    public Producto guardarProducto(Producto producto) {
        // Validar que el código no exista
        if (producto.getId() == null) {
            Optional<Producto> existente = productoRepository.findByCodigo(producto.getCodigo());
            if (existente.isPresent()) {
                throw new IllegalArgumentException("Ya existe un producto con el código: " + producto.getCodigo());
            }
        }
        
        // Convertir precio a moneda corporativa (EUR) usando SINGLETON
        double precioEUR = config.convertirAMonedaCorporativa(
            producto.getPrecioVenta(),
            producto.getMonedaVenta()
        );
        producto.setPrecioEUR(precioEUR);
        
        return productoRepository.save(producto);
    }
    
    /**
     * Busca un producto por su ID
     */
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    /**
     * Busca un producto por código
     */
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }
    
    /**
     * Lista todos los productos activos
     */
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }
    
    /**
     * Lista todos los productos
     */
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
    
    /**
     * Busca productos por categoría
     */
    public List<Producto> buscarPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }
    
    /**
     * Busca productos con stock bajo
     */
    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findProductosConStockBajo();
    }
    
    /**
     * Actualiza el stock de un producto
     */
    public Producto actualizarStock(Long id, int nuevoStock) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        producto.setStockActual(nuevoStock);
        return productoRepository.save(producto);
    }
    
    /**
     * Elimina (desactiva) un producto
     */
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        producto.setActivo(false);
        productoRepository.save(producto);
    }
    
    /**
     * Busca productos por nombre
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
