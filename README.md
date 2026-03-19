# Alliot Inventory

Aplicación Android para tablet que consume la API REST de Alliot para listar y visualizar artículos de inventario.

| Parámetro     | Valor                          |
|---------------|--------------------------------|
| Lenguaje      | Java                           |
| Min SDK       | API 25 (Android 7.1)           |
| Target SDK    | API 34                         |
| Arquitectura  | MVVM (ViewModel + LiveData)    |

## Configuración de credenciales

Las credenciales de la API se inyectan vía `BuildConfig`. **Nunca** se incluyen en el repositorio.

### Opción 1 — `gradle.properties` (recomendado)

Copiar el archivo de ejemplo y completar con credenciales reales:

```bash
cp gradle.properties.example gradle.properties
```

Luego editar `gradle.properties` y reemplazar los valores:

```properties
API_KEY=tu_api_key_aqui
API_SECRET=tu_api_secret_aqui
```

> `gradle.properties` está en `.gitignore` — nunca se sube al repositorio.

### Opción 2 — Línea de comandos

```bash
./gradlew assembleDebug -PAPI_KEY="tu_api_key" -PAPI_SECRET="tu_api_secret"
```


## Compilar y ejecutar

```bash
./gradlew assembleDebug
./gradlew installDebug
```

## Arquitectura

```
com.alliot.inventory/
├── model/        → POJOs con @SerializedName y Parcelable
├── network/      → Retrofit client, ApiService, AuthInterceptor
├── repository/   → ItemRepository con Resource wrapper (Loading/Success/Error)
├── viewmodel/    → ItemListViewModel (paginación + filtro + LiveData)
├── adapter/      → ItemAdapter (RecyclerView con Glide)
├── ui/           → ItemDetailActivity
├── util/         → ParcelCompat (compat API 33+)
└── MainActivity  → Pantalla principal con grid responsivo
```

## Decisiones técnicas

- **MVVM con AndroidViewModel + LiveData** — sobrevive cambios de configuración, separación clara UI/datos.
- **Resource wrapper** — clase genérica con estados `LOADING`, `SUCCESS`, `ERROR` para manejo uniforme en la UI.
- **OkHttp Interceptor** — los headers de autenticación se inyectan automáticamente en cada request.
- **Paginación manual con OnScrollListener** — carga infinita al hacer scroll. Se eligió sobre Paging 3 por simplicidad.
- **Parcelable sobre Serializable** — más eficiente en Android para pasar objetos entre Activities.
- **Grid dinámico** — columnas calculadas según ancho de pantalla (mín. 3 en tablet).
- **Strings externalizados** — todos los textos de UI en `strings.xml` con soporte español/inglés.

## Librerías utilizadas

| Librería              | Versión | Justificación                                   |
|-----------------------|---------|--------------------------------------------------|
| Retrofit 2            | 2.9.0   | Cliente HTTP type-safe, estándar en Android      |
| OkHttp                | 4.12.0  | Interceptors para auth, logging y timeouts       |
| Gson Converter        | 2.9.0   | Deserialización JSON → POJOs con anotaciones     |
| Glide                 | 4.16.0  | Carga de imágenes con caché y placeholder        |
| Lifecycle ViewModel   | 2.7.0   | Estado que sobrevive config changes              |
| Lifecycle LiveData    | 2.7.0   | Observación reactiva de datos                    |
| Material Components   | 1.10.0  | Chips, Cards, Toolbar, Snackbar, SearchView      |
| RecyclerView          | 1.3.2   | Lista eficiente con GridLayoutManager            |

## Funcionalidades

### Obligatorias
- Lista en grid (mín. 3 columnas en tablet) con imagen, nombre, SKU y estado
- ProgressBar durante carga
- Manejo de errores con reintentar
- Detalle con imagen grande, SKUs, código de barras, propiedades, inventario
- Retrofit con Interceptor para autenticación
- Modelos con `@SerializedName` + Parcelable
- Glide con placeholder/error
- Layout optimizado para tablet

### Bonus
- Paginación infinita (infinite scroll)
- Barra de búsqueda con filtro local
- Shared element transition entre lista y detalle
- Unit tests para modelos
- i18n español/inglés
