# JAK ODPALIĆ PROJEKT - INSTRUKCJA

## Co potrzebujesz:
- Docker Desktop (pobierz z docker.com)
- To wszystko!

---

## 🔥 SZYBKIE ODPALENIE:

### 1. Sklonuj projekt:
```bash
git clone <link-do-repo>
cd internet-shop
```

### 2. **WAŻNE!** Stwórz plik .env:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/mydb
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET_KEY=supersekretnykluczjwtdlainternetshopaplikacji2025bezpieczny
SUPPORT_EMAIL=noreply@internetshop.local
```

> **BEZ TEGO KROKU APLIKACJA SIĘ NIE URUCHOMI!**

Spring możesz odpalić w IntelliJ IDEA bo może nie działać na docker

### 3. Odpala wszystko:
```bash
docker-compose up --build -d
```

### 4. Sprawdź czy działa:
- Aplikacja: http://localhost:9090/actuator/health
- Emaile: http://localhost:8025
- Swagger: http://localhost:9090/swagger-ui/index.html

---

## SZYBKI TEST:

### Wyślij testowy email:
```bash
curl -X POST "http://localhost:9090/auth/test-email?email=test@example.com"
```

### Sprawdź email:
Idź na: http://localhost:8025

---

## JEŚLI COKOLWIEK NIE DZIAŁA:

### 1. Sprawdź czy masz plik .env:
```bash
ls -la .env
```

### 2. Jeśli nie ma, stwórz go:
```bash
echo 'SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/mydb
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET_KEY=supersekretnykluczjwtdlainternetshopaplikacji2025bezpieczny
SUPPORT_EMAIL=noreply@internetshop.local' > .env
```

### 3. Zrestartuj:
```bash
docker-compose down
docker-compose up --build -d
```

### 4. Sprawdź logi:
```bash
docker-compose logs -f
```

---

## GOTOWE!

Jeśli wszystko działa:
- ✅ http://localhost:9090/actuator/health pokazuje `{"status":"UP"}`
- ✅ http://localhost:8025 otwiera interfejs Mailpit
- ✅ http://localhost:9090/swagger-ui/index.html pokazuje dokumentację API z możliwością testowania endpointów
- ✅ Test email działa

**Aplikacja gotowa do testowania! **

---

## TESTOWANIE API:

### Zarejestruj użytkownika:
```bash
curl -X POST http://localhost:9090/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"Password123"}'
```

### Sprawdź kod weryfikacyjny:
http://localhost:8025

### Zweryfikuj konto:
```bash
curl -X POST http://localhost:9090/auth/verify \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","verificationCode":"123456"}'
```

### Zaloguj się:
```bash
curl -X POST http://localhost:9090/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Password123"}'
```

**Gotowe! **
