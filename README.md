# breveTest

**breveTest** is a lightweight, declarative API testing framework that enables you to test REST APIs **without writing any code**.  
You define your tests using simple `.breve` files, and **breveTest automatically executes them during your Maven build**.

---

## Why breveTest?

- ✅ No Java / JUnit / TestNG code required
- ✅ Simple key-value based test definitions
- ✅ Built-in authentication handling
- ✅ Supports **GET, POST, PUT, DELETE**
- ✅ Seamless Maven integration
- ✅ CI/CD friendly

---

## How It Works

1. Write API test definitions in `.breve` files
2. Place them under `src/test/breve`
3. Run:
   ```bash
   mvn clean install
   ```
4. **breveTest discovers and executes all test cases automatically**

---

## Project Structure

```text
src
 └── test
     └── breve
         ├── credentials.breve
         ├── product-tests.breve
```

### Rules

- All test files **must** be inside `src/test/breve`
- File extension **must be** `.breve`

---

## Installation(TODO)

Add the **breveTest** dependency to your project:

```xml
<dependency>
    <groupId>com.brevetest</groupId>
    <artifactId>brevetest</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

---

## Writing a `.breve` Test File

A `.breve` file contains **simple key-value pairs** that describe API requests and expected responses.

---

## Authentication Configuration

```properties
username=emilys
password=emilyspass
auth_url=https://dummyjson.com/auth/login
```

### What happens automatically?

- Performs authentication
- Extracts and stores the auth token
- Reuses the token for all subsequent requests

---

## HTTP Method Examples

### GET Request

```properties
get_url=https://dummyjson.com/products/1
get_expected_status_code=200
get_expected_status_message=OK
```

✔ Fetches a resource and validates the response

---

### POST Request

```properties
post_url=https://dummyjson.com/products/add
post_json={"title": "Breve Phone", "price": 799}
post_expected_status_code=201
post_expected_status_message=Created
```

✔ Creates a new resource with JSON payload

---

### PUT Request

```properties
put_url=https://dummyjson.com/products/1
put_json={"title": "Updated Phone", "price": 999}
put_expected_status_code=200
put_expected_status_message=OK
```

✔ Updates an existing resource

---

### DELETE Request

```properties
delete_url=https://dummyjson.com/products/1
delete_expected_status_code=200
delete_expected_status_message=OK
```

✔ Deletes a resource and validates the response

---

## Complete `.breve` Example

```properties
username=emilys
password=emilyspass
auth_url=https://dummyjson.com/auth/login

get_url=https://dummyjson.com/products/1
get_expected_status_code=200
get_expected_status_message=OK

post_url=https://dummyjson.com/products/add
post_json={"title": "Breve Phone", "price": 799}
post_expected_status_code=201
post_expected_status_message=Created

put_url=https://dummyjson.com/products/1
put_json={"title": "Updated Phone", "price": 999}
put_expected_status_code=200
put_expected_status_message=OK

delete_url=https://dummyjson.com/products/1
delete_expected_status_code=200
delete_expected_status_message=OK
```

---

## Running Tests

Execute:

```bash
mvn clean install
```

This will:

- Discover all `.breve` files
- Execute all API test cases
- Fail the build if **any test fails**

---

## Supported Configuration Keys

| Key                         | Description                  |
|-----------------------------|------------------------------|
| `*_url`                     | API endpoint                 |
| `*_json`                    | Request body (POST / PUT)    |
| `*_expected_status_code`    | Expected HTTP status code    |
| `*_expected_status_message` | Expected HTTP status message |

---

## Use Cases

- Microservices API testing
- Regression testing
- CI/CD pipeline validation
- Contract testing
- Quick API sanity checks

---

## Roadmap

- ✅ API testing using `.breve` files
- ✅ Unit testing support using `.breve` files
- ⏳ JSON response body assertions
- ⏳ Header validation
- ⏳ Environment-based configuration
- ⏳ Data-driven testing
- ⏳ HTML and JSON test reports

---

## License

**MIT License**
