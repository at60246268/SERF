package com.financorp.serf.model.composite;

/**
 * Elemento individual de reporte sin hijos.
 */
public class ElementoReporte implements ComponenteReporte {
    
    private String titulo;
    private String contenido;
    private TipoElemento tipo;
    
    public enum TipoElemento {
        TEXTO,
        TABLA,
        GRAFICO,
        LISTA,
        PARRAFO,
        DATO
    }
    
    public ElementoReporte(String titulo, String contenido) {
        this(titulo, contenido, TipoElemento.PARRAFO);
    }
    
    public ElementoReporte(String titulo, String contenido, TipoElemento tipo) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.tipo = tipo;
    }
    
    @Override
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<div class='elemento-reporte tipo-").append(tipo.name().toLowerCase()).append("'>");
        
        switch (tipo) {
            case TEXTO:
            case PARRAFO:
                if (titulo != null && !titulo.isEmpty()) {
                    sb.append("<p class='elemento-titulo'><strong>")
                      .append(titulo)
                      .append(":</strong> ");
                }
                sb.append(contenido);
                if (titulo != null && !titulo.isEmpty()) {
                    sb.append("</p>");
                }
                break;
                
            case TABLA:
                sb.append("<div class='elemento-tabla'>")
                  .append(contenido)
                  .append("</div>");
                break;
                
            case GRAFICO:
                sb.append("<div class='elemento-grafico'>")
                  .append("<div class='grafico-placeholder'>")
                  .append("[Gráfico: ").append(titulo).append("]")
                  .append("</div>")
                  .append("</div>");
                break;
                
            case LISTA:
                sb.append("<ul class='elemento-lista'>")
                  .append(contenido)
                  .append("</ul>");
                break;
                
            case DATO:
                sb.append("<div class='elemento-dato'>")
                  .append("<span class='dato-label'>").append(titulo).append(":</span> ")
                  .append("<span class='dato-valor'>").append(contenido).append("</span>")
                  .append("</div>");
                break;
                
            default:
                sb.append("<p>").append(contenido).append("</p>");
        }
        
        sb.append("</div>");
        
        return sb.toString();
    }
    
    @Override
    public String getTitulo() {
        return titulo;
    }
    
    public boolean esCompuesto() {
        return false;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public TipoElemento getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoElemento tipo) {
        this.tipo = tipo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
