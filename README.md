# Cuoi5s Ecommerce Platform

Cuoi5s is a Java/Spring ecommerce web application built for multi-site storefronts. A single deployment can serve owner, client, and partner sites with site-specific domains, templates, catalog data, content areas, payment methods, shipping configuration, email templates, and settings.

The codebase includes a product catalog, Lucene-backed search, cart and checkout, promotion processing, order management, CMS/admin tooling, marketing email support, video/news/gallery content, and a nails/service booking module.

## Tech Stack

- Java 7 source/target
- Maven WAR packaging
- Servlet 2.5 web application
- Spring 3.1 MVC, Spring Security 3.0, Spring ORM, Spring scheduling
- Hibernate 3.6/JPA annotations with MySQL
- Hibernate Search/Lucene for product search
- Ehcache for application and Hibernate cache
- JSP/JSTL custom tags, SiteMesh decorators, WRO4J static resource bundling
- Velocity templates for transactional and marketing email
- Dockerfiles for Tomcat 7 application runtime and MySQL 5.7 database seed runtime

## Main Features

### Multi-Site Storefronts

Sites are first-class entities in `Site`, `SiteParam`, `SiteTemplate`, `SiteHeaderFooter`, and related models. The request context resolves the active site, and most catalog, CMS, shipping, payment, email, and user data is scoped to that site.

Relevant code:

- `src/main/java/com/easysoft/ecommerce/model/Site.java`
- `src/main/java/com/easysoft/ecommerce/service/impl/SiteServiceImpl.java`
- `src/main/java/com/easysoft/ecommerce/controller/SiteController.java`
- `src/main/java/com/easysoft/ecommerce/controller/admin/SiteAdminController.java`
- `src/main/java/com/easysoft/ecommerce/web/filter/RequestContextFilter.java`

### Catalog and Search

Catalog management supports categories, products, product variants, product files/images, refinements, attributes, related products, and site-specific product assignment. Public catalog pages render through JSP views, while admin controllers handle product/category CRUD and image management.

Search uses Hibernate Search/Lucene over `Product` fields such as name, model, keyword, category, site, price, promo price, active status, and variant inventory.

Relevant code:

- `src/main/java/com/easysoft/ecommerce/controller/CatalogController.java`
- `src/main/java/com/easysoft/ecommerce/controller/SearchController.java`
- `src/main/java/com/easysoft/ecommerce/controller/admin/CatalogAdminController.java`
- `src/main/java/com/easysoft/ecommerce/model/Product.java`
- `src/main/java/com/easysoft/ecommerce/model/ProductVariant.java`
- `src/main/java/com/easysoft/ecommerce/service/impl/SearchServiceImpl.java`
- `src/main/webapp/catalog/`

### Cart, Checkout, Payments, and Promotions

Checkout is session-based. Cart items are stored in `SessionObject`/`OrderMap`, then moved into persisted `Order` and `OrderSession` records during payment. The checkout flow includes cart updates, billing/shipping capture, review, payment redirect/callback, receipt, and promo-code application.

Checkout calculations are composed through `commerce-component.xml`. The configured process chains run inventory checks, price calculation, shipping calculation, and promotion application.

Promotion behavior is class-driven. Promotions reference a promotion class, which is loaded reflectively and executed as an `Award`. Conditions are also modeled as pluggable classes.

Payment behavior is also class-driven through `Payment` implementations.

Relevant code:

- `src/main/java/com/easysoft/ecommerce/controller/AddItemToCartController.java`
- `src/main/java/com/easysoft/ecommerce/service/impl/OrderServiceImpl.java`
- `src/main/java/com/easysoft/ecommerce/service/component/`
- `src/main/java/com/easysoft/ecommerce/service/promotion/`
- `src/main/java/com/easysoft/ecommerce/service/condition/`
- `src/main/java/com/easysoft/ecommerce/service/payment/`
- `src/main/webapp/WEB-INF/commerce-component.xml`
- `src/main/webapp/checkout/`

Supported payment implementations include COD, bank transfer, My Account balance, BaoKim, and NganLuong.

### Order Management System

The OMS/admin order module lists orders, filters by date/status, stores serialized checkout sessions, and supports order status changes. `OrderServiceImpl` handles order creation, inventory decrease, service-order handling, partner commission calculation, and status-change side effects.

Relevant code:

