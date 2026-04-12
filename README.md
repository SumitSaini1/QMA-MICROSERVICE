# QMA Microservice — Quantity Measurement App

A full-stack microservice application for performing quantity measurements and unit conversions. Built with Spring Boot microservices on the backend and Angular on the frontend, deployed on Railway (backend) and Netlify (frontend).

---

## 🏗️ Architecture Overview

```
                        ┌─────────────────────┐
                        │   Angular Frontend   │
                        │  (Netlify)           │
                        │  qmamicroservice     │
                        │  .netlify.app        │
                        └────────┬────────────┘
                                 │ HTTPS
                                 ▼
                    ┌────────────────────────┐
                    │      API Gateway        │
                    │  (Spring Cloud Gateway) │
                    │  Railway - Port 8080    │
                    └────┬──────────┬────────┘
                         │          │
              /auth/**   │          │  /api/quantity/**
                         ▼          ▼
             ┌──────────────┐  ┌──────────────────┐
             │ Auth Service │  │ Quantity Service  │
             │ Port 8081    │  │ Port 8082         │
             └──────┬───────┘  └──────────────────┘
                    │
                    ▼
          ┌──────────────────┐
          │  Eureka Service  │
          │  (Service Disc.) │
          │  Port 8761       │
          └──────────────────┘
```

---

## 🛠️ Tech Stack

### Backend
| Service | Technology | Port |
|---------|-----------|------|
| API Gateway | Spring Cloud Gateway | 8080 |
| Auth Service | Spring Boot + Spring Security + JWT + OAuth2 | 8081 |
| Quantity Service | Spring Boot | 8082 |
| Eureka Service | Spring Cloud Netflix Eureka | 8761 |

### Frontend
| Technology | Purpose |
|-----------|---------|
| Angular 17+ | SPA Framework |
| Tailwind CSS | Styling |
| TypeScript | Language |

### Infrastructure
| Service | Platform |
|---------|---------|
| Backend Services | Railway |
| Frontend | Netlify |
| Service Discovery | Eureka (self-hosted on Railway) |
| CDN/Edge | Fastly (via Railway) |

---

## 🚀 Services

### 1. API Gateway
- Single entry point for all client requests
- Routes `/auth/**` → Auth Service
- Routes `/api/quantity/**` → Quantity Service
- Handles CORS for both `localhost:4200` and `https://qmamicroservice.netlify.app`
- Uses Spring Cloud LoadBalancer with Eureka for service discovery
- Excludes Spring Security (stateless gateway)

### 2. Auth Service
- User signup and login with JWT tokens
- Google OAuth2 login support
- JWT token generation and validation
- Spring Security with `permitAll()` on `/auth/**` endpoints
- Session policy: `IF_REQUIRED` for OAuth2 compatibility

### 3. Quantity Service
- Protected endpoints (JWT required)
- Supports operations: `add`, `subtract`, `divide`, `compare`, `convert`
- Unit types: LENGTH (inches, feet, cm, etc.), WEIGHT, VOLUME, and more
- Controller base path: `/api/quantity`

### 4. Eureka Service
- Service registry for all microservices
- All services register with their `railway.internal` hostname
- Enables load-balanced routing via `lb://SERVICE-NAME`

---

## 📡 API Endpoints

### Auth Service (via Gateway)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/auth/signup` | None | Register new user |
| POST | `/auth/login` | None | Login, returns JWT |
| GET | `/oauth2/authorization/google` | None | Google OAuth2 login |

### Quantity Service (via Gateway)
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/quantity/add` | Bearer JWT | Add two quantities |
| POST | `/api/quantity/subtract` | Bearer JWT | Subtract two quantities |
| POST | `/api/quantity/divide` | Bearer JWT | Divide two quantities |
| POST | `/api/quantity/compare` | Bearer JWT | Compare two quantities |
| POST | `/api/quantity/convert` | Bearer JWT | Convert units |

### Request/Response Examples

**Signup**
```json
// POST /auth/signup
{ "username": "tushar", "password": "123" }

// Response 201
{ "id": 1, "username": "tushar" }
```

**Login**
```json
// POST /auth/login
{ "username": "tushar", "password": "123" }

// Response 200
{ "jwt": "eyJhbGci...", "id": 1 }
```

**Quantity Operation**
```json
// POST /api/quantity/subtract
// Authorization: Bearer <token>
{
  "value1": 8852,
  "unit1": "INCHES",
  "value2": 874,
  "unit2": "FEET"
}

// Response 200
{
  "operation": "SUBTRACTION",
  "value1": 8852.0,
  "unit1": "INCHES",
  "value2": 874.0,
  "unit2": "FEET",
  "resultValue": -1636.0,
  "resultUnit": "INCHES"
}
```

---

## ⚙️ Configuration

### Environment Variables

#### API Gateway (Railway)
| Variable | Value |
|----------|-------|
| `PORT` | `8080` |
| `EUREKA_URL` | `http://eureka-service.railway.internal:8761/eureka` |

#### Auth Service (Railway)
| Variable | Value |
|----------|-------|
| `PORT` | `8081` |
| `EUREKA_URL` | `http://eureka-service.railway.internal:8761/eureka` |
| `OAUTH_URI` | `https://auth-service-production-2f6f.up.railway.app/login/oauth2/code/google` |
| `FRONTEND_URL` | `https://qmamicroservice.netlify.app` |

#### Quantity Service (Railway)
| Variable | Value |
|----------|-------|
| `PORT` | `8082` |
| `EUREKA_URL` | `http://eureka-service.railway.internal:8761/eureka` |

#### Eureka Service (Railway)
| Variable | Value |
|----------|-------|
| `PORT` | `8761` |

