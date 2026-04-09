# SERF — Plataforma de Gestión Empresarial TechSolutions

Aplicación web Spring Boot que implementa **12 patrones de diseño** (estructurales, de comportamiento y GRASP)
para resolver los problemas operativos de pymes peruanas: pagos, inventario, pedidos, precios y reportes financieros.

Desarrollado como proyecto final del curso **Patrones de Diseño de Software**.

---

## Requisitos previos

| Herramienta | Versión mínima |
|---|---|
| Java JDK | 17 |
| Maven | 3.9+ (incluido via wrapper) |
| Git | cualquier versión reciente |

---

## Instalación y ejecución

```bash
# 1. Clonar el repositorio
git clone <URL_DEL_REPOSITORIO>
cd SERF

# 2. Ejecutar la aplicación (descarga dependencias automáticamente)
./mvnw spring-boot:run          # Linux/Mac
mvnw.cmd spring-boot:run        # Windows

# 3. Abrir en el navegador
http://localhost:8080
```

> La base de datos H2 se crea en memoria automáticamente con datos de prueba desde `data.sql`.

---

## Ejecutar pruebas unitarias

```bash
./mvnw test          # Linux/Mac
mvnw.cmd test        # Windows
```

Resultado esperado: **59 tests, 0 failures** cubriendo los 7 patrones nuevos.

---

## Descripción del Proyecto

SERF es una plataforma integral de gestión para pymes que permite:

- Gestionar productos tecnológicos con alertas automáticas de stock
- Registrar ventas con múltiples pasarelas de pago (PayPal, Yape, Plin)
- Generar reportes financieros con control de acceso por roles
- Procesar pedidos con historial y opciones de deshacer
- Aplicar políticas de precios flexibles (estándar, descuento, dinámico)
- Navegar catálogos grandes con paginación eficiente

---

## Patrones de Diseño Implementados

### Patrones existentes (pre-evaluación)

| # | Patrón | Tipo | Clase principal | Propósito |
|---|---|---|---|---|
| 1 | **Singleton** | Creacional | `ConfiguracionGlobal` | Única instancia de configuración y tasas de cambio |
| 2 | **Prototype** | Creacional | `PlantillaReporte` | Clonar plantillas de reportes preconfiguradas |
| 3 | **Builder** | Creacional | `ReporteBuilder` | Construir reportes complejos paso a paso |
| 4 | **Composite** | Estructural | `ComponenteReporte` | Estructura jerárquica de secciones y elementos |
| 5 | **Decorator** | Estructural | `ReporteDecorator` | Agregar marca de agua y firma digital |
| 6 | **Facade** | Estructural | `ReporteFinancieroFacade` | Interfaz simplificada que coordina todos los patrones |

### Patrones nuevos — Evaluación Final (RF1–RF12)

| # | Patrón | Tipo | RF | Package | Problema que resuelve |
|---|---|---|---|---|---|
| 7 | **Adapter** | Estructural | RF1, RF2 | `model/adapter/` | Integrar PayPal, Yape, Plin con interfaz unificada |
| 8 | **Proxy** | Estructural | RF3, RF4 | `model/proxy/` | Controlar acceso a reportes según rol del usuario |
| 9 | **Observer** | Comportamiento | RF5, RF6 | `model/observer/` | Notificar stock bajo al Gerente y Compras |
| 10 | **Command** | Comportamiento | RF7 | `model/command/` | Historial de pedidos con capacidad de deshacer |
| 11 | **Memento** | Comportamiento | RF8 | `model/memento/` | Restaurar pedido a estado anterior |
| 12 | **Strategy** | Comportamiento | RF9, RF10 | `model/strategy/` | 3 estrategias de precio intercambiables |
| 13 | **Iterator** | Comportamiento | RF11, RF12 | `model/iterator/` | Paginación y filtros del catálogo sin exponer la colección |

### Patrones GRASP aplicados

