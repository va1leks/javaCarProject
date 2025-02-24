### **AutoMarket REST API**

## **Описание проекта**
AutoMarket – это REST-сервис на **Spring Boot**, предоставляющий API для работы с автомобилями в автосалоне. Сервис позволяет получать информацию об автомобилях, фильтровать их по марке и модели, а также находить автомобиль по его уникальному идентификатору.


Функционал:
- Запуск локального REST API.
- GET-запрос с Query Parameters для фильтрации автомобилей по марке и модели.
- GET-запрос с Path Parameters для поиска автомобиля по ID.
- Подключение CheckStyle для проверки кодстайла.


## **Задание**
1. **Создать и запустить локально REST-сервис** на Java (Spring Boot + Maven/Gradle).
2. **Добавить GET эндпоинт с Query Parameters** для фильтрации товаров.
3. **Добавить GET эндпоинт с Path Parameters** для поиска товара по ID.
4. **Настроить CheckStyle** и исправить ошибки.
5. **Формат ответа – JSON.**

## **Установка и запуск**
### **1. Клонирование репозитория**
```sh
git clone https://github.com/your-repo/autosalon.git
cd autosalon
```

### **2. Сборка и запуск приложения**
С использованием **Maven**:
```sh
mvn clean install
mvn spring-boot:run
```
С использованием **Gradle**:
```sh
gradle build
gradle bootRun
```

## **Доступные эндпоинты**
### **1. Получение автомобиля по ID**

Запрос:
```
GET /api/v1/cars/{id}
```
Пример запроса:

```
GET /api/v1/cars/1
```
Пример ответа:

```json
{
  "id": 1,
  "brand": "Toyota",
  "model": "Corolla",
  "year": 2020,
  "color": "White",
  "price": 20000
}
```

### **2. Получение списка автомобилей с фильтрацией**

Запрос:

```
GET /api/v1/cars?brand=Toyota&model=Corolla
```

Параметры:

brand (опционально) – марка автомобиля.
model (опционально) – модель автомобиля.

Пример запроса:


```
GET /api/v1/cars?brand=Toyota&model=Corolla
```

Пример ответа:

```json
[
  {
    "id": 1,
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2020,
    "color": "White",
    "price": 20000
  }
]
```

### **Настройка CheckStyle**

#### **Maven**
Добавьте в pom.xml:

```xml
Copy
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <configLocation>checkstyle.xml</configLocation>
    </configuration>
</plugin>
```

Запустите проверку:
```sh
mvn checkstyle:check
```

#### **Gradle**
Добавьте в `build.gradle`:
```groovy
plugins {
    id 'checkstyle'
}

checkstyle {
    toolVersion = '10.12.0'
    configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
}

tasks.withType(Checkstyle).configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true) 
    }
}
```
Запустите проверку:
```sh
gradle check
```

## **Требования**
- Java 17+
- Spring Boot 3+
- Maven/Gradle

## **Авторы**
va1leks

