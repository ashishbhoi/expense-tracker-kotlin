# This is a REST Api Build with Spring Boot using JPA and Lombok

## Features

- User can register and login `POST /api/users/register` `POST /api/users/login`
- User can create, update and delete categories `POST, PUT, DELETE /api/categories`
- User can create, update and delete transactions `POST, PUT, DELETE /api/categories/{category-id}/transactions`
- User can view all categories with total expense `GET /api/categories`
- User can view all transactions `GET /api/categories/{category-id}/transactions`

## Security

- JWT is used for authentication and authorization
- User can only access his own data
- User can only access his own categories and transactions
- You can generate a new token using `POST /api/users/login` and use the token in the `Authorization` header with the
  value `Bearer <token>` to access the protected routes.

## Protected Routes

- `POST, PUT, DELETE /api/categories/**`
- So you need to use JWT to access these routes.
- To get JWT you refer to the [Security](#security) section.

# How to run the application

1. Clone the repository
    ```bash
    git clone https://github.com/ashishbhoi/expense-tracker-kotlin.git
    ```

2. Modify the `expense-traker-kotlin.sql` file in `.` directory
    ```sql
    create user "<your-username>" with encrypted password '<your-password>';
   create database "<your-database-name>" with template=template0 owner "<your-username>";
    ```

3. (Optional) If you don't have postgresql installed, you can use docker to run the database server.
    ```bash
    docker run --name <your-container-name> -e POSTGRES_PASSWORD=<your-password> -p 5432:5432 -d postgres
    ```
   > You can use any name for the container and any password for the database user.
4. Run the sql file in the database server
    ```bash
    psql -h <your-sql-server-address> -U <your-slq-server-username> -f expense-traker-kotlin.sql
    ```
   > If you are using docker, you can use the following command to run the sql file.
    ```bash
    docker cp expense-traker-kotlin.sql <your-container-name>:/
    docker exec -i <your-container-name> psql -U <your-username> -f expense-traker-kotlin.sql
    ```
   > Default postgres username for docker is `postgres`.

5. Modify the `application.properties` file in `src/main/resources` directory
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://<your-sql-server-address>:5432/<your-database-name>
        username: <your-username>
        password: <your-password>
    ```

   > To user other database you need to modify the `gradle.build` file and add the dependency for the database driver.

   > Then you just need to change the `JDBC_URL` to your database url and `JDBC_USER` and `JDBC_PASS` to your database
   username and password.

   > If you are using docker, your `<sql-server-address>` will be `localhost`.

6. 

7. Run the application
    ```bash
    gradle bootRun
    ```
   if you don't have gradle installed, you can use the `gradlew` file in the root directory.
    ```bash
    ./gradlew bootRun
    ```