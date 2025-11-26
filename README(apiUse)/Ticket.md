apiMarket - Ticket API
La apiMarket de Ticket gestiona el ciclo completo de creación, asignación, actualización y eliminación de tickets/ventas dentro del sistema. Incluye validaciones profesionales, manejo consistente de errores.

Endpoints disponibles
Obtener todos los tickets
GET /apiMarket/ticket Retorna una lista completa de tickets/ventas registrados.

Obtener un ticket por ID
GET /apiMarket/ticket/{id} Devuelve la información del ticket. Si el ID no existe, el backend envía un error con mensaje descriptivo.

Crear ticket
POST /apiMarket/ticket Crea un ticket nuevo. Validaciones incluidas:

Fecha de creación se asigna automáticamente si no se proporciona
Errores posibles:

Datos inválidos
Actualizar ticket
PUT /apiMarket/ticket/{id} Actualiza datos de un ticket existente. Permite modificar:

Descripción, estado, método de pago, ciudad, provincia, país, total
Errores posibles:

Ticket inexistente
Eliminar ticket
DELETE /apiMarket/ticket/{id} Elimina el ticket. Retorna 204 No Content en caso de éxito.

Agregar producto al ticket
POST /apiMarket/ticket/{ventaId}/agregar-producto Agrega un producto a un ticket ya existente. Parámetros:

productoId (Integer) - ID del producto
cantidad (int) - Cantidad solicitada
Validaciones incluidas:

Verifica stock suficiente
Cantidad mayor a 0
Errores posibles:

Ticket inexistente
Producto inexistente
Stock insuficiente
Cantidad inválida
Actualizar total del ticket
PUT /apiMarket/ticket/{ventaId}/actualizar-total Actualiza el total de un ticket en base a cantidad y precio unitario. Parámetros:

cantidad (int) - Cantidad utilizada para el cálculo
precioUnitario (double) - Precio por unidad
Errores posibles:

Ticket inexistente
Realizar pago
POST /apiMarket/ticket/pago Procesa el pago de un carrito completo. Parámetros:

carritoId (Integer) - ID del carrito
usuarioId (Integer) - ID del usuario
Validaciones incluidas:

Carrito no vacío
Stock suficiente para todos los productos
Errores posibles:

Carrito vacío
Stock insuficiente
Usuario inexistente
Manejo de errores
Todos los endpoints retornan errores de forma consistente, en uno de estos formatos:

JSON con detalle
json

Copy code
{ "error": "Venta no encontrada con ID: 5" }
o

json

Copy code
{ "message": "La cantidad debe ser mayor a 0." }
Texto plano (Spring o RuntimeException)

Copy code
Venta no encontrada con ID: 5