- `src/main/java/com/easysoft/ecommerce/controller/admin/OrdersAdminController.java`
- `src/main/java/com/easysoft/ecommerce/service/impl/OrderServiceImpl.java`
- `src/main/java/com/easysoft/ecommerce/model/Order.java`
- `src/main/java/com/easysoft/ecommerce/model/OrderSession.java`
- `src/main/java/com/easysoft/ecommerce/dao/OrderDao.java`
- `src/main/java/com/easysoft/ecommerce/dao/OrderSessionDao.java`

### CMS, Design, and Admin

The admin area includes modules for site settings, pages, menus, CMS areas, content blocks, templates, design rows/parts, users, customers, cache controls, gallery, news, video import, partners, catalog, products, categories, utilities, and system actions.

Relevant code:

- `src/main/java/com/easysoft/ecommerce/controller/admin/`
- `src/main/java/com/easysoft/ecommerce/service/impl/ContentServiceImpl.java`
- `src/main/java/com/easysoft/ecommerce/model/CmsArea.java`
- `src/main/java/com/easysoft/ecommerce/model/CmsAreaContent.java`
- `src/main/java/com/easysoft/ecommerce/model/Menu.java`
- `src/main/java/com/easysoft/ecommerce/model/Template.java`
- `src/main/webapp/admin/`
- `src/main/webapp/WEB-INF/tags/`

### Content, Marketing, Jobs, and Service Booking

Other modules include news, video import/display, galleries/albums, contact forms, marketing email subscriptions, email plans, scheduled cleanup jobs, file/image handling, YouTube integration, and a nails/service booking API with customers, employees, services, appointments, and checkout.

Relevant code:

- `src/main/java/com/easysoft/ecommerce/controller/NewsController.java`
- `src/main/java/com/easysoft/ecommerce/controller/VideoController.java`
- `src/main/java/com/easysoft/ecommerce/controller/MarketingController.java`
- `src/main/java/com/easysoft/ecommerce/controller/NailController.java`
- `src/main/java/com/easysoft/ecommerce/service/impl/JobsServiceImpl.java`
- `src/main/java/com/easysoft/ecommerce/service/impl/NailManagementServiceImpl.java`
- `src/main/java/com/easysoft/ecommerce/util/jobs/`
- `src/main/resources/emailtemplate/`

## Repository Layout

```text
.
├── pom.xml                              Maven build and dependency configuration
├── Dockerfile                           Tomcat 7 runtime image for target/cuoi5s.war
├── Dockerfile_mysql                     MySQL 5.7 image seeded from database dumps
├── databases/                           SQL dumps for cuoi5s and images databases
├── env/                                 Example Tomcat/database environment files
├── src/main/java/com/easysoft/ecommerce
│   ├── controller/                      Public MVC and JSON controllers
│   ├── controller/admin/                Admin MVC controllers
│   ├── dao/ and dao/impl/               DAO interfaces and Hibernate implementations
│   ├── model/                           JPA/Hibernate entities
│   ├── security/                        Spring Security authentication support
│   ├── service/                         Service interfaces and domain services
│   ├── service/component/               Checkout processing components
│   ├── service/payment/                 Payment provider integrations
│   ├── service/promotion/               Promotion award implementations
│   ├── service/condition/               Promotion condition implementations
│   ├── util/                            Shared utilities and scheduled job helpers
│   └── web/                             Filters, listeners, servlets, tags, cache helpers
├── src/main/resources/                  Hibernate, cache, messages, themes, email templates
└── src/main/webapp/                     JSP views, WEB-INF config, admin UI, static assets
```

## Runtime Flow

1. `web.xml` starts Spring root contexts and the Spring MVC dispatcher.
2. `RequestContextFilter` resolves site/request context for each request.
3. Controllers call services and DAOs through Spring injection or `ServiceLocator`.
4. Hibernate persists data to MySQL and uses Ehcache plus Hibernate Search indexes.
5. JSP views render storefront/admin pages through SiteMesh decorators and tag files.
6. Checkout calls `RunProcessComponent`, which loads configured component classes from `commerce-component.xml`.
7. Payment providers build external request URLs and verify callback responses.

## Local Setup

### Prerequisites

- JDK 7-compatible toolchain
- Maven 3.x
- MySQL 5.7-compatible database
- Tomcat 7 or the configured Maven Jetty plugin
- ImageMagick/GraphicsMagick-compatible tooling if using image processing paths

