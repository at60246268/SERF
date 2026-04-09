package com.financorp.serf.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.financorp.serf.model.enums.Moneda;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Filial - Representa una filial de FinanCorp
 * Principio SOLID: Single Responsibility - Solo gestiona datos de filiales
 */
@Entity
@Table(name = "filiales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        @Column(nullable = false, unique = true, length = 50)
    private String codigo;
        @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 100)
    private String pais;
    
    @Column(length = 100)
    private String ciudad;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Moneda monedaLocal;
    
    @Column(length = 200)
    private String direccion;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 150)
    private String email;
    
    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL)
    private List<Cliente> clientes = new ArrayList<>();
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    public Filial(String nombre, String pais, Moneda monedaLocal) {
        this.nombre = nombre;
        this.pais = pais;
        this.monedaLocal = monedaLocal;
    }
}
