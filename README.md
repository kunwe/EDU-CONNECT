# EduConnect FINAL POE 🎓🚀

**EduConnect** is an advanced Android educational application designed specifically for **South African high school learners (Grades 8–12)**. It provides curriculum-aligned learning materials, study modules, quizzes, career guidance, subject matching, matric requirement calculations, and AI-assisted tutoring powered by **Gemini AI**.

Built using modern Android architectural practices, Firebase, and Jetpack Compose, EduConnect empowers learners to connect their high school education with realistic and rewarding future career opportunities and tertiary education pathways.

**Repository:** [EduConnect on GitHub](https://github.com/kunwe/EDU-CONNECT)

---

## ✨ Features

### 1. 🤖 AI-Powered Career Advisor

- **Contextual South African Advice:** Provides guidance tailored to the learner's current grade and selected subjects.
- **Tertiary Requirements:** Offers information about Admission Point Scores (APS), matric points, and admission requirements for leading South African universities, including UCT, Wits, UP, UKZN, Stellenbosch, UNISA, TUT, CPUT, and DUT.
- **Further Education Opportunities:** Provides information about TVET Colleges, learnerships, and SETAs.
- **Financial Aid Assistance:** Offers guidance on funding opportunities such as NSFAS and private bursaries.
- **Persistent Conversation History:** Uses Cloud Firestore to save conversations so learners can revisit previous advice.

### 2. 📝 Interactive Smart Quizzes

- **Randomised Quiz Engine:** Each subject contains a curated bank of 15 CAPS-aligned questions. Whenever a learner starts a quiz, the application randomly selects 5 unique questions.
- **Quiz History:** Saves previous quiz attempts and scores, allowing learners to monitor their academic progress.

### 3. 📚 Smart AI Summaries

- Generates concise and useful revision notes to help learners understand difficult topics and important subject content.
- Saves generated summaries in a searchable **Summary History**, making study materials easy to access whenever needed.

### 4. 📅 Smart Timetable, Exam Planner & Calendar

- Allows learners to organise daily study sessions and school schedules.
- Helps learners keep track of upcoming examination dates and important academic events.
- **Interactive Calendar:** Includes month and year navigation using previous (◀) and next (▶) controls.
- **Calendar Visibility:** Learners can control calendar visibility through the Notifications screen.
- Encourages effective time management and helps learners stay organised.

### 5. ⏱️ Study Time & Subject Tracker

- A dedicated bottom-navigation tab allows learners to monitor their study hours for individual subjects.
- **Period Filter:** Switch between *This Week*, *This Month*, and *This Term* to view study statistics.
- **Progress Tracking:** Displays graphs, minimum and maximum study-time goals, and goal achievement rates.
- **Gamification:** Rewards learners with achievement badges such as:
  - Math Whiz
  - 7-Day Streak
  - Exam Ready

### 6. 🌍 In-App Multilingual Support

- **Supported Languages:** English, isiZulu, Afrikaans, and Sesotho.
- **How to Change Language:** Navigate to **Settings → General → Language**.
- Language preferences are applied immediately throughout the application, with AI features adapting their responses to the selected language.

### 7. 🎨 Dark, Light & System Theme Modes

- **Available Themes:**
  - Light Theme
  - Dark Theme
  - System Default
- **How to Change Theme:** Navigate to **Settings → General → Theme**.
- Theme preferences are saved locally and applied immediately.

### 8. 📂 Internal Storage File Uploads

- Allows students and teachers to upload files directly from their device's internal storage.
- Supports multiple file formats, including:
  - PDF documents
  - Microsoft Word documents
  - Text files
  - Images
- Uploaded files can be added to the relevant subject material or assignment section.

### 9. 🔒 Secure Authentication & Offline Access

- **Firebase Authentication:** Supports custom email/password registration and Google Sign-In.
- **Google Account Picker:** Selecting "Sign in with Google" opens the Android system account picker (`AccountManager.newChooseAccountIntent`), allowing users to select a Gmail account from their device.
- **Strict Registration:** Gmail accounts that have not been registered cannot log in directly. Users must register their accounts first.
- **Persistent Login Session:** When the application is closed and reopened, `MainActivity` automatically restores the user's session using `FirebaseAuth.getInstance().currentUser`, taking them directly to the Dashboard.
- **Offline Authentication & Local Caching:** Falls back to the local Room database when there is no internet connection or a network error occurs, allowing learners to access locally available profiles and study materials.

---

## 🛠️ Tech Stack & Modern Tools

| Area | Technologies |
|---|---|
| **Programming Language** | 100% Kotlin |
| **UI Framework** | Jetpack Compose, Material 3, ViewBinding and Material Components for existing XML layouts |
| **Architecture** | MVVM (Model-View-ViewModel), Repositories and ViewModels |
| **Dependency Injection** | Hilt (Dagger-Hilt) |
| **Networking & REST Client** | Retrofit 2 and OkHttp 3 |
| **AI Integration** | Google Gemini AI with integrated fallback models |
| **Backend & Cloud Storage** | Firebase Authentication and Cloud Firestore |
| **Local Database** | Room Database |
| **Asynchronous Programming** | Kotlin Coroutines, LiveData and Flow |
| **CI/CD** | GitHub Actions (`.github/workflows/android.yml`) for unit testing and debug APK builds |

---

## 🏗️ Getting Started

Follow the instructions below to download, configure, build, and run EduConnect locally.

### Prerequisites

Before running the application, ensure you have:

- Android Studio Jellyfish, Koala, or newer.
- Android SDK 34 (Upside Down Cake) or newer.
- A valid Google Gemini API key.
- A Firebase project configured for Android.
- A `google-services.json` configuration file.

### 1. Clone the Repository

Open your terminal and run:

```bash
git clone https://github.com/kunwe/EDU-CONNECT.git
```

### 2. Configure Firebase

1. Open your Firebase project.
2. Download the `google-services.json` configuration file.
3. Place the file inside the project's `app/` directory.

### 3. Configure the Gemini API Key

Open your project's `local.properties` file and add your Gemini API key:

```properties
GEMINI_API_KEY="your_api_key_here"
```

**Important:** Do not upload your actual API key or other private credentials to GitHub.

### 4. Build and Run

1. Open the cloned project in Android Studio.
2. Allow Gradle to synchronise the project dependencies.
3. Connect an Android device or start an emulator.
4. Click **Run** to launch EduConnect.

---

## 🧪 Building & Testing

### Run Unit Tests

Execute the following command to run the project's debug unit tests:

```bash
./gradlew testDebugUnitTest
```

### Build and Install the Application

To build and install EduConnect on a connected Android device or emulator, run:

```bash
./gradlew installDebug
```

---

## 🎥 Demo Video

Watch the EduConnect application in action:

[![EduConnect Demo Video](https://img.youtube.com/vi/jgj8ouS6haM/0.jpg)](https://youtu.be/jgj8ouS6haM)

[**Watch the EduConnect Demo on YouTube**](https://youtu.be/jgj8ouS6haM)

---

## 📊 Firestore Data Structure

EduConnect uses Cloud Firestore to store user information, AI conversations, quiz results, and generated study summaries.

The following structure illustrates the main Firestore collections:

```text
users/
└── {userId}/
    ├── details
    │   ├── grade
    │   ├── subjects
    │   ├── email
    │   └── name
    │
    ├── chats/
    │   └── {messageId}
    │       ├── role
    │       ├── content
    │       └── timestamp
    │
    ├── quizzes/
    │   └── {quizId}
    │       ├── subject
    │       ├── score
    │       ├── totalQuestions
    │       └── timestamp
    │
    └── summaries/
        └── {summaryId}
            ├── title
            ├── materialText
            ├── dynamicSummary
            └── timestamp
```

---

## 📱 Screenshots

EduConnect features a clean, modern, and learner-friendly interface designed to make studying, career planning, and managing academic activities easier.

### 🤖 AI Career Advisor

The AI Career Advisor helps learners explore career opportunities, university admission requirements, APS scores, and available bursaries.

<img width="363" height="778" alt="AI Career Advisor" src="https://github.com/user-attachments/assets/e5ceccc2-fa8e-443f-9297-df7704b4399b" />

### 🏠 My Subjects

The My Subjects screen allows learners to view their selected school subjects, including languages, Mathematics, Life Orientation, Physical Sciences, Life Sciences, and Agricultural Sciences.

<img width="366" height="753" alt="My Subjects 1" src="https://github.com/user-attachments/assets/8cd58948-c469-4418-9e18-c7f7adfc1d19" />

<img width="349" height="689" alt="My Subjects 2" src="https://github.com/user-attachments/assets/b3053278-1d6c-4915-8afd-d5d3f1ec734e" />

### ⚙️ Settings

The Settings screen allows learners to manage their profiles, select their current academic term, change their passwords, and log out of the application.

<img width="362" height="785" alt="Settings" src="https://github.com/user-attachments/assets/0956cd14-e07b-4dfe-8b7c-c88317cc8578" />

### 🔐 Firebase Authentication Evidence

The following screenshot demonstrates the Firebase Authentication configuration used by EduConnect.

<img alt="Firebase Authentication Evidence" src="https://github.com/kunwe/EDU-CONNECT/blob/f2002c9284bbda46d5d0643f1944a09b4eb25cfb/image.png" />

### 🤖 AI Quizzes

The AI Quizzes screen allows learners to access CAPS-aligned quizzes, test their subject knowledge, and review previous attempts through Quiz History.

<img width="350" height="757" alt="AI Quizzes" src="https://github.com/user-attachments/assets/af846ee4-2524-4946-acf2-2cdc487d8409" />

---

## 🤝 Contribution

Contributions, bug reports, and feature suggestions are always welcome!

If you would like to contribute:

1. Fork this repository.
2. Create a new feature branch.
3. Make your changes.
4. Commit and push your changes.
5. Submit a pull request for review.

---

## 📄 License

This project is distributed under the **MIT License**.

---


<p align="center">
  <strong>EduConnect 🎓 — Connecting Education with Opportunity.</strong>
</p>
