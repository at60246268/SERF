package com.financorp.serf.model.enums;

/**
 * Enum para los tipos de moneda soportados
 * Principio SOLID: Single Responsibility - Solo define monedas
 */
public enum Moneda {
    EUR("Euro", "€", "EUR"),
    USD("Dólar Estadounidense", "$", "USD"),
    CNY("Yuan Chino", "¥", "CNY"),
    PEN("Sol Peruano", "S/", "PEN"),
    CLP("Peso Chileno", "$", "CLP"),
    MXN("Peso Mexicano", "$", "MXN"),
    ARS("Peso Argentino", "$", "ARS"),
    GBP("Libra Esterlina", "£", "GBP");
    
    private final String nombre;
    private final String simbolo;
    private final String codigo;
    
    Moneda(String nombre, String simbolo, String codigo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getSimbolo() {
        return simbolo;
    }
    
    public String getCodigo() {
        return codigo;
    }
}
