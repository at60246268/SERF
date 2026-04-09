package com.financorp.serf.model.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sección de reporte que puede contener subsecciones y elementos.
 */
public class SeccionReporte implements ComponenteCompuesto {
    
    private String titulo;
    private List<ComponenteReporte> hijos;
    private int nivel;
    
    public SeccionReporte(String titulo) {
        this(titulo, 1);
    }
    
    public SeccionReporte(String titulo, int nivel) {
        this.titulo = titulo;
        this.nivel = nivel;
        this.hijos = new ArrayList<>();
    }
    
    @Override
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        
        String tagTitulo = "h" + Math.min(nivel + 1, 6);
        
        sb.append("<div class='seccion-reporte nivel-").append(nivel).append("'>");
        sb.append("<").append(tagTitulo).append(" class='seccion-titulo'>")
          .append(titulo)
          .append("</").append(tagTitulo).append(">");
        
        if (!hijos.isEmpty()) {
            sb.append("<div class='seccion-contenido'>");
            for (ComponenteReporte hijo : hijos) {
                sb.append(hijo.renderizar());
            }
            sb.append("</div>");
        }
        
        sb.append("</div>");
        
        return sb.toString();
    }
    
    @Override
    public void agregar(ComponenteReporte componente) {
        if (componente == null) {
            throw new IllegalArgumentException("El componente no puede ser nulo");
        }
        
        if (componente instanceof SeccionReporte) {
            SeccionReporte seccion = (SeccionReporte) componente;
            seccion.setNivel(this.nivel + 1);
        }
        this.hijos.add(componente);
    }
    
    @Override
    public boolean eliminar(ComponenteReporte componente) {
        return this.hijos.remove(componente);
    }
    
    @Override
    public List<ComponenteReporte> getHijos() {
        return Collections.unmodifiableList(hijos);
    }
    
    @Override
    public void limpiar() {
        this.hijos.clear();
    }
    
    @Override
    public String getTitulo() {
        return titulo;
    }
    
    public int getNivel() {
        return nivel;
    }
    
    void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
