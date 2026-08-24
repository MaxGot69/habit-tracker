# Habit Tracker API

REST API для отслеживания привычек с JWT-авторизацией, статистикой и сериями.

## Стек

- Java 21, Spring Boot 4.1.0, Spring Security + JWT, PostgreSQL, Liquibase, Swagger, JUnit 5

Swagger: http://localhost:8080/swagger-ui/index.html

##  Эндпоинты

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| POST | `/api/auth/register` | Регистрация |
| POST | `/api/auth/login` | Логин → JWT |
| POST | `/api/habits` | Создать привычку |
| GET | `/api/habits` | Все привычки |
| GET | `/api/habits/{id}` | Привычка по ID |
| PUT | `/api/habits/{id}` | Обновить |
| DELETE | `/api/habits/{id}` | Удалить |
| POST | `/api/habits/{id}/records` | Отметить выполнение |
| GET | `/api/habits/{id}/records` | Записи привычки |
| GET | `/api/habits/{id}/stats` | Статистика |
| GET | `/api/stats/daily` | Сводка за сегодня |
| GET | `/api/stats/week` | Прогресс за неделю |

##  Авторизация

```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"user","password":"secret","email":"user@example.com"}'
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"user","password":"secret"}'
```

Используй токен: `Authorization: Bearer <token>`

##  Тесты

```bash
mvn test
```

---
