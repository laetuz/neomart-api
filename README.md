# Neomart API Service Specification (v0.1.2)

## 1. Service Identity
- Name: neomart
- Role: E-commerce and store API for the Neo Ecosystem.
- Port: `8083` (internal) / `dev.neotica.id/neomart` (external)
- Internal Database: `db_neomart` (Hosted on `neoserver-db`)

## 2. Technology Stack
- Framework: Ktor Server (Netty) v3.x
- Language: Kotlin
- ORM: Exposed DAO (UUIDTable)
- DI: Koin
- Migrations: Flyway (Targeting `db_neomart` only)

## 3. Database Schema (First Phase)
Every table uses `UUIDTable` to match `neoserver` standards.

| Table          | Key Fields                                                 | Relationship                            |
|----------------|------------------------------------------------------------|-----------------------------------------|
| ProductTable   | id, name, price, stock, description, image_url, created_at | -                                       |
| CategoryTable  | id, name                                                   | 1:N with Products                       |
| CartTable      | id, user_id (UUID), product_id, quantity, created_at       | user_id (JWT), product_id (ProductTable |
| OrderTable     | id, user_id (UUID), total_amount, status, created_at       | user_id (JWT)                           |
| OrderItemTable | id, order_id, product_id, quantity, price_at_purchase      | order_id (OrderTable, CASCADE delete)   |


## 4. Authentication Protocol (Ktor 3.x)
- Secret: Loaded securely via `Utils.EnvLoader["HMAC_256_SECRET"]`.
- Issuer: Loaded securely via `Utils.EnvLoader["NEOSERVER_URL"]`.
- Claim Name: Must look for `"id"` (matches `neoserver` token payload).
- Architecture Note (CRITICAL): - Ktor 3.x's `Authentication` plugin is installed globally to act as a strict gatekeeper (verifies signature and issuer).
  - However, due to a Ktor 3 context-dropping quirk, we **do not** use `call.principal<JWTPrincipal>()` in our routes.
  - Instead, routes use a custom extension function `call.getUserIdFromToken()` which safely extracts the raw `Authorization` header and decodes the `"id"` claim directly after Ktor has validated the request.
  - Rule: Neomart never queries `neoserver` to verify a user.

## 5. API Endpoints:

| Method | Route                | Auth | Description                                      |
|--------|----------------------|------|--------------------------------------------------|
| GET    | /products            | no   | List all products                                |
| GET    | /products/{id}       | no   | Product details                                  |
| POST   | /cart                | yes  | Add item to cart (uses JWT `id`)                 |
| GET    | /cart                | yes  | View User's Cart                                 |
| DELETE | /cart/{id}           | yes  | Delete User's Cart                               |
| POST   | /checkout            | yes  | [TRANSACTION] Process cart, deduct stock, order. |
| POST   | /admin/products      | yes  | Add new product (Requires Admin Roles)           |
| PUT    | /admin/products/{id} | yes  | Update existing product (Requires Admin Roles)   |
| DELETE | /admin/products/{id} | yes  | Delete a product (Requires Admin Roles)          |

## 6. Storage & Assets
- Proxy Logic: Mirror the `seaweedProxy` from `neoserver`.
- Target bucket: `neomart-asset/neomart`
- Access URL: `dev.neotica.id/neomart/assets/{fileName}`
- Database Storage: The `products` table strictly stores the relative path, allowing the client to prepend the base URL dynamically.

## 7. Development Roadmap (Upcoming Features)
1. Order History (`GET /orders`)
  - Fetch all `orders` (and eager-load nested `order_items` via Exposed's `.with()`) where the `user_id` matches the JWT token.
2. Admin Order Fulfillment (`PUT /admin/orders/{id}`)
  - Secure route for store managers to change an order's `status` (e.g. from "COMPLETED" to "SHIPPED" or "CANCELLED").
3. Pagination & Search
  - Update `ProductRepositoryImpl` to read query parameters and use Exposed's `.limit()` and `.offset()` to prevent memory overflow on large datasets.

### Z. Current Project Structure
  ```text
  id.neotica.neomart
  /neomart
  ├── application
  │   ├── Frameworks.kt (Dependency Injection config)
  │   ├── Monitoring.kt (Call Logging config)
  │   ├── Routing.kt (Base Route config)
  │   ├── Security.kt (Authentication config)
  │   └── Serialization.kt (Serialization config)
  ├── data
  │   ├── dao
  │   │   ├── cart
  │   │   │   ├── CartTable.kt
  │   │   │   └── CartEntity.kt
  │   │   ├── order
  │   │   │   ├── OrderTable.kt
  │   │   │   └── OrderEntity.kt
  │   │   └── product
  │   │       ├── ProductTable.kt
  │   │       └── ProductEntity.kt
  │   ├── repository
  │   │   └── CartRepositoryImpl.kt
  │   │   └── ProductRepositoryImpl.kt
  │   └── DatabaseImpl.kt (Points to db_neomart)
  │   └── NeoDatabase.kt (Interface for DatabaseImpl.kt)
  ├── di
  │   └── NeoMartModule.kt (Koin module)
  ├── domain
  │   ├── repository
  │   │   └── mapper
  │   │       └── CartMapper.kt
  │   │       └── OrderMapper.kt
  │   │       └── ProductMapper.kt
  │   │   └── CartRepository.kt
  │   │   └── OrderRepository.kt
  │   │   └── ProductRepository.kt
  │   └── models (NeoProduct, etc.)
  │       └── CartItem.kt
  │       └── Order.kt
  │       └── OrderItem.kt
  │       └── Product.kt
  ├── route
  │   └── CartRoute.kt
  │   └── OrderRoute.kt
  │   └── ProductRoute.kt
  ├── utils
  │   └── Constants.kt
  │   └── Extensions.kt
  │   └── Utils.kt
  └── Application.kt (Frameworks.kt, Routing.kt, Authentication.kt)
  ```