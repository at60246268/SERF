package com.financorp.serf.model.enums;

/**
 * Enum para las categorías de productos tecnológicos
 * Principio SOLID: Single Responsibility - Solo define categorías
 */
public enum Categoria {
    LAPTOP("Laptop"),
    SMARTPHONE("Smartphone"),
    ACCESORIO("Accesorio"),
    EQUIPO_RED("Equipo de Red"),
    TABLET("Tablet"),
    SMARTWATCH("Smartwatch"),
    AURICULARES("Auriculares"),
    OTROS("Otros");
    
    private final String descripcion;
    
    Categoria(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
