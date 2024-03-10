package mx.com.app.mockito.test.services;

import mx.com.app.mockito.test.models.Exam;
import mx.com.app.mockito.test.repositories.ExamRepository;
import mx.com.app.mockito.test.repositories.QuestionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static mx.com.app.mockito.test.data_models.RepositoryData.EXAM;
import static mx.com.app.mockito.test.data_models.RepositoryData.EXAMS_DATA_ID_NULL;
import static mx.com.app.mockito.test.data_models.RepositoryData.QUESTIONS_DATA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static mx.com.app.mockito.test.data_models.RepositoryData.examsData;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    private TestInfo testInfo;

    @InjectMocks
    private ExamServiceImpl examService;

    private TestReporter testReporter;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private QuestionsRepository questionsRepository;

    @BeforeEach
    void setUp(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo = testInfo;
        this.testReporter = testReporter;
//        examRepository = mock(ExamRepository.class);
//        questionsRepository = mock(QuestionsRepository.class);
//        examService = new ExamServiceImpl(examRepository, questionsRepository);
        testReporter.publishEntry(
                "DisplayName : " + testInfo.getDisplayName() +
                        "TestMethod : " + testInfo.getTestMethod().orElseThrow().getName());
    }

    @Test
    @DisplayName("find exam by name test")
    void findExamByName() {
        when(examRepository.findAll()).thenReturn(examsData);
        Optional<Exam> examOptional = examService.findExamByName("Mathematics");

        assumingThat(examOptional.isPresent(), ()-> {
            assertEquals(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446"),
                    examOptional.orElseThrow().getId(),()-> "fail in compare id");
            assertEquals("Mathematics",
                    examOptional.orElseThrow().getName(), () -> "fail in compare name");
        }
                    );
    }

    @Test
    @DisplayName("find exam by name test when the list is empty")
    void findExamByNameInEmptyList() {
        List<Exam> examList = Collections.emptyList();

        when(examRepository.findAll()).thenReturn(examList);
        Optional<Exam> examOptional = examService.findExamByName("Mathematics");

        assumingThat(examOptional.isPresent(), ()-> {
                    assertEquals(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446"),
                            examOptional.orElseThrow().getId(),()-> "fail in compare id");
                    assertEquals("Mathematics",
                            examOptional.orElseThrow().getName(), () -> "fail in compare name");
                }
        );
    }

    @Test
    @DisplayName("find exam by name and then assign questions")
    void findQuestionsExam(){

        when(examRepository.findAll()).thenReturn(examsData);
        when(questionsRepository.findQuestionsByExamId(
                UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446")))
                .thenReturn(QUESTIONS_DATA);
        Exam exam = examService.findExamByNameWithQuestions("Mathematics");

        assumingThat(Objects.nonNull(exam), ()-> {
            assertEquals(4, exam.getQuestions().size(), ()-> "number of questions does not match");
            assertTrue(exam.getQuestions().contains("integrals"));
        });
    }

    @Test
    @DisplayName("find exam by name and then assign questions with verify")
    void findQuestionsExamVerify(){

        when(examRepository.findAll()).thenReturn(examsData);
        when(questionsRepository.findQuestionsByExamId(
                UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446")))
                .thenReturn(QUESTIONS_DATA);
        Exam exam = examService.findExamByNameWithQuestions("Mathematics");
        assumingThat(Objects.nonNull(exam), ()-> {
            verify(questionsRepository).findQuestionsByExamId(
                    UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446"));
            assertEquals(4, exam.getQuestions().size(), ()-> "number of questions does not match");
            assertTrue(exam.getQuestions().contains("integrals"));
        });
    }

    @Test
    @DisplayName("find when exam is null does not call repositories verify test")
    void examNotExistVerify(){
        when(examRepository.findAll()).thenReturn(examsData);
        when(questionsRepository.findQuestionsByExamId(
                UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446")))
                .thenReturn(QUESTIONS_DATA);
        Exam exam = examService.findExamByNameWithQuestions("Mathematics");
        assumingThat(Objects.isNull(exam), ()-> {
            //verify(questionsRepository).findQuestionsByExamId(
                   // UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446"));
            assertEquals(4, exam.getQuestions().size(), ()-> "number of questions does not match");
            assertTrue(exam.getQuestions().contains("integrals"));
        });
    }

    @Test
    @DisplayName("save exam, invocation answer and changing aurgument test")
    void saveExamTest(){
        EXAM.setQuestions(QUESTIONS_DATA);
        when(examRepository.save(any(Exam.class))).then(new Answer<Exam>(){
            @Override
            public Exam answer(InvocationOnMock invocation) throws Throwable {
                Exam exam = invocation.getArgument(0);
                exam.setQuestions(QUESTIONS_DATA);
                exam.setId(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446"));
                return exam;
            }
        });
        var exam = examService.saveExam(EXAM);
        assertNotNull(exam);
        assertEquals("physic",exam.getName());
        assertEquals("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446", exam.getId().toString());
        verify(examRepository).save(any(Exam.class));
        verify(questionsRepository).saveQuestions(anyList());
    }


    @Test
    @DisplayName("exception handler test")
    void exceptionHandlerTest(){
        when(examRepository.findAll()).thenReturn(EXAMS_DATA_ID_NULL);
        when(questionsRepository.findQuestionsByExamId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exeption = assertThrows(IllegalArgumentException.class, ()-> {
            examService.findExamByNameWithQuestions("Mathematics");
        });

        assertAll(
                ()-> assertEquals(IllegalArgumentException.class, exeption.getClass()),
                ()-> verify(examRepository).findAll(),
                ()-> verify(questionsRepository).findQuestionsByExamId(null)
                );

    }


    @Test
    @DisplayName("argument Matchers test")
    void testArgumentMatcher() {
        when(examRepository.findAll()).thenReturn(examsData);
        when(questionsRepository.findQuestionsByExamId(any(UUID.class))).thenReturn(QUESTIONS_DATA);
        examService.findExamByNameWithQuestions("Spanish");


        assertAll(
                () -> verify(examRepository).findAll(),
                () -> verify(questionsRepository)
                        .findQuestionsByExamId(argThat(arg -> Objects.nonNull(arg) &&
                                        (arg.equals(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446")) ||
                                        arg.equals(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa442")) ||
                                                arg.equals(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa445")))))
        );
    }


    public static class MyArgsMatchers implements ArgumentMatcher<UUID>{

        @Override
        public boolean matches(UUID argument) {
            return false;
        }
    }

}