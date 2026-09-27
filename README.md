# Vehicle Rental Management Web Application

A Java web application for managing a mixed vehicle-rental fleet. Customers can register, browse vehicle categories, start rentals, and record returns. An administrative interface supports fleet management, service/incident handling, and rental or income reports. The application is backed by MySQL and is packaged as a Java web archive (WAR).

## Application workflow

1. The browser opens one of the HTML pages in `src/main/webapp`—for example, the landing, login, registration, customer, rental, return, or administration pages.
2. Page scripts use form submissions and AJAX calls to reach Java servlets. The servlets validate or interpret the request, call the database-layer classes, and return HTML or JSON for the page to display.
3. The database classes map request data to Java model objects and execute SQL against MySQL. The core fleet record is stored in `vehicles`; cars have extra attributes in the related `cars` table. Customers, current/completed rentals, and service events have their own tables.
4. Rental processing calculates the duration-based price and optional insurance cost, records an active rental, and increments the vehicle's rental count. Return processing calculates any additional charge and closes the active rental.
5. Administrative pages call reporting servlets for popular vehicles, rental-duration statistics, rental income, vehicle income, service costs, and incident handling.

The codebase also contains a small JAX-RS endpoint at `/resources/rest` that returns `ping`. Most application features use servlets rather than REST resources.

## Main features

- Customer registration and username checks, plus a username-based login flow.
- Fleet browsing and availability/cost queries for cars, motorbikes, bikes, and skateboards.
- Rental creation with driver information and optional insurance, followed by a return workflow with additional-cost calculation.
- Admin operations for adding vehicles/cars, reporting incidents, recording service, and viewing rental/popularity/income statistics.
- JSON conversion helpers used between request data, Java model objects, and SQL-backed records.

## Data model

The schema is created in `src/main/java/database/tables/`:

- **`vehicles`** is the common fleet table: registration number, type, rental count, kilometres, colour, model, brand, daily rental cost, and daily insurance cost.
- **`cars`** extends a vehicle identified by registration number with car type and passenger capacity. Its registration number references `vehicles`.
- **`customers`** stores customer identity/contact and driving/payment-card fields, plus a username.
- **`rented_vehs`** records a rental's vehicle, customer username, driver, rental/return dates and times, insurance, total cost, and active/completed status. It references `vehicles` by registration number.
- **`service`** stores a vehicle's service duration, date, and cost, and references `vehicles`.

The central relationships are one vehicle to optional car-specific details, and one vehicle to many rental and service records. Customer/rental association is represented by username in the current schema.

## Technology and project layout

- **Java 8 source level**, Maven, and a Java EE/Jakarta EE 8-compatible web container (`javax.*` APIs).
- **Servlets** in `src/main/java/servlets/` implement registration, login, checks, fleet/rental actions, incidents, and reports. `src/main/webapp/WEB-INF/web.xml` maps the servlet URLs.
- **Database access** in `src/main/java/database/`: `DB_Connection.java` opens MySQL connections; `database/tables/` contains table-specific queries and schema creation; `database/init/` initializes and seeds a development database.
- **Model classes** in `src/main/java/mainClasses/` represent vehicles, cars, customers, rentals, service events, and report data.
- **Web UI** in `src/main/webapp/` contains HTML pages, CSS, JavaScript/AJAX, and web application configuration.
- **`screenshots_vash/`** contains screenshots of the database tables and example query output.
- **`sxesiako_montelo.pdf`**, `Vaseis_Report.odt`, and `SQL_queries.odt` are project documentation and database-design/query artifacts.
- **`target/`** is generated Maven output (compiled classes and the WAR) and is excluded from Git.

## Database configuration

Database settings are read from process environment variables first, then from a local `.env` file in the application's working directory. Copy `.env.example` to `.env` for local development and set the MySQL account values there. The `.env` file is ignored by Git. In a deployed server, prefer configuring these values as environment variables because the server's working directory may not be the project folder.

Supported values:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD
```

The `.env` reader supports simple `KEY=VALUE` lines, blank lines, comments beginning with `#`, and optional matching single or double quotes around values. `DB_NAME` is restricted to letters, digits, and underscores because it is also used by the schema initializer.

## Build and deployment

Requirements: a JDK compatible with the configured Java 8 source/target level, Maven, MySQL, and a Java EE 8/Jakarta EE 8-compatible servlet container. Configure the database settings before starting the app.

Build the deployable WAR from the project root:

```sh
mvn clean package
```

The artifact is written to:

```text
target/Project_Vaseis-1.0-SNAPSHOT.war
```

Deploy the WAR to the compatible application server and open the context path assigned by that server. The MySQL connector and other application libraries are declared in `pom.xml`; the Java EE API is provided by the container.

### Development database initialization

`database.init.InitDatabase` creates the configured database, creates the five tables, inserts example vehicle/car data, and prints sample JSON conversions. The web application also exposes an `/InitDB` servlet that invokes this initializer. Use initialization only against a disposable local development database: it creates tables without migration logic, seeds example records, and the endpoint is a GET route with no authentication. Do not expose it in a deployed environment; disable or remove the route after local setup.

## Security and project status

This is an academic project, not a production-ready rental service. The existing login checks whether a username exists and includes a hard-coded admin username; it does not implement password-based authentication or sessions. Several database operations build SQL by concatenating request values, and customer records include payment-card and driving-licence fields. Do not use real customer/payment data or expose the app publicly without adding proper authentication and authorization, parameterized SQL, input/output protections, and appropriate handling of personal data.

The database configuration has been moved out of Java source into `.env`/environment variables. The local `.env` is ignored; use a dedicated, least-privilege MySQL account with a strong password instead of an administrator account.