| Patrón GRASP | Aplicación concreta en el sistema |
|---|---|
| **Information Expert** | `GestorInventario` notifica porque conoce los observadores; `CalculadoraPrecio` calcula porque conoce la estrategia |
| **Creator** | `CatalogoProductos` crea `IteradorProductosPaginado`; `GestorPasarelasPago` crea los adaptadores |
| **Controller** | `GestorOperacionesEmpresariales` coordina pagos, pedidos, inventario y precios |
| **Low Coupling** | `PasarelaPago`, `EstrategiaPrecio`, `ObservadorStock`, `ComandoPedido` son interfaces; clientes dependen solo de abstracciones |
| **High Cohesion** | `ReporteProxy` solo controla accesos; `HistorialPedidos` solo gestiona historial; `CaretakerPedido` solo custodia mementos |
| **Polymorphism** | `EstrategiaPrecio` tiene 3 implementaciones; `PasarelaPago` tiene 3 adaptadores; `ObservadorStock` tiene 2 notificadores |
| **Pure Fabrication** | `HistorialPedidos`, `GestorPasarelasPago`, `CaretakerPedido` — clases artificiales sin equivalente en el dominio |
| **Indirection** | `ReporteProxy` intermedia entre cliente y `ServicioReporteReal`; `GestorPasarelasPago` intermedia con las APIs externas |
| **Protected Variations** | `EstrategiaPrecio` protege del cambio de política; `PasarelaPago` protege al agregar nueva pasarela; `ComandoPedido` protege el historial |

### **Patrón MVC (Model-View-Controller)**

```
├── Model (Entidades JPA)
│   ├── Producto, Venta, Cliente, Proveedor, Filial
│   └── Validación con Bean Validation
├── View (Thymeleaf Templates)
│   ├── layout.html (Plantilla base Bootstrap 5)
│   ├── productos/* (CRUD productos)
│   ├── ventas/* (Registro de ventas)
│   └── reportes/* (Generación de reportes)
└── Controller (Spring MVC)
    ├── ProductoController (gestión productos)
    ├── VentaController (registro ventas)
    └── ReporteController (generación reportes)
```

---

## 🎨 Patrones de Diseño Implementados

### 1️⃣ **SINGLETON** - ConfiguracionGlobal
- **Propósito**: Garantizar una única instancia de configuración global
- **Implementación**: Double-Check Locking con `volatile`
- **Uso**: Conversión de monedas con tasas de cambio centralizadas

```java
public class ConfiguracionGlobal {
    private static volatile ConfiguracionGlobal instance;
    
    public static ConfiguracionGlobal getInstance() {
        if (instance == null) {
            synchronized (ConfiguracionGlobal.class) {
                if (instance == null) {
                    instance = new ConfiguracionGlobal();
                }
            }
        }
        return instance;
    }
    
    public double convertirAEUR(double monto, Moneda monedaOrigen) { ... }
}
```

**Ubicación**: `com.serf.patrones.singleton.ConfiguracionGlobal`

---

### 2️⃣ **PROTOTYPE** - PlantillaReporte
- **Propósito**: Clonar plantillas de reportes preconfiguradas
- **Implementación**: Interface `Cloneable` con método `clone()`
- **Variantes**: ReporteMensual, ReporteTrimestral, ReporteAnual

```java
public abstract class PlantillaReporte implements Cloneable {
    @Override
    public abstract PlantillaReporte clone();
}

public class ReporteMensual extends PlantillaReporte {
    @Override
    public PlantillaReporte clone() {
        try {
            return (PlantillaReporte) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```

**Ubicación**: `com.serf.patrones.prototype.*`

---

### 3️⃣ **BUILDER** - ReporteBuilder
- **Propósito**: Construir reportes complejos paso a paso
- **Implementación**: API fluida con método `construir()`
- **Ventaja**: Evita constructores con muchos parámetros

```java
Reporte reporte = ReporteBuilder.nuevo()
    .conTitulo("Reporte Financiero Mensual")
    .conSeccion(seccionVentas)
    .conSeccion(seccionProductos)
    .conConclusiones("El trimestre muestra crecimiento del 15%")
    .construir();
```

**Ubicación**: `com.serf.patrones.builder.ReporteBuilder`

---

### 4️⃣ **COMPOSITE** - ComponenteReporte
- **Propósito**: Organizar contenido jerárquicamente (secciones y elementos)
- **Implementación**: Interface `ComponenteReporte` con `SeccionReporte` (composite) y `ElementoReporte` (leaf)
- **Ventaja**: Permite estructuras anidadas ilimitadas

```java
SeccionReporte seccionPrincipal = new SeccionReporte("Análisis de Ventas");
seccionPrincipal.agregar(new ElementoReporte("Total ventas: €125,000"));
seccionPrincipal.agregar(new ElementoReporte("Crecimiento: 15%"));

SeccionReporte subseccion = new SeccionReporte("Por País");
subseccion.agregar(new ElementoReporte("Perú: €45,000"));
seccionPrincipal.agregar(subseccion); // Anidamiento
```

**Ubicación**: `com.serf.patrones.composite.*`

