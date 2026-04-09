-- ====================================================================
-- SERF - Sistema Empresarial de Reportes Financieros
-- Script de Inicialización de Datos de Prueba
-- ====================================================================

-- PROVEEDORES (China - proveedores tecnológicos)
INSERT INTO proveedores (id, codigo, nombre, pais, ciudad, contacto, telefono, email, activo) VALUES
(1, 'PROV-CN-001', 'Shenzhen Tech Electronics Co.', 'China', 'Shenzhen', 'Li Wei', '+86-755-88889999', 'liwei@shenzhentech.cn', true),
(2, 'PROV-CN-002', 'Beijing Innovation Systems', 'China', 'Beijing', 'Wang Fang', '+86-10-66667777', 'wangfang@beijinginno.cn', true),
(3, 'PROV-CN-003', 'Shanghai Computer Hardware Ltd.', 'China', 'Shanghai', 'Zhang Min', '+86-21-55556666', 'zhangmin@shanghaicomp.cn', true);

-- FILIALES (Oficinas multinacionales)
INSERT INTO filiales (id, codigo, nombre, pais, ciudad, moneda_local, direccion, telefono, email, activo) VALUES
(1, 'FIL-PE-001', 'FinanCorp Perú', 'Perú', 'Lima', 'PEN', 'Av. Javier Prado 1234, San Isidro', '+51-1-4445555', 'peru@financorp.com', true),
(2, 'FIL-ES-001', 'FinanCorp España', 'España', 'Madrid', 'EUR', 'Calle Gran Vía 88, Madrid', '+34-91-5556666', 'espana@financorp.com', true),
(3, 'FIL-CL-001', 'FinanCorp Chile', 'Chile', 'Santiago', 'CLP', 'Av. Providencia 2500, Santiago', '+56-2-7778888', 'chile@financorp.com', true);

-- PRODUCTOS (Tecnología importada desde China)
INSERT INTO productos (id, codigo, nombre, descripcion_tecnica, categoria, costo_importacion, moneda_importacion, precio_venta, moneda_venta, fecha_importacion, stock_actual, stock_minimo, proveedor_id, activo) VALUES
-- Laptops
(1, 'PROD-LAP-001', 'Laptop Dell XPS 15', 'Intel Core i7-13700H, 16GB RAM DDR5, 512GB SSD NVMe, NVIDIA RTX 4050 6GB, Pantalla 15.6" FHD IPS', 'LAPTOP', 800.00, 'USD', 3500.00, 'PEN', '2024-01-15', 25, 10, 1, true),
(2, 'PROD-LAP-002', 'Laptop HP Pavilion 14', 'AMD Ryzen 5 7535HS, 8GB RAM DDR4, 256GB SSD, Radeon Graphics, Pantalla 14" FHD', 'LAPTOP', 450.00, 'USD', 1800.00, 'PEN', '2024-01-20', 30, 10, 2, true),
(3, 'PROD-LAP-003', 'Laptop Lenovo ThinkPad X1', 'Intel Core i7-13800H, 32GB RAM DDR5, 1TB SSD NVMe, Intel Iris Xe, Pantalla 14" 2K IPS', 'LAPTOP', 1200.00, 'USD', 5500.00, 'PEN', '2024-02-01', 15, 5, 3, true),

-- Smartphones
(4, 'PROD-MOV-001', 'iPhone 15 Pro Max', '256GB, A17 Pro, Cámara 48MP, Pantalla 6.7" Super Retina XDR, 5G', 'SMARTPHONE', 900.00, 'USD', 4800.00, 'PEN', '2024-02-10', 40, 15, 1, true),
(5, 'PROD-MOV-002', 'Samsung Galaxy S24 Ultra', '512GB, Snapdragon 8 Gen 3, Cámara 200MP, Pantalla 6.8" AMOLED, S Pen', 'SMARTPHONE', 850.00, 'USD', 4500.00, 'PEN', '2024-02-15', 35, 15, 2, true),
(6, 'PROD-MOV-003', 'Xiaomi 14 Pro', '256GB, Snapdragon 8 Gen 3, Cámara Leica 50MP, Pantalla 6.7" AMOLED', 'SMARTPHONE', 600.00, 'USD', 2800.00, 'PEN', '2024-02-20', 50, 20, 3, true),

