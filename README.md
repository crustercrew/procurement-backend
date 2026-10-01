# Procurement Microservices System (Procure-to-Pay)

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot 4.1.1](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud 2025.1.3](https://img.shields.io/badge/Spring%20Cloud-2025.1.3-blue.svg)](https://spring.io/projects/spring-cloud)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)
[![OAuth2 & JWT](https://img.shields.io/badge/Security-OAuth2%20%2F%20JWT-red.svg)](https://spring.io/projects/spring-security)
[![OpenAPI / Swagger](https://img.shields.io/badge/API%20Doc-Swagger%20UI-85EA2D.svg)](https://swagger.io/)

Arsitektur backend sistem pengadaan barang (Procure-to-Pay) berbasis **Microservices** menggunakan ekosistem **Java Spring Cloud** dan **Spring Boot**. Sistem ini dirancang untuk mendemonstrasikan pemisahan domain layanan, konfigurasi terpusat, penemuan layanan dinamis, komunikasi antar-layanan menggunakan OpenFeign, Spring Data REST, dan keamanan tingkat lanjut berbasis OAuth2 & JWT Role-Based Access Control (RBAC).

---

## 1. Arsitektur Sistem & Komponen

Sistem mengadopsi pola arsitektur **Cloud-Native Microservices** dengan komponen-komponen terisolasi:

```
                                  +-----------------------+
                                  |    Postman / Client   |
                                  +-----------+-----------+
                                              |
                                              v (Port 8080)
+-------------------+             +-----------------------+
|  Config Server    |             |      API Gateway      | (JWT Validation & RBAC)
|  (Port 8888)      |             +-----------+-----------+
+---------+---------+                         |
          | (Shared Config)                   | Dynamic Routing
          +-------------------------+         v
                                    |   +---------------------------------------------+
+-------------------+               |   |                                             |
| Discovery Server  |<--------------+   |                                             |
| (Eureka: 8761)    |                   v                                             v
+---------+---------+          +-----------------+    OpenFeign Call    +---------------------+
          ^                    |  User Service   |  (Lookup Item & Price)| Requisition Service |
          | Registry           |   (Port 8081)   |                      |     (Port 8083)     |
          |                    +--------+--------+                      +----------+----------+
          |                             |                                          |
          |                             v                                          v
          |                    +-----------------+                                 |
          +--------------------+ Catalog Service |<--------------------------------+
                               |   (Port 8082)   |
                               +--------+--------+
                                        |
                                        v
                            +-----------------------+
                            | PostgreSQL Database   | (Port 54320 / Docker)
                            +-----------------------+
```

---

## 2. Overview of Tech Stack (Requirement 5a)

| Kategori | Teknologi | Deskripsi / Versi |
| :--- | :--- | :--- |
| **Language** | Java 17 (LTS) | Bahasa pemrograman utama dengan modern features (Record, Pattern Matching, Text Blocks). |
| **Framework** | Spring Boot 4.1.1 | Fondasi microservice mandiri, autoconfiguration, dan embedded server. |
| **Cloud Ecosystem** | Spring Cloud 2025.1.3 | Orkestrasi microservices (Eureka, Gateway, Config Server, OpenFeign). |
| **Service Discovery** | Spring Cloud Netflix Eureka | Dynamic service registration dan client-side load balancing. |
| **API Gateway** | Spring Cloud Gateway (Reactive) | Single entry point, path routing, JWT validation, dan authorization enforcement. |
| **Centralized Config**| Spring Cloud Config Server | Manajemen konfigurasi terpusat untuk profil environment microservices. |
| **Inter-Service Call**| Spring Cloud OpenFeign | Declarative HTTP REST client untuk komunikasi sinkron antar microservice. |
| **Data Access** | Spring Data JPA / Hibernate | ORM untuk pemetaan entitas basis data relasional. |
| **HATEOAS REST** | Spring Data REST | RESTful endpoints otomatis berbasis standar HAL/Hypermedia. |
| **API Documentation** | SpringDoc OpenAPI 2.6.0 (Swagger UI) | Dokumentasi interaktif visual untuk seluruh endpoints microservice. |
| **Unit Testing** | JUnit 5 & Mockito | Pengujian unit terisolasi untuk business logic PR & approval. |
| **Security & Auth** | Spring Authorization Server & JWT | OAuth2 Client Credentials, Token Signing via RSA KeyPair (JWKS), dan RBAC. |
| **Database** | PostgreSQL 16 (Alpine) | RDBMS relasional untuk persistensi data transaksi pengadaan. |
| **Containerization** | Docker & Docker Compose | Standardisasi deployment database dan microservices. |
| **Build Tool** | Apache Maven 3.9+ | Manajemen dependensi multi-module dan proses kompilasi. |

---

## 3. Why This Tech Stack Was Chosen (Requirement 5b)

1. **Spring Cloud Ecosystem Synergy**:
   - Memilih Spring Cloud (Eureka, Gateway, Config Server) memberikan integrasi *out-of-the-box* yang stabil tanpa perlu perkakas pihak ketiga yang rumit. Penemuan layanan (Eureka) dan gateway bekerja otomatis dengan konfigurasi minimal.
2. **Pemisahan Domain Tanggung Jawab (Domain Decoupling)**:
   - Domain `User`, `Catalog`, dan `Requisition` dipisahkan ke dalam service independen. Jika beban pembuatan PR (Requisition) meningkat, service tersebut dapat di-scale secara horizontal tanpa membebani service katalog atau user.
3. **Declarative HTTP Communication (OpenFeign)**:
   - OpenFeign menghilangkan boilerplate code `RestTemplate` atau `WebClient`. Pemanggilan REST antar-service cukup didefinisikan lewat Java Interface, meningkatkan keterbacaan kode dan kemudahan *unit testing*.
4. **Centralized Configuration & Dynamic Profiles**:
   - Penggunaan Config Server memungkinkan perubahan kredensial database atau parameter bisnis tanpa perlu me-rebuild image aplikasi.
5. **Centralized Security Enforcement**:
   - Keamanan ditegakkan di level API Gateway. Token JWT diverifikasi secara kriptografis menggunakan public key dari JWK Set (`/oauth2/jwks`) tanpa perlu menghubungi database otentikasi di setiap request.

---

## 4. Daftar Service & Port Allocation

| Service | Port | Deskripsi Fungsi | Health / Info URL |
| :--- | :---: | :--- | :--- |
| **Discovery Service** | `8761` | Netflix Eureka Service Registry & Dashboard | `http://localhost:8761` |
| **Config Service** | `8888` | Spring Cloud Config Server (Shared Configuration) | `http://localhost:8888/{service-name}/default` |
| **API Gateway** | `8080` | Entry point tunggal, routing dinamis & JWT Security | `http://localhost:8080` |
| **User Service** | `8081` | OAuth2 Auth Server, REST Login, User Management, Swagger | `http://localhost:8081/swagger-ui.html` |
| **Catalog Service** | `8082` | Spring Data REST untuk Kategori & Item Katalog, Swagger | `http://localhost:8082/swagger-ui.html` |
| **Requisition Service** | `8083` | Business Logic PR, Review Approval, Feign Client, Swagger | `http://localhost:8083/swagger-ui.html` |
| **PostgreSQL Database**| `54320`| Database PostgreSQL container (`procurement_db`) | Port mapping host: `54320` (internal: `5432`) |
| **pgAdmin (Opsional)** | `5050` | Database Web UI Management (Auto-Connect Server) | `http://localhost:5050` |

---

## 5. Setup & Running Instructions (Requirement 5c)

Sistem ini mendukung **2 opsi cara menjalankan**:

### Prasyarat Lingkungan (Prerequisites)
- **Java 17 (JDK)** terpasang (`java -version`).
- **Docker & Docker Compose** terpasang dan berjalan (`docker --version`).
- Port `8080`, `8081`, `8082`, `8083`, `8761`, `8888`, dan `54320` tersedia (tidak terpakai aplikasi lain).

---

### OPSI 1: Manual / Hybrid Start (Sangat Direkomendasikan untuk Dev & Evaluasi)

Di opsi ini, PostgreSQL dijalankan via Docker (atau lokal), sementara microservices Java dijalankan langsung lewat terminal atau IDE (IntelliJ IDEA / VS Code).

#### Langkah 0: Inisialisasi Database (Wajib Dilakukan Sebelum Start Service Java!)
Microservices (`user-service`, `catalog-service`, dan `requisition-service`) membutuhkan koneksi database aktif saat booting untuk Hibernate JPA initialization.

* **Cara A: Menggunakan Docker Postgres (Paling Mudah & Otomatis)**:
  Cukup jalankan container postgres di background:
  ```bash
  docker compose up -d postgres
  ```
  *Container akan otomatis membuat database `procurement_db` dengan user `postgres` dan password `postgrespassword` pada port `54320`.*

* **Cara B: Menggunakan PostgreSQL Lokal (Non-Docker / Native)**:
  Jika Anda menggunakan database PostgreSQL lokal yang terinstall di laptop:
  1. Pastikan PostgreSQL berjalan di port `54320` (atau sesuaikan port di shared-config).
  2. Buka psql atau query editor, lalu buat databasenya:
     ```sql
     CREATE DATABASE procurement_db;
     ```
  3. *(Opsional)*: Eksekusi skrip `DDL_procurement.sql` jika ingin membuat struktur tabel di awal. Namun, Hibernate JPA sudah disetel `ddl-auto: update` sehingga tabel dan relasi foreign key akan otomatis di-generate saat microservices pertama kali start.

#### Langkah 1: Build Seluruh Modul Microservices
Gunakan Maven Wrapper yang sudah tersedia di root:

* **Windows (Command Prompt / PowerShell)**:
  ```cmd
  .\mvnw.cmd clean package -DskipTests
  ```
* **Linux / macOS**:
  ```bash
  chmod +x mvnw
  ./mvnw clean package -DskipTests
  ```

#### Langkah 2: Jalankan Microservices secara Berurutan
Buka terminal terpisah untuk masing-masing service (atau Run via Run Configuration IntelliJ IDEA) dengan **urutan wajib** sebagai berikut:

1. **Discovery Service** (Wajib Pertama):
   ```bash
   # Windows:
   .\mvnw.cmd -pl discovery-service spring-boot:run
   # Linux:
   ./mvnw -pl discovery-service spring-boot:run
   ```
   *(Tunggu hingga dashboard `http://localhost:8761` dapat diakses di browser)*.

2. **Config Service** (Wajib Kedua):
   ```bash
   .\mvnw.cmd -pl config-service spring-boot:run
   ```
   *(Tunggu hingga konfigurasi shared aktif)*.

3. **Core Services** (Dapat dijalankan bersamaan):
   ```bash
   # Terminal 3:
   .\mvnw.cmd -pl user-service spring-boot:run

   # Terminal 4:
   .\mvnw.cmd -pl catalog-service spring-boot:run

   # Terminal 5:
   .\mvnw.cmd -pl requisition-service spring-boot:run
   ```

4. **API Gateway** (Jalankan Terakhir):
   ```bash
   # Terminal 6:
   .\mvnw.cmd -pl api-gateway spring-boot:run
   ```

---

### OPSI 2: Full Docker (Semua Berjalan di Container)

Jika Anda ingin menjalankan seluruh ekosistem (database dan seluruh 6 microservices) di dalam container Docker sekaligus:

1. **Kompilasi paket JAR dari root**:
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Build dan jalankan seluruh container**:
   ```bash
   docker compose up --build
   ```

3. **Akses pgAdmin Web UI (Auto-Connected)**:
   - Buka browser: `http://localhost:5050`
   - Login: `admin@procurement.com` / `admin`
   - Di panel kiri menu **Servers**, server `Procurement DB` sudah otomatis terhubung ke container PostgreSQL!

4. **Hentikan container**:
   ```bash
   docker compose down
   ```

---

## 6. Autentikasi & Keamanan (OAuth2 & RBAC)

Sistem mengimplementasikan **Spring Authorization Server** dan **API Gateway Resource Server**:

### 1. Kredensial Default (Auto-seeded ke Database)
* **REQUESTER**: `username: requester` | `password: password123`
* **MANAGER**: `username: manager` | `password: password123`
* **VENDOR**: `username: vendor_dell` | `password: password123`
* **ADMIN**: `username: admin` | `password: password123`

### 2. Aturan Hak Akses (Enforced at API Gateway)
* `POST /api/auth/login` : Endpoint publik untuk login dan mendapatkan token JWT ber-role.
* `POST /api/requisitions` : **Hanya boleh diakses role `REQUESTER`**. Jika role `MANAGER` mencoba membuat PR $\rightarrow$ Ditolak **`403 Forbidden`**.
* `PUT /api/requisitions/{id}/review` : **Hanya boleh diakses role `MANAGER`**. Jika role `REQUESTER` mencoba approve/reject PR $\rightarrow$ Ditolak **`403 Forbidden`**.
* `GET /api/requisitions/**` : Boleh diakses oleh user yang terotentikasi.

---

## 7. Pengujian API via Postman Collection

File Postman Collection v2.1 sudah disediakan di root repositori:
* File: **`Procurement_Microservices.postman_collection.json`**

### Skenario Pengujian (End-to-End):

1. **Import File ke Postman**:
   - Buka Postman $\rightarrow$ Klik **Import** $\rightarrow$ Pilih file `Procurement_Microservices.postman_collection.json`.
2. **Jalankan Skenario Berurutan**:
   - **`00 - Authentication & Role Logins`**:
     - Jalankan `POST Login REQUESTER` (Token otomatis tersimpan di variable `token_requester`).
     - Jalankan `POST Login MANAGER` (Token otomatis tersimpan di variable `token_manager`).
   - **`02 - Catalog Service`**:
     - Jalankan `GET All Catalog Items` (Memastikan data item katalog tersedia).
   - **`03 - Requisitions (RBAC & Approval)`**:
     - **[POS] Create PR - REQUESTER**: Berhasil dibuat (`201 Created`), memanggil catalog & user via OpenFeign, menghitung total harga, status `SUBMITTED`. ID PR otomatis tersimpan di variable `pr_id`.
     - **[NEG] Create PR - MANAGER**: Menghasilkan respon **`403 Forbidden`** (Membuktikan isolasi kewenangan).
     - **[NEG] Approve PR - REQUESTER**: Menghasilkan respon **`403 Forbidden`** (Requester dilarang menyetujui PR).
     - **[POS] Approve PR - MANAGER**: Menghasilkan respon **`200 OK`**, status PR berubah menjadi **`APPROVED`**.
     - **`PUT Reject PR - MANAGER`**: Menolak PR dan mencatat alasan penolakan (`REJECTED`).
   - **`04 - Service Discovery & Config Health`**:
     - Memeriksa registrasi Eureka (`http://localhost:8761/eureka/apps`) dan properti Config Server.

---

## 8. Panduan Dokumentasi Interaktif (SpringDoc / Swagger UI)

Sistem telah dilengkapi dokumentasi interaktif visual menggunakan **SpringDoc OpenAPI 2.6.0**. Anda dapat langsung menguji endpoint dari browser tanpa Postman:

| Layanan Microservice | URL Swagger UI (Browser) | URL OpenAPI JSON Spec |
| :--- | :--- | :--- |
| **User Service** | `http://localhost:8081/swagger-ui.html` | `http://localhost:8081/v3/api-docs` |
| **Catalog Service** | `http://localhost:8082/swagger-ui.html` | `http://localhost:8082/v3/api-docs` |
| **Requisition Service** | `http://localhost:8083/swagger-ui.html` | `http://localhost:8083/v3/api-docs` |

### Cara Menguji via Swagger UI:
1. Buka salah satu URL Swagger UI di atas pada browser.
2. Klik salah satu endpoint (misal: `POST /api/auth/login` pada User Service atau `GET /api/requisitions` pada Requisition Service).
3. Klik tombol **Try it out** $\rightarrow$ Masukkan parameter/body $\rightarrow$ Klik **Execute**.
4. Swagger akan menampilkan HTTP status code, format response JSON, dan cURL command secara real-time.

---

## 9. Unit Testing (Mockito & JUnit 5)

Proyek ini telah dilengkapi **Unit Testing** pada layer business logic `requisition-service` menggunakan **Mockito** terisolasi. Unit test ini mengeksekusi pengujian secara instan (~0.4 detik) tanpa perlu menghubungkan database fisik.

### Skenario Unit Test yang Dicakup:
1. `testCreateRequisition_Success`: Pengujian pembuatan PR sukses, pemanggilan mock `UserFeignClient` & `CatalogFeignClient`, serta kalkulasi subtotal dan grand total secara presisi.
2. `testCreateRequisition_UserNotFound`: Validasi lemparan exception saat requester tidak terdaftar di `user-service`.
3. `testCreateRequisition_CatalogItemInactive`: Validasi lemparan exception saat item katalog statusnya tidak aktif.
4. `testReviewRequisition_ApproveSuccess`: Pengujian persetujuan (approval) oleh Manager (status berubah menjadi `APPROVED`).
5. `testReviewRequisition_RejectSuccess`: Pengujian penolakan (rejection) oleh Manager beserta pencatatan alasan penolakan.

### Cara Menjalankan Unit Test:
Jalankan perintah Maven berikut dari root direktori:

* **Windows**:
  ```cmd
  .\mvnw.cmd test -pl requisition-service
  ```
* **Linux / macOS**:
  ```bash
  ./mvnw test -pl requisition-service
  ```

**Hasil Eksekusi:**
```text
[INFO] Running RequisitionService Unit Tests
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.423 s
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 10. Struktur Direktori Proyek

```
procurement-backend/
├── .gitignore
├── pom.xml                                           # Parent Maven Multi-Module POM
├── mvnw / mvnw.cmd                                   # Maven Wrapper
├── docker-compose.yaml                               # Docker Compose Orchestration
├── dbserver.json                                     # pgAdmin Auto-Connect Server Configuration
├── Procurement_Microservices.postman_collection.json  # Postman Collection v2.1 Siap Import
├── DDL_procurement.sql                               # Skrip DDL Database PostgreSQL
├── README.md                                         # Dokumentasi Lengkap Proyek
├── DEMO_DOCUMENTATION.md                             # Laporan Bukti Demo & Pengujian Sistem
│
├── discovery-service/                                # Port 8761 (Netflix Eureka Service Registry)
├── config-service/                                   # Port 8888 (Spring Cloud Config Server)
├── api-gateway/                                      # Port 8080 (Gateway & JWT Authorization RBAC)
├── user-service/                                     # Port 8081 (OAuth2 Auth Server, Users, Swagger)
├── catalog-service/                                  # Port 8082 (Spring Data REST Catalog, Swagger)
└── requisition-service/                             # Port 8083 (OpenFeign PR & Approval, Unit Tests, Swagger)
```

---

## 11. Penulis & Lisensi

* **Author**: CrusterCrew
* **Project**: Software Engineer Test - Java Microservices System
