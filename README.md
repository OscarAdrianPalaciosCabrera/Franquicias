# 📦 Franquicias API

Esta es una API REST para gestionar franquicias, sucursales y productos, construida con **Spring Boot (WebFlux)** y persistencia en **MongoDB Atlas** En la nube. El proyecto está listo para ser ejecutado con **Docker** y **Docker Compose**.
Esta appp esta empaquetada con Docker, Debe serciorarse que docker desktop este corriendo en su maquina para que funcione
---

## ⚙️ Tecnologías

- Java 17
- Spring Boot 3
- Spring WebFlux (programación reactiva)
- Spring Data Reactive MongoDB
- MongoDB Atlas
- Docker & Docker Compose

---

## 📁 Estructura relevante

- `Dockerfile`: define cómo se construye la imagen del backend.
- `docker-compose.yml`: orquesta la ejecución del contenedor y conexion a base de datos deplegada en la nube de Atlas.
- `application.properties`: contiene la configuración del backend (aunque se sobrescribe por variables de entorno).

---

## 🚀 Cómo levantar la aplicación con Docker

> Asegúrate de tener Docker y Docker Compose instalados.
Docker desktop  debe estar corriendo para que se ejecute la aplicacion empaquetada

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/franquicias-api.git
cd franquicias-api
```

### 2. Ejecutar la aplicación

```bash
docker-compose up --build

o si se usa un IDE como Intelij hacer click derechosobrecho sobre docker-compose y ejecutar
```

Esto levantará la API en:

```
http://localhost:8080
```

---

## 📬 Endpoints de ejemplo

Para probar se recomienda usar postman y ejecutar en orden congruente (sin embargo estan bien manejadas las excepciones)

POST: http://localhost:8080/franquicias -------> Body en formato JSON(raw) {
    "nombre": "Exito"
}

POST: http://localhost:8080/franquicias/Exito/sucursales -------> Body en formato JSON(raw) {
    "nombre":"Sucursal Sur"
}

POST: http://localhost:8080/franquicias/Exito/sucursales/Sucursal Sur/productos -------> Body en formato JSON(raw) {
    "nombre":"yogurt",
    "stock":80
}

GET: http://localhost:8080/franquicias
GET: http://localhost:8080/franquicias/Merca Mio/productos-mayor-stock

PUT: http://localhost:8080/franquicias/Merca Mio/sucursales/Sucursal Sur/productos/leche/stock?stock=27
PUT: http://localhost:8080/franquicias/Merca Mio/actualizar-nombre -------> Body en formato JSON(raw) {
    "nuevoNombre":"Exito"
}

PUT: http://localhost:8080/franquicias/Exito/sucursales/Sucursal Sur/actualizar-nombre  -------->  Body en formato JSON(raw) {
    "nuevoNombre":"Sucursal Suramerica"
}

PUT: http://localhost:8080/franquicias/Exito/sucursales/Sucursal Suramerica/productos/panela/actualizar-nombre --------> Body en formato JSON(raw) {
    "nuevoNombre": "pepinillo"
}

DELETE: http://localhost:8080/franquicias/Exito/sucursales/Sucursal Suramerica/productos/pepinillo

---

## 🛠 Construcción manual (sin Docker)

Si prefieres ejecutar el proyecto desde tu máquina local:

```bash
./mvnw clean package
java -jar target/franquicias-api.jar
```

Y en tu `application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://franquiciasAdmin:adminadmin@cluster0.scxvxmk.mongodb.net/franquicias-db?retryWrites=true&w=majority&appName=Cluster0
```

---

## 🧪 Pruebas

```bash
./mvnw test
```

---

## 📌 Notas importantes

- El backend usa **WebFlux**, por lo tanto todos los controladores son reactivos.
- es impo de permitir conexiones desde tu IP en MongoDB Atlas (Network Access > IP Whitelist).
- La base de datos usada es: `franquicias-db`.

---

## 👨‍💻 Autor

**Oscar Adrián**  
📧 osapaca@gmail.com

---
