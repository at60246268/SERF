# 🚀 Guía Rápida de Inicio - SERF

## Requisitos Previos
- ✅ Java JDK 17 o superior
- ✅ Maven 3.6+
- ✅ IDE (opcional): IntelliJ IDEA, Eclipse o VS Code

## Pasos de Instalación

### 1. Verificar Java
```bash
java -version
```
Debe mostrar Java 17 o superior.

### 2. Verificar Maven
```bash
mvn -version
```

### 3. Compilar el Proyecto
```bash
cd "d:\patrones de diseño\SERF"
mvn clean install
```

### 4. Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

### 5. Acceder al Sistema
Abrir navegador en: **http://localhost:8080**

## Consola H2 Database
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:serfdb`
- Usuario: `sa`
- Contraseña: *(dejar vacío)*

## Estructura de Navegación

### Dashboard Principal
```
http://localhost:8080/
```
- Visualiza estadísticas
- Accesos rápidos a módulos

### Gestión de Productos
```
http://localhost:8080/productos
```
- **Listar**: Ver todos los productos
- **Nuevo**: http://localhost:8080/productos/nuevo
- **Editar**: http://localhost:8080/productos/editar/{id}

### Gestión de Ventas
```
http://localhost:8080/ventas
```
- **Listar**: Ver todas las ventas
- **Nueva**: http://localhost:8080/ventas/nueva

### Generador de Reportes
```
http://localhost:8080/reportes
```
Opciones:
1. Seleccionar tipo: Mensual / Trimestral / Anual
2. Marca de agua: Sí/No
3. Firma digital: Sí/No
4. Generar reporte

## Datos de Prueba Incluidos

### Proveedores (3)
- Shenzhen Tech Electronics Co. (China)
- Beijing Innovation Systems (China)
- Shanghai Computer Hardware Ltd. (China)

### Filiales (3)
- FinanCorp Perú (Lima)
- FinanCorp España (Madrid)
- FinanCorp Chile (Santiago)

### Productos (13)
- **Laptops**: Dell XPS 15, HP Pavilion 14, Lenovo ThinkPad X1
- **Smartphones**: iPhone 15 Pro Max, Samsung Galaxy S24 Ultra, Xiaomi 14 Pro
- **Tablets**: iPad Pro 12.9", Samsung Galaxy Tab S9
- **Accesorios**: AirPods Pro 2, Mouse Logitech MX Master 3S, Teclado Keychron K8
- **Periféricos**: Monitor LG UltraWide 34", Webcam Logitech Brio 4K

### Clientes (6)
- 2 en Perú
- 2 en España
- 2 en Chile

### Ventas (15+)
Transacciones en múltiples monedas con conversión automática a EUR.

## Patrones de Diseño

Al generar un reporte, podrás ver en acción los 6 patrones:

1. **SINGLETON**: ConfiguracionGlobal para tasas de cambio
2. **PROTOTYPE**: Clonación de plantillas según tipo
3. **BUILDER**: Construcción paso a paso del reporte
4. **COMPOSITE**: Organización jerárquica de secciones
5. **DECORATOR**: Marca de agua y firma digital
6. **FACADE**: Coordinación de todos los patrones

## Solución de Problemas

### Error: Puerto 8080 ocupado
Cambiar puerto en `application.properties`:
```properties
server.port=8081
```

### Error: Java no encontrado
Instalar JDK 17 y configurar `JAVA_HOME`:
```bash
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
```

### Error: Maven no encontrado
Descargar Maven desde: https://maven.apache.org/download.cgi

### Error de compilación
```bash
mvn clean
mvn install -DskipTests
```

## Comandos Útiles

### Limpiar y reinstalar
```bash
mvn clean install
```

### Ejecutar sin compilar
```bash
mvn spring-boot:run
```

### Ver dependencias
```bash
mvn dependency:tree
```

### Empaquetar JAR
```bash
mvn package
java -jar target/serf-1.0.0.jar
```

## Capturas de Funcionalidades

### Dashboard
- 4 tarjetas estadísticas
- Enlaces rápidos a módulos

### Productos
- Tabla con todos los productos
- Conversión automática a EUR visible
- Alertas de stock bajo

### Ventas
- Registro de ventas multinacionales
- Conversión automática según filial
- Reducción de stock automática

### Reportes
- Interfaz de selección con cards
- Visualización HTML del reporte
- Información de patrones aplicados

## Soporte

Si tienes problemas:
1. Verifica que Java 17 esté instalado
2. Verifica que Maven esté configurado
3. Revisa los logs en la consola
4. Consulta el README.md completo

---

**¡Listo para usar! 🎉**
