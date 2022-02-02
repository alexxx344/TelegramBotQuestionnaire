package ru.voronec.botbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "question")
@Builder
public class Question {
    @Id
    String id;
    String questionText;
    String creator;
    String numberOfQuestion;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String rightAnswer;
    boolean important;
    boolean newQuestion;
    String notice;
    boolean viewed;

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", questionText='" + questionText + '\'' +
                ", answer1='" + answer1 + '\'' +
                ", answer2='" + answer2 + '\'' +
                ", answer3='" + answer3 + '\'' +
                ", answer4='" + answer4 + '\'' +
                '}';
    }
}
