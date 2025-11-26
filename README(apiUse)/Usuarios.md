APIMARKET — Usuarios

La APIMARKET de **Usuarios** gestiona el ciclo completo de registro, autenticación y administración dentro del sistema.
Incluye validaciones profesionales y  manejo consistente de errores.

---

##Endpoints disponibles

### Obtener todos los usuarios
**GET** `/apiMarket/usuarios`
Retorna una lista completa de usuarios registrados.

### 2. Obtener un usuario por ID
**GET** `/apiMarket/usuarios/{id}`
Devuelve la información del usuario.
Si el ID no existe, el backend envía un error con mensaje descriptivo.

### Crear usuario
**POST** `/apiMarket/usuarios`
Crea un usuario nuevo.
Validaciones incluidas:
- Email obligatorio
- Contraseña obligatoria
- Email debe ser único
- Fecha de registro autogenerada si no se envía

Errores posibles:
- `400` datos faltantes
- `409` email en uso

### Actualizar usuario
**PUT** `/apiMarket/usuarios/{id}`
Actualiza datos de un usuario existente.
Permite modificar:
- Email (si es distinto y no está en uso)
- Apodo
- Contraseña
- Descripción
- Ciudad
- Provincia

Errores posibles:
- Usuario inexistente
- Email duplicado

### Eliminar usuario
**DELETE** `/apiMarket/usuarios/{id}`
Elimina el usuario.
Retorna `204 No Content` en caso de éxito.

### Crear usuario + carrito asignado automáticamente
**POST** `/apiMarket/usuarios/createUsuarioWithCarrito`
Flujo completo:
1. Crea el usuario
2. Genera un carrito vacío
3. Lo asigna al usuario
4. Devuelve ambos objetos (`usuario`, `carrito`)

Responde siempre en **JSON estructurado**.

### Login
**POST** `/apiMarket/usuarios/login`
Recibe:
```json
{
  "email": "test@mail.com",
  "password": "1234"
}
```

Respuestas posibles:
- `200 OK` → usuario válido
- `401 Unauthorized` → contraseña incorrecta o email no registrado
- `400 Bad Request` → campos vacíos

---

##  Manejo de errores

Todos los endpoints retornan errores de forma **consistente**, en uno de estos formatos:

### JSON con detalle
```json
{ "error": "Contraseña incorrecta" }
```
o
```json
{ "message": "Ya existe un usuario registrado con ese email." }
```

###  Texto plano (Spring o RuntimeException)
```
Usuario no encontrado con ID: 5
```


