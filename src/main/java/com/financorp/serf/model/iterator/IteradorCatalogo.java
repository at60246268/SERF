package com.financorp.serf.model.iterator;

/**
 * PATRÓN ITERATOR - Interfaz del iterador de catálogo
 *
 * RF12: Define el recorrido del catálogo sin exponer su estructura interna.
 * El cliente solo usa estos métodos y nunca accede directamente a la lista.
 */
public interface IteradorCatalogo<T> {

    /** Indica si hay más elementos disponibles. */
    boolean tieneSiguiente();

    /** Retorna el siguiente elemento. */
    T siguiente();

    /** Reinicia el iterador al inicio. */
    void reiniciar();
}
