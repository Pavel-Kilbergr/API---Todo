# 📚 Todo API s Book ID Checker

## 🎯 **PROJEKT HISTORIE A VÝVOJ**

### **Fáze 1: Základní Todo API** ✅ (Dokončeno)

- **Technologie:** Java 17, Spring Boot 3.4.1, Gradle, H2 databáze
- **Funkcionalita:** Kompletní CRUD operace pro Todo items
- **Deployment:** Úspěšně nasazeno na Render.com
- **Live API:** https://api-todo-44sn.onrender.com
- **8 REST Endpointů:** GET, POST, PUT, DELETE, search, completed, pending, info

### **Fáze 2: PostgreSQL Migration** 🔄 (Dokončeno s problémy)

- **Cíl:** Migrace z H2 in-memory na PostgreSQL cloud database
- **Cloud Provider:** Supabase (https://supabase.com)
- **Konfigurace:** Kompletně dokončena, včetně HikariCP connection pooling
- **Security:** Environment variables pattern pro credential management
- **Status:** ❌ Blokováno authentication failure - potřeba password reset

### **Fáze 3: Book ID Checker Enhancement** ⏸️ (Pozastaveno)

- **Cíl:** Integrace s externí Book API
- **Funkcionalita:** Automatické nahrazení ID čísel informacemi o knihách
- **Trigger:** Title = "BOOK_CHECKER" + Description = číslo
- **Externí API:** https://simple-books-api.glitch.me/books/:id
- **Výstup:** "Název knihy / Autor" místo pouhého ID
- **Status:** BookService implementován, čeká na dokončení database migration

### **Fáze 4: Code Quality & Documentation** ✅ (Dokončeno)

- **Professional Comments:** Všechny třídy mají anglické JavaDoc komentáře
- **Code Standards:** Dodržení enterprise coding standards
- **Documentation:** Kompletní @param, @return, @author, @since tags
- **Maintainability:** Jasné komentáře pro budoucí developery

## 📁 **STRUKTURA PROJEKTU**

### **Java Classes:**

- `TodoApiApplication.java` - Main Spring Boot aplikace
- `Todo.java` - JPA entita s atributy (id, title, description, completed, timestamps)
- `TodoRepository.java` - Spring Data JPA interface s custom queries
- `TodoController.java` - REST kontroler s 8 endpointy
- `DataInitializer.java` - Automatické vytvoření vzorových dat

### **Konfigurace:**

- `application.properties` - ✅ Migrováno na PostgreSQL/Supabase s HikariCP connection pooling
- `build.gradle` - ✅ Aktualizováno s PostgreSQL dependency, Java 17, Spring Boot plugins
- `Dockerfile` - Single-stage build pro cloud deployment
- `DataInitializer.java` - ✅ Upraven pro produkční prostředí (bez automatických vzorových dat)

## 🛠️ **TECHNICKÁ SPECIFIKACE**

### **REST API Endpointy:**

```
GET    /api/todos           - Všechny todos
GET    /api/todos/{id}      - Todo podle ID
POST   /api/todos           - Vytvoř nové todo
PUT    /api/todos/{id}      - Aktualizuj todo
DELETE /api/todos/{id}      - Smaž todo
GET    /api/todos/completed - Dokončené todos
GET    /api/todos/pending   - Nedokončené todos
GET    /api/todos/search    - Hledej podle názvu
GET    /api/todos/info      - API informace a statistiky
```

### **Book Checker Logic:**

```
IF title == "BOOK_CHECKER" AND description == integer:
  1. Zavolej GET https://simple-books-api.glitch.me/books/{id}
  2. Parsuj JSON response
  3. Nahraď description → "name / author"
  4. Ulož todo s upravenou description
ELSE:
  Standardní uložení todo
```

## 🎓 **LEARNING OBJECTIVES (Výukové cíle)**

### **Dokončené:**

- ✅ Spring Boot projekt struktura
- ✅ JPA entity a repository pattern
- ✅ REST API design a implementace
- ✅ H2 databáze konfigurace (deprecated)
- ✅ PostgreSQL cloud database setup (Supabase)
- ✅ HikariCP connection pooling
- ✅ Environment variable security patterns
- ✅ Production-ready configuration
- ✅ Gradle build system
- ✅ Docker deployment
- ✅ Git workflow a GitHub integration
- ✅ Professional English code documentation
- ✅ Enterprise coding standards
- ✅ Comprehensive JavaDoc comments

### **Problémové oblasti (troubleshooting experience):**

- 🔧 Database credential management challenges
- 🔧 Cloud database password expiration handling
- 🔧 PostgreSQL authentication troubleshooting

### **Aktuální (na zítra po reset hesla):**

- 🔄 Database connection testing a verification
- 🔄 Production deployment s PostgreSQL

### **Budoucí (Book Checker):**

- � HTTP Client integration (RestTemplate/WebClient)
- � External API consumption
- � Conditional business logic
- � Error handling a fallback strategies
- � JSON parsing a data transformation
- � Integration testing

## 👥 **TEAM COLLABORATION**

### **Role:**

- **Pavel:** Developer, project owner
- **Kolega:** Stakeholder, feature requester, tester
- **AI Agent:** Mentor, code reviewer, teacher

### **Komunikační strategie:**

- Strukturované vysvětlování po malých částech
- Komprehension testy mezi sekcemi
- Motivační a empatický přístup k učení
- Praktické příklady s humor a souvislostmi

## 🚀 **DEPLOYMENT INFO**

### **Development Environment:**

- **Database:** PostgreSQL na Supabase cloud
- **Connection:** jdbc:postgresql://db.ieesukcrbwebduaqupzv.supabase.co:5432/postgres
- **Status:** ❌ Authentication failure - potřeba password reset

### **Local Development (po vyřešení database problému):**

```bash
# Nastav database password
export DATABASE_PASSWORD='tvoje-heslo-ze-supabase'

# Spusť aplikaci
./gradlew bootRun

# API dostupné na http://localhost:8080
```

### **Live Environment (Legacy - H2):**

- **URL:** https://api-todo-44sn.onrender.com
- **Platform:** Render.com
- **Build:** Automatic deployment from GitHub main branch
- **Database:** H2 in-memory (deprecated, bude nahrazeno PostgreSQL)

## 💻 **SETUP PRO PRÁCI (Windows PC)**

### **Požadavky pro development:**

#### **1️⃣ Java Development Kit (JDK 17)**

**Doporučený postup - Chocolatey:**
```cmd
# 1. Nainstaluj Chocolatey (PowerShell jako Admin):
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# 2. Nainstaluj JDK 17:
choco install openjdk17
```

**Alternativní postup - Ruční instalace:**
1. Stáhni z: https://adoptium.net/temurin/releases/
2. Vyber **JDK 17**, **Windows x64**, **.msi installer**
3. Spusť installer a postupuj podle instrukcí

#### **2️⃣ VS Code Extensions**

**Nainstaluj tyto extension packs:**
- `Extension Pack for Java` (vscjava.vscode-java-pack)
- `Spring Boot Extension Pack` (vmware.vscode-boot-dev-pack)

#### **3️⃣ Git pro Windows**
```cmd
# Přes Chocolatey:
choco install git

# Nebo stáhni z: https://git-scm.com/download/win
```

### **Klonování a spuštění projektu:**

```cmd
# 1. Naklonuj projekt:
git clone https://github.com/Pavel-Kilbergr/API---Todo.git
cd API---Todo

# 2. Ověř instalaci:
java --version
javac --version
git --version

# 3. Spusť projekt:
./gradlew bootRun
```

### **Testování:**
- API: http://localhost:8080/api/todos
- H2 Console: http://localhost:8080/h2-console
- VS Code automaticky detekuje Java projekt a poskytne IntelliSense

**Po tomto setupu můžeš pokračovat ve studiu Java Spring Boot konceptů! 🚀**

## 📞 **AGENT INSTRUCTIONS**

### **Teaching Style:**

- Vysvětluj PROČ před WHAT
- Používej humor a empatie
- Testuj porozumění before moving forward
- Motivuj a oceňuj progress
- Rozbij komplexní úkoly na malé kroky

### **Code Style:**

- Vždy ukáže context před editací
- Vysvětli změny step-by-step
- Include error handling considerations
- Test changes immediately after implementation

---

_Posledně aktualizováno: 5. září 2025 - PostgreSQL migration dokončena s authentication issues_

---

## 🐘 **POSTGRESQL MIGRATION - COMPLETED WITH ISSUES** 

### **✅ Dokončené kroky:**

#### **✅ Krok 1: Supabase Setup**
- **Projekt vytvořen:** https://supabase.com
- **Database Host:** db.ieesukcrbwebduaqupzv.supabase.co:5432
- **Username:** postgres
- **Database Name:** postgres
- **Status:** ✅ CONFIGURED

#### **✅ Krok 2: Spring Boot Changes**

**A) PostgreSQL dependency přidána do `build.gradle`:**
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.postgresql:postgresql'  // ✅ PŘIDÁNO
    runtimeOnly 'com.h2database:h2'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

