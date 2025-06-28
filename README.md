# Franquicias API

API para manejar franquicias, sucursales y productos.

## 🧪 Correr local

```bash
docker-compose up -d
./mvnw spring-boot:run
```

## 🐳 Docker

```bash
docker build -t franquicias-api .
docker run -p 8080:8080 franquicias-api
```

## 🧪 Pruebas

```bash
./mvnw test
```

## Terraform (opcional)

```bash
cd terraform
terraform init
terraform apply
```