-- Tablets
(7, 'PROD-TAB-001', 'iPad Pro 12.9"', 'M2, 256GB, Pantalla Liquid Retina XDR, WiFi + 5G', 'TABLET', 1000.00, 'USD', 5000.00, 'PEN', '2024-03-01', 20, 8, 1, true),
(8, 'PROD-TAB-002', 'Samsung Galaxy Tab S9', '256GB, Snapdragon 8 Gen 2, Pantalla 11" AMOLED, S Pen incluido', 'TABLET', 550.00, 'USD', 2500.00, 'PEN', '2024-03-05', 25, 10, 2, true),

-- Accesorios
(9, 'PROD-ACC-001', 'AirPods Pro 2', 'Cancelación activa de ruido, USB-C, Audio Espacial', 'ACCESORIO', 180.00, 'USD', 950.00, 'PEN', '2024-03-10', 60, 20, 1, true),
(10, 'PROD-ACC-002', 'Mouse Logitech MX Master 3S', 'Inalámbrico, 8000 DPI, Ergonómico, Bluetooth + USB-C', 'ACCESORIO', 70.00, 'USD', 380.00, 'PEN', '2024-03-12', 80, 30, 2, true),
(11, 'PROD-ACC-003', 'Teclado Mecánico Keychron K8', 'RGB, Hot-swappable, Switches Gateron Brown, Bluetooth + Cable', 'ACCESORIO', 90.00, 'USD', 450.00, 'PEN', '2024-03-15', 45, 15, 3, true),

-- Periféricos (OTROS)
(12, 'PROD-PER-001', 'Monitor LG UltraWide 34"', '3440x1440, IPS, 144Hz, HDR10, USB-C PD 90W', 'OTROS', 450.00, 'USD', 2200.00, 'PEN', '2024-03-18', 18, 8, 1, true),
(13, 'PROD-PER-002', 'Webcam Logitech Brio 4K', '4K HDR, Autofocus, 90 FPS, Campo de visión ajustable', 'OTROS', 150.00, 'USD', 780.00, 'PEN', '2024-03-20', 35, 15, 2, true);

-- CLIENTES
INSERT INTO clientes (id, codigo, nombre, documento_identidad, email, telefono, direccion, pais, ciudad, activo) VALUES
(1, 'CLI-PE-001', 'Juan Carlos Rodríguez García', 'DNI-12345678', 'jcrodriguez@email.com', '+51-999-888777', 'Calle Los Olivos 456, Miraflores', 'Perú', 'Lima', true),
(2, 'CLI-PE-002', 'María Elena Quispe Mamani', 'DNI-23456789', 'mquispe@email.com', '+51-988-777666', 'Av. Salaverry 789, Jesús María', 'Perú', 'Lima', true),
(3, 'CLI-ES-001', 'Carlos Fernández López', 'NIE-X1234567A', 'cfernandez@email.es', '+34-666-555444', 'Calle Alcalá 123, Madrid', 'España', 'Madrid', true),
(4, 'CLI-ES-002', 'Laura García Martínez', 'DNI-45678901B', 'lgarcia@email.es', '+34-655-444333', 'Av. Diagonal 456, Barcelona', 'España', 'Barcelona', true),
(5, 'CLI-CL-001', 'Andrés Muñoz Silva', 'RUT-11222333-4', 'amunoz@email.cl', '+56-9-88776655', 'Av. Apoquindo 789, Las Condes', 'Chile', 'Santiago', true),
(6, 'CLI-CL-002', 'Valentina González Torres', 'RUT-22333444-5', 'vgonzalez@email.cl', '+56-9-77665544', 'Calle Providencia 321, Providencia', 'Chile', 'Santiago', true);