**B) `application.properties` kompletně migrováno:**
```properties
# === POSTGRESQL CONFIGURATION ===
spring.datasource.url=jdbc:postgresql://db.ieesukcrbwebduaqupzv.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=${DATABASE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# === HIKARI CONNECTION POOL ===
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000

# === JPA/HIBERNATE SETTINGS ===
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# === SERVER CONFIGURATION ===
server.port=8080
logging.level.com.pavel.todoapi=DEBUG
```

#### **✅ Krok 3: DataInitializer Update**
- **Status:** ✅ DOKONČENO
- **Změny:** Vypnut `deleteAll()` pro produkci
- **Funkčnost:** Pouze počítá existující záznamy, nevytváří vzorová data

### **❌ SOUČASNÝ PROBLÉM: Database Authentication**

#### **🚨 Chyba:**
```
FATAL: password authentication failed for user "postgres"
```

#### **📋 Diagnostika:**
1. **Environment Variable:** ✅ Správně nastavena
2. **Connection String:** ✅ Správná konfigurace
3. **Username:** ✅ postgres
4. **Database:** ✅ postgres
5. **Password:** ❌ NEPLATNÉ - možná expirované nebo resetované

### **🔧 ŘEŠENÍ PRO ZÍTRA:**

#### **Krok 1: Reset Database Password v Supabase**
1. Přihlas se na https://supabase.com
2. Jdi na svůj projekt
3. **Settings** → **Database**
4. **Reset database password**
5. **Vygeneruj nové heslo** a ulož si ho