---

## 🔐 Security

### JWT Flow
```
1. Client → POST /auth/login → Auth Service
2. Auth Service validates credentials → generates JWT
3. Client stores JWT in localStorage
4. Client sends JWT in Authorization: Bearer <token> header
5. Quantity Service validates JWT on every request
```

### Google OAuth2 Flow
```
1. Client → clicks "Login with Google"
2. Angular redirects to auth-service/oauth2/authorization/google
3. Auth Service → Google OAuth2 → redirects back with code
4. Auth Service generates JWT → redirects to frontend with token in URL
5. Frontend extracts token from URL → stores in localStorage
```

### CORS Configuration
Allowed origins in API Gateway `CorsConfig.java`:
- `http://localhost:4200` (local development)
- `https://qmamicroservice.netlify.app` (production)
- `https://api-gateway-production-4492.up.railway.app` (Swagger UI)
- `http://localhost:8080` (local Swagger)

---

## 🏃 Running Locally

### Prerequisites
- Java 17+
- Node.js 18+
- Angular CLI

### Backend
```bash
# Start Eureka first
cd eureka-service && mvn spring-boot:run

# Then start other services (in any order)
cd auth-service && mvn spring-boot:run
cd quantity-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
ng serve
# Visit http://localhost:4200
```

### Local URLs
| Service | URL |
|---------|-----|
| Frontend | http://localhost:4200 |
| API Gateway | http://localhost:8080 |
| Auth Service | http://localhost:8081 |
| Quantity Service | http://localhost:8082 |
| Eureka Dashboard | http://localhost:8761 |
| Swagger UI | http://localhost:8080/swagger-ui.html |

---

## 🚢 Deployment

### Backend (Railway)
Each service is deployed as a separate Railway service in the same project:
1. Connect GitHub repo to Railway
2. Set environment variables per service
3. Railway auto-detects Spring Boot and builds via Maven
4. Services communicate via `railway.internal` private network

### Frontend (Netlify)
1. Connect GitHub repo to Netlify
2. Build command: `npm run build`
3. Publish directory: `dist/qma-angular/browser`
4. Angular uses `environment.prod.ts` in production builds (via `fileReplacements` in `angular.json`)

---

## 📁 Project Structure

```
QMA-Microservice/
├── api-gateway/
│   └── src/main/
│       ├── java/.../config/
│       │   └── CorsConfig.java          # CORS allowed origins
│       └── resources/
│           └── application.yml          # Routes, Eureka, Gateway config
├── auth-service/
│   └── src/main/
│       ├── java/.../config/
│       │   └── SecurityConfig.java      # Spring Security, JWT, OAuth2
│       ├── java/.../security/
│       │   ├── JwtFilter.java           # JWT validation filter
│       │   └── OAuth2SuccessHandler.java # Redirects to frontend with token
│       └── resources/
│           └── application.properties
├── quantity-service/
│   └── src/main/
│       ├── java/.../controller/
│       │   └── QuantityMeasurementController.java  # /api/quantity/**
│       └── resources/
│           └── application.properties
├── eureka-service/
│   └── src/main/resources/
│       └── application.properties
└── frontend/ (Angular)
    └── src/
        ├── app/
        │   ├── core/
        │   │   ├── services/
        │   │   │   ├── auth.service.ts      # Login, signup, token mgmt
        │   │   │   └── quantity.service.ts  # Quantity API calls
        │   │   ├── interceptors/
        │   │   │   └── auth.interceptor.ts  # Auto-attach JWT, handle 401/403
        │   │   └── guards/
        │   │       └── auth.guard.ts        # Route protection
        │   └── pages/
        │       ├── home/
        │       ├── login/
        │       ├── signup/
        │       └── dashboard/
        ├── environments/
        │   ├── environment.ts             # Local: localhost:8080
        │   └── environment.prod.ts        # Production: Railway URLs
        └── _redirects                     # Netlify SPA routing fix
```

---

## 🐛 Common Issues & Fixes

| Issue | Cause | Fix |
|-------|-------|-----|
| `403 Forbidden` from Gateway | CORS origin not allowed | Add origin to `CorsConfig.java` |
| `401 Unauthorized` on quantity endpoints | Missing/expired JWT | Re-login to get fresh token |
| `500 No static resource` | Wrong `StripPrefix` value | Set `StripPrefix=0` for quantity-service |
| `404` on Netlify page refresh | Angular SPA routing | Add `src/_redirects` with `/* /index.html 200` |
| `redirect_uri_mismatch` on Google OAuth | URI not registered in Google Cloud | Add Railway auth-service URL to Google Console |
| Frontend hitting `localhost` in production | Missing `fileReplacements` in `angular.json` | Add `fileReplacements` for `environment.prod.ts` |
| Duplicate CORS headers | Both `globalcors` and `CorsConfig.java` active | Remove `globalcors` from `application.yml` |

---

## 📝 Swagger UI

API documentation available at:
```
https://api-gateway-production-4492.up.railway.app/swagger-ui.html
```

Aggregates docs from all services:
- Auth Service: `/aggregate/auth-service/v3/api-docs`
- Quantity Service: `/aggregate/quantity-service/v3/api-docs`

---

## 🔗 Live URLs

| Service | URL |
|---------|-----|
| Frontend | https://qmamicroservice.netlify.app |
| API Gateway | https://api-gateway-production-4492.up.railway.app |
| Auth Service | https://auth-service-production-2f6f.up.railway.app |
| Eureka Dashboard | https://eureka-service-production-eea8.up.railway.app |
| Swagger UI | https://api-gateway-production-4492.up.railway.app/swagger-ui.html |


