package com.example.educonnect.data.repository

import com.example.educonnect.data.local.dao.QuizDao
import com.example.educonnect.data.local.entities.Question
import com.example.educonnect.data.local.entities.Quiz
import com.example.educonnect.data.local.entities.QuizAttempt
import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizDao: QuizDao) {

    fun getQuizzesFlow(): Flow<List<Quiz>> = quizDao.getAllQuizzesFlow()
    fun getAttemptsFlow(): Flow<List<QuizAttempt>> = quizDao.getAllAttemptsFlow()

    suspend fun getQuestionsForQuiz(quizId: String): List<Question> {
        var questions = quizDao.getQuestionsForQuiz(quizId)
        if (questions.size < 15) {
            val preseeded = when (quizId) {
                "math_101_quiz" -> listOf(
                    Question("math_q1", quizId, "What is the value of x in the equation 2x + 4 = 10?", "Multiple-Choice", "2,3,4,5", "3", "2x = 6 -> x = 3."),
                    Question("math_q2", quizId, "What is the derivative of x^2?", "Written Response", "", "2x", "Using power rule, derivative of x^n is n*x^(n-1)."),
                    Question("math_q3", quizId, "What is the area of a circle with radius r?", "Multiple-Choice", "π r^2,2π r,π d,4π r^2", "π r^2", "Area formula is A = π r^2."),
                    Question("math_q4", quizId, "Is the number 2 a prime number?", "True/False", "True,False", "True", "2 is the only even prime number."),
                    Question("math_q5", quizId, "What is the sum of angles in a triangle?", "Multiple-Choice", "90°,180°,270°,360°", "180°", "The interior angles of a triangle always sum to 180°."),
                    Question("math_q6", quizId, "What is 2^3 × 2^2?", "Multiple-Choice", "2^5,2^6,4^5,8^2", "2^5", "When multiplying exponents with the same base, add exponents: 3 + 2 = 5."),
                    Question("math_q7", quizId, "What is the slope of the line passing through (0,0) and (2,4)?", "Multiple-Choice", "1,2,4,0.5", "2", "Slope m = (y2 - y1) / (x2 - x1) = 4 / 2 = 2."),
                    Question("math_q8", quizId, "What is the value of sin(90°)?", "Multiple-Choice", "0,1,-1,0.5", "1", "Sine of 90 degrees is 1."),
                    Question("math_q9", quizId, "What is 15% of 200?", "Multiple-Choice", "15,20,30,45", "30", "0.15 * 200 = 30."),
                    Question("math_q10", quizId, "What is the next prime number after 7?", "Multiple-Choice", "9,10,11,13", "11", "Primes: 2, 3, 5, 7, 11."),
                    Question("math_q11", quizId, "What is the volume of a cube with side length s?", "Multiple-Choice", "s^2,6s^2,s^3,1/3 s^3", "s^3", "Volume of a cube is side cubed."),
                    Question("math_q12", quizId, "Simplify: 3(x + 2) - 5", "Multiple-Choice", "3x + 1,3x - 1,3x + 6,3x + 5", "3x + 1", "3x + 6 - 5 = 3x + 1."),
                    Question("math_q13", quizId, "What is the median of the data set: 3, 7, 9, 2, 5?", "Multiple-Choice", "5,7,3,9", "5", "Ordered: 2, 3, 5, 7, 9 -> median is 5."),
                    Question("math_q14", quizId, "What is the square root of 144?", "Multiple-Choice", "10,11,12,14", "12", "12 * 12 = 144."),
                    Question("math_q15", quizId, "Solve for y if 3y - 6 = 9", "Multiple-Choice", "3,5,6,9", "5", "3y = 15 -> y = 5.")
                )
                "lit_202_quiz" -> listOf(
                    Question("lit_q1", quizId, "Who wrote the play 'Macbeth'?", "Multiple-Choice", "Charles Dickens,William Shakespeare,Jane Austen,Mark Twain", "William Shakespeare", "Macbeth is a tragedy written by William Shakespeare."),
                    Question("lit_q2", quizId, "What figure of speech compares two things using 'like' or 'as'?", "Multiple-Choice", "Metaphor,Simile,Personification,Hyperbole", "Simile", "A simile compares two things using 'like' or 'as'."),
                    Question("lit_q3", quizId, "In which play does the character Juliet appear?", "Multiple-Choice", "Hamlet,Macbeth,Romeo and Juliet,Othello", "Romeo and Juliet", "Juliet Capulet is the female protagonist in Romeo and Juliet."),
                    Question("lit_q4", quizId, "What is a poem consisting of 14 lines called?", "Multiple-Choice", "Haiku,Sonnet,Ballad,Epic", "Sonnet", "A sonnet traditionally has 14 lines."),
                    Question("lit_q5", quizId, "What term describes words that sound alike but have different meanings?", "Multiple-Choice", "Homophone,Synonym,Antonym,Acronym", "Homophone", "Homophones are words like bare and bear."),
                    Question("lit_q6", quizId, "Who is the author of 'Pride and Prejudice'?", "Multiple-Choice", "Charlotte Brontë,Jane Austen,Emily Dickinson,Virginia Woolf", "Jane Austen", "Pride and Prejudice was published in 1813 by Jane Austen."),
                    Question("lit_q7", quizId, "What is the term for the turning point or peak of tension in a story?", "Multiple-Choice", "Exposition,Climax,Resolution,Falling Action", "Climax", "The climax is the most intense point of the story."),
                    Question("lit_q8", quizId, "What literary device gives human traits to non-human objects?", "Multiple-Choice", "Metaphor,Personification,Alliteration,Onomatopoeia", "Personification", "Personification attributes human qualities to inanimate objects."),
                    Question("lit_q9", quizId, "What is the repetition of initial consonant sounds in nearby words?", "Multiple-Choice", "Assonance,Alliteration,Rhyme,Irony", "Alliteration", "Alliteration repeats consonant sounds at the start of words."),
                    Question("lit_q10", quizId, "What genre is Mary Shelley's 'Frankenstein'?", "Multiple-Choice", "Gothic/Science Fiction,Romance,Historical Fiction,Western", "Gothic/Science Fiction", "Frankenstein is a pioneering work of science fiction and gothic horror."),
                    Question("lit_q11", quizId, "What is the opposing character to the protagonist called?", "Multiple-Choice", "Antagonist,Foil,Narrator,Confidant", "Antagonist", "The antagonist works against the protagonist."),
                    Question("lit_q12", quizId, "What is a writer's attitude toward their subject called?", "Multiple-Choice", "Tone,Mood,Theme,Style", "Tone", "Tone reflects the author's attitude."),
                    Question("lit_q13", quizId, "Which of these is an example of onomatopoeia?", "Multiple-Choice", "Bright star,Buzz,Running fast,Soft whisper", "Buzz", "Onomatopoeia mimics sounds such as 'buzz'."),
                    Question("lit_q14", quizId, "What is the underlying central idea of a literary work?", "Multiple-Choice", "Plot,Theme,Setting,Conflict", "Theme", "Theme is the main message or underlying concept."),
                    Question("lit_q15", quizId, "In Dickens' 'A Christmas Carol', what is Scrooge's first name?", "Multiple-Choice", "Robert,Ebenezer,Jacob,Fred", "Ebenezer", "Ebenezer Scrooge is the main character.")
                )
                else -> listOf(
                    Question("${quizId}_q1", quizId, "What is 5 + 7?", "Multiple-Choice", "10,11,12,13", "12", "5 + 7 equals 12."),
                    Question("${quizId}_q2", quizId, "Is algebra a branch of mathematics?", "True/False", "True,False", "True", "Algebra deals with symbols and rules."),
                    Question("${quizId}_q3", quizId, "What is the derivative of x^2?", "Written Response", "", "2x", "Using power rule, derivative of x^n is n*x^(n-1)."),
                    Question("${quizId}_q4", quizId, "Which gas do plants absorb for photosynthesis?", "Multiple-Choice", "Oxygen,Carbon Dioxide,Nitrogen,Hydrogen", "Carbon Dioxide", "Plants take in carbon dioxide."),
                    Question("${quizId}_q5", quizId, "What is the chemical symbol for Gold?", "Multiple-Choice", "Ag,Au,Pb,Fe", "Au", "Au is Gold."),
                    Question("${quizId}_q6", quizId, "In which year did SA hold its first democratic elections?", "Multiple-Choice", "1990,1994,1999,2004", "1994", "Elections held in 1994."),
                    Question("${quizId}_q7", quizId, "What is the value of pi (π) rounded to 2 decimal places?", "Multiple-Choice", "3.12,3.14,3.16,3.18", "3.14", "Pi is ~3.14."),
                    Question("${quizId}_q8", quizId, "What is the powerhouse of the cell?", "Multiple-Choice", "Nucleus,Ribosome,Mitochondria,Endoplasmic Reticulum", "Mitochondria", "Mitochondria generate energy."),
                    Question("${quizId}_q9", quizId, "Who wrote Macbeth?", "Multiple-Choice", "Charles Dickens,William Shakespeare,Jane Austen,Mark Twain", "William Shakespeare", "Shakespeare wrote Macbeth."),
                    Question("${quizId}_q10", quizId, "What is the speed of light in vacuum?", "Multiple-Choice", "300,000 km/s,150,000 km/s,1,000 km/s,3,000 km/s", "300,000 km/s", "Speed of light is ~300,000 km/s."),
                    Question("${quizId}_q11", quizId, "What is the SI unit of force?", "Multiple-Choice", "Joule,Watt,Newton,Pascal", "Newton", "Force unit is Newton."),
                    Question("${quizId}_q12", quizId, "Administrative capital of SA?", "Multiple-Choice", "Johannesburg,Cape Town,Pretoria,Durban", "Pretoria", "Pretoria is capital."),
                    Question("${quizId}_q13", quizId, "Solve: 2x + 4 = 10", "Multiple-Choice", "2,3,4,5", "3", "x = 3."),
                    Question("${quizId}_q14", quizId, "Largest planet in solar system?", "Multiple-Choice", "Earth,Saturn,Jupiter,Neptune", "Jupiter", "Jupiter is largest."),
                    Question("${quizId}_q15", quizId, "Energy in a stretched spring?", "Multiple-Choice", "Kinetic,Potential (Elastic),Thermal,Chemical", "Potential (Elastic)", "Elastic potential energy.")
                )
            }
            quizDao.insertQuestions(preseeded)
            questions = preseeded
        }
        return questions
    }

    suspend fun saveAttempt(attempt: QuizAttempt) {
        quizDao.insertAttempt(attempt)
    }

    suspend fun seedInitialQuizzes() {
        val quizzes = listOf(
            Quiz("math_101_quiz", "math_101", "Algebra Fundamentals", "Easy"),
            Quiz("lit_202_quiz", "lit_202", "Shakespeare Anagrams", "Medium"),
            Quiz("sci_303_quiz", "sci_303", "Chemical Kinetics & Cells", "Medium"),
            Quiz("hist_404_quiz", "hist_404", "South African History", "Easy")
        )
        quizDao.insertQuizzes(quizzes)
    }
}
