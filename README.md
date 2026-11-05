# EduConnect

EduConnect is an advanced Android educational application designed specifically for South African high school learners. It provides curriculum-aligned learning materials, study modules, quizzes, and AI-assisted tutoring powered by Gemini.

## Repository URL
[https://github.com/kunwe/EDU-CONNECT](https://github.com/kunwe/EDU-CONNECT)

## Design Considerations & Architecture
- **UI Framework:** Built entirely with **Jetpack Compose** and Material 3 design components.
- **Architecture:** Follows the **MVVM (Model-View-ViewModel)** architectural pattern.
- **Dependency Injection:** Powered by **Google Hilt**.
- **Backend & Persistence:** Firebase Authentication, Cloud Firestore, and local Room database caching for complete offline functionality.
- **AI Integration:** Retrofit and OkHttp REST client communicating with the Gemini API for intelligent tutoring, summaries, and adaptive quiz generation.

---

## New Features & Capabilities

### 1. In-App Multilingual Support
- **Languages Supported:** English, isiZulu, Afrikaans, and Sesotho.
- **How to Use:** Navigate to **Settings** -> **General** -> **Language**. Selection applies immediately across all Android devices, with AI features adapting their prompts to respond in the chosen language.

### 2. Dark, Light, and System Theme Mode
- **Modes:** Light Theme, Dark Theme, and System Default.
- **How to Use:** Navigate to **Settings** -> **General** -> **Theme**. Persists locally and updates instantly.

### 3. Offline Authentication & Local Caching
- Seamlessly falls back to the local Room database if offline or network errors occur, ensuring learners can always access their profiles and study materials.

### 4. Google Sign-In & Strict Registration
- **Account Picker:** Tapping "Sign in with Google" prompts the system account picker (`AccountManager.newChooseAccountIntent`), allowing users to choose from any Gmail account on their device.
- **Security:** Unregistered Gmail accounts cannot log in directly; users must register their accounts first.

### 5. Persistent Login Session
- When the app is closed and reopened, `MainActivity` automatically restores the session (`FirebaseAuth.getInstance().currentUser`), opening directly to the Dashboard.

### 6. Internal Storage File Uploads
- Students and teachers can upload any file (PDFs, Word documents, text files, images) from their device internal storage directly to any subject's material or assignment section.

### 7. Randomized Quiz Engine
- Each subject features a curated bank of 15 curriculum-aligned quiz questions. Every time a user enters a quiz, the app randomly shuffles and presents 5 unique questions.

### 8. Study Time & Subject Tracker Tab (`⏱️ Study`)
- **Study Tracking:** A dedicated bottom navigation tab that tracks study hours per subject.
- **Period Filter:** Switch dynamically between *This Week*, *This Month*, and *This Term* to view graphs, min/max study time goals, and adherence rates.
- **Gamification:** Unlockable study milestone badges (*"Math Whiz"*, *"7-Day Streak"*, *"Exam Ready"*).

### 9. Interactive Calendar View
- Fully interactive calendar with month and year navigation (◀ / ▶) and a visibility toggle in the Notifications screen.

### 10. GitHub Actions CI/CD & Testing
- Automated workflow defined in `.github/workflows/android.yml` running unit tests and building debug APKs on push/pull requests.

---

## Getting Started & Testing
Clone the repository:
```bash
git clone https://github.com/kunwe/EDU-CONNECT.git
```
Open the project in Android Studio, sync Gradle, and run unit tests:
```bash
./gradlew testDebugUnitTest
```
To build and install the app on an emulator or device:
```bash
./gradlew installDebug
```
