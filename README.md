# 📚 Todo API s Book ID Checker

## 🎯 **PROJEKT HISTORIE A VÝVOJ**

### **Fáze 1: Základní Todo API** ✅ (Dokončeno)

- **Technologie:** Java 17, Spring Boot 3.4.1, Gradle, H2 databáze
- **Funkcionalita:** Kompletní CRUD operace pro Todo items
- **Deployment:** Úspěšně nasazeno na Render.com
- **Live API:** https://api-todo-44sn.onrender.com
- **8 REST Endpointů:** GET, POST, PUT, DELETE, search, completed, pending, info

### **Fáze 2: Book ID Checker Enhancement** 🚧 (Aktuální)

- **Cíl:** Integrace s externí Book API
- **Funkcionalita:** Automatické nahrazení ID čísel informacemi o knihách
- **Trigger:** Title = "BOOK_CHECKER" + Description = číslo
- **Externí API:** https://simple-books-api.glitch.me/books/:id
- **Výstup:** "Název knihy / Autor" místo pouhého ID
- **Status:** BookService implementován, čeká na controller integration

### **Fáze 3: Code Quality & Documentation** ✅ (Dokončeno)

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

- `application.properties` - H2 databáze, port nastavení, logging
- `build.gradle` - Závislosti, Java 17, Spring Boot plugins
- `Dockerfile` - Single-stage build pro cloud deployment

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
- ✅ H2 databáze konfigurace
- ✅ Gradle build system
- ✅ Docker deployment
- ✅ Git workflow a GitHub integration
- ✅ Professional English code documentation
- ✅ Enterprise coding standards
- ✅ Comprehensive JavaDoc comments

### **Aktuální (Book Checker):**

- 🔄 HTTP Client integration (RestTemplate/WebClient)
- 🔄 External API consumption
- 🔄 Conditional business logic
- 🔄 Error handling a fallback strategies
- 🔄 JSON parsing a data transformation
- 🔄 Integration testing

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

### **Live Environment:**

- **URL:** https://api-todo-44sn.onrender.com
- **Platform:** Render.com
- **Build:** Automatic deployment from GitHub main branch
- **Database:** H2 in-memory (resets on deployment)

### **Local Development:**

```bash
./gradlew bootRun
# API dostupné na http://localhost:8080
# H2 Console: http://localhost:8080/h2-console
```

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

_Posledně aktualizováno: 22. srpna 2025_
