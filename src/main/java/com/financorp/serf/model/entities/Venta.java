package com.financorp.serf.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.financorp.serf.model.enums.MetodoPago;
import com.financorp.serf.model.enums.Moneda;

import java.time.LocalDate;

/**
 * Entidad Venta - Transacciones de venta
 * Principio SOLID: Single Responsibility - Solo gestiona datos de ventas
 * Principio SOLID: Dependency Inversion - Depende de abstracciones (Producto, Cliente)
 */
@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String numeroFactura;
    
    @Column(nullable = false)
    private LocalDate fechaVenta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private Double precioUnitario;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Moneda monedaLocal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filial_id")
    private Filial filial;
    
    @Column
    private Double descuento;
    
    @Column
    private Double montoSubtotal;
    
    @Column
    private Double montoDescuento;
    
    @Column
    private Double montoTotal;
    
    @Column(length = 150)
    private String vendedorResponsable;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;
    
    @Column(length = 500)
    private String observaciones;
    
    // Monto en moneda corporativa (EUR) - calculado por SINGLETON
    @Column
    private Double montoEUR;
    
    // Monto total en moneda local
    @Column
    private Double montoTotalLocal;
    
    /**
     * Calcula el monto total en moneda local
     * Principio SOLID: Encapsulamiento - Lógica de cálculo dentro de la entidad
     */
    public Double calcularMontoTotal() {
        return cantidad * precioUnitario;
    }
    
    /**
     * Pre-persist: Se ejecuta antes de guardar
     * Calcula automáticamente el monto total
     */
    @PrePersist
    @PreUpdate
    public void calcularMontos() {
        this.montoTotalLocal = calcularMontoTotal();
    }
    
    /**
     * Valida que la cantidad sea positiva
     */
    public void validarVenta() {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (precioUnitario == null || precioUnitario <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }
        if (!producto.tieneStockDisponible(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para realizar la venta");
        }
    }
}
