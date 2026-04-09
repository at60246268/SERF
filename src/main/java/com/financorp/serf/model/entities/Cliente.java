package com.financorp.serf.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Cliente
 * Principio SOLID: Single Responsibility - Solo gestiona datos de clientes
 * Principio SOLID: Dependency Inversion - Depende de la abstracción Filial
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        @Column(nullable = false, unique = true, length = 50)
    private String codigo;
        @Column(nullable = false, length = 150)
    private String nombre;
    
    @Column(length = 100)
    private String apellido;
    
    @Column(unique = true, length = 50)
    private String documentoIdentidad;
    
    @Column(unique = true, length = 100)
    private String email;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 200)
    private String direccion;
    
    @Column(length = 100)
    private String pais;
    
    @Column(length = 100)
    private String ciudad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filial_id")
    private Filial filial;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    public Cliente(String nombre, String email, Filial filial) {
        this.nombre = nombre;
        this.email = email;
        this.filial = filial;
    }
    
    public String getNombreCompleto() {
        return nombre + (apellido != null ? " " + apellido : "");
    }
}
