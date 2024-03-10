package mx.com.app.mockito.test.data_models;

import mx.com.app.mockito.test.models.Exam;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RepositoryData {

    public final static String MATHEMATICS_NAME = "Mathematics";
    public final static String SPANISH_NAME = "Spanish";
    public final static UUID UUID_446 = UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446");

    public final static List<Exam> examsData = Arrays.asList(
            new Exam(UUID_446, MATHEMATICS_NAME),
            new Exam(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa442"), "Spanish"),
            new Exam(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa445"), "History")
    );
    public final static List<Exam> EXAMS_DATA_ID_NULL = Arrays.asList(
            new Exam(null, MATHEMATICS_NAME),
            new Exam(null, SPANISH_NAME),
            new Exam(null, "History")
    );
    public final static List<String> QUESTIONS_DATA = Arrays.asList(
            "arithmetic", "integrals", "trigonometry" , "geometry"
    );

    public final static Exam EXAM = new Exam(UUID.randomUUID(), "physic");
}
