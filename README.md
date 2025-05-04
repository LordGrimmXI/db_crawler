**DB Crawler App** - A Database Schema Crawler & Model Generator

A Java/Spring Boot application that connects to a MySQL database, extracts schema metadata (tables, columns, relationships, indexes), and dynamically generates JPA-annotated model classes. Includes REST APIs for metadata inspection and model generation.

**🚀 Features:**

- **Database Schema Crawling**: Extract table/column metadata, primary/foreign keys, and indexes.
- **Dynamic Model Generation**: Auto-generate JPA entities with Lombok, `_@OneToMany_`, `_@ManyToOne_`, and `_@ManyToMany_` annotations.
- **REST API Support**: Fetch metadata or trigger model generation via HTTP endpoints.
- **Configurable**: Define database credentials and output paths via `_config.json_`.
