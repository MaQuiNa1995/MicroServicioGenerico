# Descripción
Microservicio completamente genérico con soporte para keycloak y swagger

# Usar Docker para el despliegue en local de keycloak y  PostgreSQL

docker-compose.yml
```
# Docker Compose Keycloak Postgres ( Docker compose for Keycloak with Postgres Database).

version: '3.1'


volumes:
  postgres_data:
      driver: local


services:
  postgres:
      image: postgres
      volumes:
        - postgres_data:/var/lib/postgresql/data
      environment:
        POSTGRES_DB: keycloak
        POSTGRES_USER: keycloak
        POSTGRES_PASSWORD: keycloak
  keycloak:
      image: jboss/keycloak:latest
      environment:
        DB_VENDOR: POSTGRES
        DB_ADDR: postgres
        DB_DATABASE: keycloak
        DB_USER: keycloak
        DB_PASSWORD: keycloak
        KEYCLOAK_USER: MaQui
        KEYCLOAK_PASSWORD: MaQui
        
      ports:
        - 9000:8080
      depends_on:
        - postgres

```
Estando en la misma carpeta que el archivo: `docker-compose up -d`

Cuando estén levantadas las 2 imágenes tendremos que ir a: http://localhost:9000/auth/ para entrar a keycloack

Le daremos a *administration console*
y las credenciales serán MaQui y MaQui o las que hayais definido en el docker-compose
