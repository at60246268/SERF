package com.financorp.serf.repository;

import com.financorp.serf.model.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Venta
 * Principio SOLID: Interface Segregation - Interface específica para ventas
 * 
 * @author FinanCorp S.A.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    /**
     * Busca una venta por número de factura
     */
    Optional<Venta> findByNumeroFactura(String numeroFactura);
    
    /**
     * Busca ventas en un rango de fechas
     */
    List<Venta> findByFechaVentaBetween(LocalDate inicio, LocalDate fin);
    
    /**
     * Busca ventas de un producto específico
     */
    @Query("SELECT v FROM Venta v WHERE v.producto.id = :productoId ORDER BY v.fechaVenta DESC")
    List<Venta> findByProductoId(@Param("productoId") Long productoId);
    
    /**
     * Busca ventas de un cliente específico
     */
    @Query("SELECT v FROM Venta v WHERE v.cliente.id = :clienteId ORDER BY v.fechaVenta DESC")
    List<Venta> findByClienteId(@Param("clienteId") Long clienteId);
    
    /**
     * Calcula el total de ventas en EUR en un periodo
     */
    @Query("SELECT SUM(v.montoEUR) FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin")
    Double calcularTotalVentasEnPeriodo(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
    
    /**
     * Obtiene ventas agrupadas por categoría en un periodo
     */
    @Query("SELECT v.producto.categoria, SUM(v.montoEUR) FROM Venta v " +
           "WHERE v.fechaVenta BETWEEN :inicio AND :fin " +
           "GROUP BY v.producto.categoria")
    List<Object[]> obtenerVentasPorCategoria(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
