package ru.voronec.botbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "myWord")
@Builder
public class MyWord {
    @Id
    String id;
    String word;
    String creator;
    String translate;
    String difficult;
    boolean newWord;
    boolean important;
    String notice;
}
