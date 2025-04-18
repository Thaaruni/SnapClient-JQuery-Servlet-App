# 👨‍💼 SnapClient - Servlet jQuery Customer Manager App

Welcome to the **SnapClient** – a lightweight yet powerful customer management application built using Java Servlets, jQuery, and PostgreSQL. It features a smooth, animated UI powered by Bootstrap and SCSS, with robust backend logic and test coverage.

---

##  Why Java Servlets & jQuery?

### 🟠 Java Servlets (Backend)

Java Servlets offer a mature, efficient, and scalable way to handle HTTP requests. For this app, Servlets act as RESTful endpoints (`/v1/customers`) that manage:

- `POST` requests for uploading customer data and images (with multipart support)
- `GET` requests to retrieve all customers
- `DELETE` requests for removing customers

We chose Servlets because:

- They integrate tightly with Java EE technologies.
- They give full control over request/response lifecycle.
- They’re easy to unit test and deploy on servlet containers like Tomcat or Jetty.
- With tools like DBCP2, they allow efficient DB connection pooling.

### 🟠 jQuery (Frontend)

jQuery may be old school, but it's fast, lightweight, and perfect for dynamic DOM manipulation and AJAX interactions. In this app:

- `XMLHttpRequest` and `FormData` are used to asynchronously send/receive JSON and file data.
- DOM elements are dynamically updated based on API responses.
- Drag-and-drop image preview, progress bars, modal management, and event delegation are all handled with clean jQuery code.

It’s a solid fit when you don’t need a full frontend framework like React or Vue.

---

##  PostgreSQL Database

We used PostgreSQL for:

- A robust, open-source relational database that scales well.
- Storing structured customer data with strong ACID guarantees.
- Binary storage of image data via the file system (with path refs in DB).

### 🟠 Schema Highlights

- UUIDs as primary keys
- Indexing for fast lookup
- Nullable image fields for flexibility

---

##  Testing: H2 + JUnit

To keep the backend reliable and maintainable, we wrote test cases using:

-  JUnit 5 for unit testing DAO and servlet logic
-  H2 in-memory database for fast, isolated, throwaway tests
-  Assertions for response status, JSON validity, and DB integrity

### 🟠 Why test?

- Catch regressions early
- Improve confidence during refactoring
- Ensure servlet behavior matches expectations
- Test DB interactions without touching production data

---

##  UI & UX Highlights

-  SCSS styling for cleaner and modular CSS
-  Animate.css for smooth transitions (fade-in, fade-out rows)
-  Drag-and-drop + preview upload for profile images
-  AJAX-driven table with live updates (no reloads)
-  Responsive design tweaks for smaller viewports
-  Progress bar for file uploads and deletions

---

##  Special Things Worth Noting

- Full client-server separation
- Multipart file handling using native Servlet API
- Bootstrap 5 modal with form validation and file management
- Custom spinners and loaders for enhanced UX
- Seamless CRUD experience with zero page reloads
- Modular frontend code using ES modules (`main.js`)
- Modern form validation with feedback and UI hints

---

##  Tech Stack

| Layer        | Technology                                   |
|--------------|----------------------------------------------|
| Frontend     | HTML, SCSS, Bootstrap, jQuery, Animate.css   |
| Backend      | Java Servlets, Jakarta EE, DBCP2             |
| Database     | PostgreSQL                                   |
| Testing      | JUnit 5, H2                                  |
| Build Tool   | Gradle                                       |

---


