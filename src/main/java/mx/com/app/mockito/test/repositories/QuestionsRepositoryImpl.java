package mx.com.app.mockito.test.repositories;

import mx.com.app.mockito.test.data_models.RepositoryData;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static mx.com.app.mockito.test.data_models.RepositoryData.QUESTIONS_DATA;

public class QuestionsRepositoryImpl implements QuestionsRepository{

    @Override
    public List<String> findQuestionsByExamId(UUID id) {
        System.out.println("QuestionsRepositoryImpl.findQuestionsByExamId");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return QUESTIONS_DATA;
    }

    @Override
    public void saveQuestions(List<String> questions) {
        System.out.println("QuestionsRepositoryImpl.saveQuestions");

    }
}
