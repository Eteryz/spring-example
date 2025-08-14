## Профили Spring

- `standalone` - для запуска модулей `admin-server`, `catalogue-service`, `feedback-service`, `customer-app` и `manager-app` без Spring Cloud Eureka, Spring Cloud Config, Docker и Kubernetes.
- `cloud` - для запуска модулей `admin-server`, `eureka-server`, `catalogue-service`, `feedback-service`, `customer-app` и `manager-app` без Spring Cloud Config, Docker и Kubernetes.
- `cloudconfig` - для запуска модулей `admin-server`, `eureka-server`, `catalogue-service`, `feedback-service`, `customer-app` и `manager-app` без Docker и Kubernetes.
- `gateway` - для запуска модулей `catalogue-service`, `feedback-service`, `customer-app` и `manager-app` за API Gateway
- `native` - для запуска модуля `config-server` с конфигами из локальной директории
- `git` - для запуска модуля `config-server` с конфигами из git-репозитория

### Зачем ip-адрес 172.17.0.1?

Это адрес хост-машины в соединении типа "мост" для Docker: все контейнеры могут обращаться к хост-системе по этому адресу. Если в вашей системе адрес отличается от указанного, то измените его в файлах конфигурации на корректный.

### Запуск тестов

```
mvn clean verify
```