-- VENTAS (Transacciones multinacionales con diferentes monedas)
INSERT INTO ventas (id, numero_factura, fecha_venta, cliente_id, producto_id, filial_id, cantidad, precio_unitario, moneda_local, descuento, monto_subtotal, monto_descuento, monto_total, metodo_pago, observaciones) VALUES
-- Ventas en Perú (PEN)
(1, 'FAC-PE-2024-001', '2024-03-01', 1, 1, 1, 2, 3500.00, 'PEN', 0.00, 7000.00, 0.00, 7000.00, 'TARJETA_CREDITO', 'Cliente corporativo - descuento especial en próxima compra'),
(2, 'FAC-PE-2024-002', '2024-03-05', 2, 4, 1, 1, 4800.00, 'PEN', 100.00, 4800.00, 100.00, 4700.00, 'TRANSFERENCIA', 'Entrega inmediata'),
(3, 'FAC-PE-2024-003', '2024-03-10', 1, 9, 1, 4, 950.00, 'PEN', 200.00, 3800.00, 200.00, 3600.00, 'TARJETA_DEBITO', 'Kit corporativo de 4 unidades'),
(4, 'FAC-PE-2024-004', '2024-03-15', 2, 12, 1, 1, 2200.00, 'PEN', 0.00, 2200.00, 0.00, 2200.00, 'EFECTIVO', 'Setup en oficina incluido'),

-- Ventas en España (EUR - sin conversión)
(5, 'FAC-ES-2024-001', '2024-03-02', 3, 3, 2, 3, 1447.37, 'EUR', 150.00, 4342.11, 150.00, 4192.11, 'TARJETA_CREDITO', 'Compra empresarial - ThinkPad para equipo desarrollo'),
(6, 'FAC-ES-2024-002', '2024-03-08', 4, 5, 2, 2, 1184.21, 'EUR', 0.00, 2368.42, 0.00, 2368.42, 'TRANSFERENCIA', 'Plan de renovación móvil anual'),
(7, 'FAC-ES-2024-003', '2024-03-12', 3, 7, 2, 1, 1315.79, 'EUR', 50.00, 1315.79, 50.00, 1265.79, 'TARJETA_CREDITO', 'iPad para diseño gráfico'),
(8, 'FAC-ES-2024-004', '2024-03-18', 4, 10, 2, 5, 100.00, 'EUR', 25.00, 500.00, 25.00, 475.00, 'EFECTIVO', 'Pack de 5 mouse para oficina'),

-- Ventas en Chile (CLP)
(9, 'FAC-CL-2024-001', '2024-03-03', 5, 2, 3, 4, 473.87, 'CLP', 50.00, 1895.48, 50.00, 1845.48, 'TARJETA_DEBITO', 'Laptops para equipo comercial'),
(10, 'FAC-CL-2024-002', '2024-03-07', 6, 6, 3, 3, 736.84, 'CLP', 0.00, 2210.52, 0.00, 2210.52, 'TRANSFERENCIA', 'Xiaomi - precio competitivo'),
(11, 'FAC-CL-2024-003', '2024-03-14', 5, 8, 3, 2, 657.89, 'CLP', 30.00, 1315.78, 30.00, 1285.78, 'TARJETA_CREDITO', 'Galaxy Tab para presentaciones'),
(12, 'FAC-CL-2024-004', '2024-03-20', 6, 11, 3, 3, 118.42, 'CLP', 0.00, 355.26, 0.00, 355.26, 'EFECTIVO', 'Teclados mecánicos para programadores');

-- Insertar más datos estadísticos para reportes completos
INSERT INTO ventas (id, numero_factura, fecha_venta, cliente_id, producto_id, filial_id, cantidad, precio_unitario, moneda_local, descuento, monto_subtotal, monto_descuento, monto_total, metodo_pago, observaciones) VALUES
(13, 'FAC-PE-2024-005', '2024-03-22', 1, 13, 1, 2, 780.00, 'PEN', 0.00, 1560.00, 0.00, 1560.00, 'TRANSFERENCIA', 'Webcams para videoconferencias'),
(14, 'FAC-ES-2024-005', '2024-03-25', 3, 9, 2, 10, 250.00, 'EUR', 100.00, 2500.00, 100.00, 2400.00, 'TARJETA_CREDITO', 'Pack corporativo AirPods'),
(15, 'FAC-CL-2024-005', '2024-03-28', 5, 1, 3, 1, 947.74, 'CLP', 0.00, 947.74, 0.00, 947.74, 'EFECTIVO', 'Dell XPS para director general');