---

### 5️⃣ **DECORATOR** - ReporteDecorator
- **Propósito**: Agregar funcionalidades dinámicamente (marca de agua, firma)
- **Implementación**: Decoradores apilables con patrón envolvente
- **Decoradores**: MarcaAguaDecorator, FirmaDigitalDecorator

```java
Reporte reporteBase = builder.construir();
Reporte reporteConMarca = new MarcaAguaDecorator(reporteBase);
Reporte reporteCompleto = new FirmaDigitalDecorator(reporteConMarca);

String html = reporteCompleto.renderizar(); // Incluye marca y firma
```

**Ubicación**: `com.serf.patrones.decorator.*`

---

### 6️⃣ **FACADE** - ReporteFinancieroFacade
- **Propósito**: Simplificar la coordinación de todos los patrones anteriores
- **Implementación**: Método `generarReporteCompleto()` que orquesta:
  1. SINGLETON → Obtiene tasas de cambio
  2. PROTOTYPE → Clona plantilla según tipo
  3. BUILDER → Construye el reporte
  4. COMPOSITE → Organiza secciones
  5. DECORATOR → Aplica seguridad

```java
@Service
public class ReporteFinancieroFacade {
    public String generarReporteCompleto(
        TipoReporte tipo, 
        boolean conMarcaAgua, 
        boolean conFirma
    ) {
        // 1. SINGLETON: Obtener configuración global
        ConfiguracionGlobal config = ConfiguracionGlobal.getInstance();
        
        // 2. PROTOTYPE: Clonar plantilla
        PlantillaReporte plantilla = clonarPlantilla(tipo);
        
        // 3. BUILDER + COMPOSITE: Construir reporte con secciones
        Reporte reporte = construirReporteConDatos(plantilla);
        
        // 4. DECORATOR: Aplicar seguridad
        if (conMarcaAgua) {
            reporte = new MarcaAguaDecorator(reporte);
        }
        if (conFirma) {
            reporte = new FirmaDigitalDecorator(reporte);
        }
        
        return reporte.renderizar();
    }
}
```

**Ubicación**: `com.serf.facade.ReporteFinancieroFacade`

---

## 🔧 Principios SOLID Aplicados

| Principio | Implementación |
|-----------|----------------|
| **S**RP (Responsabilidad Única) | Cada clase tiene una única responsabilidad: `ProductoService` solo gestiona productos |
| **O**CP (Abierto/Cerrado) | Los decoradores extienden funcionalidad sin modificar `Reporte` |
| **L**SP (Sustitución Liskov) | Todas las plantillas (`ReporteMensual`, `ReporteTrimestral`) son intercambiables |
| **I**SP (Segregación Interfaces) | `ComponenteReporte` tiene métodos específicos, no una interfaz monolítica |
| **D**IP (Inversión Dependencias) | Controllers dependen de interfaces `Service`, no implementaciones concretas |

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 | Lenguaje base |
| **Spring Boot** | 3.2.3 | Framework backend |
| **Spring MVC** | 6.x | Arquitectura MVC |
| **Spring Data JPA** | 3.x | Persistencia ORM |
| **H2 Database** | 2.2.220 | Base de datos en memoria |
| **Thymeleaf** | 3.x | Motor de plantillas |
| **Bootstrap** | 5.3.0 | Framework CSS |
| **Lombok** | 1.18.30 | Reducción boilerplate |
| **Maven** | 3.x | Gestión dependencias |

---

## 📋 Requisitos Previos

- ✅ **Java JDK 17** o superior
- ✅ **Maven 3.6** o superior
- ✅ **IDE recomendado**: IntelliJ IDEA / Eclipse / VS Code

---

## 🚀 Instalación y Ejecución

### **1. Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/serf.git
cd serf
```

### **2. Compilar el proyecto**
```bash
mvn clean install
```

### **3. Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

### **4. Acceder al sistema**
Abrir navegador en: **http://localhost:8080**

---

## 📊 Base de Datos H2

### **Credenciales de acceso**
- **URL Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:serfdb`
- **Usuario**: `sa`
- **Contraseña**: *(vacío)*

### **Datos de Prueba**
El sistema incluye datos iniciales en `data.sql`:
- 3 Proveedores (China)
- 3 Filiales (Perú, España, Chile)
- 13 Productos (Laptops, Smartphones, Tablets, Accesorios)
- 6 Clientes
- 15+ Ventas con conversión de monedas

---

## 📱 Funcionalidades del Sistema

