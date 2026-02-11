# Orders API Gateway

Sistema de gerenciamento de pedidos composto por duas APIs Spring Boot: um Gateway de autenticação e uma API de pedidos.

### Gateway API (porta 8080)
- Responsável pela autenticação via JWT
- Atua como proxy para a Orders API
- Valida tokens em todas as requisições protegidas

### Orders API (porta 8081)
- Manipula pedidos e itens de pedidos
- Banco de dados H2 em memória

## Pré-requisitos

- Java 25
- Maven 3.9+
- Docker

## Como Executar

```

### Via Docker

1. Dentro da pasta principal **Compile os projetos**:
```bash
cd gateway-api && mvn clean package -DskipTests && cd ..
cd orders-api && mvn clean package -DskipTests && cd ..
```

2. **Suba as aplicações**:
```bash
docker-compose up -d
```

3. **Para parar**:
```bash
docker-compose down
```

## Credenciais de Teste

| Campo    | Valor      |
|----------|------------|
| Usuário  | `usuario`  |
| Senha    | `senha123` |

## Endpoints

### Gateway API (http://localhost:8080)

| Método | Endpoint           | Descrição                    | Autenticação |
|--------|--------------------|-----------------------------|--------------|
| POST   | `/auth/login`      | Autenticar e obter token JWT | Não          |
| GET    | `/health/orders`   | Verificar saúde da Orders API| Não          |
| *      | `/api/orders/**`   | Proxy para Orders API        | Sim (JWT)    |

### Orders API (http://localhost:8081)

| Método | Endpoint                | Descrição                     |
|--------|-------------------------|-------------------------------|
| GET    | `/api/orders`           | Listar pedidos (paginado)     |
| GET    | `/api/orders/{id}`      | Buscar pedido por ID          |
| POST   | `/api/orders`           | Criar novo pedido             |
| PUT    | `/api/orders/{id}`      | Atualizar pedido              |
| DELETE | `/api/orders/{id}`      | Excluir pedido                |
| GET    | `/api/orders/{id}/items`| Listar itens do pedido        |
| POST   | `/api/orders/{id}/items`| Adicionar item ao pedido      |

## Exemplos de Requisições

### 1. Autenticação (obter token JWT)

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "password": "senha123"
  }'
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Criar um pedido

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{
    "customerName": "João da Silva",
    "customerEmail": "joao.silva@email.com"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "customerName": "João da Silva",
  "customerEmail": "joao.silva@email.com",
  "orderDate": "2024-03-01T10:30:00",
  "status": "PENDING",
  "totalAmount": 0
}
```

### 3. Adicionar item ao pedido

```bash
curl -X POST http://localhost:8080/api/orders/1/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_JWT" \
  -d '{
    "productName": "Camiseta Azul M",
    "quantity": 2,
    "unitPrice": 49.90
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "customerName": "João da Silva",
  "customerEmail": "joao.silva@email.com",
  "orderDate": "2024-03-01T10:30:00",
  "status": "PENDING",
  "totalAmount": 99.80
}
```

### 4. Listar pedidos (com paginação)

```bash
curl -X GET "http://localhost:8080/api/orders?page=0&size=10" \
  -H "Authorization: Bearer TOKEN_JWT"
```

### 5. Buscar pedido por ID

```bash
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer TOKEN_JWT"
```

### 6. Atualizar pedido

```bash
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN_JWT" \
  -d '{
    "customerName": "João Silva Atualizado",
    "customerEmail": "joao.novo@email.com",
    "status": "CONFIRMED"
  }'
```

### 7. Listar itens do pedido

```bash
curl -X GET http://localhost:8080/api/orders/1/items \
  -H "Authorization: Bearer TOKEN_JWT"
```

### 8. Excluir pedido

```bash
curl -X DELETE http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer TOKEN_JWT"
```

## Swagger UI

Após iniciar as aplicações, acesse a documentação interativa:

- **Gateway API:** http://localhost:8080/swagger-ui/index.html
- **Orders API:** http://localhost:8081/swagger-ui/index.html

## Console H2 (Orders API)

Para acessar o banco de dados H2:

- **URL:** http://localhost:8081/h2-console
- **JDBC URL:** `jdbc:h2:mem:ordersdb`
- **Usuário:** `sa`
- **Senha:** *(vazio)*

## Status de Pedidos

| Status      | Descrição                    |
|-------------|------------------------------|
| `PENDING`   | Pedido criado, aguardando    |
| `CONFIRMED` | Pedido confirmado            |
| `SHIPPED`   | Pedido enviado               |
| `DELIVERED` | Pedido entregue              |
| `CANCELLED` | Pedido cancelado             |

## Executando os Testes

```bash
# Testes do Gateway API
cd gateway-api && mvn test

# Testes do Orders API
cd orders-api && mvn test
```

## Tecnologias Utilizadas

- Java 25
- Spring Boot 4.0.2
- Spring Security
- Spring Data JPA
- JWT (jjwt 0.11.5)
- H2 Database
- Swagger
- Lombok
- Log4j2
