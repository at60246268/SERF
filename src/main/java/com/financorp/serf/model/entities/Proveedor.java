package com.financorp.serf.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Proveedor - Representa proveedores (principalmente de China)
 * Principio SOLID: Single Responsibility - Solo gestiona datos de proveedores
 */
@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    
    @Column(nullable = false, length = 150)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String pais;
    
    @Column(length = 100)
    private String ciudad;
    
    @Column(length = 150)
    private String contacto;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    public Proveedor(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }
}
