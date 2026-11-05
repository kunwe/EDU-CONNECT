package com.example.educonnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educonnect.data.local.entities.*
import com.example.educonnect.data.repository.ModuleRepository
import com.example.educonnect.utils.LanguageManager
import com.example.educonnect.utils.ThemeManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.educonnect.data.model.NotificationItem
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModuleViewModel @Inject constructor(
    private val repository: ModuleRepository,
    private val languageManager: LanguageManager,
    private val themeManager: ThemeManager
) : ViewModel() {

    val subjects: StateFlow<List<Subject>> = repository.getSubjectsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assignments: StateFlow<List<Assignment>> = repository.getAssignmentsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val timetable: StateFlow<List<TimetableEntry>> = repository.getTimetableFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _materials = MutableStateFlow<List<Material>>(emptyList())
    val materials: StateFlow<List<Material>> = _materials

    private val _grades = MutableStateFlow<List<GradeRecord>>(emptyList())
    val grades: StateFlow<List<GradeRecord>> = _grades

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications

    private val _notificationFilter = MutableStateFlow("All")
    val notificationFilter: StateFlow<String> = _notificationFilter

    private val _pushNotificationsEnabled = MutableStateFlow(true)
    val pushNotificationsEnabled: StateFlow<Boolean> = _pushNotificationsEnabled

    private val _selectedLanguage = MutableStateFlow(languageManager.getCurrentLanguageName())
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    private val _selectedTheme = MutableStateFlow(themeManager.getCurrentThemeName())
    val selectedTheme: StateFlow<String> = _selectedTheme

    init {
        // Pre-seed mock data if empty
        viewModelScope.launch {
            _notifications.value = listOf(
                NotificationItem("🔔", "Assignment Due Tomorrow", "Algebra Problem Set is due tomorrow at 11:59 PM", "10:30 AM", true, Color(0xFF1E6091)),
                NotificationItem("✅", "Assignment Graded", "Your Science Lab Report has been graded", "Yesterday", true, Color(0xFF40916C)),
                NotificationItem("📢", "New Announcement", "Mathematics 101: New study materials uploaded", "Yesterday", false, Color.Gray),
                NotificationItem("📅", "Class Reminder", "Math tutorial session today at 3:00 PM", "2 May", false, Color(0xFFE65F2B)),
                NotificationItem("📢", "Exam Schedule", "Final exams for Semester 1 will start on 15 June.", "2 days ago", false, Color.Gray)
            )
            repository.getSubjectsFlow().collect { list ->
                if (list.isEmpty()) {
                    repository.addSubject(Subject("math_101", "Mathematics 101", "Mr. Dlamini", 75, "Mathematics", "Fundamental algebra, geometry, and calculus."))
                    repository.addSubject(Subject("lit_202", "English Literature", "Ms. Naidoo", 60, "Language", "Introduction to Shakespearean plays and poetry."))
                    repository.addSubject(Subject("sci_303", "Physical Science", "Mr. Mahlangu", 40, "Science", "Core chemical reactions and physical kinetics."))
                    repository.addSubject(Subject("hist_404", "History", "Mrs. Khumalo", 55, "Humanities", "South African and global history dynamics."))

                    repository.addAssignment(Assignment(0, "math_101", "Algebra Problem Set", "Due: Tomorrow", "Green", false, "algebra_set.pdf", "pdf"))
                    repository.addAssignment(Assignment(0, "sci_303", "Science Lab Report", "Due: 3 May", "Green", false, "lab_instructions.word", "word"))
                    repository.addAssignment(Assignment(0, "hist_404", "History Essay", "Due: 5 May", "Gray", false, "essay_prompt.txt", "txt"))

                    repository.addTimetableEntry(TimetableEntry(0, "math_101", "Mathematics 101", "Monday", "08:30 - 10:00", "Room 4B"))
                    repository.addTimetableEntry(TimetableEntry(0, "sci_303", "Physical Science", "Wednesday", "11:00 - 12:30", "Lab 2"))
                }
            }
        }
    }

    fun loadCourseDetails(subjectId: String) {
        viewModelScope.launch {
            _materials.value = repository.getMaterialsForSubject(subjectId)
            _grades.value = repository.getGradesForSubject(subjectId)
        }
    }

    fun addSubject(title: String, teacher: String, description: String = "") {
        val id = "subj_" + System.currentTimeMillis()
        viewModelScope.launch {
            repository.addSubject(Subject(id, title, teacher, 0, "Custom", description))
        }
    }

    fun updateSubject(subjectId: String, title: String, teacher: String, description: String) {
        val currentSubjects = subjects.value
        val subject = currentSubjects.find { it.id == subjectId } ?: return
        viewModelScope.launch {
            repository.addSubject(subject.copy(title = title, teacherName = teacher, description = description))
        }
    }

    fun deleteSubject(subject: Subject) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }

    fun addMaterial(subjectId: String, title: String, type: String) {
        viewModelScope.launch {
            repository.addMaterial(Material(0, subjectId, title, type))
            _materials.value = repository.getMaterialsForSubject(subjectId)
        }
    }

    fun addAssignment(subjectId: String, title: String, dueDate: String, attachmentName: String, attachmentType: String) {
        viewModelScope.launch {
            repository.addAssignment(Assignment(0, subjectId, title, dueDate, "Green", false, attachmentName, attachmentType))
        }
    }

    fun addGrade(subjectId: String, title: String, score: Int) {
        viewModelScope.launch {
            repository.addGrade(GradeRecord(0, subjectId, title, score))
            val allGrades = repository.getGradesForSubject(subjectId)
            _grades.value = allGrades
            
            // Automatic Calculation
            if (allGrades.isNotEmpty()) {
                val average = allGrades.map { it.scorePercentage }.average().toInt()
                repository.updateSubjectProgress(subjectId, average)
            }
        }
    }

    fun addTimetableEntry(subjectId: String, subjectTitle: String, day: String, slot: String) {
        viewModelScope.launch {
            repository.addTimetableEntry(TimetableEntry(0, subjectId, subjectTitle, day, slot))
        }
    }

    fun setNotificationFilter(filter: String) {
        _notificationFilter.value = filter
    }

    fun setPushNotificationsEnabled(enabled: Boolean) {
        _pushNotificationsEnabled.value = enabled
    }

    fun setSelectedLanguage(language: String) {
        _selectedLanguage.value = language
        languageManager.setLanguage(language)
    }

    fun setSelectedTheme(theme: String) {
        _selectedTheme.value = theme
        themeManager.setTheme(theme)
    }

    fun toggleAssignmentCompleted(assignment: Assignment) {
        viewModelScope.launch {
            repository.updateAssignment(assignment.copy(isCompleted = !assignment.isCompleted))
        }
    }
}
