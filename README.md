# Hatters Backend

**Hatters Backend** — это серверная часть приложения для сервера по майнкрафту, платформы для покупки проходок, получения всей нужной информации, а также профиля игрока.

---

## Сам сайт приложения
Для полноценного опыта пользования приложением необходимо перейти и скачать ещё фронт данного приложения. Это можно сделать по следующей ссылке [Hatters Front](https://github.com/tailorsky/hatters-front)

---

## Технологический стек

Проект построен на следующих инструментах разработки:

- Java 17 
- Spring Boot 
- Maven 
- PostgreSQL

---

## Архитектура проекта

Код организован по модульному принципу, что облегчает тестирование и поддержку:

* **Controllers:** Обработка входящих HTTP-запросов и валидация входных данных.
* **Services:** Основная бизнес-логика приложения.
* **Models/Entities:** Описание структур данных и схем базы данных.
* **DTO (Data Transfer Objects):** Определение контрактов для передачи данных между слоями.

---

## Быстрый старт (который чуть-чуть долгий)

### 1. Клонирование репозитория
```bash
git clone [https://github.com/tailorsky/hatters-backend.git](https://github.com/tailorsky/hatters-backend.git)
cd hatters-backend
```
### 2. Настройка БД
Для запуска программы постребуется установить PostgreSQL, и создать в ней базу данных 
```
CREATE DATABASE hatters;
```
После этого в **application.properties**:
```
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```
Вместо **DB_USERNAME** можно вписать postgres, а вместо **DB_PASSWORD** пароль вашей базы даннх.

### 3. Настройка Discord
Переходим по ссылке: [Discord for Developers](https://discord.com/developers/applications)
Далее необходимо создать собственное приложение

<img width="1895" height="245" alt="image" src="https://github.com/user-attachments/assets/63c792e5-1167-432e-9ba4-f9534459b3cd" />

После создания приложения переходим во вкладку **O2Auth** и там находим **Client information**
Там вы найдете Client ID и Secret, которые вставляем в **application.properties**:
```
spring.security.oauth2.client.registration.discord.client-id=${DISCORD_CLIENT_ID}
spring.security.oauth2.client.registration.discord.client-secret=${DISCORD_CLIENT_SECRET}
```
### 4. Финишная прямая
После всех этих настроек просто воспользуемся командой:
```
mvn spring-boot:run
```

---

## Тесты
Для тестов просто использовать команду:
```
mvn test
```

---



