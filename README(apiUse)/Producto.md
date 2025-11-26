APIMARKET de Productos

La APIMARKET de Productos administra el catálogo completo utilizado por el sistema, permitiendo registrar, consultar, actualizar y eliminar productos.
Incluye validaciones de campos obligatorios, control de precios, stock y manejo consistente de errores mediante excepciones personalizadas.

Endpoints disponibles
Obtener todos los productos

GET /apiMarket/productos
Retorna la lista completa de productos registrados.

Obtener producto por ID

GET /apiMarket/productos/{codigo}
Devuelve un producto específico según su código.
Si el producto no existe, se envía un mensaje de error claro y detallado.

Crear producto

POST /apiMarket/productos
Crea un producto nuevo.
Validaciones incluidas:

La descripción es obligatoria

El precio debe ser mayor que 0

El stock no puede ser negativo

Si no se envía fechaCreacion, se asigna automáticamente la fecha actual

Errores posibles:

400 campos inválidos o faltantes

400 precio o stock fuera de rango

Actualizar un producto completo

PUT /apiMarket/productos/{codigo}
Actualiza todos los datos del producto.
Incluye validaciones:

El nombre no puede ser nulo o vacío

Errores posibles:

Producto inexistente

Datos inválidos

Actualizar solo el stock

PUT /apiMarket/productos/{codigo}/stock
Actualiza únicamente el stock del producto.
Ideal para operaciones rápidas desde el panel administrativo o procesos automáticos.

Errores posibles:

Producto inexistente

Eliminar producto

DELETE /apiMarket/productos/{codigo}
Elimina un producto del catálogo.
Retorna 204 No Content en caso de éxito.
Si el producto no existe, devuelve un error indicando el código no encontrado.

Validaciones principales

La API incluye verificaciones obligatorias para evitar datos corruptos o inconsistentes:

Descripción requerida

Precio mayor a cero

Stock igual o mayor a cero

Nombre obligatorio al actualizar

Código no editable en operaciones sensibles

Fecha de creación automática

Manejo de errores

Los errores se devuelven en formatos consistentes, dependiendo del tipo de excepción:

Formato JSON con detalle
{ "message": "Producto no encontrado con código: 15" }


o

{ "error": "El precio debe ser mayor a 0." }

Texto plano (cuando aplica)
Producto no encontrado con código: 15
