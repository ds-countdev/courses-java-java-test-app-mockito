package mx.com.app.mockito.test.practice.services;

import mx.com.app.mockito.test.models.Exam;
import mx.com.app.mockito.test.repositories.ExamRepository;
import mx.com.app.mockito.test.repositories.QuestionsRepository;
import mx.com.app.mockito.test.services.ExamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static mx.com.app.mockito.test.data_models.RepositoryData.EXAMS_DATA_ID_NULL;
import static mx.com.app.mockito.test.data_models.RepositoryData.MATHEMATICS_NAME;
import static mx.com.app.mockito.test.data_models.RepositoryData.QUESTIONS_DATA;
import static mx.com.app.mockito.test.data_models.RepositoryData.UUID_446;
import static mx.com.app.mockito.test.data_models.RepositoryData.EXAMS_DATA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExamServiceImplTest {

    @InjectMocks
    private ExamServiceImpl examService;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private QuestionsRepository questionsRepository;

    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void setUp(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        testReporter.publishEntry(
                "DisplayName" + testInfo.getDisplayName() + "\n" +
                        "TestMethod : " + testInfo.getTestMethod().orElseThrow().getName());
    }

    @Test
    @DisplayName("find exam by name test when the list is empty test")
    void findExamByNameInEmptyList() {
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        var service = examService.findExamByName("Mathematics");
        assertTrue(service.isEmpty());
    }

    @Test
    @DisplayName("find exam by name test")
    void findExamByName() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
        Optional<Exam> examservice = examService.findExamByName("Mathematics");

        assumingThat(examservice.isPresent(), () -> {
           assertEquals(UUID_446,
                   examservice.orElseThrow().getId(), () -> "fail comparing ids");
           assertEquals(MATHEMATICS_NAME,
                   examservice.orElseThrow().getName(), ()-> "fail comparing names");
        });
    }

    @Test
    @DisplayName("find exam by name and then assign questions")
    void findQuestionsExam() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
        when(questionsRepository.findQuestionsByExamId(UUID_446)).thenReturn(QUESTIONS_DATA);

        var exam = examService.findExamByNameWithQuestions(MATHEMATICS_NAME);
        assumingThat(Objects.nonNull(exam), ()-> {
            assertEquals(4, exam.getQuestions().size(), ()-> "number of questions does not match");
            assertTrue(exam.getQuestions().contains("integrals"));
        });
    }

    @Test
    @DisplayName("find exam by name and then assign questions with verify")
    void findQuestionsExamVerify() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
        when(questionsRepository.findQuestionsByExamId(UUID_446)).thenReturn(QUESTIONS_DATA);

        var exam = examService.findExamByNameWithQuestions(MATHEMATICS_NAME);

        assumingThat(Objects.nonNull(exam), ()-> {
            verify(questionsRepository).findQuestionsByExamId(UUID_446);
        });
    }

    @Test
    @DisplayName("save exam, invocation answer and changing aurgument test")
    void saveExamTest() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA_ID_NULL);
        when(questionsRepository.findQuestionsByExamId(isNull())).thenThrow(IllegalArgumentException.class);

        var exeption = assertThrows(IllegalArgumentException.class, ()-> {
            examService.findExamByNameWithQuestions(MATHEMATICS_NAME);
        });

        assertAll(
                ()-> assertEquals(IllegalArgumentException.class, exeption.getClass()),
                ()-> verify(examRepository).findAll(),
                ()-> verify(questionsRepository).findQuestionsByExamId(null)
        );



    }
}
