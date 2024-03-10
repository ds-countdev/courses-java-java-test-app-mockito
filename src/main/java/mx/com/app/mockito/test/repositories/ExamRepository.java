package mx.com.app.mockito.test.repositories;

import mx.com.app.mockito.test.models.Exam;

import java.util.List;

public interface ExamRepository {
    List<Exam> findAll();
    Exam save(Exam exam);

}
