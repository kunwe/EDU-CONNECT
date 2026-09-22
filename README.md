# EduConnect 🎓🚀

EduConnect is an advanced Android application designed specifically to empower and assist **South African high school learners** (Grades 8–12) with career guidance, subject matching, matric requirement calculations, study summaries, interactive quizzes, and time management tools.

Powered by modern Android architectural practices, Firebase, and **Gemini AI**, the app helps learners successfully connect their high school education with realistic, rewarding future career choices and tertiary paths.

---

## ✨ Features

### 1. 🤖 AI-Powered Career Advisor (Chat)
* **Contextual South African Advice:** Tailored specifically to the learner's chosen high school grade and subjects.
* **Tertiary Requirements:** Provides information on Admission Point Scores (APS), Matric points, and prerequisites for leading South African universities (UCT, Wits, UP, UKZN, Stellenbosch, UNISA, TUT, CPUT, DUT), TVET Colleges, learnerships, and SETAs.
* **Financial Aid Assistance:** Guidance on funding opportunities such as NSFAS and private bursaries.
* **Persistent Conversation History:** Powered by Cloud Firestore to review advice at any point.

### 2. 📝 Interactive Smart Quizzes
* Test subject knowledge and exam readiness through comprehensive multiple-choice question formats.
* Track score progression with a dedicated **Quiz History** feature to evaluate academic strength over time.

### 3. 📚 Smart AI Summaries
* Generate concise, actionable revision and study notes tailored to help master difficult subject criteria.
* Store generated summaries in a searchable **Summary History** ledger for convenient, on-the-go studying.

### 4. 📅 Smart Timetable & Exam Planner
* Easily structure daily study periods, school schedules, and track important upcoming exam dates.
* Designed to promote healthy study habits and ensure high schoolers never miss a deadline.

### 5. 🔒 Secure Authentication
* Effortless onboarding using **Firebase Authentication**.
* Secure custom email/password registration along with seamless **Google Sign-In** integration.

---

## 🛠️ Tech Stack & Modern Tools

* **Language:** 100% Kotlin 
* **UI Pattern:** MVVM Architecture pattern with clean Separation of Concerns (Repositories & ViewModels).
* **Dependency Injection:** Hilt (Dagger-Hilt) for robust dependency provision and decoupled components.
* **Networking & REST Client:** Retrofit 2 + OkHttp 3 for robust asynchronous requests to the Google Gemini AI inference engines with integrated fallback models.
* **Database & Cloud Services:**
  * **Firebase Auth:** Handles secure user credentials.
  * **Cloud Firestore:** Real-time data storage for chats, quizzes, timetables, and summaries.
* **Jetpack Components:**
  * ViewBinding & Material Components for beautiful, accessible Android UI.
  * Coroutines & LiveData/Flow for lifecycle-aware reactive programming.

---

## 🏗️ Getting Started

### Prerequisites
* Android Studio Jellyfish / Koala or newer.
* Android SDK 34 (Upside Down Cake) or newer.
* A valid Google Gemini API Key.
* A Firebase Project initialized for Android (`google-services.json`).

### Configuration
1. Clone this repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/educonnect.git
   ```
2. Place your `google-services.json` file inside the `app/` directory.
3. Open your global `local.properties` file or configure your build environment to include your Gemini API key:
   ```properties
   GEMINI_API_KEY="your_api_key_here"
   ```
4. Perform a **Gradle Sync** inside Android Studio and press **Run**.

---

## 📊 Firestore Data Structure

```text
users/
 └── {userId}/
      ├── details (grade, subjects, email, name)
      ├── chats/
      │    └── {messageId} -> (role, content, timestamp)
      ├── quizzes/
      │    └── {quizId} -> (subject, score, totalQuestions, timestamp)
      └── summaries/
           └── {summaryId} -> (title, materialText, dynamicSummary, timestamp)
```

---

## 🤝 Contribution & License

Contributions, bug reports, and features suggestions are always welcome! Feel free to fork the repository and open a pull request. Distributed under the MIT License.