### Database

The app expects two MySQL schemas from the SQL dumps:

- `databases/cuoi5s.sql`
- `databases/images.sql`

The current Hibernate configuration points at a MySQL host named `mysql` and database `cuoi5s`:

```text
jdbc:mysql://mysql:3306/cuoi5s?useUnicode=true&characterEncoding=UTF-8
```

For local development, either run MySQL on a Docker network where the app can resolve `mysql`, or update `src/main/resources/hibernate.cfg.xml` / Tomcat environment configuration to match your local database host and credentials.

### Build

```bash
mvn clean package
```

This produces:

```text
target/cuoi5s.war
```

### Run with Maven Jetty

The `pom.xml` includes an old Jetty plugin configured for ports `80` and `443`. For local development, change those ports to non-privileged values such as `8080` and `8443`, or run with a servlet container of your choice.

```bash
mvn jetty:run
```

### Run with Docker

Build the application WAR first, then build the app image:

```bash
mvn clean package
docker build -t cuoi5s:latest .
```

The repository also includes a MySQL image definition that copies the SQL dumps:

```bash
docker build -f Dockerfile_mysql -t cuoi5s-mysql:latest .
```

The historical run notes in `guide.txt` use a shared Docker network named `my-network` and run the app container on host port `8081`.

## Configuration Notes

- Main Spring configuration lives in `src/main/webapp/WEB-INF/spring/`.
- Hibernate mappings and database settings live in `src/main/resources/hibernate.cfg.xml`.
- Default locale, timezone, theme, message bundle, and working directory live in `src/main/webapp/WEB-INF/config.properties`.
- Checkout component chains live in `src/main/webapp/WEB-INF/commerce-component.xml`.
- URL rewrites live in `src/main/webapp/WEB-INF/urlrewrite.xml`.
- WRO4J bundles live in `src/main/webapp/WEB-INF/wro.xml`.
- Email templates live in `src/main/resources/emailtemplate/`.
- Theme message bundles live in `src/main/resources/themes/`.

Important: this legacy codebase currently contains hardcoded database and email credentials in configuration files. Before using this outside a local/dev environment, rotate those credentials and move secrets into environment-specific configuration.

## Admin and Security

Spring Security protects `/admin/**` routes with role-based access. Notable roles include:

- `ROLE_SYSTEM_ADMIN`
- `ROLE_SITE_ADMIN`
- `ROLE_MANAGE_ORDER`
- `ROLE_SITE_CONTENT_ADMIN`
- `ROLE_SITE_VIDEO_ADMIN`
- `ROLE_SITE_PARTNER_ADMIN`

Admin login is routed through `/admin/login.html`; static admin assets under `/admin/assets/**` are excluded from authentication.

## Scheduled Jobs

Spring scheduling is enabled in `applicationContext.xml`. Active scheduled tasks include:

- Daily user-session cleanup through `JobsServiceImpl.deleteUserSession`
- Weekly demo-site expiration updates through `JobsServiceImpl.updateExpiredDateForDemoSite`

Additional product import, inventory import, promo price, marketing email, and video statistic jobs exist in code but are commented out or run manually.

## Testing

The repository has test dependencies for JUnit, Spring Test, and HtmlUnit, plus a DBUnit import fixture at `src/test/resources/import.xml`. There are no Java test classes currently present under `src/test`.

Useful validation commands:

```bash
mvn test
mvn clean package
```

## Deployment

`build.sh` and `deploy.sh` document the historical Tomcat deployment flow:

1. Pull latest code.
2. Build `target/cuoi5s.war`.
3. Stop Tomcat.
4. Copy the WAR to Tomcat as `ROOT.war`.
5. Remove exploded app/work/temp directories.
6. Restart Tomcat.

The Docker application image follows the same WAR model and copies `target/cuoi5s.war` into Tomcat as `ROOT.war`.

## Development Tips

- Keep feature work scoped by module: controller, service, DAO/model, JSP/admin UI, and configuration usually change together.
- Re-run checkout calculations by using the existing component-chain pattern instead of duplicating cart math in controllers.
- New promotion behavior should implement `Award`; new promotion checks should implement `Condition`.
- New payment providers should implement `Payment` and be registered in the database so checkout can load the provider class.
- Site-specific behavior should usually be stored in `SiteParam` or site-scoped entities rather than hardcoded globally.
