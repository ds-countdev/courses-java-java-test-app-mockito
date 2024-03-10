package mx.com.app.mockito.test.repositories;

import java.util.List;
import java.util.UUID;

public interface QuestionsRepository {
    List<String> findQuestionsByExamId(UUID id);
    void saveQuestions (List<String> questions);
}
