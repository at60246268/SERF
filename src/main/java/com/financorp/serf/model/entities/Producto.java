package com.financorp.serf.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.financorp.serf.model.enums.Categoria;
import com.financorp.serf.model.enums.Moneda;

import java.time.LocalDate;

/**
 * Entidad Producto - Productos tecnológicos importados
 * Principio SOLID: Single Responsibility - Solo gestiona datos de productos
 * Principio SOLID: Open/Closed - Extensible sin modificación
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcionTecnica;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;
    
    @Column(nullable = false)
    private Double costoImportacion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Moneda monedaImportacion;
    
    @Column(nullable = false)
    private Double precioVenta;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Moneda monedaVenta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;
    
    @Column(nullable = false)
    private LocalDate fechaImportacion;
    
    @Column(nullable = false)
    private Integer stockActual = 0;
    
    @Column(nullable = false)
    private Integer stockMinimo = 10;
    
    // Precio en moneda corporativa (EUR) - calculado por SINGLETON
    @Column
    private Double precioEUR;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    /**
     * Verifica si el producto tiene stock disponible
     * Principio SOLID: Encapsulamiento - Lógica de negocio dentro de la entidad
     */
    public boolean tieneStockDisponible(int cantidad) {
        return stockActual >= cantidad;
    }
    
    /**
     * Verifica si el stock está bajo el mínimo
     */
    public boolean stockBajo() {
        return stockActual <= stockMinimo;
    }
    
    /**
     * Reduce el stock después de una venta
     * Validación de negocio encapsulada
     */
    public void reducirStock(int cantidad) {
        if (!tieneStockDisponible(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + nombre);
        }
        this.stockActual -= cantidad;
    }
    
    /**
     * Aumenta el stock después de una devolución o reposición
     */
    public void aumentarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.stockActual += cantidad;
    }
}
