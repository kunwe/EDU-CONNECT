package com.example.educonnect.data.repository

import com.example.educonnect.data.local.dao.*
import com.example.educonnect.data.local.entities.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModuleRepository @Inject constructor(
    private val subjectDao: SubjectDao,
    private val assignmentDao: AssignmentDao,
    private val timetableDao: TimetableDao,
    private val materialDao: MaterialDao,
    private val gradeDao: GradeDao
) {
    fun getSubjectsFlow(): Flow<List<Subject>> = subjectDao.getAllSubjectsFlow()
    fun getAssignmentsFlow(): Flow<List<Assignment>> = assignmentDao.getAllAssignmentsFlow()
    fun getTimetableFlow(): Flow<List<TimetableEntry>> = timetableDao.getAllTimetableFlow()

    suspend fun getMaterialsForSubject(subjectId: String): List<Material> = materialDao.getMaterialsForSubject(subjectId)
    suspend fun getGradesForSubject(subjectId: String): List<GradeRecord> = gradeDao.getGradesForSubject(subjectId)

    suspend fun addSubject(subject: Subject) {
        subjectDao.insertSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject) {
        subjectDao.deleteSubject(subject)
    }

    suspend fun addAssignment(assignment: Assignment) {
        assignmentDao.insertAssignment(assignment)
    }

    suspend fun updateAssignment(assignment: Assignment) {
        assignmentDao.updateAssignment(assignment)
    }

    suspend fun addMaterial(material: Material) {
        materialDao.insertMaterial(material)
    }

    suspend fun addGrade(grade: GradeRecord) {
        gradeDao.insertGrade(grade)
    }

    suspend fun addTimetableEntry(entry: TimetableEntry) {
        timetableDao.insertTimetableEntry(entry)
    }

    suspend fun updateSubjectProgress(subjectId: String, progress: Int) {
        val subject = subjectDao.getSubjectById(subjectId)
        if (subject != null) {
            subjectDao.insertSubject(subject.copy(progress = progress))
        }
    }
}
