package mx.com.app.mockito.test.repositories;

import mx.com.app.mockito.test.models.Exam;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ExamRepositoryImpl implements ExamRepository{
    @Override
    public List<Exam> findAll() {
        return Arrays.asList(
                new Exam(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446"), "Mathematics"),
                new Exam(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa442"), "Spanish"),
                new Exam(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa445"), "History")
        );
    }

    @Override
    public Exam save(Exam exam) {
        return new Exam(exam.getId(),exam.getName());
    }

}
