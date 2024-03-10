package mx.com.app.mockito.test.services;

import mx.com.app.mockito.test.models.Exam;

import java.util.Optional;

public interface ExamService {
    Optional<Exam> findExamByName(String name);
    Exam findExamByNameWithQuestions(String name);
    Exam saveExam(Exam exam);
}
