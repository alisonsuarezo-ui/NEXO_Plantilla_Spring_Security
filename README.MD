NEXO – Gestión Conectada
Ampliación de la plantilla Spring Security – User Role Management con tres nuevas entidades (Categorías, Métodos de pago y Promociones) para que el sistema sea más completo y la interfaz más moderna y visual.

Autoras de los cambios: Ing. Lisseth y Alisson

Qué se agregó
Entidad	Relación	Ruta base	Aporte
Categorías	1 categoría → N productos	/api/categories	Filtros y organización de productos
Métodos de pago	1 método → N ventas	/api/payment-methods	Ventas más completas y visuales
Promociones	1 promoción → N productos	/api/promotions	Tarjetas, descuentos y campañas
Campos
Categoría: id, nombre (único), descripción, icono, estado.
Método de pago: id, nombre (único), descripción, icono, estado.
Promoción: id, nombre, descripción, descuento (%), fecha de inicio, fecha de fin, estado y productos asociados.
Cambios en el backend
Módulos nuevos (src/main/java/com/roles/usermanagement/modules/): category, paymentmethod, promotion y publiccatalog. Cada uno sigue el patrón de la plantilla: entidad, repositorio, Request, Response, servicio y controlador.

Módulos modificados:

Productos: nuevo campo opcional categoryId; la respuesta devuelve categoryId y categoryName; filtro GET /api/products?categoryId=1.
Ventas: nuevo campo opcional paymentMethodId; la venta guarda el nombre del método como copia histórica y lo devuelve en la respuesta.
Seguridad:

12 permisos nuevos: CATEGORY_*, PAYMENT_METHOD_* y PROMOTION_* (READ, CREATE, UPDATE, DELETE). El rol ADMIN los recibe al arrancar.
Rutas públicas de solo lectura (sin token):
GET /api/public/categories: categorías activas con su cantidad de productos.
GET /api/public/promotions: promociones vigentes hoy, con status y progress.
Todo lo demás sigue exigiendo JWT y el permiso correspondiente.
Reglas:

DELETE desactiva el registro, no lo borra.
No se permiten nombres repetidos en categorías ni en métodos de pago (409).
La fecha fin de una promoción no puede ser anterior a la de inicio (400).
Una categoría, método de pago o producto inactivo no se puede asociar (409).
Las promociones son informativas: no modifican el precio de la venta.
Cambios en el frontend
Archivos: Frontend/index.html, Frontend/app.js, Frontend/styles.css.

Menú lateral: nuevas opciones Categorías, Promociones y Métodos de pago (según permisos).
Categorías y Métodos de pago: listado con icono, estado y acciones de crear, editar y desactivar; las categorías muestran cuántos productos tienen.
Productos: columna y selector de categoría, y filtro por categoría sobre la tabla.
Promociones: descuento, vigencia, barra de progreso, estado y productos asociados.
Ventas: selector visual de método de pago, columna Pago en el listado y método en el detalle.
Inicio: tarjetas de Promociones activas con botón Ver productos → y contadores de los nuevos módulos.
Cómo ejecutarlo
Requisitos: Java 21 y PostgreSQL.
Define las variables DB_URL, DB_USERNAME y DB_PASSWORD. Para crear el primer administrador, define también ADMIN_USERNAME, ADMIN_EMAIL y ADMIN_PASSWORD.
Desde spring-security-user-role-management:
.\gradlew.bat test
.\gradlew.bat bootRun
En Mac o Linux usa ./gradlew.
Swagger: http://localhost:8050/swagger-ui.html
Frontend: abre Frontend/index.html e inicia sesión como ADMIN.
Prueba rápida
Crea una categoría y un método de pago.
Crea un producto con categoryId.
Crea una promoción con productIds.
Registra una venta con paymentMethodId.
Consulta /api/public/promotions sin token.
Pendiente
Ejecutar las pruebas (gradlew test) y corregir lo que aparezca, porque los cambios se escribieron sin poder compilar.
Agregar pruebas automáticas para los módulos nuevos (hoy solo se actualizaron los conteos de rutas y permisos).
Decidir si categoryId y paymentMethodId pasan a ser obligatorios.
Créditos
Cambios y ampliación del proyecto realizados por Ing. Lisseth y Alisson.
