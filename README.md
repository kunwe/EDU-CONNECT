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

5 . Youtube link.
https://youtu.be/jgj8ouS6haM?si=_awWNcLN7yWmHnJ9

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
📱 Screenshots

EduConnect provides a simple and learner-friendly interface designed to make studying, career planning, and managing academic activities easy.

🤖 AI Career Advisor

Ask about careers, university requirements, APS scores, and bursary opportunities.

<img width="363" height="778" alt="WhatsApp Image 2026-09-22 at 21 25 03" src="https://github.com/user-attachments/assets/e5ceccc2-fa8e-443f-9297-df7704b4399b" />


🏠 My Subjects

The My Subjects screen allows learners to view their selected subjects, including languages, Mathematics, Life Orientation, Physical Sciences, Life Sciences, and Agricultural Sciences.

<img width="366" height="753" alt="WhatsApp Image 2026-09-22 at 21 25 02" src="https://github.com/user-attachments/assets/8cd58948-c469-4418-9e18-c7f7adfc1d19" />

<img width="349" height="689" alt="WhatsApp Image 2026-09-22 at 21 25 01" src="https://github.com/user-attachments/assets/b3053278-1d6c-4915-8afd-d5d3f1ec734e" />

⚙️ Settings

The Settings screen allows learners to manage their profile, select their current academic term, change their password, and log out of the application.

<img width="362" height="785" alt="WhatsApp Image 2026-09-22 at 21 25 02 (1)" src="https://github.com/user-attachments/assets/0956cd14-e07b-4dfe-8b7c-c88317cc8578" />

Firebase Authentication evidence
<img width="362" height="785" alt="WhatsApp Image 2026-09-22 at 21 25 02 (1)" src="https://github.com/kunwe/EDU-CONNECT/blob/f2002c9284bbda46d5d0643f1944a09b4eb25cfb/image.png" />

🤖 AI Quizzes

The AI Quizzes screen allows learners to generate CAPS-aligned quizzes and access their previous quiz attempts through Quiz History.

<img width="350" height="757" alt="WhatsApp Image 2026-09-22 at 21 25 01 (1)" src="https://github.com/user-attachments/assets/af846ee4-2524-4946-acf2-2cdc487d8409" />

## 🤝 Contribution & License

Contributions, bug reports, and features suggestions are always welcome! Feel free to fork the repository and open a pull request. Distributed under the MIT License.