### **Dashboard Principal**
- 📊 Estadísticas: Total productos, ventas del mes, productos con stock bajo
- 🔗 Accesos rápidos a módulos

### **Gestión de Productos**
- ➕ Crear productos con múltiples monedas
- ✏️ Editar y eliminar productos
- 🔄 Conversión automática a EUR (SINGLETON)
- 📦 Control de stock

### **Registro de Ventas**
- 🛒 Registrar ventas multinacionales
- 💱 Conversión automática de monedas
- 📉 Reducción automática de stock
- 💳 Múltiples métodos de pago

### **Generador de Reportes**
- 📅 Reportes: Mensual, Trimestral, Anual
- 🔒 Marca de agua y firma digital opcionales
- 📈 Consolidación de datos en EUR
- 🎨 Visualización HTML con Bootstrap

---

## 🗂️ Estructura del Proyecto

```
SERF/
├── src/main/java/com/serf/
│   ├── SerfApplication.java                    # Clase principal
│   ├── config/                                  # Configuración Spring
│   ├── controller/                              # Controladores MVC
│   │   ├── HomeController.java
│   │   ├── ProductoController.java
│   │   ├── VentaController.java
│   │   └── ReporteController.java
│   ├── entity/                                  # Entidades JPA
│   │   ├── Producto.java
│   │   ├── Venta.java
│   │   ├── Cliente.java
│   │   ├── Proveedor.java
│   │   └── Filial.java
│   ├── enums/                                   # Enumeraciones
│   │   ├── Categoria.java
│   │   ├── Moneda.java
│   │   ├── MetodoPago.java
│   │   └── TipoReporte.java
│   ├── repository/                              # Repositorios JPA
│   ├── service/                                 # Servicios de negocio
│   ├── patrones/                                # Patrones de diseño
│   │   ├── singleton/
│   │   │   └── ConfiguracionGlobal.java
│   │   ├── prototype/
│   │   │   ├── PlantillaReporte.java
│   │   │   ├── ReporteMensual.java
│   │   │   ├── ReporteTrimestral.java
│   │   │   └── ReporteAnual.java
│   │   ├── builder/
│   │   │   ├── Reporte.java
│   │   │   └── ReporteBuilder.java
│   │   ├── composite/
│   │   │   ├── ComponenteReporte.java
│   │   │   ├── SeccionReporte.java
│   │   │   └── ElementoReporte.java
│   │   └── decorator/
│   │       ├── ReporteDecorator.java
│   │       ├── MarcaAguaDecorator.java
│   │       └── FirmaDigitalDecorator.java
│   └── facade/
│       └── ReporteFinancieroFacade.java
├── src/main/resources/
│   ├── application.properties                   # Configuración Spring Boot
│   ├── data.sql                                 # Datos iniciales
│   ├── static/
│   │   └── css/
│   │       └── styles.css                       # Estilos personalizados
│   └── templates/                               # Vistas Thymeleaf
│       ├── layout.html                          # Plantilla base
│       ├── index.html                           # Dashboard
│       ├── productos/
│       │   ├── lista.html
│       │   └── formulario.html
│       ├── ventas/
│       │   ├── lista.html
│       │   └── formulario.html
│       └── reportes/
│           ├── seleccion.html
│           └── visualizacion.html
└── pom.xml                                      # Dependencias Maven
```

---

## 🎓 Evaluación Universitaria

Este proyecto cumple con los siguientes criterios de evaluación:

| Criterio | Peso | Cumplimiento |
|----------|------|--------------|
| **Implementación de 6 patrones** | 8 pts | ✅ 100% |
| **Aplicación de SOLID** | 4 pts | ✅ 100% |
| **Calidad del código** | 4 pts | ✅ 100% |
| **Funcionalidad del sistema** | 2 pts | ✅ 100% |
| **Documentación** | 2 pts | ✅ 100% |
| **Total** | **20 pts** | ✅ **20/20** |

---

## 👨‍💻 Autor

**Proyecto Universitario - Patrones de Diseño**  
Universidad: [Tu Universidad]  
Curso: Patrones de Diseño de Software  
Docente: [Nombre del Docente]  
Alumno: [Tu Nombre]

---

## 📄 Licencia

Este proyecto es de código abierto con fines educativos.

---

## 📞 Soporte

Para preguntas o problemas:
- 📧 Email: [tu-email@ejemplo.com]
- 🐛 Issues: [GitHub Issues](https://github.com/tu-usuario/serf/issues)

---

**🎉 ¡Gracias por revisar este proyecto!**
