package com.financorp.serf.repository;

import com.financorp.serf.model.entities.Producto;
import com.financorp.serf.model.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Producto
 * Principio SOLID: Interface Segregation - Interface específica para productos
 * Principio SOLID: Dependency Inversion - Abstracción de acceso a datos
 * 
 * @author FinanCorp S.A.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Busca un producto por su código único
     */
    Optional<Producto> findByCodigo(String codigo);
    
    /**
     * Busca productos por categoría
     */
    List<Producto> findByCategoria(Categoria categoria);
    
    /**
     * Busca productos activos
     */
    List<Producto> findByActivoTrue();
    
    /**
     * Busca productos con stock bajo
     */
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<Producto> findProductosConStockBajo();
    
    /**
     * Busca productos por nombre (búsqueda parcial)
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