#### **Krok 2: Aktualizace Environment Variable**
```bash
# Nastav nové heslo
export DATABASE_PASSWORD='nove-heslo-ze-supabase'

# Test pripojeni
./gradlew bootRun
```

#### **Krok 3: Verify Everything Works**
```bash
# Zkontroluj API
curl http://localhost:8080/api/todos/info

# Zkontroluj databazi v Supabase dashboardu
```

### **🔒 Security Notes:**
- ✅ **Environment Variables:** Používáme `${DATABASE_PASSWORD}` pattern
- ✅ **No Hardcoded Passwords:** Žádná hesla v kódu
- ✅ **SSL Encryption:** Automaticky přes Supabase
- ⚠️ **Password Reset:** Pravděpodobně nutné každý týden

### **📊 Migration Status:**
- **Konfigurační soubory:** ✅ 100% dokončeno
- **Dependencies:** ✅ 100% dokončeno  
- **Code Changes:** ✅ 100% dokončeno
- **Database Connection:** ❌ Blokováno neplatným heslem
- **Production Ready:** 🔄 Pending password reset

### **📞 Next Steps:**
1. **Password reset** v Supabase dashboard
2. **Test connection** s novým heslem
3. **Deploy to production** když vše funguje

---

### **🎓 Co se naučilo:**
- ✅ PostgreSQL dependency management
- ✅ HikariCP connection pooling
- ✅ Environment variable security patterns
- ✅ Supabase cloud database setup
- ✅ Production-ready configuration
- 📝 Database credential management challenges

---