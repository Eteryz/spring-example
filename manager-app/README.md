Запуск keycloak
```
docker run --name eteryz-keycloak 
-p 8082:8080 
-e KEYCLOAK_ADMIN=admin 
-e KEYCLOAK_ADMIN_PASSWORD=password 
-v ./config/keycloak/import:/opt/keycloak/data/import quay.io/keycloak:23.0.4 start-dev --import-realm
```