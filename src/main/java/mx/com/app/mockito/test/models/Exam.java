package mx.com.app.mockito.test.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Exam {

    private UUID id;
    private String name;
    private List<String> questions;

    public Exam(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
