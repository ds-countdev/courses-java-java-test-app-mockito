package mx.com.app.mockito.test.services;

import mx.com.app.mockito.test.models.Exam;
import mx.com.app.mockito.test.repositories.ExamRepository;
import mx.com.app.mockito.test.repositories.ExamRepositoryImpl;
import mx.com.app.mockito.test.repositories.QuestionsRepository;
import mx.com.app.mockito.test.repositories.QuestionsRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static mx.com.app.mockito.test.data_models.RepositoryData.EXAM;
import static mx.com.app.mockito.test.data_models.RepositoryData.EXAMS_DATA_ID_NULL;
import static mx.com.app.mockito.test.data_models.RepositoryData.MATHEMATICS_NAME;
import static mx.com.app.mockito.test.data_models.RepositoryData.QUESTIONS_DATA;
import static mx.com.app.mockito.test.data_models.RepositoryData.UUID_442;
import static mx.com.app.mockito.test.data_models.RepositoryData.UUID_445;
import static mx.com.app.mockito.test.data_models.RepositoryData.UUID_446;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static mx.com.app.mockito.test.data_models.RepositoryData.EXAMS_DATA;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    private TestInfo testInfo;

    @InjectMocks
    private ExamServiceImpl examService;

    private TestReporter testReporter;

    @Mock
    private ExamRepositoryImpl examRepository;

    @Mock
    private QuestionsRepositoryImpl questionsRepository;

    @Captor
    ArgumentCaptor<UUID> captor;

    @BeforeEach
    void setUp(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo = testInfo;
        this.testReporter = testReporter;
//        examRepository = mock(ExamRepository.class);
//        questionsRepository = mock(QuestionsRepository.class);
//        examService = new ExamServiceImpl(examRepository, questionsRepository);
        testReporter.publishEntry(
                "DisplayName : " + testInfo.getDisplayName() +"\n"+
                        "TestMethod : " + testInfo.getTestMethod().orElseThrow().getName());
    }

    @Test
    @DisplayName("find exam by name test")
    void findExamByName() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
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

        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
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

        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
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
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
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
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
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


    @Test
    @DisplayName("Argument matchers test")
    void ArgumentMatcherTest() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
        when(questionsRepository.findQuestionsByExamId(any(UUID.class))).thenReturn(QUESTIONS_DATA);
        examService.findExamByNameWithQuestions(MATHEMATICS_NAME);

        verify(questionsRepository).findQuestionsByExamId(argThat(arg -> arg != null && !arg.equals(UUID_442)));
        verify(questionsRepository).findQuestionsByExamId(argThat(arg -> arg != null && arg.equals(UUID_446)));
    }

    @Test
    @DisplayName("Argument matchers in a inner class test")
    void ArgumentMatcher2Test() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
        when(questionsRepository.findQuestionsByExamId(any(UUID.class))).thenReturn(QUESTIONS_DATA);
        examService.findExamByNameWithQuestions(MATHEMATICS_NAME);

        verify(questionsRepository).findQuestionsByExamId(argThat(new MyArgsMatchers()));
    }

    public static class MyArgsMatchers implements ArgumentMatcher<UUID>{

        UUID argument;

        @Override
        public boolean matches(UUID argument) {
        this.argument = argument;
            return argument != null && (argument.equals(UUID_446) || argument.equals(UUID_445));
        }

        @Override
        public String toString() {
            return "This is a error message when " +argument +" is incorrect";
        }
    }

    @Test
    @DisplayName("Argument matchers in a inner class test")
    void ArgumentCaptorTest() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
        when(questionsRepository.findQuestionsByExamId(any(UUID.class))).thenReturn(QUESTIONS_DATA);
        examService.findExamByNameWithQuestions(MATHEMATICS_NAME);

       // var captor = ArgumentCaptor.forClass(UUID.class);
        verify(questionsRepository).findQuestionsByExamId(captor.capture());

        assertEquals(UUID_446, captor.getValue());
    }

    @Test
    @DisplayName("throw exception when call a void method")
    void doThrowTest() {
        var exam = EXAM;
        exam.setQuestions(QUESTIONS_DATA);
        doThrow(IllegalArgumentException.class).when(questionsRepository).saveQuestions(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            questionsRepository.saveQuestions(QUESTIONS_DATA);
        });
    }

    @Test
    @DisplayName("do answer personalized when in a test method")
    void doAnswerTest() {

        when(examRepository.findAll()).thenReturn(EXAMS_DATA);

        doAnswer(invocation -> {
            UUID id = invocation.getArgument(0);
            return id.equals(UUID_446) ? QUESTIONS_DATA : Collections.emptyList();
        }).when(questionsRepository).findQuestionsByExamId(any(UUID.class));

        var exam = examService.findExamByNameWithQuestions(MATHEMATICS_NAME);
        assertEquals(UUID_446, exam.getId());
        assertEquals(MATHEMATICS_NAME, exam.getName());
        assertEquals(4 , exam.getQuestions().size());
    }




    @Test
    @DisplayName("save exam, invocation answer and changing argument with do answer method test")
    void saveExamDoAnswerTest(){
        EXAM.setQuestions(QUESTIONS_DATA);

        doAnswer(new Answer<Exam>() {
            @Override
            public Exam answer(InvocationOnMock invocation) throws Throwable {
                Exam exam = invocation.getArgument(0);
                exam.setId(UUID.fromString("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446"));
                return exam;
            }
        }).when(examRepository).save(any(Exam.class));
        var exam = examService.saveExam(EXAM);
        assertNotNull(exam);
        assertEquals("physic",exam.getName());
        assertEquals("ce37aa61-1aaf-48d1-a66e-dc3ca5faa446", exam.getId().toString());
        verify(examRepository).save(any(Exam.class));
        verify(questionsRepository).saveQuestions(anyList());
    }

    @Test
    void doCallRealMethodTest() {
        when(examRepository.findAll()).thenReturn(EXAMS_DATA);
        doCallRealMethod().when(questionsRepository).findQuestionsByExamId(any(UUID.class));

        var serviceResponse = examService.findExamByNameWithQuestions(MATHEMATICS_NAME);
        assertEquals(UUID_446, serviceResponse.getId());
    }

    @Test
    void spyTest() {
        var examSpyRepository = spy(ExamRepositoryImpl.class);
        var questionsSpyRepository = spy(QuestionsRepositoryImpl.class);

        var examSpyService = new ExamServiceImpl(examSpyRepository, questionsSpyRepository);

        List<String> examList = Arrays.asList("spanish");
        when(questionsRepository.findQuestionsByExamId(any(UUID.class))).thenReturn(examList);

        var examSpyResponse = examSpyService.findExamByNameWithQuestions(MATHEMATICS_NAME);
        assertEquals(UUID_446, examSpyResponse.getId() );
        assertEquals(MATHEMATICS_NAME,examSpyResponse.getName());
        assertEquals(4, examSpyResponse.getQuestions().size());

    }
}