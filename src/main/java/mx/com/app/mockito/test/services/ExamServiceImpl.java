package mx.com.app.mockito.test.services;

import mx.com.app.mockito.test.models.Exam;
import mx.com.app.mockito.test.repositories.ExamRepository;
import mx.com.app.mockito.test.repositories.QuestionsRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService{

    private final ExamRepository examRepository;
    private final QuestionsRepository questionsRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionsRepository questionsRepository) {
        this.examRepository = examRepository;
        this.questionsRepository = questionsRepository;
    }

    @Override
    public Optional<Exam> findExamByName(String name) {
        return examRepository.findAll()
                .stream()
                .filter(questions -> questions.getName().contains(name))
                .findFirst();
    }

    @Override
    public Exam findExamByNameWithQuestions(String name) {
        Optional<Exam> examOptional = findExamByName(name);
        Exam exam = null;
        if (examOptional.isPresent()) {
            exam = examOptional.orElseThrow();
            List<String> questionsList = questionsRepository.findQuestionsByExamId(exam.getId());
            exam.setQuestions(questionsList);
        }
        return exam;
    }

    @Override
    public Exam saveExam(Exam exam) {
        if (!exam.getQuestions().isEmpty()) questionsRepository.saveQuestions(exam.getQuestions());
        return examRepository.save(exam);
    }
}
