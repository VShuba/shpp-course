# Java Practical Assignments – Dev Training

This repository contains solutions for 7 practical assignments related to Java, CI/CD, AWS, data processing, and design patterns. Each task was completed following best development practices and includes deployment and automation aspects.

## 📘 English Version

### Practical #1 – Message Generator JAR (Difficulty: 2 s.p.)

A Maven project builds a JAR that outputs a JSON message like:

```json
{ "message": "Hello <username from properties>!" }
```

* Uses `jackson-databind` for JSON/XML output switchable via system property.
* Username is taken from an external `.properties` file.
* Includes LogBack logger with various logging levels.
* JAR built in two profiles: thin (assembly plugin) and fat (shade plugin).
* Deployed to AWS EC2 and executed as a JAR.

---

### Practical #2 – Pre-Deploy Code Check System (Difficulty: 5 s.p.)

* Outputs a multiplication table with dynamic numeric types (via system variable).
* Range (min, max, step) is read from a properties file.
* CI/CD setup with GitHub/GitLab/CodeCommit + Jenkins or other CI.
* Tests implemented.
* Sonar recommendations applied in `dev` branch.
* Merge request to `main`, triggers Jenkins to deploy on EC2 automatically.

---

### Practical #3 – ActiveMQ Message Stream Validator (Difficulty: 5 s.p.)

* Stream-generates `N` random POJOs (with name, eddr, count, date).
* Sends to ActiveMQ queue (configurable via properties).
* Generation stops after time defined in `.properties` (via poison pill).
* Messages consumed in parallel, validated:

  * `name` length ≥ 7, contains 'a'
  * `count` ≥ 10
  * Valid EDDR number
* Valid messages → `valid.csv`; invalid → `invalid.csv` with error JSON.
* Full Jenkins integration with testing and performance benchmark (≥ 3k msg/sec).

---

### Practical #4 – PostgreSQL Data Processor (Difficulty: 8 s.p.)

* Created tables with DDL for Epitsentr products.
* Generated ≥ 3M records via JDBC, validated with Hibernate Validator.
* Console parameter for product type to fetch address with max quantity.
* Execution time measured with `StopWatch`.
* Used VisualVM/IDEA Profiler for threading/memory analysis.
* Applied parallel processing with `java.util.concurrent`.
* Optional: explored DuckDB performance for similar task.

---

### Practical #5 – NoSQL Version of Task #4

* Reimplemented Practical #4 using a NoSQL database (MongoDB, Firebase, etc).

---

### Practical #6 – Design Patterns Showcase

* Implemented **Builder**, **Decorator**, and **State** patterns in a separate GitHub repo.

---

### Practical #7 – RESTful API with Validation and Deployment

* Created full-featured REST controller with 5 HTTP methods (CRUD).
* DTO with ІПН (Ukrainian taxpayer number), includes custom validator.
* Valid data saved to in-memory DB (via Spring Data JPA).
* API deployed to AWS Elastic Beanstalk (without autoscaling).

---
